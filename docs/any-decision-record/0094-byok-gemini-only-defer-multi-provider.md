# 0094: Issue #16のBYOK設定画面は今回Geminiのみ実装し、プロバイダ選択UIは作らない

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #16のスコープには「プロバイダ選択（Koogが対応する任意のプロバイダ）」とあるが、現状
`vision-recognition`モジュールの`LLMProvider` enumには`GOOGLE_GEMINI`しかなく、
`recognizeStructure`関数もGemini用の分岐しか持たない。複数プロバイダを今回のIssue #16で
実際に実装するかどうかを検討した。

## Decision

今回はGeminiのみを実装し、プロバイダ選択UI（ドロップダウン等）は作らない。複数プロバイダ対応
は別Issueとして切り出し、将来実装する。

## Alternatives

- UIだけプロバイダ選択（ドロップダウン等）を用意し、Gemini以外は「近日対応」として無効化
  表示する: 将来の拡張を見越したUI構造を先に作れる利点はあるが、実装のないUI要素を作ることに
  なり実質的な価値が薄く、「対応予定」表示自体もいつ対応するか未定で中途半端になりやすいため
  不採用とした。

## Consequences

- `vision-recognition`モジュールに新しいKoogクライアント依存（OpenAI等）を追加する必要がなく、
  Issue #16のスコープが小さく済む。AnyDR 0029の「開発・デモ用途のデフォルトはGemini」という
  方針とも整合する。
- 複数プロバイダ対応は別Issueとして起票し、`vision-recognition`側（`LLMProvider` enum拡張・
  `recognizeStructure`の分岐追加）とBYOK設定画面側（プロバイダ選択UI）の両方に後日手を入れる
  ことになる。

## Related

- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
- [0092-api-key-storage-android-keystore](./0092-api-key-storage-android-keystore.md)
- [0029-gemini-default-vision-llm-provider](./0029-gemini-default-vision-llm-provider.md)
