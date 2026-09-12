# 0091: Issue #16（BYOK設定画面）をIssue #15（手描き構造式認識UI）より先に実装する

- Date: 2026-09-09
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

`/grill-with-docs`でIssue #15（手描き構造式認識UI）の設計に着手したところ、`vision-recognition`
モジュールの`recognizeStructure(imageBytes, apiKey, provider)`が`apiKey`を必須引数として要求
する一方、そのAPIキーの入力元となるはずのIssue #16（BYOK設定画面）はまだ未着手であり、しかも
Issue #15はIssue #16を`blocked_by`として持っていない（`blocked_by: #7, #14, #13`のみ）ことが
判明した。#15を今のまま進める場合、APIキーの取得元を先に決める必要があった。

## Decision

Issue #16（BYOK設定画面）をIssue #15（手描き構造式認識UI）より先に実装する。当初提案した
「#15に一時的な最小限のAPIキー入力欄を追加し、#16で正式なセキュア実装に置き換える」という
アプローチは撤回する。

## Alternatives

- **#15に一時的なAPIキー入力欄を追加し#16で置き換える**: 提案時点では「#15を今すぐ進められる」
  「#16とスコープを分離できる」を利点として推奨したが、ユーザーの指摘で再検討した結果、
  #15と#16はいずれ両方とも実装が必要であり、一時UIとして捨てられる部分は`apiKey`用の一時
  変数程度に過ぎず、#15の実質的な作業（カメラ/ギャラリーUI・権限処理・認識呼び出し・結果
  反映・エラー表示）はどちらの順序でも一度しか作らない。「出戻りが少ない」という利点は
  実質的にほぼ無く、総工数の観点でBの方が優れていると判断し撤回した。
- **デモ用にGemini APIキーをビルドに埋め込む**: APKからのキー抽出・悪用リスクがあり、
  AnyDR 0029のBYOK方針とも矛盾するため不採用。

## Consequences

- Issue #15のグリルセッションを一旦中断し、Issue #16の設計に切り替える。Issue #16完了後に
  Issue #15の設計・実装に戻る。
- Issue #16は#15より実装スコープが大きい（プロバイダ選択・Android Keystore/
  EncryptedSharedPreferencesでの安全な保存・注意書き文言）ため、#15の着手が当初より後ろ倒し
  になる。

## Related

- [0028-handdrawn-structure-recognition-mvp](./0028-handdrawn-structure-recognition-mvp.md)
- [0029-gemini-default-vision-llm-provider](./0029-gemini-default-vision-llm-provider.md)
