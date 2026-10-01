package thuc.ute.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

import thuc.ute.entity.User_24110349;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import thuc.ute.service.IUserService_24110349;
import thuc.ute.service.impl.UserServiceImpl_24110349;
import thuc.ute.utils.Constants_24110349;


@WebServlet(urlPatterns = {"/verify-otp"})
public class VerifyOtpController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IUserService_24110349 userService = new UserServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!hasPendingRegistration(session)) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String enteredOtp = req.getParameter("otp");

        HttpSession session = req.getSession(false);
        if (!hasPendingRegistration(session)) {
            req.setAttribute("alert", "Mã OTP đã hết hạn hoặc không tồn tại. Vui lòng đăng ký lại!");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        LocalDateTime expiresAt = (LocalDateTime) session.getAttribute(Constants_24110349.SESSION_OTP_EXPIRES_AT);
        if (expiresAt == null || LocalDateTime.now().isAfter(expiresAt)) {
            clearOtpSession(session);
            req.setAttribute("alert", "Mã OTP đã hết hạn. Vui lòng đăng ký lại!");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        if (enteredOtp == null || !enteredOtp.trim().matches("\\d{6}")) {
            req.setAttribute("alert", "Mã OTP phải gồm đúng 6 chữ số.");
            req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
            return;
        }

        String sessionOtp = (String) session.getAttribute(Constants_24110349.SESSION_OTP_CODE);
        User_24110349 tempUser = (User_24110349) session.getAttribute(Constants_24110349.SESSION_TEMP_USER);

        if (MessageDigest.isEqual(sessionOtp.getBytes(StandardCharsets.UTF_8),
                enteredOtp.trim().getBytes(StandardCharsets.UTF_8))) {
            // Kiểm tra lại để tránh hai yêu cầu đăng ký đồng thời cùng một email.
            if (userService.findByEmail(tempUser.getEmail()) != null) {
                clearOtpSession(session);
                req.setAttribute("alert", "Email này đã được đăng ký trên hệ thống!");
                req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
                return;
            }

            userService.registerUser(tempUser);
            clearOtpSession(session);

            session.setAttribute(Constants_24110349.SESSION_SUCCESS_MESSAGE,
                    "Đăng ký và kích hoạt tài khoản thành công! Vui lòng đăng nhập.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            Integer attempts = (Integer) session.getAttribute(Constants_24110349.SESSION_OTP_ATTEMPTS);
            attempts = attempts == null ? 1 : attempts + 1;
            session.setAttribute(Constants_24110349.SESSION_OTP_ATTEMPTS, attempts);

            if (attempts >= Constants_24110349.OTP_MAX_ATTEMPTS) {
                clearOtpSession(session);
                req.setAttribute("alert", "Bạn đã nhập sai OTP quá nhiều lần. Vui lòng đăng ký lại!");
                req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("alert", "Mã OTP không chính xác. Vui lòng thử lại!");
            req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
        }
    }

    private boolean hasPendingRegistration(HttpSession session) {
        return session != null
                && session.getAttribute(Constants_24110349.SESSION_OTP_CODE) instanceof String
                && session.getAttribute(Constants_24110349.SESSION_TEMP_USER) instanceof User_24110349;
    }

    private void clearOtpSession(HttpSession session) {
        session.removeAttribute(Constants_24110349.SESSION_OTP_CODE);
        session.removeAttribute(Constants_24110349.SESSION_TEMP_USER);
        session.removeAttribute(Constants_24110349.SESSION_OTP_EXPIRES_AT);
        session.removeAttribute(Constants_24110349.SESSION_OTP_ATTEMPTS);
    }
}
