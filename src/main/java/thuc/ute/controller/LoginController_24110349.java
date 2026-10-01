package thuc.ute.controller;

import java.io.IOException;

import thuc.ute.entity.User_24110349;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import thuc.ute.service.IUserService_24110349;
import thuc.ute.service.impl.UserServiceImpl_24110349;
import thuc.ute.utils.Constants_24110349; 

@WebServlet(urlPatterns = {"/login"})
public class LoginController_24110349 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final IUserService_24110349 userService = new UserServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constants_24110349.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        if (session != null) {
            Object successMessage = session.getAttribute(Constants_24110349.SESSION_SUCCESS_MESSAGE);
            if (successMessage != null) {
                req.setAttribute("success", successMessage);
                session.removeAttribute(Constants_24110349.SESSION_SUCCESS_MESSAGE);
            }
        }

        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String email = req.getParameter("email");
        String passwd = req.getParameter("passwd");

        if (isBlank(email) || isBlank(passwd)) {
            req.setAttribute("alert", "Tài khoản hoặc mật khẩu không được để trống");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        User_24110349 user = userService.login(email.trim(), passwd);

        if (user != null) {
            // Tạo Session mới sau khi xác thực để tránh session fixation.
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = req.getSession(true);
            session.setAttribute(Constants_24110349.SESSION_ACCOUNT, user);
            session.setMaxInactiveInterval(30 * 60);

            // Load giỏ hàng từ Database và đếm tổng số lượng
            thuc.ute.service.ICartItemService_24110349 cartService = new thuc.ute.service.impl.CartItemServiceImpl_24110349();
            java.util.List<thuc.ute.entity.CartItem_24110349> cartList = cartService.findByUser(user.getId());
            int totalItems = cartList.stream().mapToInt(thuc.ute.entity.CartItem_24110349::getQuantity).sum();
            session.setAttribute("cartTotalItems", totalItems);

            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        req.setAttribute("alert", "Tài khoản hoặc mật khẩu không đúng hoặc tài khoản chưa được kích hoạt");
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
