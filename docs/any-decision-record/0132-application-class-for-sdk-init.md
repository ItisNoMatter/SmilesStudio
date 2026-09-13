# 0132: Firebase Auth・RevenueCatの初期化は新設する`Application`クラスで行う

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #35で、Firebase Auth匿名サインイン・RevenueCat初期化のコードをどこに置くか検討した。
現状`android-app`には専用の`Application`クラスがなく、`MainActivity.kt`もシンプルな構成
だった。

## Decision

`Application`クラスを新設し、`onCreate()`でFirebase Auth匿名サインイン・RevenueCat初期化を
行う。

## Alternatives

- `SmilesStudioApp()`コンポーザブル内で`LaunchedEffect(Unit)`を使って初期化する: 新しい
  クラスが不要で既存ファイル内で完結する利点はあるが、アプリ全体・Composeより下位レイヤーの
  関心事をUIコンポーザブルに持ち込むことになり、将来UI構造を変更した際に初期化コードを
  誤って動かしてしまうリスクがあるため不採用とした。

## Consequences

- 新しい`Application`クラス（例: `SmilesStudioApplication`）を追加し、
  `AndroidManifest.xml`の`<application>`タグに`android:name`を登録する必要がある。
- Composeのライフサイクル（画面回転等によるComposable再生成）の影響を受けない、安定した
  初期化処理になる。

## Related

- [0130-anonymous-auth-at-app-launch](./0130-anonymous-auth-at-app-launch.md)
- [0131-subscription-status-via-listener](./0131-subscription-status-via-listener.md)
