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
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert-success"><c:out value="${success}" /></div>
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
</body>
