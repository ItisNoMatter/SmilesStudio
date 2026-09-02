# 0043: ツイートのハッシュタグは`#Shipaton #BuildInPublic`、トーンは開発の過程・学びを共有する方向性とする

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[[0039]]〜[[0042]]でBuildInPublicツイート作成Skillの配置・英語版AnyDRの格納場所・呼び出し方・ツイート言語を決めた後、実際に使うハッシュタグ・メンション・トーンを検討した。Shipaton 2026の公式ルール（[Devpost Official Rules](https://revenuecat-shipaton-2026.devpost.com/rules)）を調査したところ、以下の事実が判明した。

- 必須ハッシュタグ: `#Shipaton`（"Posts should be tagged #Shipaton"）
- 任意ハッシュタグ: `#BuildInPublic`（"and may also be tagged #BuildInPublic"）
- RevenueCat公式アカウントへのメンション義務はなし
- 審査基準では「フォロワー数は関係ない（audience size does not matter）」「開発の旅路をどれだけ独創的に共有したか」「コミュニティからのフィードバックをどう取り入れたか」が重視され、期間を通じて継続的に投稿することが推奨されている

## Decision

ツイートのハッシュタグは`#Shipaton #BuildInPublic`を使用する。RevenueCat公式アカウントへのメンションは行わない（ルール上義務がないため）。トーンは、単なる完了報告ではなく「なぜその決定に至ったか」「何を学んだか」を一言添える方向性とする（審査基準がストーリー性・学びの共有を重視しているため）。

## Alternatives

本文中に代替案の比較記述はなし。ハッシュタグは公式ルールに基づく事実確認であり、選択の余地があったのはトーンの方向性程度だった。

## Consequences

- BuildInPublicツイート作成Skill（[[0039]]）の実装時、この規約をSmileStudio側のCLAUDE.mdに記載し、Skillはそこから動的に読み込む（[[0039]]の設計方針通り）。
- 技術系の追加ハッシュタグ（`#KotlinMultiplatform`等）を個別のツイートで足すかどうかはユーザーの裁量に委ねる。

## Related

- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0042-english-only-tweets](./0042-english-only-tweets.md)
