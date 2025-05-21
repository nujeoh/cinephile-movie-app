import { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

// 認証状態を管理するProviderコンポーネント
// 関数: AuthProvider
export const AuthProvider = ({ children }) => {
  // ログイン状態を示す状態
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated, // 現在のログイン状態の値
        setIsAuthenticated, // 認証状態を変更する関数
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// AuthContextを使うためのカスタムフック
export const useAuthContext = () => useContext(AuthContext);
