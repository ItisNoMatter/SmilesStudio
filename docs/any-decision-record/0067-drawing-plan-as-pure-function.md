# 0067: MoleculeCanvasの描画内容は純粋関数として切り出しテスト可能にする

- Date: 2026-09-04
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[Issue #7](https://github.com/ItisNoMatter/SmilesStudio/issues/7)（`MoleculeCanvas`の描画実装）の実装に着手するにあたり、CLAUDE.mdの方針に従い実装前に選択肢を提示した。Compose CanvasのDrawScope内のロジックはユニットテストで直接検証できないため、[AnyDR 0066](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0066-five-step-tdd-workflow-with-per-step-confirmation.md)で再定義した5ステップTDDプロセスをどこまで適用できるかが論点だった。

## Decision

原子・結合の描画内容（線分の始点終点とBondType、原子ラベルの位置とテキスト等）を計算する純粋関数を新設し、`MoleculeCanvas`のCompose側はその結果をイテレートして`drawLine`/`drawCircle`等のCanvas APIを呼ぶだけの薄いアダプタにする。

## Alternatives

- `MoleculeCanvas`のCompose CanvasスコープDrawScope内に直接描画ロジックを書く: 中間データ構造が不要でシンプルという利点があったが、Compose CanvasのDrawScope内のロジックはユニットテストで検証できず目視確認のみに頼ることになり、CLAUDE.mdのTDD方針・AnyDR 0066の5ステップTDDプロセスと根本的に相性が悪いため不採用。

## Consequences

- [AnyDR 0045](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0045-layout-as-independent-function.md)の`computeLayout`、[AnyDR 0065](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0065-kekulize-independent-function.md)の`kekulize`と同じ「独立関数」パターンをこの描画計画にも踏襲する。
- 新しい中間データ構造（描画コマンド等）を定義する必要がある。
- Compose Canvas自体の実際の描画呼び出し（薄いアダプタ部分）は引き続きユニットテスト困難で、目視確認（`desktop-app`から実行）に頼る。

## Related

- [0045-layout-as-independent-function](./0045-layout-as-independent-function.md)
- [0065-kekulize-independent-function](./0065-kekulize-independent-function.md)
- [0066-five-step-tdd-workflow-with-per-step-confirmation](./0066-five-step-tdd-workflow-with-per-step-confirmation.md)
