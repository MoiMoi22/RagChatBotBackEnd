/*
 * Script chat tùy chỉnh cho RagChatBot TTG.
 *
 * Script này giữ lại logic cốt lõi để kết nối WebSocket qua SockJS và STOMP,
 * gửi câu hỏi đến backend, và hiển thị bong bóng chat cho cả người dùng và bot.
 * Thay đổi chính so với bản gốc là cách hiển thị liên kết tài liệu nguồn.
 * Thay vì hiển thị các placeholder đánh số như [1], [2], v.v., giờ đây sẽ hiển thị
 * trực tiếp liên kết cho từng tài liệu nguồn. Nhấn vào liên kết sẽ mở tài liệu trong tab mới
 * và không gây ra chuyển hướng ngoài ý muốn trong ứng dụng chat.
 */

let stompClient = null;

// Biến toàn cục để lưu trữ hàng đợi "typing indicator" và tham chiếu
// sử dụng khi cần loại bỏ hiệu ứng. Khi người dùng gửi câu hỏi, một
// bong bóng chat đặc biệt sẽ được tạo ra hiển thị thông báo “RAG đang trích
// xuất” với các chấm nhấp nháy. Khi bot gửi câu trả lời, bong bóng này
// sẽ được xóa bỏ.
let typingIndicatorRow = null;

/**
 * Hiển thị bong bóng “đang trích xuất” trong khu vực chat. Nếu đã có một
 * bong bóng chỉ báo đang hoạt động, nó sẽ được loại bỏ trước khi tạo mới.
 */
function showTypingIndicator() {
  const chatContainer = document.getElementById('chatContainer');
  // Xóa bong bóng typing hiện tại nếu có
  removeTypingIndicator();
  // Tạo hàng mới cho bot đang trích xuất
  const row = document.createElement('div');
  row.className = 'message-row bot typing-row';
  const bubble = document.createElement('div');
  bubble.className = 'message bot typing';
  // Tạo nội dung: dòng chữ và ba chấm nhấp nháy
  const textSpan = document.createElement('span');
  textSpan.textContent = 'RAG đang trích xuất';
  bubble.appendChild(textSpan);
  const dotsSpan = document.createElement('span');
  dotsSpan.className = 'typing-dots';
  // Tạo ba chấm
  for (let i = 0; i < 3; i++) {
    const dot = document.createElement('span');
    dot.className = 'dot';
    dotsSpan.appendChild(dot);
  }
  bubble.appendChild(dotsSpan);
  row.appendChild(bubble);
  chatContainer.appendChild(row);
  chatContainer.scrollTop = chatContainer.scrollHeight;
  typingIndicatorRow = row;
}

/**
 * Loại bỏ bong bóng “đang trích xuất” nếu tồn tại.
 */
function removeTypingIndicator() {
  if (typingIndicatorRow && typingIndicatorRow.parentNode) {
    typingIndicatorRow.parentNode.removeChild(typingIndicatorRow);
    typingIndicatorRow = null;
  }
}

/**
 * Chuyển đổi một chuỗi Markdown đơn giản thành HTML để hiển thị trong bong bóng chat.
 * Hỗ trợ in đậm bằng **...**, xuống dòng và danh sách bắt đầu bằng '-' hoặc '*'.
 *
 * @param {string} text Chuỗi Markdown cần chuyển đổi.
 * @returns {string} HTML đã được chuyển đổi.
 */

