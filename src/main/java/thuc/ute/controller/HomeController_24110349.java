package thuc.ute.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import thuc.ute.service.IBookService_24110349;
import thuc.ute.service.impl.BookServiceImpl_24110349;

@WebServlet(urlPatterns = {"/home", "/products"})
public class HomeController_24110349 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;

    private final IBookService_24110349 bookService = new BookServiceImpl_24110349();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long totalBooks = bookService.countAll();
        int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);
        int currentPage = parsePage(req.getParameter("page"));

        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        }

        req.setAttribute("books", bookService.findPage(currentPage, PAGE_SIZE));
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalBooks", totalBooks);
        req.getRequestDispatcher("/views/home.jsp").forward(req, resp);
    }

    private int parsePage(String pageValue) {
        try {
            return Math.max(1, Integer.parseInt(pageValue));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
