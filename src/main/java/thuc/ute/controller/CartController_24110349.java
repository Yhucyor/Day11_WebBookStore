package thuc.ute.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import thuc.ute.entity.Book_24110349;
import thuc.ute.entity.CartItem_24110349;
import thuc.ute.entity.User_24110349;
import thuc.ute.service.IBookService_24110349;
import thuc.ute.service.ICartItemService_24110349;
import thuc.ute.service.impl.BookServiceImpl_24110349;
import thuc.ute.service.impl.CartItemServiceImpl_24110349;
import thuc.ute.utils.Constants_24110349;

@WebServlet(urlPatterns = {"/cart"})
public class CartController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IBookService_24110349 bookService = new BookServiceImpl_24110349();
    private final ICartItemService_24110349 cartService = new CartItemServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24110349 user = (User_24110349) session.getAttribute(Constants_24110349.SESSION_ACCOUNT);
        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Object alert = session.getAttribute("alert");
        if (alert != null) {
            req.setAttribute("alert", alert);
            session.removeAttribute("alert");
        }
        
        // Lấy giỏ hàng từ Database
        List<CartItem_24110349> cartList = cartService.findByUser(user.getId());
        
        // Chuyển List thành Map để tương thích ngược với file JSP cũ
        Map<Integer, CartItem_24110349> cartMap = new LinkedHashMap<>();
        int totalItems = 0;
        for (CartItem_24110349 item : cartList) {
            cartMap.put(item.getBook().getBookId(), item);
            totalItems += item.getQuantity();
        }
        
        req.setAttribute("cart", cartMap);
        session.setAttribute("cartTotalItems", totalItems);

        req.getRequestDispatcher("/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24110349 user = (User_24110349) session.getAttribute(Constants_24110349.SESSION_ACCOUNT);
        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        Integer bookId = parsePositiveInt(req.getParameter("bookId"));
        
        if (bookId == null) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        if ("add".equals(action)) {
            int quantity = parseInt(req.getParameter("quantity"), 1);
            if (quantity <= 0) quantity = 1;
            
            Book_24110349 book = bookService.findById(bookId);
            if (book != null && book.getQuantity() != null && book.getQuantity() > 0) {
                CartItem_24110349 existingItem = cartService.findByUserAndBook(user.getId(), bookId);
                
                int newQty = (existingItem != null ? existingItem.getQuantity() : 0) + quantity;
                if (newQty > book.getQuantity()) {
                    newQty = book.getQuantity();
                    session.setAttribute("alert", "Số lượng thêm vào vượt quá tồn kho hiện có!");
                }
                
                if (existingItem == null) {
                    CartItem_24110349 newItem = new CartItem_24110349(user, book, newQty);
                    cartService.insert(newItem);
                } else {
                    existingItem.setQuantity(newQty);
                    cartService.update(existingItem);
                }
            } else if (book != null && (book.getQuantity() == null || book.getQuantity() <= 0)) {
                 session.setAttribute("alert", "Sản phẩm đã hết hàng!");
            }
        } else if ("update".equals(action)) {
            int quantity = parseInt(req.getParameter("quantity"), 1);
            CartItem_24110349 existingItem = cartService.findByUserAndBook(user.getId(), bookId);
            
            if (existingItem != null) {
                if (quantity <= 0) {
                    cartService.delete(existingItem.getId());
                } else {
                    if (existingItem.getBook().getQuantity() != null && quantity > existingItem.getBook().getQuantity()) {
                        quantity = existingItem.getBook().getQuantity();
                        session.setAttribute("alert", "Số lượng vượt quá giới hạn tồn kho!");
                    }
                    existingItem.setQuantity(quantity);
                    cartService.update(existingItem);
                }
            }
        } else if ("remove".equals(action)) {
            CartItem_24110349 existingItem = cartService.findByUserAndBook(user.getId(), bookId);
            if (existingItem != null) {
                cartService.delete(existingItem.getId());
            }
        }

        // Cập nhật lại số lượng trong session để hiển thị trên Header
        List<CartItem_24110349> cartList = cartService.findByUser(user.getId());
        int totalItems = cartList.stream().mapToInt(CartItem_24110349::getQuantity).sum();
        session.setAttribute("cartTotalItems", totalItems);

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private Integer parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
