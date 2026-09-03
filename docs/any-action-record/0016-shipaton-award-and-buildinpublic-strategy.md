# 0016: Shipaton受賞戦略・BuildInPublic運用戦略を確定しbuildinpublic-poll Skillを新設

- Date: 2026-09-03
- Related AnyDR: 0048, 0049, 0055, 0056

## Objective

Shipaton 2026の21ある受賞カテゴリのうちどれを狙うか、また開発着手からBuildInPublic投稿基盤
整備までの約1ヶ月のギャップをどう取り返すかという運用戦略を確定する。

## Action

公式ルール・過去受賞者の事例を調査した結果に基づき、受賞戦略をNext Gen Award（学生向け、
ストア公開不要）＋#BuildInPublic Awardの二枚看板に絞り、iOS対応を要件とするKotlin賞・
トラクション重視のグランプリは狙わない方針を確定した（0048）。BuildInPublic運用は既存AnyDR
バックログの遡及投稿（ログ発掘型）と、公開の場で設計判断を問いかけ実際の反応を意思決定に
反映するフィードバック実証型を主軸とする方針を確定した（0049）。フィードバック実証型の具体的
な運用ルール（対象パラメータ、投稿の出し方、24〜48時間の応答期間とデフォルト値へのフォール
バック、投票結果より返信の理由付けを優先する判断ロジック）を確定した（0055）。この新しい
投稿種別（未確定の問いかけ投稿）は既存のbuildinpublic-tweet Skillのスコープ外だったため、
独立した兄弟Skill`buildinpublic-poll`を新設した（0056）。

## Result

4件のAnyDR（0048, 0049, 0055, 0056）が確定し`8596285`でコミットされた。`buildinpublic-poll`
Skillが新設され、既存`buildinpublic-tweet`にも本来の設計意図（明示呼び出しのみ）を
`disable-model-invocation: true`で機械的に担保する修正が追加された。

## Reflections

本エントリはこのAnyAR作業を行っているセッション自身が直接参加していない、並行するセッション
の成果であり、AnyDRファイルの内容のみを根拠に事後的に再構成した記録である。新Skill設計の
過程で、既存の`buildinpublic-tweet`Skillの「明示呼び出しのみ」という設計意図が実は文章による
指示だけで担保されており、frontmatterのフラグで機械的に強制されていなかったという抜け漏れが
見つかり、あわせて修正された点が、記録として残しておく価値のある発見だった。
