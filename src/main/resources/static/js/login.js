/*
 * Script xử lý đăng nhập cho ứng dụng nội bộ sử dụng JWT.
 *
 * Trang login.html gọi script này để gửi thông tin tài khoản tới API
 * '/api/auth/login'. Nếu đăng nhập thành công, server trả về chuỗi JWT
 * (không bọc trong JSON). Token sẽ được lưu vào localStorage và người dùng
 * được chuyển tới trang chat. Nếu đăng nhập thất bại, hiển thị thông báo
 * lỗi cho người dùng.
 */

document.addEventListener('DOMContentLoaded', () => {
  const loginForm = document.getElementById('loginForm');
  const errorDiv = document.getElementById('loginError');

  loginForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    // Ẩn thông báo lỗi trước khi thử đăng nhập
    errorDiv.classList.add('d-none');

    try {
      const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });
      if (!res.ok) {
        throw new Error('Thông tin đăng nhập không chính xác.');
      }
      const token = await res.text();
      // Lưu JWT vào localStorage để sử dụng cho các yêu cầu sau
      localStorage.setItem('jwt', token);
      // Chuyển tới trang chat
      window.location.href = '/chat-demo';
    } catch (err) {
      errorDiv.textContent = err.message;
      errorDiv.classList.remove('d-none');
    }
  });
});