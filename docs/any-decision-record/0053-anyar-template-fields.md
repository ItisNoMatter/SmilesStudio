# 0053: AnyARテンプレートはObjective/Action/Result/Reflectionsを中核とする構成にする

- Date: 2026-09-03
- Status: Accepted
- Category: naming
- Deciders: the user, Claude Code

## Context

[[0050]]〜[[0052]]で`any-action-record`（AnyAR）の主目的・自動発火方式・採番方式を確定した後、AnyDRのフォーマット（Title/Date/Status/Category/Deciders/Context/Decision/Alternatives/Consequences/Related）に対応する具体的なテンプレート構成を検討した。

## Decision

AnyARテンプレートは以下の構成とする。

```markdown
# <NNNN>: <Title>

- Date: <YYYY-MM-DD>
- Related AnyDR: <このActionのきっかけになったAnyDRがあれば番号でリンク（任意）>
- Related Issue: <関連するGitHub Issue番号（任意）>

## Objective
<何を目指していたか>

## Action
<実際に何をしたか>

## Result
<結果どうなったか（テスト結果、成果物、数値等）>

## Reflections
<学び・気づき・次に活かしたいこと>
```

AnyDR固有の概念（`Status`＝決定の状態、`Category`＝決定の分類、`Deciders`＝決定者）はAction記録には不要と判断し外す。代わりに`Reflections`フィールドを追加し、[[0050]]で確定した主目的（BuildInPublic向けの「学びの共有」）に直結させる。

## Alternatives

本文中に明示的な代替案の比較記述はなし。提案したテンプレートがそのまま承認された。

## Consequences

- `any-action-record`Skill実装時、このテンプレートに沿ってファイルを生成する。
- `Reflections`フィールドが、`buildinpublic-tweet`Skill（[[0039]]）がツイート文を作成する際の「なぜ・何を学んだか」の材料として直接使える形になる。

## Related

- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0050-any-action-record-purpose](./0050-any-action-record-purpose.md)
- [0052-anyar-independent-numbering](./0052-anyar-independent-numbering.md)
