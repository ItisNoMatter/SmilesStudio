# 0026: Issue #11着手前の実利用可能性チェックとMoleculeEditor実装

- Date: 2026-09-06
- Related AnyDR: なし（判断は既存AnyDR 0060・Issue #8の再確認）
- Related Issue: #11, #18, #8

## Objective
Issue #11（Shipaton 2026ロードマップ）を進めるにあたり、Phase 1の最後のブロッカーである
Issue #18（Google Play非公開テスト開始）に向けたリリース署名設定に着手する前に、モバイルアプリの
実装が実際にユーザーに触ってもらえるレベルまで進んでいるかを確認する。

## Action
*   `android-app`（`MainActivity.kt`）・`desktop-app`（`Main.kt`）を確認したところ、両方とも
    `MoleculeCanvas(molecule = null, ...)`を直書きしているだけで、SMILES文字列を入力する手段が
    一切ないことが判明。リポジトリ全体を`TextField`/`mutableStateOf`/`remember`で検索しても
    ヒットせず、テキスト入力・状態管理を持つComposableがどこにも存在しないことを確認した。
*   この機能に対応するはずのIssue #8「desktop-app: SMILES入力欄とパースエラー表示の実装」が
    まだOPENで、Shipaton/Androidマップ（Issue #11）側に一度も引き継がれておらず、Issue #18の
    ブロッカーにも含まれていないことを発見。[AnyDR 0060](../any-decision-record/0060-minimal-build-scope-rendering-pipeline-only.md)
    自身が「テキストでSMILESを入力すると構造式が描画されるアプリ」を最小ビルドの定義として
    いたにもかかわらず、そのブロッカー（#6・#7・#13）がこの機能を作るIssueを含んでいなかった、
    という設計上の見落としだった。
*   ユーザーに状況を報告し、CLAUDE.mdのPros/Consフォーマットで実装方針（状態を呼び出し側に
    ホイストするA案 / Composable自身が状態を内包するB案）を提示。AnyDR 0028（手描き認識結果を
    既存のテキスト入力パイプラインに反映する）との整合性からA案を推奨し、ユーザーもA案を選択。
*   TDDで`ui-compose`に`resolveMoleculeEditorState`（純粋関数、パース結果と直前の有効な
    Moleculeからレンダリング状態を決定）と`MoleculeEditor`Composable（TextField＋エラー表示＋
    MoleculeCanvas）を実装。`MainActivity.kt`・`Main.kt`をこれに配線。
*   `SmileStudio_Test`エミュレータに実際にインストールして動作確認したところ、画面が完全に
    空白でTextFieldが見えないという別の不具合に遭遇。`uiautomator dump`でビュー階層を調べた
    結果、`AndroidManifest.xml`にテーマ指定が一切なく、プラットフォームのデフォルトActionBar
    （「SmileStudio」というタイトルバー）がCompose UIの先頭部分を完全に覆い隠していたことが
    判明。`NoActionBar`テーマを追加して修正。
*   再度エミュレータで検証: `c1ccccc1`と入力→ベンゼン環がKekulé構造で正しく描画。続けて
    環閉包が閉じない不正な文字を追加→「位置8: 環閉包ラベル2が閉じられていません」という
    エラーが表示されつつ、直前の正しいベンゼン環の描画は消えずに保持されることを確認。

## Result
*   コミット 5f9ba9d（署名設定）・ 8a7883a （MoleculeEditor実装＋ActionBar修正）をpush。
*   `./gradlew allTests build`成功。
*   エミュレータでの実機動作確認により、Android版アプリで「SMILES文字列を入力すると構造式が
    描画される」という最小ビルドの核となる体験が初めて動く状態になった。

## Reflections
「closeできるか確認して」（Issue #14）に続き、今回も「実装が完了している」という前提を鵜呑みに
せず実際にコード・実機を確認したことで、ロードマップ上の見落とし（Issue #8がAndroidマップに
引き継がれていなかったこと）と、実装上の見落とし（ActionBarがUIを覆い隠していたこと）の
両方を発見できた。特に後者は、`molecule = null`という無害に見えるハードコードのおかげで
これまで誰にも気づかれずに隠れていたバグで、「動くはずのものを実際に動かして確認する」
プロセスの価値を改めて実感した。ドキュメント上の整合性（AnyDR・Issueのブロッカー関係）と
実機での動作確認は、どちらか一方では見つけられない種類の問題を互いに補完する。
