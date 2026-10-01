<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<head>
    <title>Xác minh OTP</title>
</head>
<body>
    <div class="auth-container">
        <div class="auth-card small">
            <h2>Xác minh OTP 🔐</h2>
            <p class="desc">Chúng tôi đã gửi một mã OTP gồm 6 chữ số đến email của bạn. Vui lòng nhập mã để hoàn tất đăng ký.</p>
            
            <c:if test="${not empty alert}">
                <div class="alert-error">${alert}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/verify-otp" method="post">
                <div class="form-group">
                    <input type="text" class="otp-input" id="otp" name="otp" placeholder="••••••" maxlength="6" required autocomplete="off">
                </div>

                <button type="submit" class="btn btn-primary auth-btn">Xác minh ngay</button>
            </form>
        </div>
    </div>
</body>