package thuc.ute.repository.impl;

import java.util.List;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import thuc.ute.config.JpaConfig_24110349;
import thuc.ute.entity.Book_24110349;
import thuc.ute.entity.Author_24110349;
import thuc.ute.entity.RatingId_24110349;
import thuc.ute.entity.Rating_24110349;
import thuc.ute.model.RatingView_24110349;
import thuc.ute.repository.IBookRepository_24110349;

public class BookRepositoryImpl_24110349 implements IBookRepository_24110349 {

    @Override
    public List<Book_24110349> findPage(int page, int pageSize) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            TypedQuery<Book_24110349> query = entityManager.createQuery(
                    "SELECT b FROM Book_24110349 b ORDER BY b.bookId ASC",
                    Book_24110349.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Book_24110349> books = query.getResultList();
            TypedQuery<Long> reviewQuery = entityManager.createQuery(
                    "SELECT COUNT(r) FROM Rating_24110349 r WHERE r.bookId = :bookId",
                    Long.class);

            for (Book_24110349 book : books) {
                // Khởi tạo tập tác giả trước khi đóng EntityManager.
                book.getAuthors().size();
                reviewQuery.setParameter("bookId", book.getBookId());
                book.setReviewCount(reviewQuery.getSingleResult());
            }
            return books;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countAll() {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT COUNT(b) FROM Book_24110349 b", Long.class)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Book_24110349 findById(int bookId) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            Book_24110349 book = entityManager.find(Book_24110349.class, bookId);
            if (book != null) {
                book.getAuthors().size();
                Long reviewCount = entityManager.createQuery(
                        "SELECT COUNT(r) FROM Rating_24110349 r WHERE r.bookId = :bookId",
                        Long.class)
                        .setParameter("bookId", bookId)
                        .getSingleResult();
                book.setReviewCount(reviewCount);
            }
            return book;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<RatingView_24110349> findReviewsByBookId(int bookId) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            return entityManager.createQuery(
                    "SELECT new thuc.ute.model.RatingView_24110349("+
                            "u.id, u.fullname, r.rating, r.reviewText) " +
                            "FROM Rating_24110349 r, User_24110349 u " +
                            "WHERE r.userId = u.id AND r.bookId = :bookId " +
                            "ORDER BY u.fullname ASC",
                    RatingView_24110349.class)
                    .setParameter("bookId", bookId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void saveReview(int userId, int bookId, short rating, String reviewText) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            RatingId_24110349 ratingId = new RatingId_24110349(userId, bookId);
            Rating_24110349 savedRating = entityManager.find(Rating_24110349.class, ratingId);

            if (savedRating == null) {
                savedRating = new Rating_24110349();
                savedRating.setUserId(userId);
                savedRating.setBookId(bookId);
                savedRating.setRating(rating);
                savedRating.setReviewText(reviewText);
                entityManager.persist(savedRating);
            } else {
                savedRating.setRating(rating);
                savedRating.setReviewText(reviewText);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void insert(Book_24110349 book, Set<Integer> authorIds) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            attachAuthors(book, authorIds, entityManager);
            entityManager.persist(book);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Book_24110349 book, Set<Integer> authorIds) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Book_24110349 savedBook = entityManager.find(Book_24110349.class, book.getBookId());
            if (savedBook == null) {
                throw new IllegalArgumentException("Không tìm thấy sách.");
            }

            savedBook.setIsbn(book.getIsbn());
            savedBook.setTitle(book.getTitle());
            savedBook.setPublisher(book.getPublisher());
            savedBook.setPrice(book.getPrice());
            savedBook.setDescription(book.getDescription());
            savedBook.setPublishDate(book.getPublishDate());
            savedBook.setCoverImage(book.getCoverImage());
            savedBook.setQuantity(book.getQuantity());
            attachAuthors(savedBook, authorIds, entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(int bookId) {
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.createNativeQuery("DELETE FROM rating WHERE bookid = :bookId")
                    .setParameter("bookId", bookId)
                    .executeUpdate();
            entityManager.createNativeQuery("DELETE FROM book_author WHERE bookid = :bookId")
                    .setParameter("bookId", bookId)
                    .executeUpdate();
            Book_24110349 book = entityManager.find(Book_24110349.class, bookId);
            if (book != null) {
                entityManager.remove(book);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean existsByIsbn(Integer isbn, Integer excludedBookId) {
        if (isbn == null) {
            return false;
        }
        EntityManager entityManager = JpaConfig_24110349.getEntityManager();
        try {
            String jpql = "SELECT COUNT(b) FROM Book_24110349 b WHERE b.isbn = :isbn";
            if (excludedBookId != null) {
                jpql += " AND b.bookId <> :excludedBookId";
            }
            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                    .setParameter("isbn", isbn);
            if (excludedBookId != null) {
                query.setParameter("excludedBookId", excludedBookId);
            }
            return query.getSingleResult() > 0;
        } finally {
            entityManager.close();
        }
    }

    private void attachAuthors(Book_24110349 book, Set<Integer> authorIds,
            EntityManager entityManager) {
        book.getAuthors().clear();
        if (authorIds == null) {
            return;
        }
        for (Integer authorId : authorIds) {
            if (authorId == null) {
                continue;
            }
            Author_24110349 author = entityManager.find(Author_24110349.class, authorId);
            if (author != null) {
                book.getAuthors().add(author);
            }
        }
    }
}
