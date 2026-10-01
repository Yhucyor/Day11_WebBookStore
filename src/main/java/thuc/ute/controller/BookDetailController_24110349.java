package thuc.ute.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import thuc.ute.entity.Book_24110349;
import thuc.ute.entity.User_24110349;
import thuc.ute.service.IBookService_24110349;
import thuc.ute.service.impl.BookServiceImpl_24110349;
import thuc.ute.utils.Constants_24110349;

@WebServlet(urlPatterns = {"/book-detail"})
public class BookDetailController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IBookService_24110349 bookService = new BookServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer bookId = parsePositiveInt(req.getParameter("id"));
        if (bookId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã sách không hợp lệ.");
            return;
        }

        if ("1".equals(req.getParameter("saved"))) {
            req.setAttribute("success", "Review của bạn đã được lưu thành công.");
        }
        renderDetail(bookId, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Integer bookId = parsePositiveInt(req.getParameter("bookId"));
        Integer rating = parsePositiveInt(req.getParameter("rating"));
        String reviewText = req.getParameter("reviewText");

        if (bookId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã sách không hợp lệ.");
            return;
        }

        if (rating == null || rating > 5 || reviewText == null || reviewText.trim().isEmpty()) {
            req.setAttribute("alert", "Vui lòng chọn điểm từ 1 đến 5 và nhập nội dung review.");
            renderDetail(bookId, req, resp);
            return;
        }

        if (reviewText.trim().length() > 2000) {
            req.setAttribute("alert", "Nội dung review không được vượt quá 2000 ký tự.");
            renderDetail(bookId, req, resp);
            return;
        }

        HttpSession session = req.getSession(false);
        User_24110349 user = session == null ? null
                : (User_24110349) session.getAttribute(Constants_24110349.SESSION_ACCOUNT);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (bookService.findById(bookId) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sách.");
            return;
        }

        bookService.saveReview(user.getId(), bookId, rating.shortValue(), reviewText);
        resp.sendRedirect(req.getContextPath() + "/book-detail?id=" + bookId + "&saved=1");
    }

    private void renderDetail(int bookId, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Book_24110349 book = bookService.findById(bookId);
        if (book == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sách.");
            return;
        }

        req.setAttribute("book", book);
        req.setAttribute("reviews", bookService.findReviewsByBookId(bookId));
        req.getRequestDispatcher("/views/book-detail.jsp").forward(req, resp);
    }

    private Integer parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
