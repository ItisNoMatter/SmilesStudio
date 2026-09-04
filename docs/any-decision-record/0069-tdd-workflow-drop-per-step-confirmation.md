# 0069: TDDワークフローの各ステップ確認を廃止し、最後にサマリ報告のみとする

- Date: 2026-09-04
- Status: Accepted
- Category: process
- Deciders: the user

## Context

[AnyDR 0066](./0066-five-step-tdd-workflow-with-per-step-confirmation.md)で、TDDワークフローを5ステップに分割し各ステップ完了ごとに状況報告・確認を挟む運用を導入した。Issue #7の実装で実際にこの運用を1サイクル回した後、ユーザーから「随時確認は不要、いちばん最後にサマリだけ教えてほしい」という明示的な指示があった。

## Decision

TDDワークフローの5ステップ構成（テストコード作成→スケルトン実装→振る舞いのRED確認→GREEN実装→リファクタリング）自体は維持するが、各ステップごとの確認は廃止する。5ステップを通しで実行し、完了後にまとめてサマリを報告する運用に変更する。

## Alternatives

本文中に明示的な代替案の比較記述はなし。ユーザーが直接の方針として指定し、そのまま反映した。[AnyDR 0066](./0066-five-step-tdd-workflow-with-per-step-confirmation.md)を実際に1サイクル運用した上での見直しであり、5ステップという構造自体は有用と判断されたため、確認の頻度のみを変更する形にした。

## Consequences

- CLAUDE.mdの「開発ワークフロー（TDDサイクルの徹底）」から「各ステップの完了後、次のステップに進む前に必ず状況を報告し、確認を待ってください」という一文を削除し、「各ステップごとの確認は不要。5ステップを通しで実行し、完了後にまとめてサマリを報告してください」に置き換えた。
- `./gradlew allTests`によるグリーン確認・失敗時の自律的な原因分析と修正という既存の運用ルールは変更なく維持する。
- 実際に運用してみて再度往復コストが気になる、あるいは細かい確認が必要と判断された場合は、AnyDR 0066の形に戻すか、また別の粒度を検討する余地がある。

## Related

- [0066-five-step-tdd-workflow-with-per-step-confirmation](./0066-five-step-tdd-workflow-with-per-step-confirmation.md)
