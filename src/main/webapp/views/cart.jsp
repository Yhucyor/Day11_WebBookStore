<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<head>
    <title>Giỏ hàng của bạn</title>
</head>
<body>
    <section class="cart-page" style="max-width: 1000px; margin: 40px auto; padding: 0 20px;">
        <div class="section-title-row">
            <div>
                <p class="book-page-eyebrow">YOUR CART</p>
                <h1>Giỏ hàng của bạn</h1>
            </div>
        </div>

        <c:if test="${not empty alert}">
            <div class="alert-error" style="margin-bottom: 20px;"><c:out value="${alert}" /></div>
        </c:if>

        <c:choose>
            <c:when test="${empty cart or cart.size() == 0}">
                <div class="book-empty" style="text-align: center; padding: 60px 20px; background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                    <span style="font-size: 3rem; display: block; margin-bottom: 20px;">🛒</span>
                    <h2>Giỏ hàng trống</h2>
                    <p>Bạn chưa thêm sản phẩm nào vào giỏ hàng.</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary" style="display: inline-block; margin-top: 20px;">Tiếp tục mua sắm</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="cart-content" style="display: grid; gap: 30px; grid-template-columns: 2fr 1fr;">
                    <div class="cart-items" style="display: flex; flex-direction: column; gap: 20px;">
                        
                        <c:forEach var="entry" items="${cart}">
                            <c:set var="item" value="${entry.value}" />
                            <c:set var="book" value="${item.book}" />
                            
                            <article class="cart-item-card" style="display: flex; gap: 20px; background: #fff; padding: 20px; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); align-items: center;">
                                <!-- Checkbox chọn sản phẩm -->
                                <div style="display: flex; align-items: center;">
                                    <input type="checkbox" class="item-checkbox" value="${book.bookId}" data-price="${item.totalPrice}" checked 
                                           style="width: 20px; height: 20px; cursor: pointer;" onchange="calculateTotal()">
                                </div>
                                
                                <div class="cart-item-image" style="width: 100px; flex-shrink: 0;">
                                    <c:choose>
                                        <c:when test="${empty book.coverImage}">
                                            <c:url var="coverUrl" value="/assets/images/book-default.svg" />
                                        </c:when>
                                        <c:when test="${fn:startsWith(book.coverImage, 'http://') or fn:startsWith(book.coverImage, 'https://')}">
                                            <c:set var="coverUrl" value="${book.coverImage}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:url var="coverUrl" value="/${book.coverImage}" />
                                        </c:otherwise>
                                    </c:choose>
                                    <img src="${coverUrl}" alt="Bìa sách" style="width: 100%; border-radius: 4px; object-fit: cover;"
                                         onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/book-default.svg';">
                                </div>
                                <div class="cart-item-details" style="flex: 1; display: flex; flex-direction: column; justify-content: space-between;">
                                    <div>
                                        <h3 style="margin: 0 0 10px 0; font-size: 1.25rem;">
                                            <a href="${pageContext.request.contextPath}/book-detail?id=${book.bookId}" style="color: #333; text-decoration: none;">
                                                <c:out value="${book.title}" />
                                            </a>
                                        </h3>
                                        <p style="margin: 0; color: #666; font-size: 0.95rem;">Đơn giá: <strong><c:out value="${book.price}" /> VNĐ</strong></p>
                                    </div>
                                    <div style="display: flex; align-items: center; justify-content: space-between; margin-top: 15px;">
                                        <form action="${pageContext.request.contextPath}/cart" method="post" style="display: flex; align-items: center; gap: 10px;">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="bookId" value="${book.bookId}">
                                            <label for="qty-${book.bookId}" class="visually-hidden">Số lượng</label>
                                            <input type="number" id="qty-${book.bookId}" name="quantity" value="${item.quantity}" min="1" max="${book.quantity}" style="width: 70px; padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
                                            <button type="submit" class="btn" style="padding: 6px 12px; font-size: 0.85rem; background: #eee; color: #333;">Cập nhật</button>
                                        </form>
                                        
                                        <form action="${pageContext.request.contextPath}/cart" method="post" style="margin: 0;">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="bookId" value="${book.bookId}">
                                            <button type="submit" class="btn" style="background: transparent; color: #d32f2f; padding: 0; text-decoration: underline;" onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này?');">Xóa</button>
                                        </form>
                                    </div>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                    
                    <aside class="cart-summary" style="background: #fff; padding: 30px; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); height: fit-content; position: sticky; top: 20px;">
                        <h2 style="margin-top: 0; font-size: 1.5rem; border-bottom: 1px solid #eee; padding-bottom: 15px; margin-bottom: 20px;">Tóm tắt đơn hàng</h2>
                        <div style="display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 1.1rem;">
                            <span>Đã chọn:</span>
                            <strong id="selectedItemsCount">0</strong>
                        </div>
                        <div style="display: flex; justify-content: space-between; margin-bottom: 25px; font-size: 1.2rem;">
                            <span>Tổng tiền:</span>
                            <strong style="color: #2e7d32;" id="selectedTotalPrice">0 VNĐ</strong>
                        </div>
                        
                        <button onclick="proceedToCheckout()" class="btn btn-primary" style="display: block; width: 100%; padding: 12px; font-size: 1.1rem; text-align: center; box-sizing: border-box; border: none; cursor: pointer;">Tiến hành đặt hàng</button>
                        
                        <div style="text-align: center; margin-top: 20px;">
                            <a href="${pageContext.request.contextPath}/home" style="color: #1976d2; text-decoration: none;">← Tiếp tục mua sắm</a>
                        </div>
                    </aside>
                </div>
                
                <script>
                    function calculateTotal() {
                        const checkboxes = document.querySelectorAll('.item-checkbox');
                        let total = 0;
                        let count = 0;
                        checkboxes.forEach(cb => {
                            if (cb.checked) {
                                total += parseFloat(cb.getAttribute('data-price'));
                                count++;
                            }
                        });
                        document.getElementById('selectedItemsCount').innerText = count;
                        document.getElementById('selectedTotalPrice').innerText = total.toFixed(2) + ' VNĐ';
                    }
                    
                    function proceedToCheckout() {
                        const checkboxes = document.querySelectorAll('.item-checkbox:checked');
                        if (checkboxes.length === 0) {
                            alert("Vui lòng chọn ít nhất 1 sản phẩm để thanh toán!");
                            return;
                        }
                        
                        const form = document.createElement('form');
                        form.method = 'GET';
                        form.action = '${pageContext.request.contextPath}/checkout';
                        
                        checkboxes.forEach(cb => {
                            const input = document.createElement('input');
                            input.type = 'hidden';
                            input.name = 'selectedIds';
                            input.value = cb.value;
                            form.appendChild(input);
                        });
                        
                        document.body.appendChild(form);
                        form.submit();
                    }

                    // Tính toán lần đầu khi load trang
                    document.addEventListener('DOMContentLoaded', calculateTotal);
                </script>
            </c:otherwise>
        </c:choose>
    </section>
</body>
