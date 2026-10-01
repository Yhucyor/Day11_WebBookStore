package thuc.ute.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetail_24110349 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order_24110349 order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookid", nullable = false)
    private Book_24110349 book;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    public OrderDetail_24110349() {}

    public int getDetailId() { return detailId; }
    public void setDetailId(int detailId) { this.detailId = detailId; }
    public Order_24110349 getOrder() { return order; }
    public void setOrder(Order_24110349 order) { this.order = order; }
    public Book_24110349 getBook() { return book; }
    public void setBook(Book_24110349 book) { this.book = book; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
