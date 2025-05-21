import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Card, Container, Spinner, Image } from 'react-bootstrap';
import { VscCommentDraft } from 'react-icons/vsc';
import dayjs from 'utils/dayjs';
import { api } from 'api/apiClient';

// CommentListコンポーネント
// 関数: CommentList
const CommentList = ({ reviewId, initialComments, initialPage, initialHasNext }) => {
  // ステート管理
  const [comments, setComments] = useState(initialComments); // コメント一覧
  const [loading, setLoading] = useState(false); // ローディングフラグ
  const pageRef = useRef(initialPage); // 現在のページ
  const hasNextRef = useRef(initialHasNext); // 次ページの有無
  const loadingRef = useRef(false); // 内部ローディングロック
  const observerRef = useRef(null); // インフィニットスクロール要素の参照

  // コメントを取得する関数
  const fetchComments = useCallback(async () => {
    // 既に読み込み中、またはこれ以上取得すべきデータがない場合は終了
    if (loadingRef.current || !hasNextRef.current) return;

    loadingRef.current = true;
    setLoading(true);

    try {
      const { data } = await api.get(`/api/review/${reviewId}/comments`, {
        params: { page: pageRef.current, size: 10 },
      });

      // 取得したコメントを追加
      setComments((prev) => [...prev, ...data.content]);

      // 次ページの有無を更新
      hasNextRef.current = !data.last;
      pageRef.current += 1;
    } catch (err) {
      console.error('コメント取得失敗', err);
    } finally {
      loadingRef.current = false;
      setLoading(false);
    }
  }, [reviewId]);

  // 無限スクロール設定
  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) fetchComments();
      },
      {
        rootMargin: '0px 0px -200px 0px',
      }
    );

    const el = observerRef.current;
    if (el) observer.observe(el);

    return () => {
      if (el) observer.unobserve(el);
      observer.disconnect();
    };
  }, [fetchComments]);

  useEffect(() => {
    setComments(initialComments);
    pageRef.current = initialPage;
    hasNextRef.current = initialHasNext;
  }, [initialComments, initialPage, initialHasNext]);

  return (
    <Container fluid="md" className="p-0">
      {comments.length === 0 && (
        <div className="text-center text-muted py-4 mb-2">
          <div className="text-center text-muted pt-3 pb-2">
            <VscCommentDraft style={{ fontSize: '80px' }} />
          </div>
          <div>まだコメントがありません。</div>
        </div>
      )}

      {comments.map((comment, idx) => (
        <Card key={idx} className={idx !== 0 ? 'border-top pt-3' : ''}>
          <Card.Body className="px-4">
            <div className="d-flex justify-content-between align-items-center">
              <div className="d-flex align-items-center">
                <Image
                  src={comment.profileImageUrl || '/defaultProfile.png'}
                  roundedCircle
                  width={28}
                  height={28}
                  style={{ objectFit: 'cover' }}
                  className="me-2"
                />
                <strong>{comment.userName}</strong>
              </div>
              <span className="text-muted" style={{ whiteSpace: 'nowrap' }}>
                {dayjs(comment.createdAt).fromNow()}
              </span>
            </div>
            <p className="mt-3">{comment.content}</p>
          </Card.Body>
        </Card>
      ))}

      <div ref={observerRef} className="text-center py-3">
        {loading && <Spinner animation="border" variant="danger" />}
      </div>
    </Container>
  );
};

export default CommentList;
