USE Ktra_web;
GO

-- Cloudinary có thể trả về URL dài hơn giới hạn 100 ký tự ban đầu.
IF COL_LENGTH('dbo.books', 'cover_image') < 500
BEGIN
    ALTER TABLE dbo.books ALTER COLUMN cover_image varchar(500) NULL;
END;
GO
