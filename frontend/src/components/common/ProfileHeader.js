import React, { useRef, useState } from 'react';
import { Card, Container, Row, Col, Image, Button, Spinner } from 'react-bootstrap';
import { FiCamera } from 'react-icons/fi';
import 'styles/ProfileHeader.css';
import { api } from 'api/apiClient';

// 関数: ProfileHeader
const ProfileHeader = ({ user }) => {
  const fileInputRef = useRef(null);
  const [preview, setPreview] = useState(user.profileImageUrl || '/defaultProfile.png'); // プレビュー用URL
  const [uploading, setUploading] = useState(false);

  // ファイル選択ダイアログを開く
// イベント処理関数: handleClickUpload
  const handleClickUpload = () => {
    fileInputRef.current?.click();
  };

  // ファイル選択後にプレビュー更新＆アップロード
  const handleFileChange = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // アップロード用FormDataを作成
    const form = new FormData();
    form.append('file', file);

    try {
      setUploading(true);
      const { data } = await api.put(`/api/user/${user.id}/profile-image`, form, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      // サーバーから返却されたURLでプレビューを更新
      setPreview(data.url);
    } catch (err) {
      console.error('プロフィール画像更新失敗', err);
    } finally {
      setUploading(false);
      // ファイル選択をリセット
      e.target.value = '';
    }
  };

  return (
    <Container className="d-flex justify-content-center mb-4 mt-5">
      <Card className="profile-card p-4 w-100" style={{ maxWidth: '600px' }}>
        <Row className="align-items-center mb-3">
          <Col xs="auto" className="position-relative">
            <Image key={preview} src={preview} roundedCircle width={100} height={100} className="profile-avatar" />

            <Button
              variant="dark"
              className="position-absolute bottom-0 end-0"
              onClick={handleClickUpload}
              style={{ borderRadius: '50%' }}
            >
              {uploading ? <Spinner animation="border" size="sm" /> : <FiCamera />}
            </Button>
            <input
              type="file"
              accept="image/*"
              ref={fileInputRef}
              onChange={handleFileChange}
              style={{ display: 'none' }}
            />
          </Col>

          <Col className="ms-3">
            <h5 className="mb-1">{user.name}</h5>
            <div className="text-muted mb-2">{user.email}</div>
          </Col>
        </Row>

        <hr />
        <Row className="text-center">
          <Col>
            <div className="stat-number">{user.reviewCount}</div>
            <div className="text-muted">レビュー</div>
          </Col>
          <Col>
            <div className="stat-number">{user.commentCount}</div>
            <div className="text-muted">コメント</div>
          </Col>
          <Col>
            <div className="stat-number">{user.likeCount}</div>
            <div className="text-muted">いいね済み</div>
          </Col>
        </Row>
      </Card>
    </Container>
  );
};

export default ProfileHeader;
