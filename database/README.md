# Database - MSSV 24110349

Các script SQL Server của dự án `ktra-web-24110349`.

## Thứ tự chạy

1. Chạy `01_schema_24110349.sql` để tạo database `Ktra_web` và các bảng.
2. Chạy `02_cloudinary_cover_url_24110349.sql` nếu database được tạo từ schema cũ có `cover_image varchar(100)`.
3. Chạy file dữ liệu mẫu `../src/main/resources/db/cau3_seed_24110349.sql`.

Các script được thiết kế để có thể chạy lại. Script schema chỉ tạo những bảng chưa tồn tại; script seed kiểm tra dữ liệu trước khi thêm.

## Các bảng

- `users`: tài khoản User/Admin.
- `books`: thông tin sách, URL ảnh bìa hoặc URL sau khi upload Cloudinary.
- `author`: tác giả.
- `book_author`: quan hệ nhiều-nhiều giữa sách và tác giả.
- `rating`: điểm đánh giá và nội dung review của User cho sách.

## Cấu hình JPA

Thông tin kết nối ứng dụng nằm tại `src/main/resources/META-INF/persistence.xml`.
