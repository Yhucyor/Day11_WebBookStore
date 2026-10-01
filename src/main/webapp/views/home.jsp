<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<head>
    <title>Trang chủ sách</title>
</head>
<body>
    <section class="book-page">
        <div class="book-page-heading">
            <div>
                <p class="book-page-eyebrow">K-STORE BOOKS</p>
                <h1>Khám phá kho sách</h1>
                <p>Tìm thấy <strong>${totalBooks}</strong> cuốn sách trong thư viện.</p>
            </div>
        </div>

        <c:if test="${not empty alert}">
            <div class="alert-error"><c:out value="${alert}" /></div>
            <c:remove var="alert" scope="session" />
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert-success" style="margin-bottom: 20px;"><c:out value="${success}" /></div>
            <c:remove var="success" scope="session" />
        </c:if>

        <c:choose>
            <c:when test="${empty books}">
                <div class="book-empty">
                    <span>📚</span>
                    <h2>Chưa có sách</h2>
                    <p>Hãy thêm dữ liệu vào bảng <code>books</code> để hiển thị tại đây.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="book-grid">
                    <c:forEach var="book" items="${books}">
                        <article class="book-card">
                            <div class="book-cover-wrap">
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
                                <img src="${coverUrl}" alt="Bìa sách ${fn:escapeXml(book.title)}" loading="lazy"
                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/book-default.svg';">
                            </div>

                            <div class="book-card-content">
                                <h2 title="${fn:escapeXml(book.title)}">
                                    <a href="${pageContext.request.contextPath}/book-detail?id=${book.bookId}">
                                        <c:out value="${book.title}" />
                                    </a>
                                </h2>
                                <dl class="book-meta">
                                    <div>
                                        <dt>Giá</dt>
                                        <dd><strong style="color: #d32f2f;"><c:out value="${book.price}" default="0" /> VNĐ</strong></dd>
                                    </div>
                                    <div>
                                        <dt>Mã ISBN</dt>
                                        <dd><c:out value="${book.isbn}" default="Đang cập nhật" /></dd>
                                    </div>
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
                                    <div>
                                        <dt>Nhà xuất bản</dt>
                                        <dd><c:out value="${book.publisher}" default="Đang cập nhật" /></dd>
                                    </div>
                                    <div>
                                        <dt>Ngày xuất bản</dt>
                                        <dd><c:out value="${book.publishDate}" default="Đang cập nhật" /></dd>
                                    </div>
                                    <div>
                                        <dt>Số lượng</dt>
                                        <dd><c:out value="${book.quantity}" default="0" /></dd>
                                    </div>
                                </dl>

                                <div class="book-review-count">
                                    <span aria-hidden="true">★</span>
                                    Review (${book.reviewCount})
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </div>

                <c:if test="${totalPages > 1}">
                    <c:set var="currentUrl" value="${requestScope['jakarta.servlet.forward.servlet_path']}" />
                    <c:if test="${empty currentUrl}">
                        <c:set var="currentUrl" value="${pageContext.request.servletPath}" />
                    </c:if>
                    <nav class="pagination" aria-label="Phân trang sách">
                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}${currentUrl}?page=${currentPage - 1}" aria-label="Trang trước">‹</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                            <a href="${pageContext.request.contextPath}${currentUrl}?page=${pageNumber}"
                               class="${pageNumber == currentPage ? 'active' : ''}"
                               aria-current="${pageNumber == currentPage ? 'page' : 'false'}">${pageNumber}</a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}${currentUrl}?page=${currentPage + 1}" aria-label="Trang sau">›</a>
                        </c:if>
                    </nav>
                </c:if>
            </c:otherwise>
        </c:choose>
    </section>
</body>
