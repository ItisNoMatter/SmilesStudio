# 0111: Issue #30でプライバシーポリシー本文も実態に合わせて更新する

- Date: 2026-09-13
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

Issue #30（アプリ内にプライバシーポリシー導線を追加）の設計中、現在の
`docs/privacy-policy/index.html`本文が「本アプリは、現時点でユーザーの個人情報を収集、保存、
または外部に送信しません」「（Vision LLM機能は）実際に組み込まれる際にはポリシーを改定する」
と書かれたままであることが判明した。Issue #15でそのVision LLM機能（手描き画像をGemini APIへ
送信）が既に実装・出荷済みのため、この本文は実態と異なっている。

## Decision

Issue #30のスコープに、プライバシーポリシー本文の実態への更新も含める。アプリ内リンク追加と
本文更新を同じタイミングでまとめて対応する。

## Alternatives

- 今回はリンク追加のみとし、本文更新は別Issueに切り出す: Issue #30の文面上のスコープには忠実
  だが、リンク先の内容が「まだ送信しません」のまま実態と食い違うことになり、Google Playの
  ユーザーデータポリシー対応という本来の目的（正確な開示）を果たせないため不採用とした。

## Consequences

- Issue #30の作業範囲が、当初の文面（リンク追加のみ）より広がる。
- プライバシーポリシー本文の更新内容（Vision LLMへの画像送信について何をどう明記するか）を
  別途詰める必要がある。

## Related

- [0109-privacy-policy-link-in-about-dialog](./0109-privacy-policy-link-in-about-dialog.md)
- [0088-privacy-policy-github-pages](./0088-privacy-policy-github-pages.md)
