# 0137: SmilesStudioAppの状態管理・ロジックを軽量State Holderクラスに分離する

- Date: 2026-09-16
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context
`android-app`の`SmilesStudioApp.kt`が単一の`@Composable fun SmilesStudioApp()`に240行超のロジックを抱え込み、肥大化していた。具体的には以下が1つのComposable関数に同居していた。

- 状態変数10個以上（タブ選択、SMILESテキスト、APIキー、各種ダイアログ/シート表示フラグ、認識中フラグ、キャプチャURIなど）
- `ApiKeyStore`/`ImageRecognitionCoordinator`のDI配線（具体実装の組み立て）
- 画像認識のビジネスロジック（`runRecognition`: 画像読込 → 認識 → outcome 3分岐 → Snackbar/ペイウォール制御）
- Android `ActivityResultLauncher`の管理
- UIツリー構築（Scaffold/TopBar/BottomBar/AnimatedContent + ダイアログ4種）

特に`runRecognition`のoutcome分岐（Recognized/Failed/FreeTierExhausted）はComposable関数内のローカル関数に埋もれており、UIなしに単体テストできなかった。認識結果まわりの決定（[[0135-outcome-nullable-remaining-count]]など）は今後も増える見込みで、テスト不能な状態はリスクと判断した。

## Decision
状態とロジックを`@Stable`なプレーンKotlinクラス`SmilesStudioAppState`に抽出し、`rememberSmilesStudioAppState()`ファクトリComposableで生成する方式（案A）を採用した。`SmilesStudioApp`本体はUIツリー構築のみに専念させる。

- `SmilesStudioAppState`が状態（`selectedTab`/`smilesText`/各種表示フラグ/`isRecognizing`/`pendingCaptureUri`等）と`saveApiKey`/`deleteApiKey`/`runRecognition`を保持する。
- `ApiKeyStore`/`ImageRecognitionCoordinator`のDI配線は`rememberSmilesStudioAppState()`内に集約する。
- `runRecognition`はUri解決済みの`ByteArray?`を受け取るsuspend関数とし、Android依存（`Context`/`Uri`）を持たない形にした。これによりComposeランタイム（`mutableStateOf`）はJVM単体テストでも問題なく動作することを確認済み。
- Issue #40を先に作成し、TDD5ステップ（テスト作成→スケルトンでコンパイル→振る舞いのRED確認→GREEN実装→リファクタリング）で実施した。

## Alternatives
- **案B: `androidx.lifecycle.ViewModel`導入**: Android標準で構成変更時の状態保持が得られるが、`lifecycle-viewmodel-compose`等の新規依存とStateFlow化のボイラープレートが必要になり、Shipaton締切（2026-09-30）前の工数として見合わないと判断し却下。
- **案C: UIツリーのみComposable分割（状態はそのまま）**: 変更範囲が最小でリスクは低いが、`runRecognition`のテスト不能という一番の問題が解決せず、見た目の整理に留まるため却下。

## Consequences
- `runRecognition`のoutcome分岐がComposeに依存しないJVM単体テスト（`SmilesStudioAppStateTest.kt`）で検証可能になった。
- `SmilesStudioApp.kt`: 322行 → 239行、`SmilesStudioAppState.kt`: 105行（新設）。
- 新規依存は追加していない（ViewModel/DIフレームワーク不使用）。
- 画面回転などの構成変更時の状態保持は引き続き`remember`頼みの自前対応であり、この点は今回のリファクタリングでは解決していない（後退もしていない）。

## Related
- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
- [0135-outcome-nullable-remaining-count](./0135-outcome-nullable-remaining-count.md)
