package thuc.ute.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import thuc.ute.entity.Order_24110349;
import thuc.ute.entity.User_24110349;
import thuc.ute.service.IOrderService_24110349;
import thuc.ute.service.impl.OrderServiceImpl_24110349;
import thuc.ute.utils.Constants_24110349;

@WebServlet(urlPatterns = {"/user/orders"})
public class UserOrderHistoryController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IOrderService_24110349 orderService = new OrderServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User_24110349 user = (User_24110349) req.getSession().getAttribute(Constants_24110349.SESSION_ACCOUNT);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String status = req.getParameter("status");
        if (status == null || status.isEmpty()) {
            status = "T\u1EA5t c\u1EA3"; // Tất cả
        }

        List<Order_24110349> orders = orderService.findOrdersByUserAndStatus(user.getId(), status);

        req.setAttribute("orders", orders);
        req.setAttribute("currentStatus", status);
        
        req.getRequestDispatcher("/views/user/orders.jsp").forward(req, resp);
    }
}
