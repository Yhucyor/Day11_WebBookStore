package thuc.ute.controller;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import thuc.ute.entity.Author_24110349;
import thuc.ute.service.IAuthorService_24110349;
import thuc.ute.service.impl.AuthorServiceImpl_24110349;

@WebServlet(urlPatterns = {"/admin/authors"})
public class AdminAuthorController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;

    private final IAuthorService_24110349 authorService = new AuthorServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            showForm(new Author_24110349(), req, resp);
            return;
        }
        if ("edit".equals(action)) {
            Integer authorId = parsePositiveInt(req.getParameter("id"));
            Author_24110349 author = authorId == null ? null : authorService.findById(authorId);
            if (author == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy tác giả.");
                return;
            }
            showForm(author, req, resp);
            return;
        }
        showList(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            Integer authorId = parsePositiveInt(req.getParameter("id"));
            if (authorId != null) {
                authorService.delete(authorId);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/authors?status=deleted");
            return;
        }

        Author_24110349 author = new Author_24110349();
        try {
            boolean update = "update".equals(action);
            if (update) {
                Integer authorId = parsePositiveInt(req.getParameter("authorId"));
                if (authorId == null) {
                    throw new IllegalArgumentException("Mã tác giả không hợp lệ.");
                }
                author.setAuthorId(authorId);
            }

            author.setAuthorName(req.getParameter("authorName"));
            author.setDateOfBirth(parseOptionalDate(req.getParameter("dateOfBirth")));
            if (update) {
                authorService.update(author);
            } else {
                authorService.insert(author);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/authors?status="
                    + (update ? "updated" : "created"));
        } catch (IllegalArgumentException e) {
            req.setAttribute("alert", e.getMessage());
            showForm(author, req, resp);
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long totalItems = authorService.countAll();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        int currentPage = parsePage(req.getParameter("page"));
        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        }

        req.setAttribute("authors", authorService.findPage(currentPage, PAGE_SIZE));
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("success", statusMessage(req.getParameter("status")));
        req.getRequestDispatcher("/views/admin/authors/list.jsp").forward(req, resp);
    }

    private void showForm(Author_24110349 author, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("author", author);
        req.setAttribute("editing", author.getAuthorId() > 0);
        req.getRequestDispatcher("/views/admin/authors/form.jsp").forward(req, resp);
    }

    private LocalDate parseOptionalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ.");
        }
    }

    private Integer parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int parsePage(String value) {
        Integer page = parsePositiveInt(value);
        return page == null ? 1 : page;
    }

    private String statusMessage(String status) {
        if ("created".equals(status)) return "Đã thêm tác giả thành công.";
        if ("updated".equals(status)) return "Đã cập nhật tác giả thành công.";
        if ("deleted".equals(status)) return "Đã xóa tác giả thành công.";
        return null;
    }
}
