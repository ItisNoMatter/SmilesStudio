# 0040: 英語版AnyDRは`docs/any-decision-record/en/`サブディレクトリに格納する

- Date: 2026-09-03
- Status: Accepted
- Category: naming
- Deciders: the user, Claude Code

## Context

[[0039]]でBuildInPublicツイート作成Skillをグローバルスコープに配置することを決めた。このSkillはツイート作成のついでに、対象AnyDRの英語版がまだなければオンデマンドで生成する（既存38件を一括翻訳するのではなく、実際にツイートで公開する決定だけを英訳する方針、[[0037]]の三層防御OSS戦略・BuildInPublicAward獲得を狙った運用の一環）。この英語版AnyDRの格納場所を決める必要があった。

## Decision

英語版AnyDRは`docs/any-decision-record/en/`サブディレクトリに格納する。ファイル名は日本語版と同じAnyDR番号・スラッグを保つ（例: 日本語版`docs/any-decision-record/0028-handdrawn-structure-recognition-mvp.md`に対し、英語版は`docs/any-decision-record/en/0028-handdrawn-structure-recognition-mvp.md`）。ツイートされた決定のみが英語版を持つため、`en/`配下の番号は歯抜けになることを前提とする（欠番を埋める作業は発生させない）。

## Alternatives

- 同一ディレクトリ内でファイル名にサフィックス（`docs/any-decision-record/0028-....en.md`）: 日本語版・英語版が番号順に隣接して並ぶという利点があったが、ディレクトリ内のファイル数が実質2倍になり一覧性が下がることと、「なぜ`.en.md`も既存スキルの番号スキャン対象になるのか」を将来読む人が一瞬迷う可能性があるため不採用。

## Consequences

- 既存の`any-decision-record`スキルの番号スキャンロジック（`docs/any-decision-record/`直下のみを対象）には一切手を入れる必要がない。
- BuildInPublicツイート作成Skill（[[0039]]）は、対象AnyDR番号について`docs/any-decision-record/en/<番号>-*.md`の有無を確認し、なければ生成してからツイート文を作成する、という手順になる。
- `en/`配下のファイル一覧を見れば、どの決定が実際にツイートで公開されたかが分かる（歯抜けの番号列がそのまま「公開済み決定の一覧」を兼ねる）。

## Related

- [0037-three-layer-defense-oss-strategy](./0037-three-layer-defense-oss-strategy.md)
- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
