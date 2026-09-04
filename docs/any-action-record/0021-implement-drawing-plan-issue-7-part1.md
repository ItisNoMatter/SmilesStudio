# 0021: 描画計画の純粋関数化（Issue #7前半）を実装した

- Date: 2026-09-04
- Related AnyDR: 0067, 0068
- Related Issue: #7（未完了、前半部分）

## Objective

Issue #7（`MoleculeCanvas`の描画実装）に、直前に見直したばかりの新しい5ステップTDDプロセス
（AnyDR 0066→0069）を初めて実際に適用する。

## Action

実装前にCLAUDE.mdの複数案提示ルールに従い2つの設計フォークを解決した: (1) Compose Canvasの
DrawScopeは直接ユニットテストできないため、描画内容を計算する純粋関数
`planMoleculeDrawing(molecule): List<DrawCommand>`を切り出し、`computeLayout`/`kekulize`と
同じ独立関数パターンを踏襲する方針（AnyDR 0067）、(2) 原子ラベルの表示方針を、化学の骨格式
（Skeletal Formula）の慣習に従い炭素はラベルなし・炭素以外の元素のみ元素記号を表示する方針
（AnyDR 0068）。

その後、新5ステップTDDプロセスを1サイクル通しで実行した。テスト6件（`DrawCommand.BondLine`/
`AtomLabel`の内容検証）を先に書き構造のRED（未定義参照によるコンパイルエラー）を確認、
`DrawCommand` sealed interfaceと`emptyList()`を返すスケルトンでコンパイルを通し、6件中5件が
振る舞いのREDで失敗することを確認、`computeLayout`と`kekulize`を組み合わせた実装でGREENにし、
`mapNotNull`+早期returnを`filter`+`map`に整理するリファクタリングを行った。

## Result

`./gradlew allTests`がグリーン。Issue #7自体はまだ未完了（残りはCompose Canvas APIへの実際の
描画呼び出しという、ユニットテスト不可能な薄いアダプタ部分）。コミット`9fb3785`・push完了。

## Reflections

新しい5ステップTDDプロセス（AnyDR 0066）を初めて実際のIssueに適用してみた結果、直後に「各
ステップの確認は不要」（AnyDR 0069）へと運用が見直された。プロセス自体は機能したが、確認の
頻度がボトルネックになるとすぐにわかった、というのがこのサイクルの一番の実務的な学びだった。
