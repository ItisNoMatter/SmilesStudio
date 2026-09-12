# 0109: プライバシーポリシーへのリンクは既存の「このアプリについて」ダイアログに追加する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #30（アプリ内にプライバシーポリシー導線を追加）で、Google Playのユーザーデータポリシー
対応として、プライバシーポリシー（https://itisnomatter.github.io/SmilesStudio/privacy-policy/ 、
AnyDR 0088）へのリンクをアプリ内に掲示する必要があった。`SmilesStudioApp.kt`には既に
その他メニューから開く「このアプリについて」の`AlertDialog`が実装済みだった。

## Decision

プライバシーポリシーへのリンクは、既存の「このアプリについて」ダイアログ内に追加する。
新しい独立したメニュー項目は作らない。

## Alternatives

- 新しく「プライバシーポリシー」独立のメニュー項目を追加する: より見つけやすい利点はあるが、
  頻繁に使う項目ではなく、「このアプリについて」と役割が重複するため不採用とした。

## Consequences

- 新しいメニュー項目を増やさずに済み、UI変更が最小限で済む。
- Google Playのポリシー上は「アプリ内のどこかに存在すること」が要件であり、トップレベルに
  常時表示する必要はないため、Aboutダイアログ内という配置でも要件を満たす。

## Related

- [0088-privacy-policy-github-pages](./0088-privacy-policy-github-pages.md)
