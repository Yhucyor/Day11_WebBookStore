<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<head>
    <title>Lịch sử đặt hàng</title>
</head>
<body>
    <section class="admin-page admin-form-page" style="width: min(1000px, calc(100% - 32px)); max-width: 1000px;">
        <div class="admin-page-header">
            <div>
                <p class="book-page-eyebrow">YOUR ACCOUNT</p>
                <h1>Lịch sử đặt hàng</h1>
            </div>
            
            <!-- Bộ lọc trạng thái -->
            <form action="${pageContext.request.contextPath}/user/orders" method="get" style="display: flex; gap: 10px; align-items: center; margin: 0;">
                <label for="statusFilter" style="font-weight: 600; color: #486581;">Lọc theo trạng thái:</label>
                <select id="statusFilter" name="status" onchange="this.form.submit()" style="padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 8px; font: inherit;">
                    <option value="Tất cả" ${currentStatus == 'Tất cả' ? 'selected' : ''}>Tất cả</option>
                    <option value="Đơn hàng mới" ${currentStatus == 'Đơn hàng mới' ? 'selected' : ''}>Đơn hàng mới</option>
                    <option value="Đã xác nhận" ${currentStatus == 'Đã xác nhận' ? 'selected' : ''}>Đã xác nhận</option>
                    <option value="Chuẩn bị hàng" ${currentStatus == 'Chuẩn bị hàng' ? 'selected' : ''}>Chuẩn bị hàng</option>
                    <option value="Vận chuyển" ${currentStatus == 'Vận chuyển' ? 'selected' : ''}>Vận chuyển</option>
                    <option value="Giao hàng" ${currentStatus == 'Giao hàng' ? 'selected' : ''}>Giao hàng</option>
                    <option value="Đã giao" ${currentStatus == 'Đã giao' ? 'selected' : ''}>Đã giao</option>
                    <option value="Đơn hàng hủy" ${currentStatus == 'Đơn hàng hủy' ? 'selected' : ''}>Đơn hàng hủy</option>
                    <option value="Đơn hàng hoàn" ${currentStatus == 'Đơn hàng hoàn' ? 'selected' : ''}>Đơn hàng hoàn</option>
                </select>
            </form>
        </div>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="book-empty">
                    <span style="display:block; font-size:3rem; margin-bottom: 20px;">📦</span>
                    <h2>Không tìm thấy đơn hàng</h2>
                    <p>Bạn không có đơn hàng nào khớp với trạng thái "${currentStatus}".</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary" style="display: inline-block; margin-top: 20px;">Tiếp tục mua sắm</a>
                </div>
            </c:when>
            <c:otherwise>
                <div style="display: flex; flex-direction: column; gap: 20px;">
                    <c:forEach var="order" items="${orders}">
                        <article style="background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05); overflow: hidden;">
                            
                            <!-- Header đơn hàng -->
                            <div style="background: #f8fafc; padding: 16px 20px; border-bottom: 1px solid #e2e8f0; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px;">
                                <div>
                                    <strong style="color: #102a43; font-size: 1.1rem;">Đơn hàng #${order.orderId}</strong>
                                    <span style="color: #627d98; margin-left: 15px; font-size: 0.9rem;">
                                        Ngày đặt: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                                    </span>
                                </div>
                                <div style="display: flex; gap: 10px; align-items: center;">
                                    <span style="font-size: 0.9rem; color: #486581;">Trạng thái:</span>
                                    <span style="background: #dbeafe; color: #1e40af; padding: 4px 12px; border-radius: 20px; font-size: 0.85rem; font-weight: 700;">
                                        ${order.status}
                                    </span>
                                </div>
                            </div>
                            
                            <!-- Chi tiết đơn hàng -->
                            <div style="padding: 20px;">
                                <div style="display: grid; gap: 15px;">
                                    <c:forEach var="detail" items="${order.orderDetails}">
                                        <div style="display: flex; gap: 15px; align-items: center;">
                                            <div style="width: 50px; height: 70px; flex-shrink: 0; background: #eee; border-radius: 4px; overflow: hidden;">
                                                <c:choose>
                                                    <c:when test="${empty detail.book.coverImage}">
                                                        <c:set var="coverUrl" value="${pageContext.request.contextPath}/assets/images/book-default.svg" />
                                                    </c:when>
                                                    <c:when test="${fn:startsWith(detail.book.coverImage, 'http')}">
                                                        <c:set var="coverUrl" value="${detail.book.coverImage}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="coverUrl" value="${pageContext.request.contextPath}/${detail.book.coverImage}" />
                                                    </c:otherwise>
                                                </c:choose>
                                                <img src="${coverUrl}" style="width: 100%; height: 100%; object-fit: cover;" onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/book-default.svg';">
                                            </div>
                                            <div style="flex: 1;">
                                                <h3 style="margin: 0 0 5px 0; font-size: 1rem;"><a href="${pageContext.request.contextPath}/book-detail?id=${detail.book.bookId}" style="color: #333; text-decoration: none;">${detail.book.title}</a></h3>
                                                <div style="color: #627d98; font-size: 0.9rem;">
                                                    Phân loại: ${detail.book.publisher}
                                                </div>
                                            </div>
                                            <div style="text-align: right;">
                                                <div style="color: #627d98;">x${detail.quantity}</div>
                                                <strong style="color: #333;">${detail.price} VNĐ</strong>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                
                                <hr style="border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;">
                                
                                <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px;">
                                    <div style="font-size: 0.9rem; color: #486581; max-width: 60%;">
                                        <strong>Người nhận:</strong> ${order.recipientName}<br>
                                        <strong>SĐT:</strong> ${order.phoneNumber}<br>
                                        <strong>Địa chỉ:</strong> ${order.shippingAddress}<br>
                                        <strong>Thanh toán:</strong> ${order.paymentMethod} (${order.paymentStatus})
                                    </div>
                                    <div style="text-align: right;">
                                        <div style="font-size: 0.95rem; color: #486581; margin-bottom: 5px;">Thành tiền</div>
                                        <strong style="font-size: 1.4rem; color: #d32f2f;">${order.totalAmount} VNĐ</strong>
                                    </div>
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</body>
