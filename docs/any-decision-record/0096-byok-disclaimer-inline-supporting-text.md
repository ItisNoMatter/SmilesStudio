# 0096: APIキーの免責文言は入力欄の直下に常時表示する

- Date: 2026-09-12
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

AnyDR 0029で「チャット向けサブスクリプション（Claude Pro/ChatGPT Plus等）とAPIキーは別物」
という免責文言をBYOK設定画面に表示すると決めていたが、その表示方法（常時表示か、ユーザー操作
で開く形か）は未定だった。

## Decision

免責文言はAPIキー入力欄の直下に、`supportingText`のような形で常時表示する。「？」アイコンや
「詳細」リンクをタップした時だけ表示するダイアログ/ツールチップ形式は採用しない。

## Alternatives

- 「？」アイコンや「詳細」リンクをタップした時だけ表示: 通常時の画面はすっきりするが、
  ユーザーが自発的にタップしないと注意書きに気づかない可能性があり、AnyDR 0029が意図する
  「誤解・問い合わせ防止」という目的を果たしにくいため不採用とした。ユーザー自身も「APIキーを
  入力するときにユーザーの目に入れば良い」と常時表示を支持した。

## Consequences

- `HomeContent.kt`のSMILESエラー表示（`OutlinedTextField`の`supportingText`）と同様のパターンを
  踏襲でき、実装が一貫する。

## Related

- [0091-byok-settings-before-recognition-ui](./0091-byok-settings-before-recognition-ui.md)
- [0029-gemini-default-vision-llm-provider](./0029-gemini-default-vision-llm-provider.md)
