# 0049: BuildInPublic運用戦略をログ発掘型+フィードバック実証型に定める

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

#BuildInPublic Awardを狙う方針（[[0048]]）を受け、開発着手（8月上旬）からBuildInPublic投稿基盤の整備（9月2〜3日）まで約1ヶ月の投稿ギャップがあり、競合も多いと想定される中でどう戦うかが課題になった。RevenueCat公式発表と過去受賞者（Rudrank Riyam氏）の事例を調査した結果、審査基準がtransparency & storytelling・engagement・learning & iterationの3軸であること、過去の受賞者は毎日投稿の継続・クロスプラットフォーム発信・競合とも助け合う互恵的なコミュニティ関与を実践していたことが分かった。

## Decision

ログ発掘型（既存45件超のAnyDRバックログを遡及的に投稿し、9月頭スタートという出遅れを取り返す）とフィードバック実証型（公開の場で設計判断を問いかけ、実際の反応を意思決定に反映しその過程を発信する）を運用の主軸とする。エンゲージメント優先型（他のShipaton参加者との相互フォロー・返信などの互恵的関与）は理想ではあるが実装時間との奪い合いになるため優先度を下げ、「余裕があれば」の扱いとする。

## Alternatives

- エンゲージメント優先型を主軸にする: 過去受賞者の実証済みの手法で審査基準の"engagement"に直接効くが、残り期間ずっと継続的な時間投入が必要で、実装（#14「Koog SDK導入」・#15「手描き構造式認識UI」など未着手のコア機能）との時間の奪い合いになるため主軸からは外した。

## Consequences

- ログ発掘型を実行するには、既存AnyDR（0001〜0047）から投稿価値のあるものを選別し英訳する作業が発生する（[[0039]]〜0044のBuildInPublicツイートSkillを継続利用）。
- フィードバック実証型を機能させるには、単なる進捗報告で終わらせず、意図的に公開の場で設計判断を問いかける投稿を意識的に作る必要がある。
- エンゲージメント優先型を切り捨てないため、時間に余裕がある時は他の参加者への返信も行う。

## Related

- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0041-explicit-invocation-only-tweet-skill](./0041-explicit-invocation-only-tweet-skill.md)
- [0042-english-only-tweets](./0042-english-only-tweets.md)
- [0043-tweet-hashtags-and-tone](./0043-tweet-hashtags-and-tone.md)
- [0048-shipaton-award-strategy-next-gen-buildinpublic](./0048-shipaton-award-strategy-next-gen-buildinpublic.md)
