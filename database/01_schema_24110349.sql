USE master;
GO

IF DB_ID('Ktra_web') IS NULL
BEGIN
    CREATE DATABASE Ktra_web;
END;
GO

USE Ktra_web;
GO

IF OBJECT_ID('dbo.users', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.users (
        id          int IDENTITY(1,1) NOT NULL,
        email       varchar(50) NOT NULL,
        fullname    nvarchar(50) NULL,
        phone       int NULL,
        passwd      varchar(32) NOT NULL,
        signup_date datetime NULL,
        last_login  datetime NULL,
        is_admin    bit NULL CONSTRAINT DF_users_is_admin DEFAULT 0,
        CONSTRAINT PK_users PRIMARY KEY (id),
        CONSTRAINT UQ_users_email UNIQUE (email)
    );
END;
GO

IF OBJECT_ID('dbo.books', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.books (
        bookid       int IDENTITY(1,1) NOT NULL,
        isbn         int NULL,
        title        varchar(200) NOT NULL,
        publisher    varchar(100) NULL,
        price        decimal(6,2) NULL,
        description  text NULL,
        publish_date date NULL,
        cover_image  varchar(500) NULL,
        quantity     int NULL,
        CONSTRAINT PK_books PRIMARY KEY (bookid),
        CONSTRAINT CK_books_price CHECK (price IS NULL OR price >= 0),
        CONSTRAINT CK_books_quantity CHECK (quantity IS NULL OR quantity >= 0)
    );
END;
GO

IF OBJECT_ID('dbo.author', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.author (
        author_id    int IDENTITY(1,1) NOT NULL,
        author_name  varchar(100) NOT NULL,
        date_of_birth date NULL,
        CONSTRAINT PK_author PRIMARY KEY (author_id)
    );
END;
GO

IF OBJECT_ID('dbo.book_author', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.book_author (
        bookid   int NOT NULL,
        author_id int NOT NULL,
        CONSTRAINT PK_book_author PRIMARY KEY (bookid, author_id),
        CONSTRAINT FK_book_author_book FOREIGN KEY (bookid)
            REFERENCES dbo.books(bookid) ON DELETE CASCADE,
        CONSTRAINT FK_book_author_author FOREIGN KEY (author_id)
            REFERENCES dbo.author(author_id) ON DELETE CASCADE
    );
END;
GO

IF OBJECT_ID('dbo.rating', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.rating (
        userid      int NOT NULL,
        bookid      int NOT NULL,
        rating      tinyint NULL,
        review_text text NULL,
        CONSTRAINT PK_rating PRIMARY KEY (userid, bookid),
        CONSTRAINT FK_rating_user FOREIGN KEY (userid)
            REFERENCES dbo.users(id) ON DELETE CASCADE,
        CONSTRAINT FK_rating_book FOREIGN KEY (bookid)
            REFERENCES dbo.books(bookid) ON DELETE CASCADE,
        CONSTRAINT CK_rating_value CHECK (rating IS NULL OR rating BETWEEN 1 AND 5)
    );
END;
GO
