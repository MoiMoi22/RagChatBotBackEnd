# RagChatBotBackEnd

> **Spring Boot back-end** cho hệ thống **RAG Chatbot** nội bộ.  
> Cung cấp **WebSocket (SockJS + STOMP)** để nhận câu hỏi, truy vấn RAG và phản hồi theo thời gian thực.  
> Frontend tĩnh (chat UI) được phục vụ từ `/static` và **đã có typing indicator** *“RAG đang trích xuất…”*.

---

## ✨ Tính năng chính

- **Real-time chat** qua WebSocket (SockJS + STOMP).
- **RAG pipeline**: trích xuất + tổng hợp câu trả lời, kèm danh mục nguồn (source documents).
- **Bảo mật**: xác thực bằng **JWT** (token được truyền trong query khi bắt tay WebSocket).
- **Frontend tối giản**:
  - `chat.html` + `css/chat.css` + `js/chat.js`.
  - **Typing indicator** *“RAG đang trích xuất…”* (animated dots) hiển thị trong lúc RAG xử lý, tự ẩn khi bot trả lời.

---

## 🏗 Kiến trúc & Luồng dữ liệu

**Luồng chat (FE → BE → FE):**

1. FE kết nối `SockJS` tới: `/ws-chat?token=Bearer <JWT>`
2. FE gửi câu hỏi qua STOMP destination: `/app/chat`  
   Payload ví dụ:
   ```json
   { "question": "Nội dung câu hỏi" }
   ```
3. BE truy vấn RAG và publish về hàng đợi riêng của user: `/user/queue/messages`  
   Response ví dụ:
   ```json
   {
     "answer": "Nội dung trả lời…",
     "sourceDocuments": [
       { "metadata": { "title": "Quy trình A", "source": "https://..." } },
       { "metadata": { "file_name": "TaiLieu.pdf", "path": "/files/TaiLieu.pdf" } }
     ]
   }
   ```

---

## 📁 Cấu trúc thư mục (rút gọn)

```
.
├── pom.xml
├── src
│   ├── main
│   │   ├── java/… (controller, security,…)
│   │   └── resources
│   │       ├── static
│   │       │   ├── chat.html
│   │       │   ├── css/chat.css
│   │       │   └── js/chat.js
│   │       └── application.yml
└── README.md
```

---

## ⚙️ Yêu cầu hệ thống

- **JDK 17+**
- **Maven 3.8+** (đã kèm `mvnw/mvnw.cmd`)
- (Tùy chọn) Vector DB / nguồn dữ liệu phục vụ RAG

---

## 🚀 Chạy nhanh (Local)

👉 Mặc định server chạy tại: [http://localhost:8080](http://localhost:8080)

---

## 🖥 Frontend tĩnh (đi kèm)

- Không cần sửa `chat.html`.
- Đảm bảo đường dẫn nạp file:
  ```html
  <link rel="stylesheet" href="/css/chat.css" />
  <script src="/js/chat.js"></script>
  ```
- FE sẽ:
  - Tự kết nối WebSocket: `/ws-chat?token=Bearer <JWT>`
  - Gửi câu hỏi tới `/app/chat`
  - Subscribe `/user/queue/messages`
  - Hiển thị typing indicator *“RAG đang trích xuất…”* trong lúc chờ.

---

## 🔐 JWT & WebSocket

FE đọc JWT từ `localStorage` và gắn vào query khi connect SockJS:  
```
/ws-chat?token=Bearer <JWT>
```

Nếu xác thực thất bại, FE chuyển hướng `/login`.

**Ví dụ cấu hình `application.yml`:**
```yaml
server:
  port: 8080

# ví dụ: bật forward headers khi reverse proxy
server.forward-headers-strategy: native
```

---
## 🆕 FE Update (Typing Indicator)

- Đã thêm hiệu ứng *“RAG đang trích xuất…”* với chấm động trong `chat.css` + `chat.js`.
- Tự động hiển thị sau khi gửi câu hỏi và ẩn khi có trả lời từ BE.
- Không cần chỉnh sửa `chat.html`.

---
## 🤝 Đóng góp

- Quy ước commit: ngắn gọn, tiếng Anh hoặc Việt. Ví dụ:
  ```
  feat(fe): typing indicator
  fix(be): null checks in RAG pipeline
  ```
- PR nên mô tả mục tiêu, cách test, kèm ảnh/chụp log khi cần.
---

## 📄 License

Internal / Private.  
Liên hệ chủ repo nếu muốn sử dụng lại.

---

Made with ❤️ by the Phuc-Linh team.
