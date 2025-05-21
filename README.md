# 🎬 Cinephile – 映画コミュニティプラットフォーム

> 映画ファンのためのレビュー＆検索機能付きウェブアプリケーションです。<br>
> TMDB APIを利用して最新の映画情報を提供し、ユーザー認証後にレビューの作成が可能です。


<br><br>


## 🔗 デモおよびデプロイリンク
- **フロントエンド (S3 + Route 53 ドメイン接続)**  
  http://cinephile-app.com 

- **バックエンド (AWS Elastic Beanstalk)**  
  http://cinephile-app-dev-free.eba-mqph9p7r.ap-northeast-1.elasticbeanstalk.com

> **💡 Note:**  
> - デモ用として、映画 「**君の名は。**」 に複数のレビュー、いいね、コメントをあらかじめ登録しています。  
> - 機能をすぐに確認したい方は、 「**トップページの上部または新海誠監督の作品にある当該映画**」 をクリックしてご覧ください。


<br><br>


## 🧱 技術スタック

### フロントエンド
- React (Create React App)  
- React Router DOM – ルーティング
- React Context API – 認証・モーダル状態管理
- Axios – APIクライアント（インターセプターでJWT自動添付）
- React-Bootstrap –  UIコンポーネント  
- React-Toastify – 通知
- Day.js – 日付処理 
- Hashids –URLに露出する数値IDを暗号化

### バックエンド
- Java 17 + Spring Boot – REST API  
- Spring Security + JWT – 認証・認可  
- Spring Validation – リクエストDTO検証  
- Spring Data JPA – DB連携 
- Spring Batch – バッチジョブスケジューリング  
- Gradle – ビルドツール
- AWS Elastic Beanstalk – アプリホスティング
- AWS S3 – プロフィール画像保存


<br><br>


## 🏗️ アーキテクチャ構成（システム構成図）

