-- URL do Cloudinary trả về có thể dài hơn 100 ký tự.
ALTER TABLE books ALTER COLUMN cover_image varchar(500) NULL;
