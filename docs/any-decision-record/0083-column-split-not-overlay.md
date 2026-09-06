# 0083: MoleculeEditorはCanvas/TextFieldを重ねずColumnで縦分割する

- Date: 2026-09-06
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[AnyDR 0082](./0082-textfield-bottom-in-shared-editor.md)で`TextField`を画面下部に固定する
レイアウトに決めた後、`/grill-with-docs`でその具体的な組み方（`MoleculeCanvas`と`TextField`を
単純に縦分割するか、`Box`で重ねて`Canvas`を全画面edge-to-edgeにするか）を検討した。

## Decision

`MoleculeCanvas`と`TextField`は重ねず、単純な`Column`で縦分割する。`MoleculeCanvas`が
`weight(1f)`で上部の残り領域を占め、`TextField`がその下に固定される。`MoleculeCanvas`の
edge-to-edgeは画面上端（ステータスバー側）のみとし、画面下端（ジェスチャーナビゲーション
領域）までは描画しない。

## Alternatives

- `Box`で`MoleculeCanvas`を画面全体に敷き、`TextField`を下部にオーバーレイ表示する: 上下とも
  真のedge-to-edgeになり没入感が最大化されるという利点があったが、`TextField`の背景を明示的に
  指定しないと下に描画された構造式と文字が重なって読みにくくなるため、スクリム/背景色の追加
  実装が必要になる。`Box`+`align`+z順序の考慮も要る。Issue #24は本来セーフエリア対応が
  スコープであり、この作り込みは過剰と判断し不採用。

## Consequences

- `MoleculeCanvas`は画面下端まで描画されないため、構造式が大きい場合に表示領域がその分
  狭くなる。将来「没入感の最大化」を狙う場合は本AnyDRを再訪してBox案を検討する余地を残す。
- 実装は既存の`Column`構成を維持したままで済み、`MoleculeCanvas`自体のロジック変更は不要。

## Related

- [0082-textfield-bottom-in-shared-editor](./0082-textfield-bottom-in-shared-editor.md)
- [0081-canvas-edge-to-edge-textfield-inset](./0081-canvas-edge-to-edge-textfield-inset.md)