function parseMarkdown(text) {
  const input = String(text || '');
  // Thoát các ký tự HTML đặc biệt để tránh XSS
  const escapeHtml = (str) =>
    str.replace(/[&<>\"]|'/g, (ch) =>
      ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[ch] || ch)
    );
  const escaped = escapeHtml(input);
  const lines = escaped.split(/\r?\n/);
  let html = '';
  let inList = false;
  for (let rawLine of lines) {
    const line = rawLine.trimEnd();
    // Danh sách
    const listMatch = line.match(/^[-*]\s+(.*)/);
    if (listMatch) {
      if (!inList) {
        html += '<ul>';
        inList = true;
      }
      let item = listMatch[1].replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
      html += '<li>' + item + '</li>';
      continue;
    }
    // Đóng danh sách nếu kết thúc
    if (inList) {
      html += '</ul>';
      inList = false;
    }
    // Heading (###, ##, #)
    const headingMatch = line.match(/^#{1,6}\s+(.*)/);
    if (headingMatch) {
      let headingText = headingMatch[1].replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
      // Hiển thị heading như một đoạn in đậm để phù hợp trong bong bóng chat
      html += '<p><strong>' + headingText + '</strong></p>';
      continue;
    }
    // Dòng trống
    if (line === '') {
      html += '<br>';
      continue;
    }
    // Đoạn văn bình thường, xử lý in đậm
    const paragraph = line.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    html += '<p>' + paragraph + '</p>';
  }
  if (inList) {
    html += '</ul>';
  }
  return html;
}
/**
 * Thêm một tin nhắn chat vào khung chat.
 *
 * @param {string} sender 'user' hoặc 'bot'.
 * @param {string} text   Nội dung tin nhắn.
 */
function addMessage(sender, text) {
  const chatContainer = document.getElementById('chatContainer');
  const row = document.createElement('div');
  row.className = 'message-row ' + sender;
  const bubble = document.createElement('div');
  bubble.className = 'message ' + sender;
  bubble.textContent = text;
  row.appendChild(bubble);
  chatContainer.appendChild(row);
  chatContainer.scrollTop = chatContainer.scrollHeight;
}

/**
 * Hiển thị phản hồi của bot cùng với các liên kết tài liệu nguồn (nếu có).
 *
 * @param {string} answer  Nội dung trả lời từ backend.
 * @param {Array|undefined} sources Mảng các đối tượng tài liệu nguồn. Mỗi đối tượng có thể chứa thuộc tính `metadata` với các trường như `source`, `file_path`, `path`, `title`, hoặc `file_name`, hoặc có thể có thuộc tính `source` ở cấp cao nhất. Liên kết thực tế được lấy từ các trường này.
 */
function addBotMessage(answer, sources) {
  // Khi bot trả lời, hãy xóa chỉ báo gõ phím trước
  removeTypingIndicator();
  const chatContainer = document.getElementById('chatContainer');
  const row = document.createElement('div');
  row.className = 'message-row bot';
  const bubble = document.createElement('div');
  bubble.className = 'message bot';
  // answer text
  const answerEl = document.createElement('div');
  answerEl.className = 'message-text';
  // Hiển thị Markdown trong câu trả lời bot
  answerEl.innerHTML = parseMarkdown(answer || '');
  bubble.appendChild(answerEl);
  // display source document links when available
  if (Array.isArray(sources) && sources.length > 0) {
    const sourcesEl = document.createElement('div');
    sourcesEl.className = 'source-links';
    sources.forEach((doc, idx) => {
      let url = '';
      let linkText = '';
      if (doc) {
        try {
          if (typeof doc === 'string') {
            // When the source is a plain string, treat it as the URL
            url = doc;
            linkText = doc;
          } else if (doc.metadata) {
            // Prefer `source`, then `file_path`, then `path` if present
            url =
              doc.metadata.source ||
              doc.metadata.file_path ||
              doc.metadata.path ||
              '';
            // Use a title or file name if available as link text
            linkText = doc.metadata.title || doc.metadata.file_name || url;
          } else if (doc.source) {
            url = doc.source;
            linkText = doc.source;
          }
        } catch (e) {
          url = '';
          linkText = '';
        }
      }
      // Skip empty URLs so we don't render useless anchors
      if (!url) return;
      const link = document.createElement('a');
      link.href = url;
      link.target = '_blank';
      link.rel = 'noopener noreferrer';
      // Display descriptive text if available, otherwise show the URL
      link.textContent = linkText || url;
      link.addEventListener('click', function (e) {
        e.stopPropagation();
      });
      sourcesEl.appendChild(link);
      if (idx < sources.length - 1) {
        sourcesEl.appendChild(document.createTextNode(' '));
      }
    });
    bubble.appendChild(sourcesEl);
  }
  row.appendChild(bubble);
  chatContainer.appendChild(row);
  chatContainer.scrollTop = chatContainer.scrollHeight;
}

/**
 * Gửi tin nhắn chat đến server qua STOMP.
 *
 * @param {string} text Câu hỏi của người dùng.
 */
function sendMessage(text) {
  if (stompClient && stompClient.connected) {
    const payload = { question: text };
    stompClient.send('/app/chat', {}, JSON.stringify(payload));
  }
}

/**
 * Thiết lập kết nối WebSocket sử dụng SockJS và STOMP. Lấy JSON Web Token (JWT) từ localStorage và thêm vào query parameter để xác thực. Tin nhắn nhận từ server sẽ kích hoạt việc hiển thị phản hồi của bot.
 */
function connect() {
  const token = localStorage.getItem('jwt');
  if (!token) {
    // If no token is found, redirect to login
    window.location.href = '/login';
    return;
  }
  // Connect to the WebSocket with the Bearer token encoded in the URL
  const socket = new SockJS('/ws-chat?token=' + encodeURIComponent('Bearer ' + token));
  stompClient = Stomp.over(socket);
  // Disable STOMP debugging for cleaner console output
  stompClient.debug = null;
  stompClient.connect(
    {},
    function (frame) {
      console.log('Connected: ' + frame);
      // Subscribe to the user's private queue for messages
      stompClient.subscribe('/user/queue/messages', function (message) {
        try {
          const body = JSON.parse(message.body);
          addBotMessage(body.answer, body.sourceDocuments);
        } catch (e) {
          console.error('Error parsing message from server:', e);
        }
      });
    },
    function (error) {
      console.error('Failed to connect to WebSocket:', error);
      // Remove token and redirect to login on failure
      localStorage.removeItem('jwt');
      window.location.href = '/login';
    }
  );
}

// Khởi tạo khi DOM đã sẵn sàng
document.addEventListener('DOMContentLoaded', () => {
  const chatForm = document.getElementById('chatForm');
  const messageInput = document.getElementById('messageInput');
  const logoutBtn = document.getElementById('logoutBtn');
  // Xử lý gửi form để gửi tin nhắn
  chatForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const text = messageInput.value.trim();
    if (text !== '') {
      addMessage('user', text);
      sendMessage(text);
      messageInput.value = '';
      // Đặt lại chiều cao textarea sau khi gửi
      messageInput.style.height = 'auto';
      // Hiển thị hiệu ứng đang trích xuất khi người dùng gửi câu hỏi
      showTypingIndicator();
    }
  });
  // Đăng xuất: xóa token và gọi API logout phía backend
  logoutBtn.addEventListener('click', async () => {
    try {
      await fetch('/api/auth/logout', { method: 'POST' });
    } catch (err) {
      console.error('Error during logout:', err);
    }
    localStorage.removeItem('jwt');
    window.location.href = '/login';
  });
  // Hiển thị lời chào từ bot khi trang được tải
  addMessage('bot', 'Chào mừng bạn đến với RagChatBot TTG! Hãy đặt câu hỏi để bắt đầu trò chuyện.');
  // Thiết lập kết nối WebSocket
  connect();
  // Tự động thay đổi kích thước textarea khi người dùng nhập
  const autoResize = () => {
    messageInput.style.height = 'auto';
    messageInput.style.height = messageInput.scrollHeight + 'px';
  };
  messageInput.addEventListener('input', autoResize);
  // Gửi form khi nhấn Enter, cho phép Shift+Enter để xuống dòng
  messageInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      chatForm.requestSubmit();
    }
  });
});
