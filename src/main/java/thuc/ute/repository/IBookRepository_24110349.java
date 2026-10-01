package thuc.ute.repository;

import java.util.List;
import java.util.Set;

import thuc.ute.entity.Book_24110349;
import thuc.ute.model.RatingView_24110349;

public interface IBookRepository_24110349 {
    List<Book_24110349> findPage(int page, int pageSize);

    long countAll();

    Book_24110349 findById(int bookId);

    List<RatingView_24110349> findReviewsByBookId(int bookId);

    void saveReview(int userId, int bookId, short rating, String reviewText);

    void insert(Book_24110349 book, Set<Integer> authorIds);

    void update(Book_24110349 book, Set<Integer> authorIds);

    void delete(int bookId);

    boolean existsByIsbn(Integer isbn, Integer excludedBookId);
}
