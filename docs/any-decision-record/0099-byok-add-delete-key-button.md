# 0099: BYOK設定ダイアログに「削除」ボタンを追加する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0098]]で保存時に空文字を許可しないと決めたため、上書き保存だけでは一度設定したAPIキーを
完全に削除する手段がない状態だった。削除手段を設けるかどうかを検討した。

## Decision

BYOK設定ダイアログに明示的な「削除」ボタンを追加する。

## Alternatives

- 削除機能を用意しない（スコープ外とし、必要になれば別途対応）: 実装が最小限で済み、キーの
  上書き保存で大半のユースケースはカバーできるという理由で当初推奨したが、ユーザーが
  「B（削除ボタンを追加する）」を選択したため不採用とした。

## Consequences

- ダイアログのボタン配置・誤削除防止のための確認フローを設計する必要がある。

## Related

- [0098-byok-validation-format-only](./0098-byok-validation-format-only.md)
- [0097-byok-settings-as-alert-dialog](./0097-byok-settings-as-alert-dialog.md)
