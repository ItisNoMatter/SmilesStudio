# 0070: AnyARが5件溜まるごとにハード検知＋提案でbuildinpublic-tweetの呼び出しを促す

- Date: 2026-09-05
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

ユーザーから「AARが5の倍数溜まるたびに、直近5本のうちからツイートする価値のあるAARを探してツイートするスキルを呼び出すようにしたい」という依頼があった。この機能は、`buildinpublic-tweet`Skillが「明示呼び出し専用、他のSkillのフローから自己発火してはいけない」と定めた[AnyDR 0041](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0041-explicit-invocation-only-tweet-skill.md)と直接関わるため、実装前にCLAUDE.mdの複数案提示ルールに従い選択肢を提示した。

## Decision

`any-action-record`Skillがレコード作成後、AnyAR総数が5の倍数になったことを検知したら、直近5件のReflectionsからツイート候補を選び、ユーザーに提案する。ユーザーが確認したら`buildinpublic-tweet`をSkillとして呼び出す。AnyARの既存の自動発火（[AnyDR 0051](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0051-hybrid-hook-plus-judgment-trigger.md)、`git commit`フック）と同じ「ハード検知＋Claude/ユーザーの判断」ハイブリッド方式を踏襲する。

## Alternatives

- 5件ごとに完全自動で`buildinpublic-tweet`を呼び出す（確認なし）: 手間がゼロになるという利点があったが、AnyDR 0041で明示的に決めた「他のSkillのフローから自己発火してはいけない」という設計原則を直接破ることになり、ツイートする価値があるかどうかの判断にユーザーが介在せず質の低い自動生成ツイートが量産されるリスクがあるため不採用。

## Consequences

- `any-action-record`Skill（グローバル、`~/.claude/skills/any-action-record/SKILL.md`）の「Create the record」フローに、レコード作成後の総数チェックとユーザーへの提案ステップを追加する必要がある。
- `buildinpublic-tweet`側の実装・AnyDR 0041の「明示呼び出し専用」という原則は変更しない。あくまで「呼び出しを提案するタイミング」を新設するだけで、最終的な起動判断は引き続きユーザーが行う。
- AnyARの総数が5, 10, 15, 20...に達するたびにこの提案が発生する。既に22件溜まっている現時点では次の発火は25件目。

## Related

- [0041-explicit-invocation-only-tweet-skill](./0041-explicit-invocation-only-tweet-skill.md)
- [0050-any-action-record-purpose](./0050-any-action-record-purpose.md)
- [0051-hybrid-hook-plus-judgment-trigger](./0051-hybrid-hook-plus-judgment-trigger.md)
