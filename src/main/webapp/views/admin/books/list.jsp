<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<head><title>Quản lý sách</title></head>
<body>
    <section class="admin-page">
        <div class="admin-page-header">
            <div>
                <p class="book-page-eyebrow">ADMIN / BOOKS</p>
                <h1>Quản lý sách</h1>
                <p>Tổng cộng <strong>${totalItems}</strong> cuốn sách.</p>
            </div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/books?action=create">+ Thêm sách</a>
        </div>

        <c:if test="${not empty success}"><div class="alert-success"><c:out value="${success}" /></div></c:if>

        <div class="admin-table-wrap">
            <table class="admin-table">
                <thead>
                    <tr><th>ID</th><th>Bìa</th><th>Thông tin sách</th><th>Tác giả</th><th>Số lượng</th><th>Thao tác</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="book" items="${books}">
                        <tr>
                            <td>#${book.bookId}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty book.coverImage}"><c:url var="adminCover" value="/assets/images/book-default.svg" /></c:when>
                                    <c:when test="${fn:startsWith(book.coverImage, 'http://') or fn:startsWith(book.coverImage, 'https://')}"><c:set var="adminCover" value="${book.coverImage}" /></c:when>
                                    <c:otherwise><c:url var="adminCover" value="/${book.coverImage}" /></c:otherwise>
                                </c:choose>
                                <img class="admin-book-cover" src="${adminCover}" alt="Bìa ${fn:escapeXml(book.title)}"
                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/book-default.svg';">
                            </td>
                            <td>
                                <strong><c:out value="${book.title}" /></strong>
                                <small>ISBN: <c:out value="${book.isbn}" default="-" /></small>
                                <small><c:out value="${book.publisher}" default="Chưa có NXB" /></small>
                            </td>
                            <td>
                                <c:forEach var="author" items="${book.authors}" varStatus="status">
                                    <c:out value="${author.authorName}"/><c:if test="${not status.last}"><br></c:if>
                                </c:forEach>
                            </td>
                            <td>${book.quantity}</td>
                            <td>
                                <div class="admin-actions">
                                    <a class="btn-action edit" href="${pageContext.request.contextPath}/admin/books?action=edit&id=${book.bookId}">Sửa</a>
                                    <form action="${pageContext.request.contextPath}/admin/books" method="post"
                                          onsubmit="return confirm('Bạn chắc chắn muốn xóa sách này? Review liên quan cũng sẽ bị xóa.');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${book.bookId}">
                                        <button class="btn-action delete" type="submit">Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty books}"><tr><td colspan="6" class="table-empty">Chưa có dữ liệu sách.</td></tr></c:if>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages > 1}">
            <nav class="pagination" aria-label="Phân trang sách quản trị">
                <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                    <a class="${pageNumber == currentPage ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin/books?page=${pageNumber}">${pageNumber}</a>
                </c:forEach>
            </nav>
        </c:if>
    </section>
</body>
