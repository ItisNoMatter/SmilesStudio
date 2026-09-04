# 0061: Play Store申請Issueを初回申請/機能追加版申請に分割し依存関係を再配線する

- Date: 2026-09-04
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[AnyDR 0060](./0060-minimal-build-scope-rendering-pipeline-only.md)で最小ビルドのスコープ（レンダリングパイプライン＋モバイル調整のみ）を確定した後、段階的リリース（初回申請→機能追加版の再申請）をIssue構成上どう表現するかを`/grill-with-docs`セッションで検討した。

## Decision

既存の[Issue #18](https://github.com/ItisNoMatter/SmilesStudio/issues/18)「Google Play Console申請準備」を「初回（最小ビルド）申請」としてスコープを絞り、ブロッカーを[Issue #6](https://github.com/ItisNoMatter/SmilesStudio/issues/6)・[Issue #7](https://github.com/ItisNoMatter/SmilesStudio/issues/7)・[Issue #13](https://github.com/ItisNoMatter/SmilesStudio/issues/13)のみに変更する。新たに「機能追加版のアップデート申請」Issueを追加し、[Issue #14](https://github.com/ItisNoMatter/SmilesStudio/issues/14)・[Issue #15](https://github.com/ItisNoMatter/SmilesStudio/issues/15)・[Issue #16](https://github.com/ItisNoMatter/SmilesStudio/issues/16)・[Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17)でブロックする。

残りのIssueの依存関係も以下のように再配線する:
- [Issue #19](https://github.com/ItisNoMatter/SmilesStudio/issues/19)（Devpost提出物準備）: 新設「機能追加版申請」Issueと[Issue #22](https://github.com/ItisNoMatter/SmilesStudio/issues/22)（テストハーネス）にブロックされる形に変更（Shipaton参加要件のRevenueCat課金を満たす必要があるため、初回申請ではなく機能追加版申請に依存させる）
- Issue #22（テストハーネス）: Play申請系ではなく#19（Devpost提出物準備）のブロッカーとする（技術的にはストア提出のブロッカーではなく、AnyDR 0037の「三層防御」構想としてDevpostのREADME/デモでアピールする位置づけのため）
- [Issue #21](https://github.com/ItisNoMatter/SmilesStudio/issues/21)（iOS対応・年額プラン検討、低優先度）: #18（初回申請）ではなく新設「機能追加版申請」Issueにブロックされる形に変更
- [Issue #20](https://github.com/ItisNoMatter/SmilesStudio/issues/20)（AIレビューCI、低優先度）: 現状のまま#15・#17にブロックされる形を維持（変更不要）

## Alternatives

- #18のまま、本文のチェックリストで初回/最終提出を段階管理する（新規Issueを作らない）: Issue数が増えず管理がシンプルという利点があったが、Wayfinderの「1 Issue = 1タスク」の粒度から外れフロンティアクエリで進捗を機械的に判定しにくくなり、#18の「完了」の定義も曖昧になるため不採用。

## Consequences

- 新設Issueの実際の作成（`gh issue create`）と、既存Issue（#18〜#22）の依存関係の付け替え（`gh api .../dependencies/blocked_by`）は、グリリングセッション終了後にまとめて実施する。
- Issue番号が1つ増え、マップ（Issue #11）の子Issueリストが長くなる。

## Related

- [0060-minimal-build-scope-rendering-pipeline-only](./0060-minimal-build-scope-rendering-pipeline-only.md)
- [0037-three-layer-defense-oss-strategy](./0037-three-layer-defense-oss-strategy.md)
- [0022-wayfinder-issue-structure](./0022-wayfinder-issue-structure.md)
