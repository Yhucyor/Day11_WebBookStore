<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<head><title>${editing ? 'Cập nhật tác giả' : 'Thêm tác giả'}</title></head>
<body>
    <section class="admin-page admin-form-page">
        <a class="back-to-books" href="${pageContext.request.contextPath}/admin/authors">← Quay lại quản lý tác giả</a>
        <div class="admin-form-card compact">
            <p class="book-page-eyebrow">ADMIN / AUTHORS</p>
            <h1>${editing ? 'Cập nhật tác giả' : 'Thêm tác giả mới'}</h1>
            <c:if test="${not empty alert}"><div class="alert-error"><c:out value="${alert}" /></div></c:if>

            <form action="${pageContext.request.contextPath}/admin/authors" method="post">
                <input type="hidden" name="action" value="${editing ? 'update' : 'create'}">
                <c:if test="${editing}"><input type="hidden" name="authorId" value="${author.authorId}"></c:if>
                <div class="form-group">
                    <label for="authorName">Tên tác giả *</label>
                    <input id="authorName" name="authorName" type="text" maxlength="100"
                           value="${fn:escapeXml(author.authorName)}" required>
                </div>
                <div class="form-group">
                    <label for="dateOfBirth">Ngày sinh</label>
                    <input id="dateOfBirth" name="dateOfBirth" type="date" value="${author.dateOfBirth}">
                </div>
                <div class="form-actions">
                    <button class="btn btn-primary" type="submit">${editing ? 'Lưu thay đổi' : 'Thêm tác giả'}</button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/authors">Hủy</a>
                </div>
            </form>
        </div>
    </section>
</body>
