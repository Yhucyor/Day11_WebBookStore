package thuc.ute.model;

public class RatingView_24110349 {
    private final int userId;
    private final String userFullname;
    private final Short rating;
    private final String reviewText;

    public RatingView_24110349(int userId, String userFullname, Short rating, String reviewText) {
        this.userId = userId;
        this.userFullname = userFullname;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public Short getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }
}
