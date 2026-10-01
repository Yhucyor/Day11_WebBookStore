package thuc.ute.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "cart_items")
public class CartItem_24110349 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User_24110349 user;

    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private Book_24110349 book;

    private int quantity;

    public CartItem_24110349() {
    }

    public CartItem_24110349(User_24110349 user, Book_24110349 book, int quantity) {
        this.user = user;
        this.book = book;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User_24110349 getUser() {
        return user;
    }

    public void setUser(User_24110349 user) {
        this.user = user;
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

    @Transient
    public BigDecimal getTotalPrice() {
        if (book != null && book.getPrice() != null) {
            return book.getPrice().multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
}
