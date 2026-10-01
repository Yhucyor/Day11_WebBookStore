<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<head>
    <title>Thanh toán đơn hàng</title>
</head>
<body>
    <section class="cart-page" style="max-width: 800px; margin: 40px auto; padding: 0 20px;">
        <div class="section-title-row">
            <div>
                <p class="book-page-eyebrow">CHECKOUT</p>
                <h1>Thanh toán COD</h1>
            </div>
        </div>

        <c:if test="${not empty alert}">
            <div class="alert-error" style="margin-bottom: 20px;"><c:out value="${alert}" /></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/checkout" method="post" style="display: grid; gap: 30px; grid-template-columns: 1fr 1fr;">
            <!-- Cột trái: Thông tin nhận hàng -->
            <div class="checkout-form" style="background: #fff; padding: 30px; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                <h2 style="margin-top: 0; font-size: 1.3rem; margin-bottom: 20px;">Thông tin nhận hàng</h2>
                
                <div class="form-group">
                    <label for="recipientName">Họ tên người nhận *</label>
                    <input type="text" id="recipientName" name="recipientName" value="${sessionScope.account.fullname}" required placeholder="Nhập họ tên người nhận">
                </div>
                
                <div class="form-group">
                    <label for="phone">Số điện thoại *</label>
                    <input type="tel" id="phone" name="phone" required placeholder="Nhập số điện thoại của bạn" maxlength="20">
                </div>
                
                <div class="form-group">
                    <label for="address">Địa chỉ giao hàng *</label>
                    <textarea id="address" name="address" required placeholder="Nhập địa chỉ nhận hàng chi tiết..." rows="4" style="width: 100%; padding: 12px; border: 1px solid #d1d5db; border-radius: 8px; font-family: inherit; resize: vertical; box-sizing: border-box;"></textarea>
                </div>
                
                <div class="form-group">
                    <label>Phương thức thanh toán</label>
                    <div style="padding: 15px; border: 1px solid #4f46e5; border-radius: 8px; background: rgba(79, 70, 229, 0.05); display: flex; align-items: center; gap: 10px;">
                        <input type="radio" checked id="cod" readonly style="accent-color: #4f46e5; width: 18px; height: 18px;">
                        <label for="cod" style="margin: 0; font-weight: 600; color: #4f46e5; cursor: pointer;">Thanh toán khi nhận hàng (COD)</label>
                    </div>
                </div>
            </div>

            <!-- Cột phải: Tóm tắt -->
            <div class="cart-summary" style="background: #fff; padding: 30px; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); height: fit-content;">
                <h2 style="margin-top: 0; font-size: 1.3rem; border-bottom: 1px solid #eee; padding-bottom: 15px; margin-bottom: 20px;">Đơn hàng của bạn</h2>
                
                <input type="hidden" name="selectedIdsStr" value="${selectedIdsStr}">
                
                <div style="margin-bottom: 20px; max-height: 250px; overflow-y: auto; padding-right: 10px;">
                    <c:forEach var="item" items="${selectedItems}">
                        <div style="display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 0.95rem;">
                            <div style="flex: 1; padding-right: 10px;">
                                <strong style="color: #333;"><c:out value="${item.book.title}" /></strong>
                                <div style="color: #666; margin-top: 5px;">SL: ${item.quantity} x <c:out value="${item.book.price}" /> VNĐ</div>
                            </div>
                            <div style="font-weight: 600;"><c:out value="${item.totalPrice}" /></div>
                        </div>
                    </c:forEach>
                </div>
                
                <div style="display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 1.1rem; border-top: 1px solid #eee; padding-top: 15px;">
                    <span>Tổng số sản phẩm:</span>
                    <strong>${checkoutTotalItems}</strong>
                </div>
                <div style="display: flex; justify-content: space-between; margin-bottom: 25px; font-size: 1.2rem;">
                    <span>Tổng thanh toán:</span>
                    <strong style="color: #2e7d32;"><c:out value="${checkoutTotalPrice}" /> VNĐ</strong>
                </div>
                
                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 12px; font-size: 1.1rem; text-align: center;">Xác nhận đặt hàng</button>
                <div style="text-align: center; margin-top: 20px;">
                    <a href="${pageContext.request.contextPath}/cart" style="color: #666; text-decoration: none;">← Quay lại giỏ hàng</a>
                </div>
            </div>
        </form>
    </section>
</body>
