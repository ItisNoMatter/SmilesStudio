# 0030: android-appをMaterial 3 Expressiveデザインで作り直した

- Date: 2026-09-06
- Related Issue: #25

## Objective
ユーザーから提示された詳細なMaterial 3 Expressiveデザイン仕様（カラースキーム・タイポグラフィ・
モーション・画面構成・各部品スタイル）に基づき、Issue #25を作成した上で、android-appを
「ホーム」（SMILES入力＋Canvas表示）・「使い方」（SMILES記法の基本を示す静止画面）の2タブ構成に
作り直す。

## Action
*   Issue #25をユーザー提示の仕様全文つきで作成し、Issue #11のsub-issueとして紐づけた。
*   実装中に、このプロジェクトが使っているJetBrains Compose Multiplatformのmaterial3アーティ
    ファクト（`composeMultiplatform`バンドルの1.12.0-alpha03）では、`MaterialExpressiveTheme`・
    `MotionScheme`が依然として`internal`扱いで公開APIとして使えないことが判明。android-appは
    仕様上Android専用スコープだったため、Google公式のAndroidX material3を直接（1.5.0-alpha27、
    `@OptIn(ExperimentalMaterial3ExpressiveApi::class)`で公開されている最新alpha）依存に切り替えて
    解決した。`ui-compose`はdesktop-appとの共有のため引き続きJetBrainsのマルチプラットフォーム
    アーティファクトを使用。
*   指定のカラースキーム（primary #6750A4等）を`lightColorScheme()`のロール引数として設定し、
    `MaterialExpressiveTheme`でラップする`SmilesStudioTheme`を実装。
*   TopAppBar（タイトル＋more_vertメニュー：入力をクリア／このアプリについて）、NavigationBar
    （ホーム／使い方、2項目）、`AnimatedContent`によるタブ切り替え（`MotionScheme`のspatial/
    effectsスペックを使ったフェード＋スライド）を持つ`SmilesStudioApp`を新規実装。
*   ホームタブ（`HomeContent`）は既存の`ui-compose`の`MoleculeCanvas`・`resolveMoleculeEditorState`
    をそのまま再利用し、`OutlinedTextField`（角丸16dp、`surfaceContainerHighest`塗りつぶし）で
    ラップし直した。
*   使い方タブ（`HowToContent`）は新規コンテンツとして、原子・単結合・二重結合・三重結合・
    脂肪族環・芳香環の6例を、各例の説明文＋`SmilesParser`でパースした`MoleculeCanvas`ライブ
    プレビュー付きのカードで一覧表示するよう実装。
*   タップ時のリップル＋軽い縮小フィードバックとして、`MotionScheme`のspringに乗せた
    `expressivePressScale()`Modifierを実装し、more_vertボタンに適用。
*   エミュレータで実機確認: TopAppBar・NavigationBar・タブ切り替えアニメーション、more_vertメニュー
    の開閉、ホームタブでのSMILES入力→ベンゼン環描画、使い方タブの全6例のライブプレビュー描画
    （原子ラベル・ジグザグ鎖・二重/三重結合・脂肪族環・芳香環いずれも正しく描画）、TextField
    フォーカス時のラベル浮き上がり＋primary色の枠、を確認。

## Result
*   コミット 492c35e をpush。
*   `./gradlew allTests build`成功。
*   実機確認によりデザイン仕様のほぼ全項目（カラー・形・タイポグラフィ・モーション・2タブ構成・
    各部品スタイル・more_vertメニューの実処理・タップフィードバック）が動作することを確認。

## Reflections
「JetBrainsのCompose MultiplatformでもM3 Expressiveが使えるはず」という調査結果を鵜呑みにせず、
実際にコンパイルしてみたことで、依存しているバンドルバージョン特有の`internal`可視性という
落とし穴を早期に発見できた。android-appが最初からAndroid専用スコープとして切り出されていた
（Issue #25自体がそう明記していた）おかげで、`ui-compose`側の互換性を気にせずAndroidX本家の
material3アルファ版に切り替えるという現実的な回避策を迷わず選べた。マルチプラットフォーム
ラッパーと本家ライブラリの間でAPI公開状況にズレが生じうる、という教訓は今後Compose
Multiplatformで最新機能を使う際に活きそうだ。
