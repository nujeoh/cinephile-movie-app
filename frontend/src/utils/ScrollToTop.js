import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

// 関数: ScrollToTop
const ScrollToTop = () => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'instant' });
  }, [pathname]);

  return null;
};

export default ScrollToTop;
