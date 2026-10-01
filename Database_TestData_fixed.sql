-- =========================================================
-- FIXED TEST DATA SCRIPT FOR KTRA_WEB (SQL Server)
-- ---------------------------------------------------------
-- Adjustments:
--   • ALTER books.price to DECIMAL(12,2) to hold values up to 999,999.99.
--   • Use correct table name "rating" (singular) instead of "ratings".
--   • Remove GO statements that break variable scope; declare variables in each batch.
--   • Ensure @uid is declared before cart‑item inserts.
--   • All INSERTs keep existence checks to avoid duplicate PK errors.
-- ---------------------------------------------------------

USE Ktra_web;
GO

-- ---------------------------------------------------------
-- 1. ALTER column definitions (run once, safe if already correct)
-- ---------------------------------------------------------
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'books' AND COLUMN_NAME = 'price' AND DATA_TYPE = 'numeric')
BEGIN
    ALTER TABLE books ALTER COLUMN price DECIMAL(12,2);
END;
GO

-- ---------------------------------------------------------
-- 2. USERS (email is unique, password = 123)
-- ---------------------------------------------------------
INSERT INTO users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
SELECT 'admin@gmail.com', N'Admin Siêu Cấp', 987654321, '123', GETDATE(), GETDATE(), 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gmail.com');

INSERT INTO users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
SELECT 'user1@gmail.com', N'Khách Mới Đăng Ký (Chưa mua)', 912345678, '123', GETDATE(), GETDATE(), 0
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user1@gmail.com');

INSERT INTO users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
SELECT 'user2@gmail.com', N'Khách Hàng Thân Thiết', 923456789, '123', DATEADD(MONTH, -5, GETDATE()), GETDATE(), 0
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user2@gmail.com');

INSERT INTO users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
SELECT 'user3@gmail.com', N'Khách Hàng Vãng Lai', 934567890, '123', DATEADD(MONTH, -1, GETDATE()), GETDATE(), 0
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user3@gmail.com');
GO

-- ---------------------------------------------------------
-- 3. AUTHORS
-- ---------------------------------------------------------
INSERT INTO author (author_name, date_of_birth)
SELECT N'Nguyễn Nhật Ánh', '1955-05-07'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = N'Nguyễn Nhật Ánh');
INSERT INTO author (author_name, date_of_birth)
SELECT N'Paulo Coelho', '1947-08-24'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = N'Paulo Coelho');
INSERT INTO author (author_name, date_of_birth)
SELECT N'Tony Buổi Sáng', '1970-01-01'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = N'Tony Buổi Sáng');
INSERT INTO author (author_name, date_of_birth)
SELECT N'George R.R. Martin', '1948-09-20'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = N'George R.R. Martin');
INSERT INTO author (author_name, date_of_birth)
SELECT N'Dale Carnegie', '1888-11-24'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = N'Dale Carnegie');
GO

