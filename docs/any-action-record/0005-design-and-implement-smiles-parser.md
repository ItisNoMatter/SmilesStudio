# 0005: SmilesParser.parse()を直鎖+分岐スコープでTokenizer分離方式により設計・実装

- Date: 2026-09-01
- Related AnyDR: 0012, 0013, 0014, 0015, 0016

## Objective

ダミー実装のままだった`SmilesParser.parse()`に、実際のSMILES構文解析ロジックを実装する。

## Action

`/grill-with-docs`で最初のTDDイテレーションのスコープ（直鎖+分岐のみ、環閉包・芳香族は次段階に
送る）と内部アーキテクチャ（文字列→Tokenizer→再帰下降パースの二段階方式。ユーザーはClaude Code
が推奨した一体型再帰下降ではなくTokenizer分離を選択）を確定した（AnyDR 0012〜0016）。その後
TDDでTokenizerとSmilesParserを実装した。

## Result

`CCO`（エタノール）や`CC(=O)O`（酢酸）のような直鎖+分岐SMILESがパースできるようになった。
`60b5e09`で実装完了、テストグリーン。

## Reflections

Claude Codeが推奨した「一体型再帰下降」（実装量最小）ではなく、ユーザーは将来の拡張しやすさ
（トークン種別追加で環閉包・芳香族に対応できる）を理由にTokenizer分離を選んだ。目先の実装
コストより将来の拡張コストを重視する判断で、実際に後続のIssue #2・#3でこの見立て通りトークン
種別の追加だけで拡張できた。
