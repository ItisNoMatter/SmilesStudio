# 0120: 残り回数は認識成功時にSnackbarで表示する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

AnyDR 0115で「認識機能を使うたびに残り回数を表示する」と決めたが、具体的な表示方法は
未定だった。

## Decision

認識成功時に、既存のSnackbar（AnyDR 0107でエラー表示用に導入済みの`SnackbarHostState`）を
流用して「あと3回無料でご利用いただけます」のように表示する。BYOK利用中・購読中はこの
表示自体を出さない。

## Alternatives

- ホーム画面に常時「残り○回」の小さなバッジ/テキストを表示する: いつでも残り回数を確認
  できる利点はあるが、新しいUI要素・状態管理の実装が必要になり、BYOK/購読中の表示・非表示
  分岐も増えるため、実装コストが見合わないと判断し不採用とした。

## Consequences

- 新しいUIコンポーネントは不要で、既存の`SnackbarHostState`をそのまま使える。
- 数秒で消えるため常時確認はできないが、「急に制限されて驚く」というダークパターン的な
  体験は避けられる。

## Related

- [0115-show-remaining-count-transparently](./0115-show-remaining-count-transparently.md)
- [0107-recognition-error-snackbar](./0107-recognition-error-snackbar.md)
