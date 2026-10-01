<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<head><title>${editing ? 'Cập nhật sách' : 'Thêm sách'}</title></head>
<body>
    <section class="admin-page admin-form-page">
        <a class="back-to-books" href="${pageContext.request.contextPath}/admin/books">← Quay lại quản lý sách</a>
        <div class="admin-form-card">
            <p class="book-page-eyebrow">ADMIN / BOOKS</p>
            <h1>${editing ? 'Cập nhật sách' : 'Thêm sách mới'}</h1>
            <c:if test="${not empty alert}"><div class="alert-error"><c:out value="${alert}" /></div></c:if>

            <form action="${pageContext.request.contextPath}/admin/books" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="${editing ? 'update' : 'create'}">
                <c:if test="${editing}"><input type="hidden" name="bookId" value="${book.bookId}"></c:if>

                <div class="admin-form-grid">
                    <div class="form-group form-span-2">
                        <label for="title">Tiêu đề *</label>
                        <input id="title" name="title" type="text" maxlength="200" value="${fn:escapeXml(book.title)}" required>
                    </div>
                    <div class="form-group">
                        <label for="isbn">ISBN</label>
                        <input id="isbn" name="isbn" type="number" min="1" value="${book.isbn}">
                    </div>
                    <div class="form-group">
                        <label for="publisher">Nhà xuất bản</label>
                        <input id="publisher" name="publisher" type="text" maxlength="100" value="${fn:escapeXml(book.publisher)}">
                    </div>
                    <div class="form-group">
                        <label for="price">Giá</label>
                        <input id="price" name="price" type="number" min="0" max="9999.99" step="0.01" value="${book.price}">
                    </div>
                    <div class="form-group">
                        <label for="quantity">Số lượng</label>
                        <input id="quantity" name="quantity" type="number" min="0" value="${book.quantity}">
                    </div>
                    <div class="form-group">
                        <label for="publishDate">Ngày xuất bản</label>
                        <input id="publishDate" name="publishDate" type="date" value="${book.publishDate}">
                    </div>
                    <div class="form-group form-span-2 cover-input-panel">
                        <label>Ảnh bìa</label>
                        <div class="cover-input-options">
                            <div>
                                <label for="coverImage">Nhập URL ảnh</label>
                                <input id="coverImage" name="coverImage" type="url" maxlength="500"
                                       placeholder="https://example.com/book-cover.jpg"
                                       value="${fn:escapeXml(book.coverImage)}">
                            </div>
                            <span class="cover-or">HOẶC</span>
                            <div>
                                <label for="coverFile">Upload lên Cloudinary</label>
                                <input id="coverFile" name="coverFile" type="file"
                                       accept="image/jpeg,image/png,image/webp,image/gif">
                                <small>JPG, PNG, WEBP hoặc GIF; tối đa 5 MB. File upload được ưu tiên hơn URL.</small>
                            </div>
                        </div>
                        <c:if test="${not empty book.coverImage}">
                            <div class="current-cover-preview">
                                <span>Ảnh hiện tại</span>
                                <img src="${fn:escapeXml(book.coverImage)}" alt="Ảnh bìa hiện tại">
                            </div>
                        </c:if>
                    </div>
                    <div class="form-group form-span-2">
                        <label for="authorIds">Tác giả</label>
                        <select id="authorIds" name="authorIds" multiple size="6">
                            <c:forEach var="author" items="${allAuthors}">
                                <option value="${author.authorId}" ${selectedAuthorIds.contains(author.authorId) ? 'selected' : ''}>
                                    <c:out value="${author.authorName}" />
                                </option>
                            </c:forEach>
                        </select>
                        <small>Giữ Ctrl để chọn nhiều tác giả.</small>
                    </div>
                    <div class="form-group form-span-2">
                        <label for="description">Mô tả</label>
                        <textarea id="description" name="description" rows="5"><c:out value="${book.description}" /></textarea>
                    </div>
                </div>
                <div class="form-actions">
                    <button class="btn btn-primary" type="submit">${editing ? 'Lưu thay đổi' : 'Thêm sách'}</button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/books">Hủy</a>
                </div>
            </form>
        </div>
    </section>
</body>
