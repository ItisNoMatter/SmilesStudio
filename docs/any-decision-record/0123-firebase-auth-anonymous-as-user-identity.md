# 0123: ユーザー識別はFirebase Authenticationの匿名認証UIDを使い、RevenueCatのapp_user_idと統一する

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

AnyDR 0121・0122で新設するサーバーレス関数が、無料枠カウンター・購読状態を参照する際に
「どのユーザーか」を識別する方法が未定だった。

## Decision

Firebase Authenticationの匿名認証でユーザーごとにUIDを発行し、そのUIDをRevenueCatの
`app_user_id`としても使う。

## Alternatives

- 端末固有のランダムIDを自前で生成・ローカル保存してユーザー識別子として使う: Firebase Auth
  という新規依存を避けられる利点はあるが、RevenueCatの`app_user_id`との紐付けを自前管理する
  必要がありサーバー側の検証ロジックが複雑になる上、なりすまし防止の仕組みが標準で付いて
  こないため不採用とした。

## Consequences

- Android側にFirebase Authentication SDKの追加が必要になる。
- RevenueCatのWebhookで届く購読情報と、アプリからのリクエストを同じキー（UID）で突き合わせ
  られるようになる。
- 匿名認証のためログインUIは不要で、ユーザー体験への影響はない。

## Related

- [0121-serverless-proxy-for-app-provided-api-key](./0121-serverless-proxy-for-app-provided-api-key.md)
- [0122-serverless-proxy-firebase-nodejs](./0122-serverless-proxy-firebase-nodejs.md)
