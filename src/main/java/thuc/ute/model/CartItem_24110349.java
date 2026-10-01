package thuc.ute.model;

import java.io.Serializable;
import java.math.BigDecimal;
import thuc.ute.entity.Book_24110349;

public class CartItem_24110349 implements Serializable {
    private static final long serialVersionUID = 1L;

    private Book_24110349 book;
    private int quantity;

    public CartItem_24110349() {
    }

    public CartItem_24110349(Book_24110349 book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book_24110349 getBook() {
        return book;
    }

    public void setBook(Book_24110349 book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        if (book != null && book.getPrice() != null) {
            return book.getPrice().multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
}
