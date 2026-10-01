# BÀI KIỂM TRA QUÁ TRÌNH - LẬP TRÌNH WEB (ĐỀ 01)

* **MSSV:** 24110349
* **Công nghệ:** Java Servlet, JSP, JPA (Hibernate), SQL Server, Maven, SiteMesh Decorator

---

## 1. Yêu cầu & Chức năng thực hiện

### Đề 01 (Nền tảng):
- Cấu trúc 3 tầng (Presentation / Business / Data Access).
- Thiết lập SiteMesh Decorators cho 2 vai trò: User và Admin.
- Đăng ký xác thực OTP qua Email, Đăng nhập/Đăng xuất bằng Session.
- Trang chủ hiển thị danh sách sách (phân trang 6 cuốn/trang).
- Trang chi tiết sách & gửi đánh giá (Rating/Review).
- Quản trị (Admin): CRUD bảng Sách (Books) và Tác giả (Authors) có phân trang.

### Chức năng bổ sung (Vai trò User):
- **Giỏ hàng:** Thêm, xóa, cập nhật số lượng sách trong giới hạn tồn kho.
- **Thanh toán COD:** Đặt hàng thanh toán khi nhận hàng, lưu thông tin giao hàng, trừ tồn kho và làm trống giỏ.
- **Lịch sử đơn hàng:** Xem danh sách và lọc theo 8 trạng thái:
  - *Đơn hàng mới*, *Đã xác nhận*, *Chuẩn bị hàng*, *Vận chuyển*, *Giao hàng*, *Đã giao*, *Đơn hàng hủy*, *Đơn hàng hoàn*.

---

## 2. Cài đặt & Cấu hình Database

1. Tạo database trong SQL Server:
```sql
CREATE DATABASE Ktra_web;
GO