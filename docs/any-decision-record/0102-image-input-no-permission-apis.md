# 0102: 画像入力はカメラアプリへの委譲＋標準フォトピッカーで、ランタイム権限を要求しない

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #15（手描き構造式認識UI）のスコープ「画像入力（カメラ撮影/ギャラリー選択）のUI」を
どう実装するか検討した。`android-app`の`AndroidManifest.xml`には現状カメラ・ストレージ関連の
権限は一切宣言されておらず、`minSdk`は26、`compileSdk`/`targetSdk`は37だった。

## Decision

カメラ撮影は`ActivityResultContracts.TakePicture()` + `FileProvider`でシステムのカメラアプリに
撮影を委譲する。既存の画像ファイル選択は標準の写真ピッカー
（`ActivityResultContracts.PickVisualMedia`）を使う。どちらもアプリ側でランタイム権限
（`CAMERA`・`READ_MEDIA_IMAGES`等）を要求しない。

## Alternatives

- `CAMERA`権限を要求し、CameraX等でアプリ内蔵のカメラUIを実装する: アプリ内に統一感のある
  カメラUIを作れる利点はあるが、ランタイム権限のリクエスト・拒否時のフォールバックUXの実装、
  CameraX等の新規依存追加が必要になり、Issue #15のMVPスコープ（AnyDR 0028: 画像1枚を入力
  できればよい）に対して実装コストが見合わないため不採用とした。

## Consequences

- ランタイム権限のリクエスト・拒否時のUXを実装する必要がなくなる。
- カメラ撮影用に`FileProvider`の宣言（`file_paths.xml`リソース含む）という定型的な設定が
  `AndroidManifest.xml`に必要になる。
- アプリ内に独自のカメラプレビューUIは持てず、システムカメラアプリの画面がそのまま使われる。

## Related

- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
- [0028-handdrawn-structure-recognition-mvp](./0028-handdrawn-structure-recognition-mvp.md)
