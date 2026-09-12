# 0110: プライバシーポリシーのリンクは外部ブラウザに委譲して開く

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0109]]で追加を決めたプライバシーポリシーへのリンクを、タップ時にどう開くか検討した。

## Decision

`Intent(ACTION_VIEW, uri)`で外部ブラウザに委譲して開く。Custom Tabs（`androidx.browser`）は
採用しない。

## Alternatives

- Custom Tabs（`androidx.browser`のChrome Custom Tabs）でアプリ内に留まる形で開く:
  ブラウザに完全に切り替わらない利点はあるが、静的なページを一度確認するだけの用途に対して
  新規依存（`androidx.browser`）を追加してまでアプリ内に留める価値は薄いと判断し不採用とした。

## Consequences

- 新規依存・UIの実装が不要で、実装が最小限で済む。
- ユーザーは使い慣れたブラウザの戻る/共有/ブックマーク機能をそのまま使える。

## Related

- [0109-privacy-policy-link-in-about-dialog](./0109-privacy-policy-link-in-about-dialog.md)
