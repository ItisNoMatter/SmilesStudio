# 0076: 新規モジュール名は`vision-recognition`とする

- Date: 2026-09-05
- Status: Accepted
- Category: naming
- Deciders: the user, Claude Code

## Context

[AnyDR 0072](./0072-koog-in-new-shared-module.md)で決めた新規共通KMPモジュールの具体的な名称を検討した。

## Decision

新規モジュール名は`vision-recognition`とする。

## Alternatives

- `structure-recognition`: 「構造式認識」という化学ドメインの成果物を表すが、化学ドメイン用語に寄りすぎて`core-smiles`との役割の違いが名前だけでは伝わりにくいため不採用。
- `handdrawn-recognition`: AnyDR 0028の「手描き構造式認識」という用語にもっとも忠実だが、将来手描き以外の入力経路（例: 既存画像のアップロード）が増えた場合に名前が実態と合わなくなるため不採用。
- `smiles-recognition`: 出力（SMILES文字列）に着目した命名だが、同様に化学ドメイン用語に寄りすぎるため不採用。
- `koog-client`: 使用技術（Koog）そのものを表すが、Koogという特定技術への依存を名前に固定してしまい、将来技術選定が変わった場合に名前が陳腐化するため不採用。

`vision-recognition`は、技術（Koog/Gemini等）にもドメイン用語にも寄りすぎず、「画像を入力してテキストを得る」という機能そのものを表しており、将来プロバイダやユースケースが変わっても名前が陳腐化しにくいと判断した。

## Consequences

- 実装時、`settings.gradle.kts`に`vision-recognition`モジュールを登録する。

## Related

- [0072-koog-in-new-shared-module](./0072-koog-in-new-shared-module.md)