-- ---------------------------------------------------------
-- 4. BOOKS (including out‑of‑stock & no‑author examples)
-- ---------------------------------------------------------
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 123456, N'Mắt Biếc', N'NXB Trẻ', 85000, N'Tác phẩm nổi bật của Nguyễn Nhật Ánh.', '1990-01-01',
       'https://salt.tikicdn.com/cache/w1200/ts/product/45/3b/fc/aa81d0a534b45706ae1eee1e344e80d9.jpg', 100
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 123456);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 234567, N'Nhà Giả Kim', N'NXB Hội Nhà Văn', 79000, N'Hành trình theo đuổi giấc mơ của Santiago.', '1988-01-01',
       'https://salt.tikicdn.com/cache/w1200/ts/product/5e/18/24/2a6154ba08df6ce6161c13f4303fa19e.jpg', 50
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 234567);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 345678, N'Trên Đường Băng', N'NXB Trẻ', 65000, N'Cuốn sách truyền cảm hứng dành cho tuổi trẻ.', '2015-01-01',
       'https://salt.tikicdn.com/cache/w1200/ts/product/88/ce/16/d3d8db19ec209425adcd3df2a514b8db.jpg', 200
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 345678);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 456789, N'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', N'NXB Trẻ', 95000, N'Tuổi thơ dữ dội và êm đềm ở làng quê.', '2010-12-01',
       'https://salt.tikicdn.com/cache/w1200/ts/product/29/73/0f/503e4811a052ff37c569fdd3d8e58319.jpg', 150
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 456789);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 567890, N'Trò Chơi Vương Quyền', N'NXB Văn Học', 250000, N'Tiểu thuyết giả tưởng đồ sộ.', '1996-08-01',
       'https://salt.tikicdn.com/cache/w1200/ts/product/d4/03/f3/7abdf1c0809b4db75ab524ed1871a3de.png', 10
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 567890);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 678901, N'Đắc Nhân Tâm', N'NXB Tổng hợp TP.HCM', 68000, N'Sách nghệ thuật đối nhân xử thế.', '1936-10-01',
       'https://salt.tikicdn.com/cache/w1200/ts/product/e7/87/40/e6bbba621db70a049f31dd65427bdde9.jpg', 5
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 678901);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 789012, N'Sách Hết Hàng', N'NXB Ảo', 45000, N'Sách này dùng để test trường hợp hết hàng.', '2024-01-01',
       'https://salt.tikicdn.com/cache/w1200/ts/product/70/dd/74/4ff774aee2d93eab0b7414dfdbbb9b7c.png', 0
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 789012);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 890123, N'Sách Vô Danh Không Tác Giả', N'NXB Bí Ẩn', 150000, N'Chưa có tác giả nào nhận sách này.', '2023-05-15',
       'https://salt.tikicdn.com/cache/w1200/ts/product/9d/d0/0f/b0a40d6cde4d9a2ad9326e2e50523098.png', 20
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 890123);
GO

-- ---------------------------------------------------------
-- 5. BOOK‑AUTHOR RELATIONS (skip books without author)
-- ---------------------------------------------------------
DECLARE @authorId int;

SET @authorId = (SELECT author_id FROM author WHERE author_name = N'Nguyễn Nhật Ánh');
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, @authorId FROM books b WHERE b.isbn = 123456
  AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = @authorId);

SET @authorId = (SELECT author_id FROM author WHERE author_name = N'Paulo Coelho');
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, @authorId FROM books b WHERE b.isbn = 234567
  AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = @authorId);

SET @authorId = (SELECT author_id FROM author WHERE author_name = N'Tony Buổi Sáng');
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, @authorId FROM books b WHERE b.isbn = 345678
  AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = @authorId);

SET @authorId = (SELECT author_id FROM author WHERE author_name = N'Nguyễn Nhật Ánh');
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, @authorId FROM books b WHERE b.isbn = 456789
  AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = @authorId);

SET @authorId = (SELECT author_id FROM author WHERE author_name = N'George R.R. Martin');
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, @authorId FROM books b WHERE b.isbn = 567890
  AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = @authorId);

SET @authorId = (SELECT author_id FROM author WHERE author_name = N'Dale Carnegie');
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, @authorId FROM books b WHERE b.isbn = 678901
  AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = @authorId);
GO

-- ---------------------------------------------------------
-- 6. RATINGS (use correct table name "rating")
-- ---------------------------------------------------------
DECLARE @userId int;

SET @userId = (SELECT id FROM users WHERE email = 'user3@gmail.com');
INSERT INTO rating (userid, bookid, rating, review_text)
SELECT @userId, (SELECT bookid FROM books WHERE isbn = 123456), 5, N'Cuốn sách rất hay và cảm động! Mình đã khóc.'
WHERE NOT EXISTS (SELECT 1 FROM rating WHERE userid = @userId AND bookid = (SELECT bookid FROM books WHERE isbn = 123456));

INSERT INTO rating (userid, bookid, rating, review_text)
SELECT @userId, (SELECT bookid FROM books WHERE isbn = 234567), 5, N'Một cuốn sách gối đầu giường.'
WHERE NOT EXISTS (SELECT 1 FROM rating WHERE userid = @userId AND bookid = (SELECT bookid FROM books WHERE isbn = 234567));

