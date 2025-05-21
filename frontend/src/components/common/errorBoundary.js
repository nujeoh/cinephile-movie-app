import React from 'react';

export default class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError(error) {
    // Reactのレンダリング中にエラーが発生した場合、hasErrorをtrueに設定
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    // エラー発生時の処理（例: SentryやLogRocketなどへのログ送信）
    console.error('Uncaught error in subtree:', error, errorInfo);

    // React Routerのコンテキストに依存せず、即座にホームにリダイレクト
    window.location.replace('/');
  }

  render() {
    if (this.state.hasError) {
      // すでにwindow.location.replaceでページ遷移しているため、
      // 追加のUIは不要でnullを返却
      return null;
    }
    // エラーがない場合は、通常通り子コンポーネントをレンダリング
    return this.props.children;
  }
}
