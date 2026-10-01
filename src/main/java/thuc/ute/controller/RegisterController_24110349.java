package thuc.ute.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import thuc.ute.entity.User_24110349;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import thuc.ute.service.IUserService_24110349;
import thuc.ute.service.impl.UserServiceImpl_24110349;
import thuc.ute.utils.EmailUtil_24110349; 
import thuc.ute.utils.Constants_24110349;

@WebServlet(urlPatterns = {"/register"})
public class RegisterController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IUserService_24110349 userService = new UserServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String password = req.getParameter("password");
        String phoneStr = req.getParameter("phone");

        email = email == null ? "" : email.trim().toLowerCase();
        fullname = fullname == null ? "" : fullname.trim();

        if (email.isEmpty() || fullname.isEmpty() || password == null || password.length() < 6) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ thông tin; mật khẩu phải có ít nhất 6 ký tự.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        if (userService.findByEmail(email) != null) {
            req.setAttribute("alert", "Email này đã được đăng ký trên hệ thống!");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        User_24110349 tempUser = new User_24110349();
        tempUser.setEmail(email);
        tempUser.setFullname(fullname);
        tempUser.setPasswd(password);
        if (phoneStr != null && !phoneStr.trim().isEmpty()) {
            try {
                tempUser.setPhone(Integer.parseInt(phoneStr.trim()));
            } catch (NumberFormatException e) {
                req.setAttribute("alert", "Số điện thoại không hợp lệ.");
                req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
                return;
            }
        }
        tempUser.setSignupDate(LocalDateTime.now());
        tempUser.setIsAdmin(false);
        
        String otpCode = EmailUtil_24110349.generateOtp();
        if (!EmailUtil_24110349.sendOtpEmail(email, otpCode)) {
            req.setAttribute("alert", "Không thể gửi email OTP. Vui lòng thử lại sau.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute(Constants_24110349.SESSION_TEMP_USER, tempUser);
        session.setAttribute(Constants_24110349.SESSION_OTP_CODE, otpCode);
        session.setAttribute(Constants_24110349.SESSION_OTP_EXPIRES_AT,
                LocalDateTime.now().plus(Constants_24110349.OTP_EXPIRATION_MINUTES, ChronoUnit.MINUTES));
        session.setAttribute(Constants_24110349.SESSION_OTP_ATTEMPTS, 0);

        resp.sendRedirect(req.getContextPath() + "/verify-otp");
    }
}
