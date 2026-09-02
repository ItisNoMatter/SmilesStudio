# 0031: 型システムをAI支援コントリビューションの安全網とするOSS方針、FIR/K2プラグインは将来構想として先送り

- Date: 2026-09-02
- Status: Accepted
- Category: architecture
- Deciders: the user

## Context

「化学徒がバイブコーディングで直接コントリビュートできる、しかも安全に」というOSS化の狙いを検討した。プログラミング経験の少ない化学の専門家が生成AIの力を借りてコードを書く時代において、AI生成コード特有のリスク（型の不整合、化学的に誤った構造の許容、レビュー負荷増大）が課題となる。`core-smiles`はこれまでも`value class`・sealed interface/class・網羅的`when`を中心とした型設計を積み重ねてきた（[[0001]]、[[0003]]、[[0008]]、[[0025]]、[[0026]]等）。

## Decision

堅牢な型システム（value class / sealed class / 網羅的when / 型安全なビルダー）を、AI生成コードでも安全に取り込めるための一次的な静的安全網として位置づけ、継続して投資する。「型が通っていればある程度信頼できる」という前提を、化学コミュニティに開かれたOSSとして育てる土台にする。FIR（K2コンパイラプラグイン）によるドメインルールのコンパイル時チェックは「将来構想」として位置づけ、Shipaton 2026のハッカソン期間内では実装しない（README等で構想として言及するに留める）。

## Alternatives

- FIR/K2コンパイラプラグインによるドメインルールのコンパイル時チェックを今回のスコープに含める: より強力な長期的方向性として認識されているが、ハッカソンの期間制約により今回は見送り、技術的な当否ではなく時間的制約による先送りとした。

## Consequences

- これまでの型設計に関する決定（[[0001]]、[[0003]]、[[0008]]、[[0025]]、[[0026]]）は、内部的なコード品質向上に加えて「OSSコントリビューションの安全網」という2つ目の役割を担うことになる。
- OSSライセンス（MIT/Apache 2.0等）の選定は元の方針文書内で「今のうちに検討しておく」と言及されているのみで、実際にはまだ決定されていない。本AnyDRでは決定事項として扱わず、未決定のフォローアップ事項として残す。

## Related

- [0001-core-smiles-id-based-domain-model](./0001-core-smiles-id-based-domain-model.md)
- [0003-atom-hydrogen-count-sealed-interface](./0003-atom-hydrogen-count-sealed-interface.md)
- [0025-aromatic-atom-symbol-separate-token-type](./0025-aromatic-atom-symbol-separate-token-type.md)
- [0026-ring-value-type-for-ring-detection](./0026-ring-value-type-for-ring-detection.md)
