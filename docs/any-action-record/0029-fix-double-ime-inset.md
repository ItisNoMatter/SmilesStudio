# 0029: TextFieldがキーボードより異様に高い位置に浮く不具合を修正した

- Date: 2026-09-06
- Related Issue: #24（クローズ後の追補）

## Objective
Issue #24クローズ後、ユーザーからスクリーンショット付きで「TextFieldが異様に上に表示される」
という不具合報告を受け、原因を特定して修正する。

## Action
報告されたスクリーンショットでは、キーボード表示時に`TextField`とキーボードの間に大きな
隙間が空いていた。[Android公式ドキュメント](https://developer.android.com/develop/ui/compose/system/insets)
で`WindowInsets.safeDrawing`の定義を確認したところ、`systemBars`・`displayCutout`に加えて
`ime`（ソフトウェアキーボード）も既に含まれていることが判明。AnyDR 0082の実装では
`MoleculeEditor`の`Column`に`.imePadding()`と`.windowInsetsPadding(WindowInsets.safeDrawing
.only(Bottom))`の両方を適用しており、キーボード表示時にIME分の余白が二重に加算され、
その分`TextField`がキーボードよりずっと上に浮いていた。`.imePadding()`の呼び出しを削除し、
`windowInsetsPadding(safeDrawing.only(Bottom))`のみに統一。エミュレータで再度キーボードを
開き、`TextField`がキーボードに密着することを確認した。

## Result
*   コミット 4256992 をpush。
*   実機確認により、`TextField`とキーボードの間の不自然な隙間が解消されたことを確認。

## Reflections
`WindowInsets.safeDrawing`が`ime`を包含するという仕様を把握しないまま、「セーフエリア用の
インセット」と「IME用のインセット」を別物だと思い込み、両方を律儀に適用してしまったのが
原因だった。Issue #24の設計・実装セッション中にこの点をきちんと調べていれば防げたミスであり、
「複数のインセット系Modifierを組み合わせる時は、それぞれが何を包含しているかを先に確認する」
という教訓になった。ユーザーからのスクリーンショット付き報告のおかげで、実機確認だけでは
見落としていた具体的な症状（隙間の大きさ）から原因を素早く特定できた。
