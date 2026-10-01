package thuc.ute.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import thuc.ute.entity.OrderDetail_24110349;
import thuc.ute.entity.Order_24110349;
import thuc.ute.entity.User_24110349;
import thuc.ute.entity.CartItem_24110349;
import thuc.ute.service.ICartItemService_24110349;
import thuc.ute.service.IOrderService_24110349;
import thuc.ute.service.impl.CartItemServiceImpl_24110349;
import thuc.ute.service.impl.OrderServiceImpl_24110349;
import thuc.ute.utils.Constants_24110349;
import thuc.ute.utils.Constants_24110349;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IOrderService_24110349 orderService = new OrderServiceImpl_24110349();
    private final ICartItemService_24110349 cartService = new CartItemServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24110349 user = (User_24110349) session.getAttribute(Constants_24110349.SESSION_ACCOUNT);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<CartItem_24110349> cartList = cartService.findByUser(user.getId());
        if (cartList == null || cartList.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        
        // Convert to Map for easier lookup
        Map<Integer, CartItem_24110349> cart = cartList.stream()
            .collect(Collectors.toMap(item -> item.getBook().getBookId(), item -> item));

        String[] selectedIds = req.getParameterValues("selectedIds");
        if (selectedIds == null || selectedIds.length == 0) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        List<CartItem_24110349> selectedItems = new ArrayList<>();
        BigDecimal checkoutTotalPrice = BigDecimal.ZERO;
        int checkoutTotalItems = 0;
        
        for (String idStr : selectedIds) {
            try {
                int bookId = Integer.parseInt(idStr);
                if (cart.containsKey(bookId)) {
                    CartItem_24110349 item = cart.get(bookId);
                    selectedItems.add(item);
                    checkoutTotalPrice = checkoutTotalPrice.add(item.getTotalPrice());
                    checkoutTotalItems += item.getQuantity();
                }
            } catch (Exception ignored) {}
        }

        if (selectedItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        req.setAttribute("selectedItems", selectedItems);
        req.setAttribute("checkoutTotalPrice", checkoutTotalPrice);
        req.setAttribute("checkoutTotalItems", checkoutTotalItems);
        req.setAttribute("selectedIdsStr", String.join(",", selectedIds));

        req.getRequestDispatcher("/views/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24110349 user = (User_24110349) session.getAttribute(Constants_24110349.SESSION_ACCOUNT);
        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<CartItem_24110349> cartList = cartService.findByUser(user.getId());
        if (cartList == null || cartList.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        Map<Integer, CartItem_24110349> cart = cartList.stream()
            .collect(Collectors.toMap(item -> item.getBook().getBookId(), item -> item));

        String selectedIdsParam = req.getParameter("selectedIdsStr");
        if (selectedIdsParam == null || selectedIdsParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        String[] selectedIds = selectedIdsParam.split(",");

        req.setCharacterEncoding("UTF-8");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String recipientName = req.getParameter("recipientName");

        Order_24110349 order = new Order_24110349();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("\u0110\u01A1n h\u00E0ng m\u1EDBi"); // Đơn hàng mới
        order.setShippingAddress(address);
        order.setPhoneNumber(phone);
        order.setRecipientName(recipientName);
        order.setPaymentMethod("COD");
        order.setPaymentStatus("Ch\u01B0a thanh to\u00E1n"); // Chưa thanh toán

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderDetail_24110349> orderDetails = new ArrayList<>();
        
        Integer currentCartTotal = (Integer) session.getAttribute("cartTotalItems");
        if (currentCartTotal == null) currentCartTotal = 0;

        for (String idStr : selectedIds) {
            try {
                int bookId = Integer.parseInt(idStr);
                if (cart.containsKey(bookId)) {
                    CartItem_24110349 item = cart.get(bookId);
                    
                    OrderDetail_24110349 detail = new OrderDetail_24110349();
                    detail.setOrder(order);
                    detail.setBook(item.getBook());
                    detail.setQuantity(item.getQuantity());
                    detail.setPrice(item.getBook().getPrice());
                    
                    totalAmount = totalAmount.add(item.getTotalPrice());
                    orderDetails.add(detail);
                    
                    // Remove from database
                    cartService.delete(item.getId());
                    currentCartTotal -= item.getQuantity();
                }
            } catch (Exception ignored) {}
        }

        if (orderDetails.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        order.setTotalAmount(totalAmount);
        order.setOrderDetails(orderDetails);

        try {
            orderService.placeOrder(order);
            session.setAttribute("cartTotalItems", currentCartTotal);
            session.setAttribute("success", "\u0110\u1EB7t h\u00E0ng th\u00E0nh c\u00F4ng! Ch\u00FAng t\u00F4i s\u1EBD s\u1EDBm li\u00EAn h\u1EC7 v\u1EDBi b\u1EA1n.");
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            req.setAttribute("alert", "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            req.getRequestDispatcher("/views/checkout.jsp").forward(req, resp);
        }
    }
}
