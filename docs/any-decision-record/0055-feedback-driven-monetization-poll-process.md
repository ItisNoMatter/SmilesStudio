# 0055: フィードバック実証型の運用要件を定める（課金パラメータの公開議論プロセス）

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[[0049]]で「フィードバック実証型」を#BuildInPublic運用の主軸の一つと定めたが、具体的な仕組みは未定義だった。ユーザーから、[[0036]]のB案（回数制フリーミアム）の枠内で未確定のまま残っているパラメータを、公開の場（X/Twitterのアンケート機能等）で問いかける形にする提案が出た。過去のShipaton参加者の事例（HackerNoon記事「Why I Ignored the Winning Option」）を調査したところ、投票の集計結果よりも返信欄のテキストによる理由付けの方が実質的な判断材料になっており、投票結果と異なる判断をしてその理由を発信すること自体が強いコンテンツになっていたことが分かった。一方、実装スケジュール（[Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17)は[Issue #16](https://github.com/ItisNoMatter/SmilesStudio/issues/16)完了後着手、Play申請目標09-20）は厳しく、公開フィードバックを待ちすぎて実装が遅れるリスクもある。

## Decision

- **対象パラメータ**: [[0036]]のB案内で未確定の3点（無料枠の月間回数、価格帯、アップセルのタイミング・見せ方）を対象とする。B案でいく方針自体（[[0036]]）は変更しない。
- **出し方**: 3パラメータをまとめず、個別に連続して投稿する（1問1論点）。
- **応答期間とフォールバック**: 1パラメータあたり24〜48時間を締切とし、反応が薄くても締切時点で[[0036]]の参考値（月額300〜500円等）をデフォルトとして採用し決定する。実装スケジュールを人質に取らない。
- **判断ロジック**: 投票の集計結果そのものではなく、返信欄の理由付きコメントを優先根拠とする。投票結果と食い違う判断をした場合は、その食い違いごと発信内容に含める。
- 決定後は都度AnyDRとして記録し、[Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17)のスコープに反映する。

## Alternatives

- 3パラメータを1本の複合投稿にまとめる: 投稿数・運用の手間は少なく済むが、Xのアンケート機能は1問しか同時に出せず論点がぼやけるため不採用。[[0049]]のログ発掘型と合わせた継続的な投稿ペース確保にも寄与しにくいと判断した。
- 反応が集まるまで無期限に待つ: 実装スケジュールを圧迫するリスクが高いため不採用。24〜48h+デフォルト値へのフォールバックを設けることにした。

## Consequences

- [Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17)の「進め方」に、このフィードバック公開プロセスを実装着手前のステップとして追記する。
- 実際に投稿・収集・決定を行う際は、都度その結果を個別のAnyDRとして記録する（本AnyDRはその運用ルールを定めたメタ決定であり、個々のパラメータ決定は別途記録される）。
- BuildInPublicツイートSkill（[[0039]]〜0044）は現状「既に確定したAnyDRの紹介ツイート」を想定したスコープであり、本AnyDRが想定する「問いかけ」投稿（アンケート＋文脈説明）は対象外。同Skillを流用するか、手動で投稿するかは別途判断が必要。

## Related

- [0036-plan-b-c-monetization-supersedes-byok-hybrid](./0036-plan-b-c-monetization-supersedes-byok-hybrid.md)
- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
- [0048-shipaton-award-strategy-next-gen-buildinpublic](./0048-shipaton-award-strategy-next-gen-buildinpublic.md)
- [0049-buildinpublic-operating-strategy](./0049-buildinpublic-operating-strategy.md)
