import React, { useState, useEffect } from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import { BsSearch } from 'react-icons/bs';
import { useNavigate } from 'react-router-dom';

// 関数: SearchBar
const SearchBar = ({ keyword }) => {
  const [searchQuery, setSearchQuery] = useState(''); // 検索文字列の状態管理
  const navigate = useNavigate();

  // プロップスkeywordが更新されたら入力欄にセット
  useEffect(() => {
    if (keyword) setSearchQuery(keyword);
  }, [keyword]);

  // 入力値変更ハンドラ
  // イベント処理関数: handleSearchChange
  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };

  // 検索実行（空白のみは無視）
  // 関数: doSearch
  const doSearch = () => {
    const q = searchQuery.trim();
    if (q) {
      navigate(`/search?query=${encodeURIComponent(q)}`);
    }
  };

  // Enterキー押下で検索
  // イベント処理関数: handleKeyPress
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') doSearch();
  };

  return (
    <InputGroup style={{ width: '350px' }} className="justify-content-center">
      <Form.Control
        placeholder="映画のタイトルを検索"
        style={{ fontSize: 'small' }}
        value={searchQuery}
        onChange={handleSearchChange}
        onKeyDown={handleKeyPress} // 入力時のキーイベント
      />
      <Button
        variant="outline-secondary"
        onClick={doSearch}
        disabled={!searchQuery.trim()} // 空欄時は無効化
      >
        <BsSearch />
      </Button>
    </InputGroup>
  );
};

export default SearchBar;