SET @userId = (SELECT id FROM users WHERE email = 'user4@gmail.com');
INSERT INTO rating (userid, bookid, rating, review_text)
SELECT @userId, (SELECT bookid FROM books WHERE isbn = 345678), 2, N'Đọc hơi chán, không hợp gu mình lắm.'
WHERE NOT EXISTS (SELECT 1 FROM rating WHERE userid = @userId AND bookid = (SELECT bookid FROM books WHERE isbn = 345678));
GO

-- ---------------------------------------------------------
-- 7. ORDERS (all UI statuses)
-- ---------------------------------------------------------
DECLARE @uid int;

SET @uid = (SELECT id FROM users WHERE email = 'user3@gmail.com');
INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, GETDATE(), 164000, N'Đã giao', N'123 Đường A, Quận 1', '0912345678', N'Nguyễn Văn A', N'COD', N'Đã thanh toán'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Đã giao');

INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, DATEADD(DAY,-2,GETDATE()), 95000, N'Đang giao hàng', N'123 Đường A, Quận 1', '0912345678', N'Nguyễn Văn A', N'Momo', N'Đã thanh toán'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Đang giao hàng');

INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, DATEADD(DAY,-5,GETDATE()), 250000, N'Đang xử lý', N'Cty XYZ, Quận 3', '0988888888', N'Nguyễn Văn A (Cty)', N'VNPay', N'Đã thanh toán'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Đang xử lý');

INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, DATEADD(DAY,-10,GETDATE()), 158000, N'Đã hủy', N'456 Lê Lợi', '0912345678', N'Nguyễn Văn A', N'VNPay', N'Đã hoàn tiền'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Đã hủy');

SET @uid = (SELECT id FROM users WHERE email = 'user4@gmail.com');
INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, GETDATE(), 65000, N'Đơn hàng mới', N'Tòa nhà HUTECH', '0923456789', N'Trần Thị B', N'COD', N'Chưa thanh toán'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Đơn hàng mới');
-- Additional order statuses for comprehensive testing
INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, DATEADD(DAY,-1,GETDATE()), 120000, N'Đã xác nhận', N'123 Đường A, Quận 1', '0912345678', N'Nguyễn Văn A', N'Bank', N'Đã thanh toán'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Đã xác nhận');

INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, DATEADD(DAY,-1,GETDATE()), 130000, N'Chuẩn bị hàng', N'123 Đường A, Quận 1', '0912345678', N'Nguyễn Văn A', N'Bank', N'Đã thanh toán'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Chuẩn bị hàng');

INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, DATEADD(DAY,-1,GETDATE()), 140000, N'Vận chuyển', N'123 Đường A, Quận 1', '0912345678', N'Nguyễn Văn A', N'Bank', N'Đã thanh toán'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Vận chuyển');

INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, DATEADD(DAY,-1,GETDATE()), 150000, N'Giao hàng', N'123 Đường A, Quận 1', '0912345678', N'Nguyễn Văn A', N'Bank', N'Đã thanh toán'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Giao hàng');

INSERT INTO orders (userid, order_date, total_amount, status, shipping_address, phone_number, recipient_name, payment_method, payment_status)
SELECT @uid, DATEADD(DAY,-1,GETDATE()), 160000, N'Đã hoàn', N'123 Đường A, Quận 1', '0912345678', N'Nguyễn Văn A', N'Bank', N'Đã hoàn tiền'
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE userid = @uid AND status = N'Đã hoàn');

GO

-- ---------------------------------------------------------
-- 8. ORDER_DETAILS (match the orders above)
-- ---------------------------------------------------------
DECLARE @orderId int;

SET @orderId = (SELECT TOP 1 orderId FROM orders WHERE userid = (SELECT id FROM users WHERE email = 'user3@gmail.com') AND status = N'Đã giao');
INSERT INTO order_details (order_id, bookid, quantity, price)
SELECT @orderId, (SELECT bookid FROM books WHERE isbn = 123456), 1, 85000
WHERE NOT EXISTS (SELECT 1 FROM order_details WHERE order_id = @orderId AND bookid = (SELECT bookid FROM books WHERE isbn = 123456));
INSERT INTO order_details (order_id, bookid, quantity, price)
SELECT @orderId, (SELECT bookid FROM books WHERE isbn = 234567), 1, 79000
WHERE NOT EXISTS (SELECT 1 FROM order_details WHERE order_id = @orderId AND bookid = (SELECT bookid FROM books WHERE isbn = 234567));

