# 0082: MoleculeEditorのTextFieldを画面下部に固定し、IMEパディングもスコープに含める

- Date: 2026-09-06
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

[AnyDR 0081](./0081-canvas-edge-to-edge-textfield-inset.md)は「`TextField`は画面上端に固定、
`MoleculeCanvas`はedge-to-edge」という現行レイアウトを前提に、上方向のみのインセット対応を
決めていた。その後の`/grill-with-docs`セッション中、ユーザーから「テキストフィールドが画面
上部にあるのはユーザー体験が悪い」という指摘があり、この前提そのものを見直すことになった。

**本AnyDRは[0081](./0081-canvas-edge-to-edge-textfield-inset.md)の前提（TextField上部固定）を
撤回し、新しいレイアウト方針を決定するものである**（このプロジェクトの運用では既存AnyDRを
編集・削除せず、新しいAnyDRで上書きし相互にリンクする）。

## Decision

`ui-compose`の`MoleculeEditor`自体のレイアウトを、`TextField`を画面下部に固定する構成に変更
する。`MoleculeCanvas`を上部（画面のほとんど）に配置し、`TextField`をその下に固定する
（チャットアプリの入力欄に近い配置）。この変更は`ui-compose`の共有Composableに対して行い、
`desktop-app`・`android-app`の両方に適用する。

あわせて、[0081](./0081-canvas-edge-to-edge-textfield-inset.md)撤回時に一旦スコープ外とした
IMEパディング（`Modifier.imePadding()`）も今回のスコープに含める。下部固定の`TextField`は
ソフトウェアキーボード表示時にキーボードの上に追従して見える必要があり、画面上端固定だった
旧レイアウトとは異なりIME対応が実質的に必須になったため。

## Alternatives

- レイアウト変更をandroid-app専用の別Composableとして分離し、`MoleculeEditor`（desktop-app用）
  は現状維持する: desktop-appの見た目に一切影響を与えず、[0080](./0080-safe-area-in-android-app-call-site.md)
  （Android固有の関心事はandroid-appに集約）の方針とも一貫するという利点があった。しかし
  `TextField`・エラー表示・`MoleculeCanvas`という同一構成をほぼ複製することになり、コードの
  重複と将来の二重メンテナンスコストが生じる。「入力欄は下部の方が使いやすい」というのは
  プラットフォーム非依存のUI原則であり、インセット関連の値自体はdesktopでは無害なno-opに
  フォールバックするため、共有コンポーネントに変更を入れる方が合理的と判断し不採用。

## Consequences

- `desktop-app`の見た目も「Canvas上部＋入力欄下部」に変わる。デスクトップの一般的な慣習
  （入力欄が上部・独立ツールバー）とは異なるレイアウトになるが、ユーザーの明示的な判断として
  許容する。
- [0081](./0081-canvas-edge-to-edge-textfield-inset.md)で決めた「`TextField`上方のみインセット」
  という具体的な実装方針は本AnyDRにより無効になる。新しい実装では`TextField`の**下方向**の
  セーフエリア/ジェスチャーインセットと、IMEパディングの両方を考慮する必要がある。
- `MoleculeCanvas`は引き続き画面上部でedge-to-edge（ステータスバーの裏側まで描画）のままとする
  想定（[0081](./0081-canvas-edge-to-edge-textfield-inset.md)のこの部分の判断自体は維持）。

## Related

- [0081-canvas-edge-to-edge-textfield-inset](./0081-canvas-edge-to-edge-textfield-inset.md)（本AnyDRが前提を撤回・置き換え）
- [0080-safe-area-in-android-app-call-site](./0080-safe-area-in-android-app-call-site.md)
- [0079-safe-area-tracked-as-own-issue](./0079-safe-area-tracked-as-own-issue.md)
