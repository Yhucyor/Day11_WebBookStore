<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<head><title>Quản lý tác giả</title></head>
<body>
    <section class="admin-page">
        <div class="admin-page-header">
            <div>
                <p class="book-page-eyebrow">ADMIN / AUTHORS</p>
                <h1>Quản lý tác giả</h1>
                <p>Tổng cộng <strong>${totalItems}</strong> tác giả.</p>
            </div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/authors?action=create">+ Thêm tác giả</a>
        </div>

        <c:if test="${not empty success}"><div class="alert-success"><c:out value="${success}" /></div></c:if>

        <div class="admin-table-wrap">
            <table class="admin-table">
                <thead><tr><th>ID</th><th>Tên tác giả</th><th>Ngày sinh</th><th>Thao tác</th></tr></thead>
                <tbody>
                    <c:forEach var="author" items="${authors}">
                        <tr>
                            <td>#${author.authorId}</td>
                            <td><strong><c:out value="${author.authorName}" /></strong></td>
                            <td><c:out value="${author.dateOfBirth}" default="Đang cập nhật" /></td>
                            <td>
                                <div class="admin-actions">
                                    <a class="btn-action edit" href="${pageContext.request.contextPath}/admin/authors?action=edit&id=${author.authorId}">Sửa</a>
                                    <form action="${pageContext.request.contextPath}/admin/authors" method="post"
                                          onsubmit="return confirm('Bạn chắc chắn muốn xóa tác giả này?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${author.authorId}">
                                        <button class="btn-action delete" type="submit">Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty authors}"><tr><td colspan="4" class="table-empty">Chưa có dữ liệu tác giả.</td></tr></c:if>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages > 1}">
            <nav class="pagination" aria-label="Phân trang tác giả">
                <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                    <a class="${pageNumber == currentPage ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/admin/authors?page=${pageNumber}">${pageNumber}</a>
                </c:forEach>
            </nav>
        </c:if>
    </section>
</body>
