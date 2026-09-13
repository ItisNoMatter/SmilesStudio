# 0130: Firebase匿名認証はアプリ起動時に必ず行う

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #35（Android Firebase Auth・RevenueCat SDK統合）で、匿名認証（AnyDR 0123）を
いつ行うか（アプリ起動時、または画像認識機能を初めて使うタイミング）を検討した。

## Decision

アプリ起動時に必ず匿名認証でサインインする。

## Alternatives

- 画像認識機能を初めて使うタイミングで遅延初期化する: 実際に機能を使うユーザーだけが
  ネットワーク処理を発生させる利点はあるが、初回利用時の体感待ち時間の増加、実装の
  複雑化に加え、無料/有料ユーザーの比率をRevenueCatダッシュボードで正確に把握するという
  目的（ユーザーの要望）を損なう（一度も画像認識機能を使わないユーザーがRevenueCatに
  登録されない）ため不採用とした。

## Consequences

- アプリ起動時に軽量な匿名認証リクエストが常に発生する（実害は小さい）。
- BYOKのみを使うユーザーも含め、全ユーザーがRevenueCatに登録され、無料/有料比率の把握が
  正確になる。

## Related

- [0123-firebase-auth-anonymous-as-user-identity](./0123-firebase-auth-anonymous-as-user-identity.md)
