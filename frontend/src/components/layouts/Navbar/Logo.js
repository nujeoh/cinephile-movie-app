import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Navbar } from 'react-bootstrap';

// 関数: Logo
const Logo = () => {
  const navigate = useNavigate();
// イベント処理関数: handleClick
  const handleClick = () => {
    window.scrollTo({ top: 0, left: 0, behavior: 'instant' });
    navigate('/');
  };

  return (
    <Navbar.Brand onClick={handleClick} style={{ cursor: 'pointer' }}>
      <img src="/logo.png" width="135" height="40" className="d-inline-block align-top" alt="CINEPHILE Logo" />
    </Navbar.Brand>
  );
};

export default Logo;
