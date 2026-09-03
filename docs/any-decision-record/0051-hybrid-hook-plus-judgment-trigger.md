# 0051: any-action-recordの自動発火はハードフックで検知しClaudeの判断で記録するハイブリッド方式とする

- Date: 2026-09-03
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0050]]で`any-action-record`（AnyAR）の主目的を確定した後、「作業がひと段落したタイミングで自動発火する」という要件をどう実現するかを検討した。既存の`any-decision-record`はプロンプトレベルのプロアクティブトリガー（Skillのdescriptionに書かれた基準をClaudeが自己判断で適用する方式）を採用しているが、これは`buildinpublic-tweet`（[[0041]]）で明示的呼び出しのみを選んだ判断とは逆方向の要望であり、あえて仕組みごと比較検討した。

## Decision

自動発火は、ハードなシステムフック（`.claude/settings.json`のPostToolUseフック等、`update-config`Skillで設定）で「節目の検知」だけを確実に行い、「実際にAnyARへ記録する価値があるか」の判断はClaudeに委ねるハイブリッド方式とする。フックは`git commit`実行後などのタイミングで、「これはAnyARの節目か検討せよ」という一言をClaudeに提示する形とし、記録するかどうかの最終判断・ユーザーへの提案はClaudeが行う。

## Alternatives

- プロンプトレベルのプロアクティブトリガーのみ（`any-decision-record`と同じ方式）: 実装がシンプルで文脈判断の柔軟性があるという利点があったが、長い会話が続く中でClaudeがトリガー基準を見落とす・忘れるリスクがあり、「絶対に発火する」保証がないため、「自動発火」という要件を十分に満たせないと判断し不採用。
- ハードなシステムフックで毎回強制的に記録: 確実に発火するという利点があったが、「本当に記録する価値がある節目か」の判断ができず、些細なコミット（誤字修正等）でも毎回発火しAnyARが乱発されるリスクがあるため不採用。

## Consequences

- 実装時、Claude Codeのハーネス自体が「自動的な振る舞いにはメモリではなくフックが必要」と明言している方針と整合する形で、`.claude/settings.json`にPostToolUseフック（`git commit`実行後をトリガー候補とする）を設定する必要がある。
- グローバルSkillとして配布する`any-action-record`本体（Skill定義）と、プロジェクトごとに個別設定が必要なフック設定は、別々に配布・設定する形になる。
- フックが発火してもAnyARが実際に作成されるとは限らない（Claudeの判断次第）。この「判断の見送り」リスクは残るが、`any-decision-record`と同じ運用範囲であり許容する。

## Related

- [0041-explicit-invocation-only-tweet-skill](./0041-explicit-invocation-only-tweet-skill.md)
- [0050-any-action-record-purpose](./0050-any-action-record-purpose.md)
