# 0036: 実APIキー検証でGeminiモデル廃止を発見・修正し、プライバシーポリシー導線を追加した（Issue #30）

- Date: 2026-09-13
- Related AnyDR: 0109, 0110, 0111
- Related Issue: #30

## Objective

Issue #15（手描き構造式認識UI）をcloseする前に、ダミーではなく実際のGemini APIキーで
エンドツーエンドの動作確認を行う。あわせて、Issue #15の画像送信機能によって必要になった
Google Playユーザーデータポリシー対応（Issue #30: アプリ内へのプライバシーポリシー導線追加）
を`/grill-with-docs`で設計・実装する。

## Action

*   ユーザーがエミュレータ上でAPIキー設定ダイアログに実際のGemini APIキーを直接入力
    （チャットには一切貼らず、画面上で直接入力してもらう形にした）。
*   ギャラリーからベンゼン環の画像を選択して認識を試したところ、
    `GoogleModels.Gemini2_5Flash`が新規ユーザー向けに404（廃止済み、
    `gemini-3.6-flash`への移行を推奨するエラー）で失敗することが発覚。KoogのGoogleModels
    （1.1.1-beta時点）には`gemini-3.6-flash`はまだ定義されていなかったため、定義済みの中で
    最新の`Gemini3_5Flash`に切り替えて解決した。
*   再度実機確認したところ、ベンゼン環の画像から`c1ccccc1`が正しく認識・再描画されることを
    確認。
*   ユーザーからKoogの「Strategy Graph」（複数ターンのエージェント制御DSL）について質問が
    あり、`vision-recognition`モジュールをgrepして未使用であることを確認。公式ドキュメント
    （docs.koog.ai/custom-strategy-graphs）をWebFetchで参照し、「Prompt Executor層のみ採用、
    Strategy Graph層は不使用」という整理をHTMLアーティファクトに図解した。続けて「SMILESが
    パース不可能な場合に3回リトライする」という具体案についても、Strategy Graphではなく
    プレーンなKotlinループで十分という結論に至った（`ImageRecognitionCoordinator`に実装予定、
    未着手）。技術的な挑戦としてStrategy Graphに興味があるという話から、パーサーのエラーを
    ツール呼び出しとしてLLMにフィードバックする自己修正ループの技術検証をIssue #29として
    起票した。
*   Issue #15をclose後、Issue #30（Issue #15の親issue）を`/grill-with-docs`で設計。
    プライバシーポリシーへのリンクは既存の「このアプリについて」ダイアログに追加し、外部
    ブラウザ（`ACTION_VIEW`）で開く方式に決定（AnyDR 0109, 0110）。さらに、現在の
    プライバシーポリシー本文が「現時点で送信しません」という実態と異なる記述のままだった
    ことに気づき、Issue #15/#16で実装済みの画像送信・BYOK機能を反映するよう本文も同じ
    タイミングで更新することにした（AnyDR 0111）。

## Result

*   コミット a912f4f （Geminiモデル修正）・ 3a78837 （プライバシーポリシー導線追加＋本文
    更新）をpush。
*   `./gradlew allTests :android-app:installDebug`成功。
*   Issue #29（Strategy Graph技術検証、将来）を起票し#11のsub-issueに。
*   Issue #30を実装（close未実施）。

## Reflections

「テストがGREENでエミュレータでも一度動いた」だけでは不十分で、ダミーではない実際の
APIキーで検証して初めて、Koogライブラリのモデル定義が外部サービス側のライフサイクル
（モデル廃止）に追従できていないという、コードレビューやユニットテストでは絶対に
見つからない類のバグが発覚した。「本物の依存先を一度は本物のキーで叩く」ことの価値を
改めて実感した。

また、Issue #30の設計中に「プライバシーポリシー本文が実態と食い違っている」という、
Issue自体の文面には書かれていなかった問題に気づけたのも、grillingで一つずつ前提を
確認しながら進めたからこそだった。Issueのタイトル・スコープをそのまま鵜呑みにせず、
「このリンクの先の内容は今も正しいか」を確認しに行く姿勢が功を奏した例だと思う。
