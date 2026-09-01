# 0024: Issue #2の環閉包記法は番号のみ対応し、結合種別付与記法は未対応とする

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[GitHub Issue #2](https://github.com/ItisNoMatter/SmilesStudio/issues/2)（SMILESパーサー: 環閉包記法（リング）への対応）の実装に着手するにあたり、CLAUDE.mdの方針に従い実装前にスコープの選択肢を提示した。環閉包ラベル（例: `C1CCCCC1`の`1`）自体は[AnyDR 0018](./0018-v1-grammar-scope-rings-and-aromatics.md)でv1スコープに含めることが既に決まっていたが、環閉包ラベルに結合種別を付与する記法（例: `C=1CCCCC=1`）を今回のIssueに含めるかどうかは未決定だった。

## Decision

Issue #2では、環閉包ラベルは番号のみサポートする（例: `C1CCCCC1`）。環閉包ラベルの直前・直後に結合記号を伴う記法（例: `C=1CCCCC=1`）は、[AnyDR 0015](./0015-unsupported-notation-specific-error.md)のパターンに従い専用理由のFailureを返し、未対応として扱う。

## Alternatives

- 環閉包ラベルへの結合種別付与も含めてパースする: SMILES文法としてより完全なカバレッジになり将来の追加対応issueを避けられる一方、Tokenizerで「結合記号＋環閉包ラベル」を1つの複合トークンとして認識するロジックが必要になり「結合記号の次はAtomSymbolが来る」という既存の前提が崩れる。開く側・閉じる側で指定された結合種別の整合性チェックも必要になる。v1で対象とする具体的な分子（ベンゼン、シクロヘキサン、ピリジン等、[AnyDR 0018](./0018-v1-grammar-scope-rings-and-aromatics.md)参照）にはいずれも不要な機能であり、これまでのAnyDR群が一貫して採用してきた「最小構成」の方針に反するため不採用。

## Consequences

- Tokenizer/Parserの変更は環閉包ラベル（番号のみ）の追加に限定でき、既存の`BondSymbol`トークンの扱いを変更する必要がない。
- 一部のSMILES生成ツールが出力する、環閉包側に結合種別を付与する記法を入力すると「未対応」エラーになる。将来この記法への対応が必要になった場合は、本AnyDRを再訪しTokenizer/Parserを再拡張する。

## Related

- [0015-unsupported-notation-specific-error](./0015-unsupported-notation-specific-error.md)
- [0018-v1-grammar-scope-rings-and-aromatics](./0018-v1-grammar-scope-rings-and-aromatics.md)
