# 0038: サーバーレス関数（画像認識プロキシ）を実装した（Issue #34）

- Date: 2026-09-13
- Related AnyDR: 0126, 0127, 0128, 0129
- Related Issue: #34

## Objective

Issue #33（Firebase/RevenueCatプロジェクト設定）がPlay Console側の銀行口座確認待ちで
ブロックされたため、依存しないIssue #34（サーバーレス関数の実装）に先に着手する。
BYOKを使わない全ユーザー（無料枠・購読）向けに、開発者のGemini APIキーをサーバー側に
保持したまま画像認識を行うFirebase Cloud Functionsを実装する。

## Action

`/grill-with-docs`でエンドポイント形式（Callable Functions）、APIキーの保持方法
（Secret Manager）、Firestoreのデータモデル（`users/{uid}`単一ドキュメント）、Gemini
呼び出し方法（公式`@google/genai`SDK）を決定（AnyDR 0126〜0129）。

購読状態・無料枠カウンターの判定ロジック（`decideEntitlement`）を、Firestoreの
トランザクションから切り離した純粋関数として設計し、Jest（ts-jest）でTDD
（RED→GREEN、5テストケース: 購読中はカウンター消費なし、上限未満で消費・増分、上限到達で
拒否、月替わりでのリセット、新規ユーザーの初回利用）。Firestore・Gemini・RevenueCat
Webhookとの実際のI/O部分（`firestoreEntitlement.ts`、`geminiClient.ts`、
`recognizeImage.ts`、`revenueCatWebhook.ts`）は薄いラッパーとして実装し、TDD対象外とした
（Androidの`AndroidKeystoreApiKeyEncryptor`と同様のfunctional core/imperative shell
パターン）。

デプロイ用に`firebase.json`・`.firebaserc`（プロジェクトID`smilestudio-116a8`）も用意し、
`firebase deploy --only functions`がそのまま実行できる状態にした。

## Result

*   `npx tsc --noEmit`・`npx jest`・`npx tsc`（ビルド）すべて成功。
*   コミット `d3fa833` をpush。
*   `functions/`ディレクトリにNode.js/TypeScriptプロジェクトを新規構築（このプロジェクト
    初のTypeScriptコード）。
*   実際のFirebaseへのデプロイ・Secret Manager登録（`GEMINI_API_KEY`、
    `REVENUECAT_WEBHOOK_SECRET`）はユーザーの環境で別途必要。

## Reflections

Kotlin側で確立していた「プラットフォーム依存のI/Oを薄いラッパーに追い出し、判定ロジック
本体だけを純粋関数としてTDDする」というパターン（`ApiKeyStore`＋`ApiKeyEncryptor`、
`ImageRecognitionCoordinator`）が、言語もランタイムも全く異なるNode.js/TypeScriptの
Cloud Functionsにもそのまま移植できた。TDDの効果はテストランナーやFirestoreの有無に
依存しない、設計原則そのものの効果なのだと実感した。

`@google/genai`のSDKは型定義ファイル（`.d.ts`）にJSDocコメント付きの実例が豊富に
埋め込まれており、外部ドキュメントを検索しなくてもインストール済みパッケージの型定義を
直接読むだけで正しい使い方（`createUserContent`、`createPartFromBase64`等）を確認できた。
今回のように新しいSDKを初めて使う場面では、型定義ファイルを直接読むのが検索より速く
正確な場合があるという発見だった。
