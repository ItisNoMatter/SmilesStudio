# 0071: MoleculeCanvasのモバイル対応は自動フィットのスケーリングのみとする

- Date: 2026-09-05
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[Issue #13](https://github.com/ItisNoMatter/SmilesStudio/issues/13)（ui-composeのモバイル向け調整）に着手するにあたり、`/grill-with-docs`で具体的なスコープを検討した。現状`MoleculeCanvas`は固定スケール（`PIXELS_PER_UNIT = 60f`）で描画しており、画面サイズに関わらず同じ拡大率になっている。`android-app`の`MainActivity`は現状`MoleculeCanvas(molecule = null, ...)`のみで、テキスト入力等のインタラクティブなUIはまだ存在しない（Android側の主要な入力手段はIssue #15の手描き認識UIが担う想定、AnyDR 0028）。

## Decision

`MoleculeCanvas`のモバイル対応は、分子のバウンディングボックスとCanvasの実サイズから動的にスケール係数を計算し、常に画面内に収まるように描画する自動フィット機能のみとする。固定`PIXELS_PER_UNIT`は廃止する。パン・ズームなどのタッチ操作は追加しない（引き続き読み取り専用表示のまま）。

## Alternatives

- 自動フィットに加えてピンチズーム・パン操作も実装する: 大きな分子でも細部を確認できるという利点があったが、「読み取り専用描画」というv1スコープ（AnyDR 0017）に対して明らかに過剰であり、現状Android上に表示する実際のコンテンツ（手描き認識結果等）がまだ存在せず動作確認も困難なため不採用。パン・ズームは実際に必要性が出た時点（大きな分子を扱うIssueが出てきた時等）で再検討する。

## Consequences

- `MoleculeCanvas.kt`の`canvasMapper`関数を、固定`PIXELS_PER_UNIT`ではなく、Canvasの実サイズ（`size.width`/`size.height`）と分子のバウンディングボックスから動的にスケール係数を計算する形に変更する必要がある。
- デスクトップ（`desktop-app`）・モバイル（`android-app`）の両方でこの自動フィットロジックが共有される（`ui-compose`は`core-smiles`同様プラットフォーム非依存のため）。

## Related

- [0017-v1-text-input-readonly-rendering-scope](./0017-v1-text-input-readonly-rendering-scope.md)
- [0028-handdrawn-structure-recognition-mvp](./0028-handdrawn-structure-recognition-mvp.md)
- [0067-drawing-plan-as-pure-function](./0067-drawing-plan-as-pure-function.md)
