# 0041: ツイート作成Skillは明示的呼び出しのみとし、プロアクティブ提案はしない

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[[0039]]・[[0040]]でBuildInPublicツイート作成Skillの配置場所・英語版AnyDRの格納場所を決めた後、Skillの呼び出し方（ユーザーが都度明示的に呼ぶか、`any-decision-record`のようにAnyDR記録直後にプロアクティブ提案するか）を検討した。

## Decision

ツイート作成Skillは明示的呼び出しのみとする。AnyDR記録直後などにプロアクティブに「ツイートしますか？」と提案する機能は持たせない。

## Alternatives

- プロアクティブ提案あり（AnyDR記録直後などに提案）: BuildInPublicAward獲得という目標に対してツイート機会を逃しにくいという利点があったが、実装が複雑になる（`any-decision-record`自体や`grill-with-docs`等の呼び出し元に誘導ロジックを追加する必要がある）ことと、「見せ場になる決定かどうか」の判断基準が曖昧で、地味な決定でも毎回聞かれると煩わしくなる懸念があったため不採用。

## Consequences

- ユーザーが実際にツイートしたい決定に気づいたタイミングで、AnyDR番号を指定してSkillを呼び出す運用になる。
- 実際の運用でツイート頻度が低いことが分かれば、プロアクティブ化を改めて検討する余地を残す。

## Related

- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0040-english-anydr-subdirectory](./0040-english-anydr-subdirectory.md)
