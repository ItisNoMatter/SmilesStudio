# 0103: 画像入力は単一ボタン＋ボトムシートでカメラ/ギャラリーを選択させる

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0102]]でカメラ撮影・ギャラリー選択の2つの画像入力方法を実装すると決めたが、この2つを
ユーザーにどう提示するか（単一の入口から選ばせるか、最初から別々のUI要素として出すか）は
未定だった。

## Decision

ホーム画面に「画像から認識」ボタンを1つ用意し、タップ時に`ModalBottomSheet`で「カメラで
撮影」「ギャラリーから選択」の選択肢を出す。

## Alternatives

- 「カメラで撮影」「ギャラリーから選択」を最初から2つの別ボタン/アイコンとして常時表示する:
  選択肢UIを経由しない分タップ回数は減るが、ホーム画面のUI要素が2つ増え、Issue #25で確定済み
  のMaterial 3 Expressiveデザインのレイアウトへの影響が大きいため不採用とした。

## Consequences

- ホーム画面に追加するUI要素は1つで済み、既存レイアウトへの影響が小さい。
- `ModalBottomSheet`の実装が必要になる（Compose Material3に標準で用意されている）。

## Related

- [0102-image-input-no-permission-apis](./0102-image-input-no-permission-apis.md)
