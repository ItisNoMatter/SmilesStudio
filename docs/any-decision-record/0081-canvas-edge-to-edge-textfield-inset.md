# 0081: MoleculeCanvasはedge-to-edgeのまま、TextField上方のみインセットする

- Date: 2026-09-06
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[AnyDR 0080](./0080-safe-area-in-android-app-call-site.md)でSafe Area対応をandroid-app呼び出し
側に実装する方針を決めた後、`/grill-with-docs`でその具体的な適用範囲（画面全体を一律インセット
するか、要素ごとに選択的にインセットするか）を検討した。

## Decision

`MoleculeEditor`内の`TextField`（画面上端）にのみ、上方向のセーフエリアインセット
（`WindowInsets.safeDrawing`のTop側）を適用する。`MoleculeCanvas`にはインセットを適用せず、
画面全体（システムバー・カットアウトの裏側も含む）に描画されるedge-to-edgeのままとする。

## Alternatives

- 画面全体（`MoleculeEditor`のColumn全体）に`Modifier.safeDrawingPadding()`を一律適用する:
  実装が1行で完結しシンプルという利点があったが、[Android公式ガイド「Edge-to-edge design」]
  (https://developer.android.com/design/ui/mobile/guides/layout-and-content/edge-to-edge)が
  推奨する「背景・単色バーはカットアウトへ描き込んでよい、インセットが必要なのは実際に操作する
  UIだけ」という方針に反する。`MoleculeCanvas`はクリック不可の読み取り専用描画面（AnyDR 0017）
  であり、四辺とも一律に余白を作ると構造式表示の没入感を損なうため不採用。

## Consequences

- `TextField`側は`Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))`
  のように上方向のみを明示的に指定する実装になり、`Modifier.safeDrawingPadding()`を丸ごと使う
  よりわずかに複雑になる。
- 画面を横向きにした場合、カットアウトが左右に来る端末では`MoleculeCanvas`側の要素がカットアウト
  に重なる可能性がある。ただしCanvasは非インタラクティブな描画のみのため実害は小さいと判断。
  将来グラフィカルな構造式編集（AnyDR 0017で保留したアプローチB）を実装する際は、Canvas上の
  操作可能な要素がカットアウト付近に来ないか再検討が必要になる。

## Related

- [0080-safe-area-in-android-app-call-site](./0080-safe-area-in-android-app-call-site.md)
- [0079-safe-area-tracked-as-own-issue](./0079-safe-area-tracked-as-own-issue.md)
- [0017-v1-text-input-readonly-rendering-scope](./0017-v1-text-input-readonly-rendering-scope.md)
