package thuc.ute.entity;

import java.io.Serializable;
import java.util.Objects;

public class RatingId_24110349 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private int bookId;

    public RatingId_24110349() {
    }

    public RatingId_24110349(int userId, int bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof RatingId_24110349 other)) {
            return false;
        }
        return userId == other.userId && bookId == other.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
