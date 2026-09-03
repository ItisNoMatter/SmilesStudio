# 0011: android-appモジュールの追加（Issue #12）をKMP Androidターゲットとして完了

- Date: 2026-09-03
- Related AnyDR: 0027, 0034
- Related Issue: #12

## Objective

Issue #11（Shipaton 2026対応マップ）とIssue #1（デスクトップv1マップ）という2つの並行
ロードマップの優先順位をIssue #11側に定めた上で（AnyDR 0034）、そのフロンティアの一つである
Issue #12（android-appモジュールの追加）を完了させる。

## Action

`com.android.kotlin.multiplatform.library`プラグイン（AGP 9+での`com.android.library` +
`kotlin.multiplatform`組み合わせの非推奨化に伴う移行先）で`core-smiles`/`ui-compose`に
Androidターゲットを追加した。Compose Multiplatform 1.12.0のAndroid成果物がAGP≥9.1.0・
compileSdk≥37を要求する制約を踏まえてバージョンを引き上げた。legacy `avdmanager`がJDK 11+と
非互換な問題は、modern `cmdline-tools`パッケージを直接ダウンロードして解決した。実機
（エミュレータ）を新規作成して起動確認まで実施した。作業中に発覚したAndroid Studio実行構成の
不整合（存在しない`SmileStudio.app`モジュールを参照）も合わせて修正した。

## Result

`android-app`モジュールが追加され、`core-smiles`/`ui-compose`を再利用する形でAndroidエントリ
ポイントが動作することをエミュレータ上で確認した。`26e9c84`で実装完了、Issue #12クローズ。

## Reflections

AGPやCompose Multiplatformのバージョン間の非互換性、legacy SDKツールとJDKバージョンの非互換性
など、公式ドキュメントだけでは読み取れない実務的な障害が連続したが、いずれもエラーメッセージが
移行先を明示していたため、それに従うことで解決できた。「動くはず」で終わらせず実機で最後まで
確認したことで、Android Studio側の設定不整合という別種の問題も早期に見つけられた。
