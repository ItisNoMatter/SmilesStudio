# 0032: PR向けAIレビューCIワークフローを追加する（低優先度）

- Date: 2026-09-02
- Status: Accepted
- Category: process
- Deciders: the user

## Context

[[0031]]で定めた「型システムによる静的な安全網」に対する動的な補完として、PR作成時にAIレビュー（Claude Codeまたは類似ツール）を自動実行するワークフローの追加を検討した。汎用的な指摘に留めず、SMILES/化学ドメイン特有のチェック観点（原子価・構造妥当性など）をプロンプトに組み込むことで、「化学徒が安全にコントリビュートできる」という狙いを実証する material（ハッカソンデモ等での訴求材料）としても位置づける。

## Decision

GitHub Actions等でPR作成時に自動でAIレビューを実行するワークフローを追加する。プロンプトには汎用コードレビューに加え、SMILES/化学ドメイン特有のチェック観点を組み込む。優先度は低く、Koog連携・Android対応・課金実装が完了した後の「余力があれば」枠として着手する。

## Alternatives

本文中に代替案の比較記述はなし。

## Consequences

- ハッカソンの期間内に着手できない場合、「型システム（静的）＋AIレビュー（動的）」という二段構えの安全網の訴求は、静的な型システムの部分のみで提出することになる。動的レビューの部分はハッカソン後のフォローアップとして持ち越す。

## Related

- [0031-type-system-as-oss-contribution-safety-net](./0031-type-system-as-oss-contribution-safety-net.md)
