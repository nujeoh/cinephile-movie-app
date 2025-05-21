import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { VscTriangleDown } from 'react-icons/vsc';
import { useModalContext, MODAL_TYPES } from 'context/ModalContext';

// ソート基準を表示するコンポーネント
// 関数: SortRow
const SortRow = ({ movieId = null, userId = null, sort }) => {
  const { openModal } = useModalContext();

  // ソートオプションのリスト
  const options = [
    { key: 'like', label: 'いいね順' }, // いいね順
    { key: 'recent', label: '新着順' }, // 新着順
    { key: 'high', label: '高評価順' }, // 高評価順
    { key: 'low', label: '低評価順' }, // 低評価順
  ];

  // 現在選択されているオプションを取得
  const currentOption = options.find((opt) => opt.key === sort);
  const currentLabel = currentOption.label;

  return (
    <Navbar expand="md" fixed="top" className="bg-body-tertiary" style={{ top: '67px', zIndex: 100 }}>
      <Container fluid="md">
        <Nav className="me-auto">
          <Nav.Link active onClick={() => openModal(MODAL_TYPES.SORT, { sort, movieId, userId })}>
            <VscTriangleDown />
            &nbsp;
            {currentLabel}
          </Nav.Link>
        </Nav>
      </Container>
    </Navbar>
  );
};

export default SortRow;
