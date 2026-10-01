package thuc.ute.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import thuc.ute.entity.Book_24110349;
import thuc.ute.model.CartItem_24110349;
import thuc.ute.service.IBookService_24110349;
import thuc.ute.service.impl.BookServiceImpl_24110349;

@WebServlet(urlPatterns = {"/cart"})
public class CartController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IBookService_24110349 bookService = new BookServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object alert = session.getAttribute("alert");
        if (alert != null) {
            req.setAttribute("alert", alert);
            session.removeAttribute("alert");
        }
        req.getRequestDispatcher("/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        Integer bookId = parsePositiveInt(req.getParameter("bookId"));
        
        if (bookId == null) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        HttpSession session = req.getSession();
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem_24110349> cart = (Map<Integer, CartItem_24110349>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }

        if ("add".equals(action)) {
            int quantity = parseInt(req.getParameter("quantity"), 1);
            if (quantity <= 0) quantity = 1;
            
            Book_24110349 book = bookService.findById(bookId);
            if (book != null && book.getQuantity() != null && book.getQuantity() > 0) {
                CartItem_24110349 item = cart.get(bookId);
                int newQty = (item != null ? item.getQuantity() : 0) + quantity;
                if (newQty > book.getQuantity()) {
                    newQty = book.getQuantity();
                    session.setAttribute("alert", "Số lượng thêm vào vượt quá tồn kho hiện có!");
                }
                if (item == null) {
                    cart.put(bookId, new CartItem_24110349(book, newQty));
                } else {
                    item.setQuantity(newQty);
                }
            } else if (book != null && (book.getQuantity() == null || book.getQuantity() <= 0)) {
                 session.setAttribute("alert", "Sản phẩm đã hết hàng!");
            }
        } else if ("update".equals(action)) {
            int quantity = parseInt(req.getParameter("quantity"), 1);
            CartItem_24110349 item = cart.get(bookId);
            if (item != null) {
                if (quantity <= 0) {
                    cart.remove(bookId);
                } else {
                    if (item.getBook().getQuantity() != null && quantity > item.getBook().getQuantity()) {
                        quantity = item.getBook().getQuantity();
                        session.setAttribute("alert", "Số lượng vượt quá giới hạn tồn kho!");
                    }
                    item.setQuantity(quantity);
                }
            }
        } else if ("remove".equals(action)) {
            cart.remove(bookId);
        }

        session.setAttribute("cart", cart);
        
        int totalItems = cart.values().stream().mapToInt(CartItem_24110349::getQuantity).sum();
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
