package thuc.ute.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order_24110349 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User_24110349 user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status", columnDefinition = "nvarchar(50)")
    private String status; 

    @Column(name = "shipping_address", columnDefinition = "nvarchar(500)")
    private String shippingAddress;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "recipient_name", columnDefinition = "nvarchar(100)")
    private String recipientName;
    
    @Column(name = "payment_method", columnDefinition = "nvarchar(50)")
    private String paymentMethod; 

    @Column(name = "payment_status", columnDefinition = "nvarchar(50)")
    private String paymentStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail_24110349> orderDetails;

    public Order_24110349() {}

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public User_24110349 getUser() { return user; }
    public void setUser(User_24110349 user) { this.user = user; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public List<OrderDetail_24110349> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetail_24110349> orderDetails) { this.orderDetails = orderDetails; }
}
