const db = require('../../../models'); // Import semua model melalui index.js
const bcrypt = require('bcryptjs');
const { generateToken, blacklistToken } = require('../helpers/auth');

// Fungsi untuk register user
const register = async (request, h) => {
  const { name, username, email, password } = request.payload;
  try {
    // Periksa apakah email sudah ada di database
    const existingUser = await db.User.findOne({ where: { email } });
    if (existingUser) {
      return h.response({ error: 'Email sudah terdaftar' }).code(400);
    }

    // Enkripsi password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Buat user baru
    const user = await db.User.create({ name, username, email, password: hashedPassword });
    return h.response({ message: 'User berhasil terdaftar', user }).code(201);
  } catch (err) {
    console.error(err); // Untuk debugging
    return h.response({ error: 'Terjadi kesalahan server' }).code(500);
  }
};

// Fungsi untuk login user
const login = async (request, h) => {
  const { email, password } = request.payload;
  try {
    // Cari user berdasarkan email
    const user = await db.User.findOne({ where: { email } });
    if (!user) return h.response({ error: 'Email atau password salah' }).code(400);

    // Cek kecocokan password
    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) return h.response({ error: 'Email atau password salah' }).code(400);

    // Generate token untuk user
    const token = generateToken({ userId: user.id });

    return h.response({ message: 'Login berhasil', token }).code(200);
  } catch (err) {
    console.error(err); // Untuk debugging
    return h.response({ error: 'Terjadi kesalahan server' }).code(500);
  }
};

// controllers/authController.js
const logout = async (request, h) => {
  try {
    const authHeader = request.headers.authorization;
    if (!authHeader) {
      return h.response({ error: 'Header Authorization diperlukan' }).code(400);
    }

    // Ekstrak token (support "Bearer <token>" atau langsung token)
    const token = authHeader.replace(/^Bearer\s+/i, '').trim();
    if (!token) {
      return h.response({ error: 'Format token tidak valid' }).code(400);
    }

    // Simpan ke blacklist
    await blacklistToken(token); // Akan throw error jika gagal

    return h.response({ 
      message: 'Logout berhasil',
      details: 'Token telah ditambahkan ke blacklist'
    }).code(200);

  } catch (err) {
    console.error('Logout Error:', err.stack); // Log detail error
    return h.response({ 
      error: 'Gagal logout',
      detail: err.message // Tampilkan pesan error spesifik
    }).code(500);
  }
};


module.exports = {
  register,
  login,
  logout
};