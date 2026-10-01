package thuc.ute.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.Part;

public final class CloudinaryUtil_24110349 {
    public static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;

    // Cấu hình mặc định phục vụ bài tập. Biến môi trường vẫn được ưu tiên nếu có.
    private static final String DEFAULT_CLOUD_NAME = "gnht4fer";
    private static final String DEFAULT_API_KEY = "997495484919781";
    private static final String DEFAULT_API_SECRET = "Z6y76Ybd8vwWrOiOzbMXrnH_JOs";

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif");
    private static final Pattern SECURE_URL_PATTERN = Pattern.compile(
            "\\\"secure_url\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private CloudinaryUtil_24110349() {
    }

    public static String uploadImage(Part filePart, String folder) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }
        if (filePart.getSize() > MAX_IMAGE_SIZE) {
            throw new IOException("Ảnh tải lên không được vượt quá 5 MB.");
        }

        String contentType = filePart.getContentType();
        contentType = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IOException("Chỉ chấp nhận ảnh JPG, PNG, WEBP hoặc GIF.");
        }
        if (folder == null || !folder.matches("[A-Za-z0-9_/-]+")) {
            throw new IOException("Thư mục Cloudinary không hợp lệ.");
        }

        String cloudName = config("cloudinary.cloudName", "CLOUDINARY_CLOUD_NAME", DEFAULT_CLOUD_NAME);
        String apiKey = config("cloudinary.apiKey", "CLOUDINARY_API_KEY", DEFAULT_API_KEY);
        String apiSecret = config("cloudinary.apiSecret", "CLOUDINARY_API_SECRET", DEFAULT_API_SECRET);
        long timestamp = Instant.now().getEpochSecond();
        String signature = sha1("folder=" + folder + "&timestamp=" + timestamp + apiSecret);

        byte[] fileBytes;
        try (InputStream inputStream = filePart.getInputStream()) {
            fileBytes = inputStream.readAllBytes();
        }

        String boundary = "----KStore" + UUID.randomUUID().toString().replace("-", "");
        byte[] requestBody = buildMultipartBody(boundary, filePart.getSubmittedFileName(),
                contentType, fileBytes, apiKey, timestamp, signature, folder);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.cloudinary.com/v1_1/" + cloudName + "/image/upload"))
                .timeout(Duration.ofSeconds(45))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(
                    request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("Cloudinary từ chối upload (HTTP " + response.statusCode() + ").");
            }
            Matcher matcher = SECURE_URL_PATTERN.matcher(response.body());
            if (!matcher.find()) {
                throw new IOException("Cloudinary không trả về URL ảnh.");
            }
            return matcher.group(1).replace("\\/", "/");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Upload Cloudinary bị gián đoạn.", e);
        }
    }

    private static byte[] buildMultipartBody(String boundary, String submittedFileName,
            String contentType, byte[] fileBytes, String apiKey, long timestamp,
            String signature, String folder) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        writeTextPart(output, boundary, "api_key", apiKey);
        writeTextPart(output, boundary, "timestamp", String.valueOf(timestamp));
        writeTextPart(output, boundary, "signature", signature);
        writeTextPart(output, boundary, "folder", folder);

        String safeFileName = submittedFileName == null
                ? "book-cover" : submittedFileName.replaceAll("[^A-Za-z0-9._-]", "_");
        output.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        output.write(("Content-Disposition: form-data; name=\"file\"; filename=\""
                + safeFileName + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        output.write(("Content-Type: " + contentType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        output.write(fileBytes);
        output.write("\r\n".getBytes(StandardCharsets.UTF_8));
        output.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        return output.toByteArray();
    }

    private static void writeTextPart(ByteArrayOutputStream output, String boundary,
            String name, String value) throws IOException {
        output.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        output.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n")
                .getBytes(StandardCharsets.UTF_8));
        output.write(value.getBytes(StandardCharsets.UTF_8));
        output.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    private static String sha1(String input) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return HexFormat.of().formatHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Máy chủ không hỗ trợ SHA-1 để ký Cloudinary.", e);
        }
    }

    private static String config(String propertyName, String environmentName, String defaultValue) {
        String value = System.getProperty(propertyName);
        if (value == null || value.isBlank()) {
            value = System.getenv(environmentName);
        }
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }
}
