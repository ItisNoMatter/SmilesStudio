# 0054: AnyARの自動発火フックは`git commit`実行後毎回とする

- Date: 2026-09-03
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

[[0051]]でAnyARの自動発火をハードフック（検知）＋Claudeの判断（記録するか）のハイブリッド方式とすることを決めた後、フックの具体的な発火条件を検討した。

## Decision

フックの発火条件は`git commit`実行後毎回とする。`.claude/settings.json`のPostToolUseフックで、Bashツールが`git commit`を実行した後に「これはAnyARの節目か検討せよ」という一言をClaudeに提示する形で実装する。

## Alternatives

- GitHub Issueクローズ（`gh issue close`）後毎回: タスクの完了をより直接的に検知できるという利点があったが、今回のSkill設計作業自体のようにGitHub Issueを使わない作業では発火しないため不採用。

## Consequences

- このセッションの実際の進め方（タスク単位で1コミット、コミット前にユーザーに確認）と自然に対応する発火頻度になる。
- `git commit`は`gh issue close`より高頻度で発生しうるため、Claudeの「記録するかどうかの判断」（[[0051]]）が実際に機能するかどうかがAnyARの記録品質を左右する。

## Related

- [0050-any-action-record-purpose](./0050-any-action-record-purpose.md)
- [0051-hybrid-hook-plus-judgment-trigger](./0051-hybrid-hook-plus-judgment-trigger.md)
