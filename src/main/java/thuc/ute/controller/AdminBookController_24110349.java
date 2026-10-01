package thuc.ute.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import thuc.ute.entity.Book_24110349;
import thuc.ute.service.IAuthorService_24110349;
import thuc.ute.service.IBookService_24110349;
import thuc.ute.service.impl.AuthorServiceImpl_24110349;
import thuc.ute.service.impl.BookServiceImpl_24110349;
import thuc.ute.utils.CloudinaryUtil_24110349;

@WebServlet(urlPatterns = {"/admin/books"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = CloudinaryUtil_24110349.MAX_IMAGE_SIZE,
        maxRequestSize = 6L * 1024 * 1024)
public class AdminBookController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;

    private final IBookService_24110349 bookService = new BookServiceImpl_24110349();
    private final IAuthorService_24110349 authorService = new AuthorServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            showForm(new Book_24110349(), Set.of(), req, resp);
            return;
        }
        if ("edit".equals(action)) {
            Integer bookId = parsePositiveInt(req.getParameter("id"));
            Book_24110349 book = bookId == null ? null : bookService.findById(bookId);
            if (book == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sách.");
                return;
            }
            Set<Integer> selectedAuthorIds = book.getAuthors().stream()
                    .map(author -> author.getAuthorId())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            showForm(book, selectedAuthorIds, req, resp);
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
            Integer bookId = parsePositiveInt(req.getParameter("id"));
            if (bookId != null) {
                bookService.delete(bookId);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/books?status=deleted");
            return;
        }

        Set<Integer> authorIds = parseAuthorIds(req.getParameterValues("authorIds"));
        Book_24110349 book = new Book_24110349();
        try {
            boolean update = "update".equals(action);
            if (update) {
                Integer bookId = parsePositiveInt(req.getParameter("bookId"));
                if (bookId == null) {
                    throw new IllegalArgumentException("Mã sách không hợp lệ.");
                }
                book.setBookId(bookId);
            }

            bindBook(book, req);
            if (update) {
                bookService.update(book, authorIds);
            } else {
                bookService.insert(book, authorIds);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/books?status="
                    + (update ? "updated" : "created"));
        } catch (IllegalArgumentException | IllegalStateException | IOException | ServletException e) {
            req.setAttribute("alert", e.getMessage());
            showForm(book, authorIds, req, resp);
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long totalItems = bookService.countAll();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        int currentPage = parsePage(req.getParameter("page"));
        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        }

        req.setAttribute("books", bookService.findPage(currentPage, PAGE_SIZE));
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("success", statusMessage(req.getParameter("status")));
        req.getRequestDispatcher("/views/admin/books/list.jsp").forward(req, resp);
    }

    private void showForm(Book_24110349 book, Set<Integer> authorIds,
            HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("book", book);
        req.setAttribute("allAuthors", authorService.findAll());
        req.setAttribute("selectedAuthorIds", authorIds);
        req.setAttribute("editing", book.getBookId() > 0);
        req.getRequestDispatcher("/views/admin/books/form.jsp").forward(req, resp);
    }

    private void bindBook(Book_24110349 book, HttpServletRequest req)
            throws IOException, ServletException {
        book.setTitle(required(req.getParameter("title"), "Tiêu đề sách"));
        book.setIsbn(parseOptionalInt(req.getParameter("isbn"), "ISBN"));
        book.setPublisher(blankToNull(req.getParameter("publisher")));
        book.setPrice(parseOptionalDecimal(req.getParameter("price"), "Giá"));
        book.setDescription(blankToNull(req.getParameter("description")));
        book.setPublishDate(parseOptionalDate(req.getParameter("publishDate")));
        String coverImage = blankToNull(req.getParameter("coverImage"));
        Part coverFile = req.getPart("coverFile");
        if (coverFile != null && coverFile.getSize() > 0) {
            coverImage = CloudinaryUtil_24110349.uploadImage(
                    coverFile, "ktra-web-24110349/books");
        }
        book.setCoverImage(coverImage);
        book.setQuantity(parseOptionalInt(req.getParameter("quantity"), "Số lượng"));
    }

    private Set<Integer> parseAuthorIds(String[] values) {
        if (values == null) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(values)
                .map(this::parsePositiveInt)
                .filter(value -> value != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Integer parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseOptionalInt(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " không hợp lệ.");
        }
    }

    private BigDecimal parseOptionalDecimal(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " không hợp lệ.");
        }
    }

    private LocalDate parseOptionalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày xuất bản không hợp lệ.");
        }
    }

    private String required(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        return value.trim();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private int parsePage(String value) {
        Integer page = parsePositiveInt(value);
        return page == null ? 1 : page;
    }

    private String statusMessage(String status) {
        if ("created".equals(status)) return "Đã thêm sách thành công.";
        if ("updated".equals(status)) return "Đã cập nhật sách thành công.";
        if ("deleted".equals(status)) return "Đã xóa sách thành công.";
        return null;
    }
}
