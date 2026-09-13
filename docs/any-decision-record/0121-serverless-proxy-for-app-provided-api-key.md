# 0121: 開発者のGemini APIキーはサーバーレス関数経由で保持し、アプリに埋め込まない

- Date: 2026-09-13
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

Issue #17の設計中、BYOK以外の全ケース（無料枠利用中のユーザー、および「アプリのAI機能を
購読する」有料ユーザー）で、アプリが開発者自身のGemini APIキーを使ってGeminiを呼ぶ必要が
あるにもかかわらず、そのキーをどこに置くかがこれまで一切決まっていなかったことが判明した。

## Decision

軽量なサーバーレス関数（Cloud Functions等）を新設し、開発者のAPIキーはサーバー側にのみ
保持する。アプリはこのサーバーレス関数を経由してGeminiを呼び出し、関数側でRevenueCatの
購読状態・利用回数カウンターを検証してから実際にGemini APIを呼ぶ。

## Alternatives

- 開発者のAPIキーをアプリに埋め込み、クライアント側のカウンター/購読チェックのみで制御する:
  バックエンド不要で実装が最速という利点はあったが、(1) APKからキーを抽出・悪用される
  リスク、(2) 無料枠キーを全ユーザーで共有する場合、正規利用だけでGoogle側のレート制限
  （RPM/RPD）に達しうる、(3) 安定運用のため有料キーに切り替えた場合は抽出時の金銭リスクが
  青天井になる、(4) クライアント側の制御は原理的に改ざん可能、という4つの問題があり不採用
  とした。
- 「アプリのAI機能を購読する」という価値提案自体を撤回し、購読してもBYOKを必須のままにする:
  問題自体が消え実装は最もシンプルになるが、Issue #17・AnyDR 0036が想定していた主要な
  価値提案を失う大きな方針転換になるため、ユーザーの判断で不採用とした。

## Consequences

- 「サーバーなし」だった本アプリの構成に、新しいバックエンドインフラを追加する必要がある。
- サーバー側カウンターにすることで、Issue #32（ローカルカウンターの再インストールでの
  回避）も同時に解決できる見込み。
- 残り期間（Play申請目標09-20まで1週間）に対するスケジュールリスクを負う判断。
- 具体的なサーバーレス関数のプラットフォーム選定・API設計・RevenueCatとの連携方式は
  別途詰める必要がある。

## Related

- [0117-free-tier-counter-local-storage](./0117-free-tier-counter-local-storage.md)
- [0036-plan-b-c-monetization-supersedes-byok-hybrid](./0036-plan-b-c-monetization-supersedes-byok-hybrid.md)
