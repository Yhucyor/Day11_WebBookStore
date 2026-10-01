// main.js
document.addEventListener("DOMContentLoaded", () => {
    // Hiệu ứng đổ bóng mượt mà cho header khi scroll
    const header = document.querySelector(".site-header");
    if(header) {
        window.addEventListener("scroll", () => {
            if (window.scrollY > 10) {
                header.style.boxShadow = "0 10px 15px -3px rgba(0, 0, 0, 0.1)";
            } else {
                header.style.boxShadow = "0 4px 6px -1px rgba(0, 0, 0, 0.05)";
            }
        });
    }

    // Hiệu ứng fade in nhẹ nhàng cho nội dung chính
    const mainContent = document.querySelector(".main-content");
    if(mainContent) {
        mainContent.style.opacity = "0";
        mainContent.style.transform = "translateY(15px)";
        mainContent.style.transition = "all 0.6s cubic-bezier(0.16, 1, 0.3, 1)";
        
        setTimeout(() => {
            mainContent.style.opacity = "1";
            mainContent.style.transform = "translateY(0)";
        }, 150);
    }
});
