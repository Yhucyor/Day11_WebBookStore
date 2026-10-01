<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<head>
    <title>Đăng ký tài khoản</title>
</head>
<body>
    <div class="auth-container">
        <div class="auth-card">
            <h2>Tạo tài khoản mới 🚀</h2>
            
            <c:if test="${not empty alert}">
                <div class="alert-error">${alert}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="form-group">
                    <label for="fullname">Họ và tên</label>
                    <input type="text" id="fullname" name="fullname" placeholder="Vd: Nguyễn Trọng Thức" required>
                </div>
                
                <div class="form-group">
                    <label for="email">Địa chỉ Email</label>
                    <input type="email" id="email" name="email" placeholder="example@gmail.com" required>
                </div>
                
                <div class="form-group">
                    <label for="phone">Số điện thoại</label>
                    <input type="number" id="phone" name="phone" placeholder="0987654321">
                </div>
                
                <div class="form-group">
                    <label for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" placeholder="Tối thiểu 6 ký tự" required>
                </div>

                <button type="submit" class="btn btn-primary auth-btn">Đăng ký</button>
            </form>

            <div class="auth-links">
                <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
            </div>
        </div>
    </div>
</body>