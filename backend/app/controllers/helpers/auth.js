require('dotenv').config();
const jwt = require('jsonwebtoken');
const db = require('../../../models');
const { raw } = require('mysql2');

const SECRET_KEY = process.env.SECRET_KEY || "76Reg!0GradeManagementauthSecretKey!78$Secret@jwt";

function generateToken(payload) {
  return jwt.sign(payload, SECRET_KEY, { expiresIn: '1h' });
}

async function verifyToken(token) {
  if (!token) throw new Error('No token provided');

  const isRevoked = await isTokenBlacklisted(token);
  
  if (isRevoked) {
    throw new Error('Token revoked');
  }

  return jwt.verify(token, SECRET_KEY);
}


async function blacklistToken(token) {
  try {
    if (!token || typeof token !== 'string') {
      throw new Error('Token tidak valid');
    }
    
    const result = await db.BlacklistedToken.create({ token });
    return !!result;
  } catch (err) {
    console.error('Error in blacklistToken:', err.message);
    throw new Error('Gagal menyimpan token: ' + err.message);
  }
}

async function isTokenBlacklisted(token) {
  try {
    const blacklisted = await db.BlacklistedToken.findOne({ 
      where: { token },
    });
    return !!blacklisted;
  } catch (err) {
    throw new Error('Error checking blacklist: ' + err.message);
  }
}

module.exports = { generateToken, verifyToken, blacklistToken, isTokenBlacklisted };