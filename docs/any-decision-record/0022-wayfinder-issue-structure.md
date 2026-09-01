# 0022: v1ロードマップのIssue構成はWayfinder方式を採用する

- Date: 2026-09-01
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[[0017]]〜[[0021]]により、v1（最小構成でユーザーにリリースできるところまで）のコア体験・文法スコープ・レイアウト・描画方針・配布形態が確定した。これらをGitHub Issueに落とし込む段階で、Issueの構成方法（フラットなリストか、依存関係を追跡できる構造か）を決める必要があった。`docs/agents/issue-tracker.md`には、マップIssue1件の下に子Issue群を紐づけるWayfinder方式の運用手順が既に定義されている。

## Decision

v1ロードマップのIssue構成はWayfinder方式を採用する。マップIssue（`wayfinder:map`ラベル）を1件作成し、その下に子Issue（`wayfinder:task`等のラベル）を紐づけ、GitHub native issue dependencies（`blocked_by`）で依存関係を表現する。

## Alternatives

- フラットなIssueリスト: タスクごとに独立したIssueを作成し、ラベルや本文中のリンクで関連付ける方式。シンプルで追加の運用ルールが不要という利点があったが、「Ring検出はレイアウトの前提」のような依存関係を追跡する仕組みがなく、依存関係が複数存在する今回のロードマップには不向きと判断し不採用。

## Consequences

- マップIssue自体の保守（Notes/Decisions-so-far/Fogの更新）という追加の運用コストが発生する。
- 依存関係（`blocked_by`）とフロンティアクエリにより、「次に着手すべきIssue」を機械的に特定できる。
- 子Issue作成時は`docs/agents/issue-tracker.md`記載の手順（`gh api`によるsub-issue登録、依存関係の追加）に従う。

## Related

- [0017-v1-text-input-readonly-rendering-scope](./0017-v1-text-input-readonly-rendering-scope.md)
- [0021-windows-native-installer-distribution](./0021-windows-native-installer-distribution.md)
