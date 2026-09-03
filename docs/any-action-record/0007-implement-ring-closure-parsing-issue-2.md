# 0007: SMILES環閉包記法パース（Issue #2）をTDDで実装

- Date: 2026-09-01
- Related AnyDR: 0024
- Related Issue: #2

## Objective

Issue #2（SMILESパーサー: 環閉包記法への対応）を完了させる。

## Action

実装前にスコープの選択肢を提示し、環閉包ラベルは番号のみサポートし結合種別付与記法
（`C=1...=1`）は未対応とする方針を確定した（AnyDR 0024）。TDDでTokenizer/SmilesParserを拡張
し環閉包ラベルの解決ロジックを実装した。

## Result

`C1CCCCC1`（シクロヘキサン）のような環状SMILESがパースできるようになった。`0f2cfbd`で実装
完了、Issue #2クローズ。

## Reflections

「最小構成」の方針を実装レベルでも一貫させる（結合種別付与記法のような周辺機能を都度不採用に
する）ことで、Tokenizer/Parserの変更範囲を環閉包ラベルの追加だけに抑えられた。
