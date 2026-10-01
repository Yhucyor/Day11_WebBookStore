<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/> | K-Store</title>
    <!-- Nhúng CSS thẩm mỹ cao -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <sitemesh:write property="head"/>
</head>
<body>
    <!-- Nhúng Header -->
    <jsp:include page="/common/web/header.jsp" />

    <!-- Khu vực thân trang thay đổi -->
    <main class="main-content">
        <sitemesh:write property="body"/>
    </main>

    <!-- Nhúng Footer -->
    <jsp:include page="/common/web/footer.jsp" />

    <!-- Nhúng JS -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>