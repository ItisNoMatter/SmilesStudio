# 0029: 開発・デモ用のデフォルトVision LLMプロバイダとしてGemini APIを採用する

- Date: 2026-09-02
- Status: Accepted
- Category: tooling
- Deciders: the user

## Context

[[0028]]の手描き構造式パース機能で使うVision LLMプロバイダの選定が必要だった。化学構造認識（OCSR）に特化した明確なベンチマークは存在しないため、精度の僅差よりも開発効率を優先する方針で検討した。Koogは複数のLLMプロバイダを共通インターフェース経由でサポートしており（例: `GoogleLLMClient(apiKey)`、`OpenAILLMClient(apiKey)`）、どのプロバイダをデフォルトにしてもBYOK（ユーザー自身のAPIキー利用）は可能な設計になっている。

## Decision

開発・デモ用途のデフォルト/推奨プロバイダとしてGemini API（Google AI Studio）を採用する。理由は、無料枠がありプロンプト調整・繰り返しテストのコストを気にせず開発できること、ネイティブマルチモーダル設計で画像入力の扱いが強いこと、Koogが`GoogleLLMClient(apiKey)`として標準サポートしていることの3点。BYOK自体はGeminiに限定せず、Koogが対応する任意のプロバイダのキーをユーザーが使える選択肢を残す。アプリ内のデフォルト案内・公式デモではGeminiを推奨するに留める。BYOK設定画面には、Claude Pro/ChatGPT Plusなどの「チャット向けサブスクリプション」とAPIキーは別物であり、APIキーは各社の開発者向けConsole（Anthropic Console、OpenAI Platform、Google AI Studio等）で別途発行・課金設定が必要である旨を明記する。

## Alternatives

- 精度ベンチマークによるプロバイダ選定: OCSRに特化した明確なベンチマークが存在しないため、精度差を根拠にした比較が意味を持たないと判断し、開発効率を優先する方針に切り替えた（不採用というより、判断基準自体を変更した）。

## Consequences

- ハッカソン期間中の開発コスト・試行錯誤の摩擦をGeminiの無料枠で抑えられる。
- BYOK設定画面に「チャットサブスクリプション ≠ APIキー」の注意書きを実装する必要がある（ユーザーの誤解・問い合わせ増加を防ぐため）。

## Related

- [0028-handdrawn-structure-recognition-mvp](./0028-handdrawn-structure-recognition-mvp.md)
