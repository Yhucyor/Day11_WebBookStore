-- Dữ liệu mẫu cho Câu 3 - có thể chạy lại mà không tạo bản ghi trùng.
SET NOCOUNT ON;

INSERT INTO author (author_name, date_of_birth)
SELECT 'Nguyen Van An', '1980-03-12'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = 'Nguyen Van An');
INSERT INTO author (author_name, date_of_birth)
SELECT 'Tran Minh Khoa', '1978-07-21'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = 'Tran Minh Khoa');
INSERT INTO author (author_name, date_of_birth)
SELECT 'Le Hoang Nam', '1985-11-05'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = 'Le Hoang Nam');
INSERT INTO author (author_name, date_of_birth)
SELECT 'Pham Thu Ha', '1982-01-18'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = 'Pham Thu Ha');
INSERT INTO author (author_name, date_of_birth)
SELECT 'Vo Quoc Bao', '1975-09-30'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = 'Vo Quoc Bao');
INSERT INTO author (author_name, date_of_birth)
SELECT 'Dang Thanh Tung', '1988-06-14'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = 'Dang Thanh Tung');
INSERT INTO author (author_name, date_of_birth)
SELECT 'Bui Mai Anh', '1984-04-09'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = 'Bui Mai Anh');
INSERT INTO author (author_name, date_of_birth)
SELECT 'Ho Duc Long', '1979-12-25'
WHERE NOT EXISTS (SELECT 1 FROM author WHERE author_name = 'Ho Duc Long');
GO

INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 100000001, 'Lap Trinh Java Can Ban', 'NXB Giao Duc', 125.00,
       'Kien thuc nen tang ve lap trinh Java.', '2022-03-15', 'https://covers.openlibrary.org/b/isbn/0134685997-L.jpg', 20
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 100000001);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 100000002, 'Servlet va JSP Thuc Chien', 'NXB Cong Nghe', 145.50,
       'Xay dung ung dung web theo mo hinh MVC.', '2023-05-20', 'https://covers.openlibrary.org/b/isbn/0596009208-L.jpg', 15
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 100000002);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 100000003, 'Java Persistence voi JPA', 'NXB Thong Tin', 168.00,
       'Lam viec voi co so du lieu bang Jakarta Persistence.', '2023-08-10', 'https://covers.openlibrary.org/b/isbn/1617294942-L.jpg', 11
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 100000003);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 100000004, 'Thiet Ke Web Hien Dai', 'NXB Tre', 132.00,
       'HTML, CSS va ky thuat xay dung giao dien responsive.', '2021-11-08', 'https://covers.openlibrary.org/b/isbn/1491950358-L.jpg', 18
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 100000004);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 100000005, 'Co So Du Lieu SQL Server', 'NXB Bach Khoa', 155.00,
       'Thiet ke va khai thac co so du lieu SQL Server.', '2022-09-22', 'https://covers.openlibrary.org/b/isbn/1449390544-L.jpg', 9
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 100000005);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 100000006, 'Kien Truc Phan Mem Ba Tang', 'NXB Dai Hoc', 175.00,
       'To chuc ung dung theo Presentation, Business va Data Access.', '2024-01-12', 'https://covers.openlibrary.org/b/isbn/0201633612-L.jpg', 14
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 100000006);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 100000007, 'Bao Mat Ung Dung Web', 'NXB Khoa Hoc', 189.00,
       'Cac nguyen tac bao ve ung dung web hien dai.', '2024-04-18', 'https://covers.openlibrary.org/b/isbn/0132350882-L.jpg', 8
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 100000007);
INSERT INTO books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
SELECT 100000008, 'Git va Quan Ly Ma Nguon', 'NXB Lao Dong', 118.00,
       'Quan ly phien ban va cong tac trong du an phan mem.', '2020-07-06', 'https://covers.openlibrary.org/b/isbn/0321125215-L.jpg', 25
WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = 100000008);
GO

INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, a.author_id
FROM books b
JOIN author a ON a.author_name = 'Nguyen Van An'
WHERE b.isbn = 100000001
  AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM books b JOIN author a ON a.author_name = 'Tran Minh Khoa'
WHERE b.isbn = 100000002 AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM books b JOIN author a ON a.author_name = 'Le Hoang Nam'
WHERE b.isbn = 100000003 AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM books b JOIN author a ON a.author_name = 'Pham Thu Ha'
WHERE b.isbn = 100000004 AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM books b JOIN author a ON a.author_name = 'Vo Quoc Bao'
WHERE b.isbn = 100000005 AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM books b JOIN author a ON a.author_name = 'Dang Thanh Tung'
WHERE b.isbn = 100000006 AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM books b JOIN author a ON a.author_name = 'Bui Mai Anh'
WHERE b.isbn = 100000007 AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM books b JOIN author a ON a.author_name = 'Ho Duc Long'
WHERE b.isbn = 100000008 AND NOT EXISTS (SELECT 1 FROM book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
GO

DECLARE @userid int = (SELECT MIN(id) FROM users);
IF @userid IS NOT NULL
BEGIN
    INSERT INTO rating (userid, bookid, rating, review_text)
    SELECT @userid, b.bookid, 5, 'Noi dung de hieu va huu ich.'
    FROM books b
    WHERE b.isbn = 100000001
      AND NOT EXISTS (SELECT 1 FROM rating r WHERE r.userid = @userid AND r.bookid = b.bookid);

    INSERT INTO rating (userid, bookid, rating, review_text)
    SELECT @userid, b.bookid, 4, 'Vi du thuc te, trinh bay ro rang.'
    FROM books b
    WHERE b.isbn = 100000002
      AND NOT EXISTS (SELECT 1 FROM rating r WHERE r.userid = @userid AND r.bookid = b.bookid);
END;
GO
