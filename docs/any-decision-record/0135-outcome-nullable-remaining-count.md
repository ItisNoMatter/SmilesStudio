# 0135: ImageRecognitionOutcome.Recognizedにnullableな残り回数フィールドを追加する

- Date: 2026-09-14
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #36で、`ImageRecognitionOutcome`にBYOK/購読中パスと無料枠パスの成功結果、および
無料枠使い切り（ペイウォール表示トリガー）をどう表現するかを検討した。

## Decision

`Recognized`に`remainingFreeCount: Int? = null`を追加する（BYOKパス・購読中パスでは
null、無料枠パスでは残り回数）。パスごとに別のsealed classケースを用意することはしない。
使い切り時は独立した`FreeTierExhausted`ケースを新設する。既存の`MissingApiKey`ケースは
削除する（BYOKキーがない状態はIssue #36のスコープ上、クラウド経路を使う正常系になるため）。

## Alternatives

- BYOK/購読中パス用の`Recognized(smiles)`と無料枠パス用の`RecognizedFreeTier(smiles,
  remainingFreeCount: Int)`を別ケースとして用意する: 型として2つのパスを明確に区別できる
  利点はあるが、呼び出し側（`SmilesStudioApp.kt`）のUI挙動はどちらのパスでも完全に同一
  （テキスト反映のみ、Snackbar表示の有無だけが違う）であり、区別する実益がない。CLAUDE.mdの
  「将来の仮定のための設計をしない」方針に反する過剰な型分割と判断し不採用とした。

## Consequences

- `MissingApiKey`ケースの削除に伴い、既存のテスト（`getApiKey() == null`で
  `MissingApiKey`を返すケース）を書き換える必要がある。
- 呼び出し側は`Recognized`の`remainingFreeCount`が非nullの時だけSnackbarで残り回数を
  表示する、という1分岐で済む。

## Related

- [0120-remaining-count-via-snackbar](./0120-remaining-count-via-snackbar.md)
- [0133-remaining-count-via-recognize-response](./0133-remaining-count-via-recognize-response.md)
- [0134-reactive-free-tier-exhaustion-detection](./0134-reactive-free-tier-exhaustion-detection.md)
