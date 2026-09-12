# 0108: Gemini APIへの送信前に画像をリサイズ・圧縮する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

カメラで撮影した画像（数MB程度になりうる）をGemini APIに送信する前にリサイズ・圧縮するか
どうかを検討した。

## Decision

送信前に長辺1024px程度にダウンスケールし、JPEG品質80%程度で再エンコードする。

## Alternatives

- 元画像をそのまま送信する: 実装が最もシンプルだが、数MB〜十数MBの画像をそのまま送ると
  通信時間の増加・モバイル回線でのデータ消費増加につながり、Gemini APIのリクエストサイズ
  制限に引っかかる可能性もあるため不採用とした。

## Consequences

- `Bitmap`のデコード・リサイズ・再エンコードの実装が必要になる。
- 送信時間・データ消費量が削減され、APIのリクエストサイズ制限に引っかかるリスクも下がる。

## Related

- [0102-image-input-no-permission-apis](./0102-image-input-no-permission-apis.md)
