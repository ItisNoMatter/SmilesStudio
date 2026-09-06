# 0028: Issue #24（Safe Area対応）を設計・実装・実機確認しクローズした

- Date: 2026-09-06
- Related AnyDR: 0079, 0080, 0081, 0082, 0083
- Related Issue: #24

## Objective
[Android公式ガイド](https://developer.android.com/design/ui/mobile/guides/layout-and-content/layout-basics)
を参考に作成したIssue #24（android-appのSafe Area対応）を、`/grill-with-docs`で設計方針を
固めた上で実装し、実機で動作確認する。

## Action
*   `/grill-with-docs`セッションで設計ツリーを1問ずつ検討: (1) インセット対応コードの配置場所
    （android-app呼び出し側 vs 共有Composable）→ AnyDR 0080、(2) `TextField`の上方インセットか
    `MoleculeCanvas`のedge-to-edge範囲か → AnyDR 0081。
*   AnyDR 0081決定直後、ユーザーから「TextFieldが画面上部にあるのはユーザー体験が悪い」という
    指摘があり、前提を撤回。「TextFieldを画面下部に固定し、共有Composable（`MoleculeEditor`）
    自体に反映してdesktop-appにも適用、IMEパディングもスコープに含める」という新方針をAnyDR 0082
    として決定（0081は削除せず、0082からリンクして前提の撤回を明記）。続けて`MoleculeCanvas`と
    `TextField`を重ねるか単純に縦分割するかを検討し、後者を選択（AnyDR 0083）。
*   実装: `MainActivity.kt`に`enableEdgeToEdge()`を追加。`MoleculeEditor`のレイアウト順序を
    `MoleculeCanvas`（`weight(1f)`、上部edge-to-edge）→エラー表示→`TextField`（下部固定、
    `imePadding()`＋`WindowInsets.safeDrawing`のBottom側）に変更。この変更は純粋なComposable
    レイアウトの並べ替えで新規のテスト可能ロジックが生まれないため、AnyDR 0067の前例（Compose
    Canvas UI層は目視確認に頼る）に倣い、ユニットテストは追加せず実機確認に絞った。
*   AVD `SmileStudio_Test`にインストールし実機確認: `enableEdgeToEdge()`によりステータスバーの
    背景が消えedge-to-edgeになったこと、`TextField`が画面下部・ジェスチャーナビゲーション領域の
    上に正しく配置されること、`c1ccccc1`入力でベンゼン環がKekulé構造で描画されること、
    ソフトウェアキーボード表示時に`TextField`がキーボードの上に追従すること（`imePadding()`が
    機能していること）を確認した。

## Result
*   コミット 449a228 をpush。Issue #24をclose。
*   AnyDR 0079〜0083を記録（0082が0081の前提撤回を明記してリンク）。
*   実機確認により、edge-to-edge表示・下部固定TextField・IME追従のすべてが意図通り動作することを
    確認。

## Reflections
検証の途中で一度ANR（「SmileStudio isn't responding」）が発生し、最初は自分の変更が原因かと
身構えたが、`top`コマンドで確認したところエミュレータの空きメモリが119MBまで逼迫しswapを
935MB使用しており、CPU時間の大半もカーネル側（65%〜124%）だったため、システム側の資源逼迫が
主因だと判断した。直接の引き金は、こちらが20回の削除キーを別々の`adb shell`プロセスとして
間髪入れず送りつけたことで、実際のユーザーがこの速度で操作することはない。アプリを再起動し
通常の一括入力に切り替えたところ再発しなかったことも判断材料にした。「異常が起きたら即座に
自分のコードを疑う」だけでなく、検証手法自体が異常な負荷をかけていないかも同時に検討する
必要がある、という教訓になった。

また、`/grill-with-docs`セッション中にユーザーが一度決定を撤回した場面があったが、このプロジェクト
の運用（既存AnyDRは編集せず新しいAnyDRでリンクして上書きする）のおかげで、「なぜ最初は
TextField上部固定にしたのか」「なぜ後から下部固定に変えたのか」という判断の変遷がそのまま
AnyDR 0081→0082のリンクとして残った。決定を後から覆すこと自体は珍しくないので、この記録方式が
うまく機能した具体例になったと思う。