SET @orderId = (SELECT TOP 1 orderId FROM orders WHERE userid = (SELECT id FROM users WHERE email = 'user3@gmail.com') AND status = N'Đang giao hàng');
INSERT INTO order_details (order_id, bookid, quantity, price)
SELECT @orderId, (SELECT bookid FROM books WHERE isbn = 456789), 1, 95000
WHERE NOT EXISTS (SELECT 1 FROM order_details WHERE order_id = @orderId AND bookid = (SELECT bookid FROM books WHERE isbn = 456789));

SET @orderId = (SELECT TOP 1 orderId FROM orders WHERE userid = (SELECT id FROM users WHERE email = 'user3@gmail.com') AND status = N'Đang xử lý');
INSERT INTO order_details (order_id, bookid, quantity, price)
SELECT @orderId, (SELECT bookid FROM books WHERE isbn = 567890), 1, 250000
WHERE NOT EXISTS (SELECT 1 FROM order_details WHERE order_id = @orderId AND bookid = (SELECT bookid FROM books WHERE isbn = 567890));

SET @orderId = (SELECT TOP 1 orderId FROM orders WHERE userid = (SELECT id FROM users WHERE email = 'user3@gmail.com') AND status = N'Đã hủy');
INSERT INTO order_details (order_id, bookid, quantity, price)
SELECT @orderId, (SELECT bookid FROM books WHERE isbn = 234567), 2, 79000
WHERE NOT EXISTS (SELECT 1 FROM order_details WHERE order_id = @orderId AND bookid = (SELECT bookid FROM books WHERE isbn = 234567));

SET @orderId = (SELECT TOP 1 orderId FROM orders WHERE userid = (SELECT id FROM users WHERE email = 'user4@gmail.com') AND status = N'Đơn hàng mới');
INSERT INTO order_details (order_id, bookid, quantity, price)
SELECT @orderId, (SELECT bookid FROM books WHERE isbn = 345678), 1, 65000
WHERE NOT EXISTS (SELECT 1 FROM order_details WHERE order_id = @orderId AND bookid = (SELECT bookid FROM books WHERE isbn = 345678));
GO

-- ---------------------------------------------------------
-- 9. CART ITEMS (including out‑of‑stock case)
-- ---------------------------------------------------------
DECLARE @uid int;

SET @uid = (SELECT id FROM users WHERE email = 'user3@gmail.com');
INSERT INTO cart_items (userid, bookid, quantity)
SELECT @uid, (SELECT bookid FROM books WHERE isbn = 678901), 1
WHERE NOT EXISTS (SELECT 1 FROM cart_items WHERE userid = @uid AND bookid = (SELECT bookid FROM books WHERE isbn = 678901));
INSERT INTO cart_items (userid, bookid, quantity)
SELECT @uid, (SELECT bookid FROM books WHERE isbn = 234567), 3
WHERE NOT EXISTS (SELECT 1 FROM cart_items WHERE userid = @uid AND bookid = (SELECT bookid FROM books WHERE isbn = 234567));

SET @uid = (SELECT id FROM users WHERE email = 'user4@gmail.com');
INSERT INTO cart_items (userid, bookid, quantity)
SELECT @uid, (SELECT bookid FROM books WHERE isbn = 123456), 1
WHERE NOT EXISTS (SELECT 1 FROM cart_items WHERE userid = @uid AND bookid = (SELECT bookid FROM books WHERE isbn = 123456));
INSERT INTO cart_items (userid, bookid, quantity)
SELECT @uid, (SELECT bookid FROM books WHERE isbn = 789012), 1
WHERE NOT EXISTS (SELECT 1 FROM cart_items WHERE userid = @uid AND bookid = (SELECT bookid FROM books WHERE isbn = 789012));
GO

PRINT N'✅ FIXED TEST DATA INSERTED SUCCESSFULLY.';
