<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <header class="site-header">
            <div class="header-container">
                <div class="header-logo">
                    <a href="${pageContext.request.contextPath}/home">📚 K-Store</a>
                </div>

                <nav class="header-nav">
                    <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                     <a href="${pageContext.request.contextPath}/products">Sản phẩm</a>

                    <c:if test="${sessionScope.account != null and sessionScope.account.isAdmin}">
                        <a href="${pageContext.request.contextPath}/admin/home">Trang quản trị</a>
                    </c:if>

                    <c:choose>
                        <c:when test="${sessionScope.account != null}">
                            <a href="${pageContext.request.contextPath}/logout">Đăng xuất
                                (${sessionScope.account.fullname})</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                        </c:otherwise>
                    </c:choose>

                </nav>
            </div>
        </header>
