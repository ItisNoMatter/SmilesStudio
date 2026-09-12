# 0097: BYOK設定画面はAlertDialogとして実装する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[[0095]]で「その他メニュー」から「APIキー設定」項目にアクセスできるようにすると決めたが、
タップした先の画面構造（既存の「このアプリについて」と同じ`AlertDialog`か、新しい専用画面か）
は未定だった。

## Decision

BYOK設定画面は`AlertDialog`として実装する。「このアプリについて」と同じパターンを踏襲する。
新しい専用画面（フルスクリーン、Navigation Component/Compose Navigation導入）は採用しない。

## Alternatives

- 新しい専用画面（フルスクリーン）: 画面が広く使え、将来Issue #28（複数プロバイダ対応）で
  プロバイダ選択UI等が増えても窮屈にならない利点はあるが、現状の`android-app`にはNavigation
  Component/Compose Navigationが導入されておらず、Issue #16単体のスコープに対してナビゲー
  ション基盤の新規追加は過剰と判断し不採用とした。

## Consequences

- ナビゲーション基盤を新規に追加せず、既存の「このアプリについて」ダイアログと同じ実装
  パターンを踏襲できる。
- 現状の入力項目（APIキー1個＋免責文言＋保存ボタン）には収まるが、Issue #28で将来プロバイダ
  選択UIを追加する際、`AlertDialog`の情報量が増えすぎるようであれば専用画面化を再検討する
  余地がある。

## Related

- [0095-byok-settings-entry-point-overflow-menu](./0095-byok-settings-entry-point-overflow-menu.md)
- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
