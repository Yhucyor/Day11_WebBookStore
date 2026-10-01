package thuc.ute.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import thuc.ute.entity.OrderDetail_24110349;
import thuc.ute.entity.Order_24110349;
import thuc.ute.entity.User_24110349;
import thuc.ute.model.CartItem_24110349;
import thuc.ute.service.IOrderService_24110349;
import thuc.ute.service.impl.OrderServiceImpl_24110349;
import thuc.ute.utils.Constants_24110349;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IOrderService_24110349 orderService = new OrderServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24110349 user = (User_24110349) session.getAttribute(Constants_24110349.SESSION_ACCOUNT);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        @SuppressWarnings("unchecked")
        Map<Integer, CartItem_24110349> cart = (Map<Integer, CartItem_24110349>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

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

        @SuppressWarnings("unchecked")
        Map<Integer, CartItem_24110349> cart = (Map<Integer, CartItem_24110349>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String recipientName = req.getParameter("recipientName");

        Order_24110349 order = new Order_24110349();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("Đơn hàng mới");
        order.setShippingAddress(address);
        order.setPhoneNumber(phone);
        order.setRecipientName(recipientName);
        order.setPaymentMethod("COD");
        order.setPaymentStatus("Chưa thanh toán");

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderDetail_24110349> orderDetails = new ArrayList<>();

        for (CartItem_24110349 item : cart.values()) {
            OrderDetail_24110349 detail = new OrderDetail_24110349();
            detail.setOrder(order);
            detail.setBook(item.getBook());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getBook().getPrice());
            
            totalAmount = totalAmount.add(item.getTotalPrice());
            orderDetails.add(detail);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderDetails(orderDetails);

        try {
            orderService.placeOrder(order);
            // Xóa giỏ hàng
            session.removeAttribute("cart");
            session.setAttribute("cartTotalItems", 0);
            session.setAttribute("success", "Đặt hàng thành công! Chúng tôi sẽ sớm liên hệ với bạn.");
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            req.setAttribute("alert", "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            req.getRequestDispatcher("/views/checkout.jsp").forward(req, resp);
        }
    }
}
