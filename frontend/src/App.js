import 'react-toastify/dist/ReactToastify.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import HomePage from 'pages/HomePage';
import MovieDetailPage from 'pages/MovieDetailPage';
import SearchPage from 'pages/SearchPage';
import ScrollToTop from 'utils/ScrollToTop';

import ModalManager from 'components/modals/ModalManager';
import { ModalProvider } from 'context/ModalContext';
import { AuthProvider } from 'context/AuthContext';
import AuthInitializer from 'context/AuthInitializer';
import ReviewDetailPage from 'pages/ReviewDetailPage';
import ReviewListPage from 'pages/ReviewListPage';
import { ToastContainer } from 'react-toastify';
import UserDetailPage from 'pages/UserDetailPage';
import ErrorBoundary from 'components/common/errorBoundary';

// 関数: App
const App = () => {
  return (
    <AuthProvider>
      <ModalProvider>
        <BrowserRouter>
          <ErrorBoundary>
            <AuthInitializer>
              <ScrollToTop />
              <ToastContainer
                position="top-center"
                autoClose={3000}
                hideProgressBar={true}
                newestOnTop
                closeOnClick
                pauseOnHover
                draggable
                toastClassName="my-toast"
                bodyClassName="my-toast-body"
                progressClassName="my-toast-progress"
              />
              <Routes>
                <Route path="/" element={<HomePage />}></Route>
                <Route path="/movie/:movieId" element={<MovieDetailPage />}></Route>
                <Route path="/search" element={<SearchPage />}></Route>
                <Route path="/review/:reviewId" element={<ReviewDetailPage />}></Route>
                <Route path="/movie/:movieId/reviews" element={<ReviewListPage />}></Route>
                <Route path="/user/:userId/reviews" element={<ReviewListPage />}></Route>
                <Route path="/user/:userId" element={<UserDetailPage />}></Route>
                <Route path="*" element={<HomePage />} />
              </Routes>
              <ModalManager />
            </AuthInitializer>
          </ErrorBoundary>
        </BrowserRouter>
      </ModalProvider>
    </AuthProvider>
  );
};

export default App;
