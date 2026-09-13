# 0122: サーバーレス関数はFirebase Cloud Functions（Node.js/TypeScript）で実装する

- Date: 2026-09-13
- Status: Accepted
- Category: tooling
- Deciders: the user, Claude Code

## Context

AnyDR 0121で、開発者のGemini APIキーを保持するサーバーレス関数を新設すると決めたが、
プラットフォーム・言語は未定だった。

## Decision

Firebase Cloud Functions（Node.js/TypeScript）で実装する。

## Alternatives

- Firebase Cloud Functions（Kotlin/Java、2nd gen）またはCloud Run上のKotlin/Ktorサービス:
  プロジェクト全体をKotlinで統一できる利点はあるが、RevenueCatとの連携例がNode.js/Pythonに
  比べて少なく、残り期間（Play申請目標09-20まで1週間）での試行錯誤リスクが大きいため
  不採用とした。

## Consequences

- プロジェクトにTypeScript（サーバー側のみ）が新たに加わる。
- RevenueCatの公式ドキュメント・チュートリアル（Webhook受信→Firestore書き込みパターン等）を
  ほぼそのまま踏襲できる見込み。
- サーバー側のロジック自体は小さい（画像受信・RevenueCat状態確認・Gemini呼び出し）ため、
  言語混在による保守コスト増は限定的と判断。

## Related

- [0121-serverless-proxy-for-app-provided-api-key](./0121-serverless-proxy-for-app-provided-api-key.md)
