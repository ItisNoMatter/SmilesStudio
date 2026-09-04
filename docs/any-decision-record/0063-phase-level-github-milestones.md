# 0063: GitHubマイルストーンはフェーズ単位（3〜4個）で切る

- Date: 2026-09-04
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

[AnyDR 0059](./0059-phased-release-strategy-store-review-first.md)〜[AnyDR 0062](./0062-single-production-submission-parallel-closed-testing.md)でリリース戦略・Issue構成の再設計が確定した後、ユーザーから要望のあった「何日までにどのIssueが終わっているべきか」というマイルストーン設置の粒度を`/grill-with-docs`セッションで検討した。

## Decision

GitHubマイルストーンは、複数Issueをまとめた節目単位（3〜4個程度）で作成する。「Phase 1: 最小ビルド完成＋非公開テスト開始」「Phase 2: 機能実装完了」「Phase 3: 本番申請＋Devpost提出」のように区切り、それぞれに期日を設定する。各マイルストーンの説明文に、含まれるIssueごとの目安期日も書き添える。

## Alternatives

- Issue単位でほぼ1対1のマイルストーンを作る（22個近い個別マイルストーン）: 各Issueの期日がマイルストーンという第一級のフィールドとして明確に見えるという利点があったが、GitHubのマイルストーンは本来「期日を持つIssueのグルーピング単位」として設計されており1 Issue = 1 Milestoneという使い方は道具の設計意図から外れること、22個近いマイルストーンの作成・管理コストが「最小構成」の一貫方針に反することから不採用。

## Consequences

- 個々のIssueの期日はGitHub上の第一級フィールドとしては持たず、マイルストーンの説明文に記載する運用になる。
- 具体的なフェーズ区切りと期日は、このAnyDRに続くグリリングの論点として別途確定する。

## Related

- [0022-wayfinder-issue-structure](./0022-wayfinder-issue-structure.md)
- [0062-single-production-submission-parallel-closed-testing](./0062-single-production-submission-parallel-closed-testing.md)
