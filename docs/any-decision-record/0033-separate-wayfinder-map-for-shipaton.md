# 0033: Shipaton 2026対応は既存マップとは別の新しいWayfinderマップIssueとして管理する

- Date: 2026-09-02
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[[0027]]〜[[0032]]でShipaton 2026対応の方針をAnyDRとして記録した後、これらをGitHub Issueに落とし込む段階で、既存のWayfinderマップ（[Issue #1](https://github.com/ItisNoMatter/SmilesStudio/issues/1)「SmilesStudio v1: 最小構成でのユーザーリリース」）に子Issueとして追加するか、新しい別のマップIssueを作成するかを決める必要があった。Issue構成そのものにWayfinder方式（マップ+子Issue群）を使うこと自体は[[0022]]で既に決定済みで、本AnyDRはその適用範囲（マップを1つにするか2つにするか）についての決定である。

## Decision

Shipaton 2026対応は、既存のマップ（Issue #1）とは別の新しいマップIssueとして管理する。共有する`core-smiles`/`ui-compose`部分に依存関係がある場合は、2つのマップ間でIssueをGitHub native issue dependencies（`blocked_by`）でリンクする。

## Alternatives

- 既存マップ（Issue #1）に子Issueとして追加する: 単一のマップで全体像を追える利点があったが、Issue #1のタイトル・Notes・Fogはデスクトップ限定の文脈で書かれておりShipaton関連の内容を押し込むと趣旨がぼやけること、マップの粒度が大きくなりすぎフロンティアクエリ（次に着手すべきIssueの機械的特定）がデスクトップ系とモバイル系のタスクで混在し分かりにくくなることから不採用。

## Consequences

- 締切（Shipaton: 2026-09-30、Play Store申請目標: 2026-09-20）・優先順位・成果物（ストア申請、Devpost提出）をShipaton専用マップで独立して追跡できる。
- 2つのマップを並行して見る必要がある。またがる依存関係がある場合は都度`blocked_by`で明示する。
- 既存マップ（Issue #1）の「Fog」セクション（Koog連携・グラフィカルエディタを将来事項として記載）は、Shipatonマップ作成と合わせて更新し、Koogが別マップで近日実装対象であることを注記する。

## Related

- [0022-wayfinder-issue-structure](./0022-wayfinder-issue-structure.md)
- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
