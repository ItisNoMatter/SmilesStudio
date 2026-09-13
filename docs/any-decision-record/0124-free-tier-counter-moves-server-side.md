# 0124: 無料枠カウンターをサーバー側（Firestore）に移す（0117を撤回）

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

AnyDR 0117で「無料枠カウンターはローカル保存のみとする」と決めたが、これは当時
「バックエンドを持たない本アプリでは真に信頼できるサーバーサイドのカウント管理が
実現できない」という前提に基づいていた。その後AnyDR 0121・0122・0123で、開発者の
Gemini APIキーを保持するサーバーレス関数（Firebase Cloud Functions + Firestore想定、
Firebase Auth匿名認証によるユーザー識別）を新設することが決まり、この前提が崩れた。

## Decision

AnyDR 0117を撤回し、無料枠カウンターはサーバー側（Firestore、AnyDR 0123のUIDに紐付け）で
管理する。

## Alternatives

（AnyDR 0117時点での代替案の検討はAnyDR 0117自体を参照。本AnyDRは前提の変化による方針
転換であり、新たな代替案の比較は行っていない。）

## Consequences

- カウンター管理ロジックを、クライアント側（`SharedPreferences`）からサーバー側
  （Firestore）に実装し直す必要がある。
- Issue #32（ローカルカウンターの再インストールでの回避）が、この方針転換により解消される
  見込みのため、Issue #32はcloseの候補になる。
- `ApiKeyStore`（BYOK用、Issue #16）はローカル保存のままで変更なし。今回の方針転換は
  無料枠カウンターのみに関するもの。

## Related

- [0117-free-tier-counter-local-storage](./0117-free-tier-counter-local-storage.md)
- [0121-serverless-proxy-for-app-provided-api-key](./0121-serverless-proxy-for-app-provided-api-key.md)
- [0123-firebase-auth-anonymous-as-user-identity](./0123-firebase-auth-anonymous-as-user-identity.md)
