package thuc.ute.filter;

import java.io.IOException;

import thuc.ute.entity.User_24110349;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import thuc.ute.utils.Constants_24110349;

@WebFilter(urlPatterns = {
        "/home", "/book-detail", "/waiting", "/admin/*", "/user/*", "/cart", "/checkout",
        "/views/home.jsp", "/views/book-detail.jsp", "/views/admin/*", "/views/user/*", "/views/cart.jsp", "/views/checkout.jsp"
})
public class AuthFilter_24110349 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User_24110349 user = (session != null)
                ? (User_24110349) session.getAttribute(Constants_24110349.SESSION_ACCOUNT) : null;

        // 1. Chưa đăng nhập mà đòi vào các trang này
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 2. Kiểm tra quyền Admin
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());
        boolean adminResource = path.startsWith("/admin/") || path.startsWith("/views/admin/");

        if (adminResource && !Boolean.TRUE.equals(user.getIsAdmin())) {
            req.getRequestDispatcher("/views/403.jsp").forward(req, resp);
            return;
        }

        // Admin không sử dụng trang chủ dành riêng cho User.
        if (("/home".equals(path) || "/views/home.jsp".equals(path))
                && Boolean.TRUE.equals(user.getIsAdmin())) {
            resp.sendRedirect(contextPath + "/admin/home");
            return;
        }

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
