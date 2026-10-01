<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<head>
    <title>Đăng nhập</title>
</head>
<body>
    <div class="auth-container">
        <div class="auth-card small">
            <h2>Chào mừng trở lại! 👋</h2>
            
            <c:if test="${not empty alert}">
                <div class="alert-error">${alert}</div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="alert-success">${success}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label for="email">Địa chỉ Email</label>
                    <input type="email" id="email" name="email" placeholder="Nhập email của bạn" required>
                </div>
                
                <div class="form-group">
                    <label for="passwd">Mật khẩu</label>
                    <input type="password" id="passwd" name="passwd" placeholder="••••••••" required>
                </div>

                <button type="submit" class="btn btn-primary auth-btn">Đăng nhập</button>
            </form>

            <div class="auth-links">
                <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a></p>
            </div>
        </div>
    </div>
</body>
