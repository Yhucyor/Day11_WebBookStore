<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<head>
    <title><c:out value="${book.title}" /></title>
</head>
<body>
    <section class="book-detail-page">
        <a class="back-to-books" href="${pageContext.request.contextPath}/home">← Quay lại danh sách sách</a>

        <c:if test="${not empty alert}">
            <div class="alert-error"><c:out value="${alert}" /></div>
            <c:remove var="alert" scope="session" />
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert-success"><c:out value="${success}" /></div>
            <c:remove var="success" scope="session" />
        </c:if>

        <article class="book-detail-card">
            <div class="book-detail-cover">
                <c:choose>
                    <c:when test="${empty book.coverImage}">
                        <c:url var="detailCoverUrl" value="/assets/images/book-default.svg" />
                    </c:when>
                    <c:when test="${fn:startsWith(book.coverImage, 'http://') or fn:startsWith(book.coverImage, 'https://')}">
                        <c:set var="detailCoverUrl" value="${book.coverImage}" />
                    </c:when>
                    <c:otherwise>
                        <c:url var="detailCoverUrl" value="/${book.coverImage}" />
                    </c:otherwise>
                </c:choose>
                <img src="${detailCoverUrl}" alt="Bìa sách ${fn:escapeXml(book.title)}"
                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/book-default.svg';">
            </div>

            <div class="book-detail-info">
                <p class="book-page-eyebrow">BOOK DETAIL</p>
                <h1><c:out value="${book.title}" /></h1>
                <dl>
                    <div><dt>Giá</dt><dd><strong style="color: #d32f2f; font-size: 1.2rem;"><c:out value="${book.price}" default="0" /> VNĐ</strong></dd></div>
                    <div><dt>Mã ISBN</dt><dd><c:out value="${book.isbn}" default="Đang cập nhật" /></dd></div>
                    <div>
                        <dt>Tác giả</dt>
                        <dd>
                            <c:choose>
                                <c:when test="${empty book.authors}">Đang cập nhật</c:when>
                                <c:otherwise>
                                    <c:forEach var="author" items="${book.authors}" varStatus="status">
                                        <c:out value="${author.authorName}"/><c:if test="${not status.last}">, </c:if>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </dd>
                    </div>
                    <div><dt>Nhà xuất bản</dt><dd><c:out value="${book.publisher}" default="Đang cập nhật" /></dd></div>
                    <div><dt>Ngày xuất bản</dt><dd><c:out value="${book.publishDate}" default="Đang cập nhật" /></dd></div>
                    <div><dt>Số lượng</dt><dd><c:out value="${book.quantity}" default="0" /></dd></div>
                    <div><dt>Reviews</dt><dd>${book.reviewCount}</dd></div>
                </dl>
                <c:if test="${not empty book.description}">
                    <p class="book-description"><c:out value="${book.description}" /></p>
                </c:if>
                
                <div style="margin-top: 20px;">
                    <form action="${pageContext.request.contextPath}/cart" method="post" style="display: flex; gap: 10px; align-items: center;">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="bookId" value="${book.bookId}">
                        <label for="quantity">Số lượng:</label>
                        <input type="number" id="quantity" name="quantity" value="1" min="1" max="${book.quantity}" style="width: 70px; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                        <button type="submit" class="btn btn-primary" ${book.quantity <= 0 ? 'disabled' : ''}>
                            ${book.quantity <= 0 ? 'Hết hàng' : 'Thêm vào giỏ hàng'}
                        </button>
                    </form>
                </div>
            </div>
        </article>

        <section class="reviews-section">
            <div class="section-title-row">
                <div>
                    <p class="book-page-eyebrow">COMMUNITY</p>
                    <h2>Reviews (${book.reviewCount})</h2>
                </div>
            </div>

            <c:choose>
                <c:when test="${empty reviews}">
                    <div class="review-empty">Chưa có review. Hãy là người đầu tiên đánh giá cuốn sách này.</div>
                </c:when>
                <c:otherwise>
                    <div class="review-list">
                        <c:forEach var="review" items="${reviews}">
                            <c:set var="reviewerName" value="${empty review.userFullname ? 'User' : review.userFullname}" />
                            <article class="review-item">
                                <div class="review-avatar"><c:out value="${fn:substring(reviewerName, 0, 1)}" /></div>
                                <div>
                                    <div class="review-heading">
                                        <strong><c:out value="${reviewerName}" /></strong>
                                        <c:if test="${review.rating != null}">
                                            <span class="review-stars" aria-label="${review.rating} trên 5 sao">
                                                <c:forEach begin="1" end="${review.rating}">★</c:forEach>
                                            </span>
                                        </c:if>
                                    </div>
                                    <p><c:out value="${review.reviewText}" /></p>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

        <section class="review-form-card">
            <p class="book-page-eyebrow">YOUR REVIEW</p>
            <h2>Thêm review</h2>
            <p>Mỗi tài khoản có một review cho mỗi sách. Gửi lại form sẽ cập nhật review cũ.</p>

            <form action="${pageContext.request.contextPath}/book-detail" method="post">
                <input type="hidden" name="bookId" value="${book.bookId}">
                <div class="review-form-grid">
                    <div class="form-group">
                        <label for="rating">Điểm đánh giá</label>
                        <select id="rating" name="rating" required>
                            <option value="">Chọn số sao</option>
                            <option value="5">5 sao - Xuất sắc</option>
                            <option value="4">4 sao - Rất tốt</option>
                            <option value="3">3 sao - Tốt</option>
                            <option value="2">2 sao - Trung bình</option>
                            <option value="1">1 sao - Chưa tốt</option>
                        </select>
                    </div>
                    <div class="form-group review-text-group">
                        <label for="reviewText">Nội dung review</label>
                        <textarea id="reviewText" name="reviewText" rows="5" maxlength="2000"
                                  placeholder="Chia sẻ cảm nhận của bạn về cuốn sách..." required><c:out value="${param.reviewText}" /></textarea>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Gửi review</button>
            </form>
        </section>
    </section>

    <script>
        document.querySelector('form[action$="/cart"]').addEventListener('submit', function(e) {
            const actionInput = this.querySelector('input[name="action"]');
            if (actionInput && actionInput.value === 'add') {
                e.preventDefault(); // Chặn việc load sang trang Cart
                
                const formData = new FormData(this);
                const data = new URLSearchParams(formData);
                
                // 1. Gửi request AJAX
                fetch(this.getAttribute('action'), {
                    method: 'POST',
                    body: data.toString(), // Convert explicitly to string
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    credentials: 'same-origin' // Bắt buộc gửi kèm session cookie
                }).then(response => {
                    if (!response.ok) {
                        throw new Error("Server trả về lỗi " + response.status);
                    }
                    if (response.redirected && response.url.includes('/login')) {
                        // Nếu session đã mất, redirect sang trang login
                        window.location.href = response.url;
                        return;
                    }
                    
                    // Cập nhật giỏ hàng trên header
                    let addedQty = parseInt(document.getElementById('quantity').value) || 1;
                    let cartLink = document.querySelector('.header-nav a[href$="/cart"]');
                    
                    if (cartLink) {
                        let badge = cartLink.querySelector('span');
                        if (badge) {
                            badge.innerText = parseInt(badge.innerText) + addedQty;
                        } else {
                            cartLink.innerHTML = 'Giỏ hàng <span style="background: red; color: white; border-radius: 50%; padding: 2px 6px; font-size: 0.8rem;">' + addedQty + '</span>';
                        }
                    }
                    
                    // Hiện thông báo popup
                    let successMsg = document.createElement('div');
                    successMsg.className = 'alert-success';
                    successMsg.style.position = 'fixed';
                    successMsg.style.bottom = '20px';
                    successMsg.style.right = '20px';
                    successMsg.style.zIndex = '9999';
                    successMsg.style.boxShadow = '0 4px 12px rgba(0,0,0,0.15)';
                    successMsg.style.transition = 'all 0.5s ease';
                    successMsg.innerText = 'Đã thêm sản phẩm vào giỏ hàng!';
                    document.body.appendChild(successMsg);
                    
                    setTimeout(() => {
                        successMsg.style.opacity = '0';
                        setTimeout(() => successMsg.remove(), 500);
                    }, 3000);
                }).catch(err => {
                    console.error("Lỗi khi thêm giỏ hàng", err);
                    alert("Có lỗi xảy ra, vui lòng thử lại!");
                });

                // 2. Hiệu ứng bay ảnh
                const cartIcon = document.querySelector('.header-nav a[href$="/cart"]');
                const productImage = document.querySelector('.book-detail-cover img');
                
                if (cartIcon && productImage) {
                    const imgClone = productImage.cloneNode(true);
                    const imgRect = productImage.getBoundingClientRect();
                    const cartRect = cartIcon.getBoundingClientRect();
                    
                    imgClone.style.position = 'fixed';
                    imgClone.style.top = imgRect.top + 'px';
                    imgClone.style.left = imgRect.left + 'px';
                    imgClone.style.width = imgRect.width + 'px';
                    imgClone.style.height = imgRect.height + 'px';
                    imgClone.style.objectFit = 'cover';
                    imgClone.style.borderRadius = '8px';
                    imgClone.style.zIndex = '99999';
                    imgClone.style.transition = 'all 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94)';
                    
                    document.body.appendChild(imgClone);
                    
                    // Ép trình duyệt nhận diện tọa độ cũ
                    imgClone.getBoundingClientRect();
                    
                    // Tính toán tọa độ đích
                    imgClone.style.top = cartRect.top + 'px';
                    imgClone.style.left = cartRect.left + (cartRect.width / 2) - 10 + 'px';
                    imgClone.style.width = '20px';
                    imgClone.style.height = '20px';
                    imgClone.style.opacity = '0.4';
                    
                    setTimeout(() => {
                        imgClone.remove();
                        // Giật giật nút giỏ hàng một xíu
                        cartIcon.style.display = 'inline-block';
                        cartIcon.style.transition = 'transform 0.2s';
                        cartIcon.style.transform = 'scale(1.2)';
                        setTimeout(() => cartIcon.style.transform = 'scale(1)', 200);
                    }, 800);
                }
            }
        });
    </script>
</body>
