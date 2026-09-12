# 0104: 「画像から認識」ボタンはFloatingActionButtonとして配置する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0103]]で決めた「画像から認識」ボタンをホーム画面のどこに配置するかを検討した。候補は
SMILESテキストフィールドの`trailingIcon`と、`MoleculeCanvas`上に重ねる
`FloatingActionButton`の2つだった。

## Decision

`FloatingActionButton`として`MoleculeCanvas`の上に配置する。

## Alternatives

- SMILESテキストフィールドの`trailingIcon`として配置する: 実装コストが小さく「認識結果は
  このテキストフィールドに反映される」という関係性も分かりやすいという理由で当初推奨したが、
  ユーザーの指摘で不採用とした。「ほとんどのユーザーはSMILES記法を直接使うのではなく写真を
  使うだろう」という想定利用パターンを踏まえると、画像入力は副次的なアイコンではなく主要
  アクションとして扱うべきであり、テキストフィールドの脇に控えめに置く`trailingIcon`では
  この重要度を表現できないと判断した。

## Consequences

- `MoleculeCanvas`の上に浮かせる新しいレイアウト要素（`Box`での重ね合わせ等）の実装が必要
  になる。
- 分子描画エリアの一部をFABが覆う可能性があるため、配置位置（右下等）を検討する必要がある。

## Related

- [0103-image-input-single-button-bottom-sheet](./0103-image-input-single-button-bottom-sheet.md)
- [0102-image-input-no-permission-apis](./0102-image-input-no-permission-apis.md)
