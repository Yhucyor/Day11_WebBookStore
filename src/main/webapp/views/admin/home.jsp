<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>Trang Quản Trị - Kiểm Thử Câu 1</title>
</head>
<body>
    <section class="admin-page">
        <div class="admin-page-header">
            <div>
                <p class="book-page-eyebrow">ADMIN DASHBOARD</p>
                <h1>Khu vực quản trị</h1>
                <p>Quản lý dữ liệu sách và tác giả của K-Store.</p>
            </div>
        </div>
        <div class="admin-dashboard-grid">
            <a href="${pageContext.request.contextPath}/admin/books">
                <span>📚</span><strong>Quản lý sách</strong><small>Thêm, xem, cập nhật, xóa và phân trang Books</small>
            </a>
            <a href="${pageContext.request.contextPath}/admin/authors">
                <span>✍️</span><strong>Quản lý tác giả</strong><small>Thêm, xem, cập nhật, xóa và phân trang Authors</small>
            </a>
        </div>
    </section>
</body>
