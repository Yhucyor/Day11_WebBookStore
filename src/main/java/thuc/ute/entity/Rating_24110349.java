package thuc.ute.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "rating")
@IdClass(RatingId_24110349.class)
public class Rating_24110349 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "userid")
    private int userId;

    @Id
    @Column(name = "bookid")
    private int bookId;

    @Column(name = "rating")
    private Short rating;

    @Column(name = "review_text", columnDefinition = "text")
    private String reviewText;

    public Rating_24110349() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