![Image](https://github.com/user-attachments/assets/7d3a01fc-cf93-4f3e-92f2-9508a0bcfb0a)

> **💡 Note:**  
>- 以下は Cinephile アプリの全体構成図です。  
> - フロントエンド、バックエンド、DB、S3、外部APIなどの連携関係を示しています。


<br><br>


## ✨ 主な機能

### 🎥 映画情報
- **TMDB API 連携**  
  - トップページでカテゴリ別の一覧（上映中、アニメーション、監督・俳優別など）
  - 映画名による検索
  - 映画の詳細情報閲覧

### 📝 コミュニティ機能
- **JWTベースの認証**  
  - 会員登録・ログイン・ログアウト  
  - ログインが必要な操作時にモーダルで案内
- **レビュー**  
  - レビュー投稿・編集・削除（ログイン必須）  
  - レビューへの「いいね」（追加・取消し、カウント表示）
  - コメント投稿・閲覧（削除・編集未対応） 
- **レビューもっと見るページ**  
  - 映画詳細やマイページの「もっと見る」クリック時に別ページへ遷移
  - ソートオプション：最新順・いいね順・高評価順・低評価順

### 👤 マイページ
- **自分の活動を一覧表示**  
  - 自分のレビュー一覧
  - 自分がいいねしたレビュー数
  - 自分のコメント数  
- **自分のレビューもっと見るページ**  
  - 「自分のレビュー」から「もっと見る」をクリックして別ページへ遷移
  - ソートオプション：最新順・いいね順・高評価順・低評価順  
- **プロフィール画像変更**  
  - マイページで画像を選択 → S3にアップロード → プロフィール画像URLをDBに保存  
  - 即時に画面へ反映

### 🚀 トラフィック削減
- **映画情報のキャッシュ**  
  - 初回閲覧時にTMDB APIを呼び出し、DBに保存
  - 以降、同映画はDBから直接取得
- **Spring Batchによるバッチ処理**  
  - メタデータの定期収集・更新とキャッシュ更新
  - 毎日午前3時に自動実行し、最新情報維持・トラフィック分散

### 🔐 セキュリティ強化
- **Hashids**でURLに露出する数値IDを暗号化
  -  例: `/user/12` → `/user/NkK9`
  - 無作為な攻撃（Enumeration Attack）を防止  


<br><br>


## 📸 スクリーンショット

| **トップページ**  |  **映画検索結果ページ**      |
| :-----------------: | :----------------: |
| <img width="430" src="https://github.com/user-attachments/assets/507420b2-d385-4baf-8d2f-689df0a3cf42"/> | <img width="430" src="https://github.com/user-attachments/assets/6e9ffed3-6beb-479f-a2ec-65b42d9ca8ff"/>|
| **映画詳細ページ**      |  **レビュー詳細ページ**      |
| <img width="430" src="https://github.com/user-attachments/assets/12361dfd-c06c-48c5-a986-a6e1104b20f3"/> | <img width="430" src="https://github.com/user-attachments/assets/35d9f83b-45e5-4886-8787-e21e234188c1"/>|
| **レビュー作成モダール**      |  **レビュー一覧ページ**   |
| <img width="430" src="https://github.com/user-attachments/assets/7d62ed65-1f49-4eab-9c5c-f464e3f8e747"/> | <img width="430" src="https://github.com/user-attachments/assets/b6705d39-8e67-4ab1-8ffb-4197d072db63"/>|
| **会員登録 / ログイン**      |  **マイページ**   |
| <img width="430" src="https://github.com/user-attachments/assets/7df5b039-b516-4c8e-ae79-c18c583570b2"/> | <img width="430" src="https://github.com/user-attachments/assets/325c33d5-f295-41fc-b602-4ade9521a5fa"/>|


<br><br>


## 📁 ディレクトリ構造
  
```
cinephile-app/
├── frontend/
│   ├── public/               # 静的ファイル、HTMLテンプレート
│   ├── src/
│   │   ├── api/              # AxiosインスタンスとAPI呼び出し関数の定義
│   │   ├── components/       # 共通UIコンポーネント
│   │   │   ├── common/       # ボタン、カードなどの汎用パーツ
│   │   │   ├── layouts/      # ページ全体のレイアウト構成
│   │   │   └── modals/       # ログインなどのモーダルウィンドウ
│   │   ├── context/          # グローバル状態管理（認証、モーダルなど）
│   │   ├── pages/            # 各ルートに対応する主要ページコンポーネント
│   │   ├── styles/           # CSS / SCSS / スタイル関連ファイル
│   │   └── utils/            # 日付処理などの共通ユーティリティ関数
│   └── package.json          # フロントエンド依存関係および設定ファイル
│
└── backend/
    ├── src/
    │   ├── main/
    │   │   ├── java/…/
    │   │   │   ├── controller/    # REST APIのエンドポイント定義
    │   │   │   ├── dto/           # リクエスト・レスポンス用データ構造
    │   │   │   ├── entity/        # JPAエンティティクラス（DBマッピング）
    │   │   │   ├── repository/    # JPAリポジトリインターフェースとクエリ
    │   │   │   ├── service/       # ビジネスロジックの実装
    │   │   │   ├── config/        # Springの各種設定（CORS、Securityなど）
    │   │   │   ├── batch/         # Spring Batchの定期ジョブ設定
    │   │   │   ├── http/          # リクエストフィルター、レスポンスヘルパー等
    │   │   │   ├── jwt/           # JWTの発行・検証関連クラス
    │   │   │   ├── mapper/        # DTOとエンティティ間のマッピング処理
    │   │   │   ├── exception/     # 共通エラーハンドリング
    │   │   │   ├── util/          # 汎用ユーティリティ関数
    │   │   └── resources/         # application.ymlやBatch設定ファイル等│
    │   └── test/
    ├── build.gradle              # ビルド設定ファイル
    └── settings.gradle           # マルチモジュールなどのプロジェクト設定
```


<br><br>


## 📖 APIドキュメント

- **認証ヘッダー**
```
Authorization: Bearer <JWT Token>
```

### ユーザー認証
| Method |  パス                       | 説明           |
|:------:|:----------------------|:--------------:|
| GET    | `/api/user`        | 自分のプロフィール情報を取得 |
| POST   | `/api/user/signup`    | 会員登録       |
| POST   | `/api/user/login`     | ログイン → JWT Token発行  |
| POST   | `/api/user/logout`    | ログアウト       |

### 映画
| Method | パス                             | 説明                |
|:------:|:---------------------------------|:-------------------:|
| GET    | `/api/movies/{movieId}`          | 映画の詳細情報を取得 (キャッシュ利用)      |

### レビュー
| Method | パス                                             | 説明                   |
|:------:|:-------------------------------------------------|:----------------------:|
| GET    | `/api/review/movie/all/{movieId}&sort={key}`           | 特定の映画のレビュー一覧を取得 (ソート)  |
| GET    | `/api/review/movie/preview/{movieId}`           | 特定の映画の上位3件のレビューを取得  |
| GET    | `/api/review/{reviewId}`           | レビューの詳細情報を取得  |
| POST   | `/api/review/register`                                   | レビューを作成             |
| PUT    | `/api/review/update`                        | レビュー更新            |
| DELETE | `/api/review/delete`                        | レビュー削除             |
| POST   | `/api/review/{reviewId}/like`                   | レビューへの 「いいね」 追加または削除           |

### コメント
| Method | パス                                   | 説明           |
|:------:|:--------------------------------------:|:--------------:|
| GET   | `/api/reviews/{reviewId}/comments`     | コメント一覧を取得      |
| POST   | `/api/reviews/{reviewId}/comments`     | コメント作成      |

### マイページ
| Method | パス                                    | 説明                         |
|:------:|:----------------------------------------|:----------------------------:|
| GET    | `/api/review/user/all&sort={key}` | 自分が投稿したレビュー一覧を取得 (ソート)     |
| GET    | `/api/review/user/preview`           | 自分が投稿した上位3件のレビューを取得  |
| POST   | `/api/user/{userId}/profile-image`      | プロフィール画像をアップロード (S3)      |

<br>

> **💡 Note:**  
> - `userId`, `movieId`, `reviewId` は実際には`Hashids`を使用して難読化された文字列が使われます。
> - すべてのリクエスト本文において、メールアドレスやパスワードなどの機密情報は `Spring Validation（@Valid / @Validated）`で検証され、無効な場合は`HTTP 400 Bad Request`とともにエラーメッセージが返されます。
> - 認証が必要なエンドポイントには、必ず`Authorization`ヘッダーに有効なJWTを含める必要があります。


<br><br>


## 🗄️ データベースERD
![Image](https://github.com/user-attachments/assets/cf4dd571-d9a8-4f03-b283-82363a4eee2a)
> **💡 Note:**  
> - このダイアグラムは、`PostgreSQL`をベースに設計されたERDです。
> - movie_genre: (movie_id, genre) 複合PK
> - movie_original_country: (movie_id, country) 複合PK
> - movie_cast: (movie_id, name, character_name, profile_path) 複合PK
> - review_likes: (review_id, user_id) 複合PK


<br><br>


## 📚 学んだこと
  - Context APIを活用したグローバル状態管理とパフォーマンス最適化
  - Spring Batchを用いたキャッシング戦略
  - Hashidsによるセキュリティ強化
  - AWSでのデプロイ経験（S3 + Route53静的ホスティング、Elastic Beanstalkローリングアップデート）
  - JWT認証とAxiosインターセプターを利用したスムーズな認証UXの実現


<br><br>


## 🔄 今後の改善点
  - テストカバレッジの拡充
    - JUnit/Mockito、React Testing Libraryの導入
  - APIドキュメント自動化
    - Swagger/OpenAPIでドキュメント作成・バージョン管理の自動化
  - CI/CDパイプライン
    - GitHub ActionsやAWS CodePipelineでの自動化
  - 運用モニタリング強化
    - ELKスタックやCloudWatchでログ・メトリクス収集
  - 統一エラー処理構造
    - フロントエンドとバックエンドのエラーメッセージ統一、全体的なエラー処理ミドルウェア強化
