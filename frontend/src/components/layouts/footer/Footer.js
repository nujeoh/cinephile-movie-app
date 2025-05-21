import React from 'react';
import { BsFacebook, BsInstagram, BsTwitterX } from 'react-icons/bs';
import 'styles/Footer.css';

// 関数: Footer
const Footer = () => {
  return (
    <div className="footer border-top">
      <div className="container-md pt-3 pb-4">
        <div className="sns mb-2">
          <span className="facebook me-2">
            <BsFacebook />
          </span>
          <span className="twitter-x me-2">
            <BsTwitterX />
          </span>
          <span className="instagram">
            <BsInstagram />
          </span>
        </div>
        <p className="footer-text text-body-secondary mb-1">メール | hj96apan@gmail.com</p>
        <p className="footer-text text-body-secondary mb-1">ギットハブ | https://github.com/nujeoh</p>
        <p className="footer-text text-body-secondary mb-1">電話番号 | +81 80-7360-4320 / +82 10-4396-4320</p>
        <p className="footer-text text-body-secondary">
          <img src="/logo.png" className="footer-logo" alt="CINEPHILE" width="70" height="18" />
          &nbsp;&nbsp;© 2025 by CINEPHILE, Portfolio Project by Hoejun.
        </p>
      </div>
    </div>
  );
};

export default Footer;
