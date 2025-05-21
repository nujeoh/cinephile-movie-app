import React, { useEffect, useState } from 'react';
import { api } from 'api/apiClient';
import MainNavbar from 'components/layouts/Navbar/MainNavBar';
import Footer from 'components/layouts/footer/Footer';
import ProfileHeader from 'components/common/ProfileHeader';
import ReviewCol from 'components/common/ReviewCol';
import { Spinner } from 'react-bootstrap';

// ページコンポーネント: UserDetailPage
const UserDetailPage = () => {
  const [user, setUser] = useState(null);
  const [reviews, setReviews] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const [userRes, reviewRes] = await Promise.all([api.get('/api/user'), api.get('/api/review/user/preview')]);
        setUser(userRes.data);
        setReviews(reviewRes.data);
      } catch (err) {
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchUser();
  }, []);

  return (
    <div className="user-detail">
      <MainNavbar />

      {isLoading ? (
        <div className="d-flex justify-content-center align-items-center vh-100">
          <Spinner animation="border" variant="danger" />
        </div>
      ) : (
        <>
          <ProfileHeader user={user} />
          <ReviewCol review={reviews} user={user} />
          <Footer />
        </>
      )}
    </div>
  );
};

export default UserDetailPage;
