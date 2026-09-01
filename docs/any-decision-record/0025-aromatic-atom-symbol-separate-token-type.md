# 0025: 芳香族小文字表記は別トークン型`Token.AromaticAtomSymbol`として表現する

- Date: 2026-09-01
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[GitHub Issue #3](https://github.com/ItisNoMatter/SmilesStudio/issues/3)（SMILESパーサー: 芳香族小文字表記への対応）の実装に着手するにあたり、CLAUDE.mdの方針に従い実装前にトークン表現の選択肢を提示した。芳香族小文字表記（例: `c1ccccc1`の`c`）をTokenizer/Parser層でどう表現するかが論点だった。なお`Element`列挙型にはホウ素（B）が含まれていないため、芳香族小文字表記のうち`b`は引き続き未対応とし、`c,n,o,p,s`の5元素を対象とする（AnyDR 0018のターゲット分子であるベンゼン・ピリジンはいずれも`c`/`n`のみで表現できるためスコープに影響しない）。

## Decision

芳香族小文字表記は、既存の`Token.AtomSymbol`にブール値フラグを追加するのではなく、別のトークン型`Token.AromaticAtomSymbol(element: Element)`として表現する。

## Alternatives

- `Token.AtomSymbol`に`isAromatic: Boolean`フラグを追加する: `Token`のsealed interfaceに新しいcaseを追加せずに済み、`SmilesParser`の`when`式に新しい分岐が不要という利点があった。「同じ元素種だが結合コンテキストの書き方が違うだけ」という実態も素直に表現でき、AnyDR 0004（芳香族性はAtomではなくBondから導出するSSOT）とも矛盾しない設計ではあったが、ユーザーは「型レベルでの強制力はAI時代において重要」という理由でBを選択したため不採用。

## Consequences

- `SmilesParser`の`when`式に`is Token.AromaticAtomSymbol`の新しい分岐が必要になる。既存の`AtomSymbol`処理ロジック（原子生成・結合生成）と共通する部分は、コード重複を避けるため共通ヘルパー関数に切り出す。
- パーサーは原子ごとに「芳香族小文字表記で書かれたか」を記録し、結合記号省略時のデフォルト結合種別を「両端の原子がともに芳香族小文字表記ならAROMATIC、そうでなければSINGLE」というルールで決定する。環閉包で結ばれる結合（[[0024]]）にも同じルールを適用するよう、Issue #2で実装した`SmilesParser`のリング閉包解決ロジックを修正する。
- `isAromatic`は最終的な`Atom`ドメインオブジェクトには一切保存されず、パース時の一時情報にとどまる。AnyDR 0004の設計思想とは矛盾しない。
- 芳香族小文字の`b`（ホウ素）は`Element`列挙型に対応するケースがないため、引き続き未対応として扱う。

## Related

- [0004-derive-aromaticity-from-bonds](./0004-derive-aromaticity-from-bonds.md)
- [0018-v1-grammar-scope-rings-and-aromatics](./0018-v1-grammar-scope-rings-and-aromatics.md)
- [0024-ring-closure-numeric-labels-only](./0024-ring-closure-numeric-labels-only.md)
