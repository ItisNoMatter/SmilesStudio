# 0023: 配布物の作成はGitHub Actionsによる自動ビルド・自動リリースを採用する

- Date: 2026-09-01
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

[[0021]]で配布形態（jpackageベースのネイティブインストーラをGitHub Releasesで配布、まずWindowsのみ）を確定した後、その配布物をどう作成するか（手動ビルド・手動アップロードか、CIによる自動化か）を決める必要があった。

## Decision

配布物の作成はGitHub Actionsによる自動ビルド・自動リリースを採用する。タグpush等をトリガーに、CI上でパッケージング（`packageDistributionForCurrentOS`相当）からGitHub Releasesへのアップロードまでを自動化する。

## Alternatives

- 手動ビルド・手動アップロード: 開発者がローカルでパッケージングタスクを実行し、生成物を手動でGitHub Releasesにアップロードする方式。実装コストゼロで最速で初回リリースにたどり着ける利点があったが、リリースのたびに手作業が発生しヒューマンエラーが起きやすく再現性が低いため、ユーザーはCIによる自動化（アプローチB）を選択し不採用。

## Consequences

- GitHub ActionsのワークフローをWindowsランナー向けに新規設計する実装作業が必要になる。これは[[0022]]のWayfinder方式Issue構成において、配布Issueの子タスクとして扱う。
- リリースの再現性・信頼性が高まり、今後のリリースサイクルでも一貫した手順で配布できる。
- v1の実装スコープ（[[0017]]〜[[0021]]で確定した機能面）に加えて、CI/CDの構築という追加の作業項目がv1ロードマップに含まれることになる。

## Related

- [0021-windows-native-installer-distribution](./0021-windows-native-installer-distribution.md)
- [0022-wayfinder-issue-structure](./0022-wayfinder-issue-structure.md)
