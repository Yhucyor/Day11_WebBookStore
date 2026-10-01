package thuc.ute.service.impl;

import java.util.List;
import java.util.Set;

import thuc.ute.entity.Book_24110349;
import thuc.ute.repository.IBookRepository_24110349;
import thuc.ute.repository.impl.BookRepositoryImpl_24110349;
import thuc.ute.service.IBookService_24110349;
import thuc.ute.model.RatingView_24110349;

public class BookServiceImpl_24110349 implements IBookService_24110349 {
    private final IBookRepository_24110349 bookRepository = new BookRepositoryImpl_24110349();

    @Override
    public List<Book_24110349> findPage(int page, int pageSize) {
        return bookRepository.findPage(page, pageSize);
    }

    @Override
    public long countAll() {
        return bookRepository.countAll();
    }

    @Override
    public Book_24110349 findById(int bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public List<RatingView_24110349> findReviewsByBookId(int bookId) {
        return bookRepository.findReviewsByBookId(bookId);
    }

    @Override
    public void saveReview(int userId, int bookId, short rating, String reviewText) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Điểm đánh giá phải từ 1 đến 5.");
        }
        if (reviewText == null || reviewText.isBlank()) {
            throw new IllegalArgumentException("Nội dung review không được để trống.");
        }
        bookRepository.saveReview(userId, bookId, rating, reviewText.trim());
    }

    @Override
    public void insert(Book_24110349 book, Set<Integer> authorIds) {
        validate(book, null);
        bookRepository.insert(book, authorIds);
    }

    @Override
    public void update(Book_24110349 book, Set<Integer> authorIds) {
        validate(book, book.getBookId());
        bookRepository.update(book, authorIds);
    }

    @Override
    public void delete(int bookId) {
        bookRepository.delete(bookId);
    }

    @Override
    public boolean existsByIsbn(Integer isbn, Integer excludedBookId) {
        return bookRepository.existsByIsbn(isbn, excludedBookId);
    }

    private void validate(Book_24110349 book, Integer excludedBookId) {
        if (book == null || book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Tiêu đề sách không được để trống.");
        }
        book.setTitle(book.getTitle().trim());
        if (book.getTitle().length() > 200) {
            throw new IllegalArgumentException("Tiêu đề sách không được vượt quá 200 ký tự.");
        }
        if (book.getPublisher() != null) {
            book.setPublisher(book.getPublisher().trim());
            if (book.getPublisher().length() > 100) {
                throw new IllegalArgumentException("Nhà xuất bản không được vượt quá 100 ký tự.");
            }
        }
        if (book.getCoverImage() != null) {
            book.setCoverImage(book.getCoverImage().trim());
            if (book.getCoverImage().length() > 500) {
                throw new IllegalArgumentException("Đường dẫn ảnh không được vượt quá 500 ký tự.");
            }
            if (!book.getCoverImage().startsWith("https://")
                    && !book.getCoverImage().startsWith("http://")) {
                throw new IllegalArgumentException("URL ảnh phải bắt đầu bằng http:// hoặc https://.");
            }
        }
        if (book.getPrice() != null && (book.getPrice().signum() < 0
                || book.getPrice().compareTo(new java.math.BigDecimal("9999.99")) > 0)) {
            throw new IllegalArgumentException("Giá sách phải từ 0 đến 9999.99.");
        }
        if (book.getQuantity() != null && book.getQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng sách không được âm.");
        }
        if (bookRepository.existsByIsbn(book.getIsbn(), excludedBookId)) {
            throw new IllegalArgumentException("Mã ISBN đã tồn tại.");
        }
    }
}
