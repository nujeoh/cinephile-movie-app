import { jwtDecode } from 'jwt-decode';

// 関数: isTokenExpired
export const isTokenExpired = (token) => {
  try {
    const decoded = jwtDecode(token);
    const now = Date.now() / 1000;
    return decoded.exp < now;
  } catch (e) {
    return true;
  }
};
