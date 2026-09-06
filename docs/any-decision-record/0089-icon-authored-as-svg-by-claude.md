# 0089: アプリアイコンはClaudeがSVGで作成する

- Date: 2026-09-06
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

[AnyDR 0087](./0087-store-listing-assets-own-issue.md)で切り出したストア掲載素材Issueの中で、
アプリアイコンをどう用意するかを決める必要があった。

## Decision

アプリアイコンは、アプリのテーマカラー（purple系、AnyDR 0080〜0083等のMaterial 3
Expressiveデザインで確定した配色）と分子構造をモチーフにした幾何学的なアイコンを、
ClaudeがSVGで作成する。AndroidのアダプティブアイコンやPlay Store掲載用の高解像度
アイコンに必要な各サイズへの変換もあわせて行う。

## Alternatives

- ユーザー自身が外部デザインツールやAI画像生成で別途用意する: デザインの自由度が
  高いという利点があったが、Shipaton締切が迫る中でユーザーの追加作業を増やすことになり、
  シンプルな幾何学的アイコンであればSVGでの自作で十分と判断し不採用。

## Consequences

- アイコンのデザインクオリティはSVGでの幾何学的表現の範囲に限られる（写真的・複雑な
  イラストレーションは対象外）。
- Android各解像度向けのアダプティブアイコン・Play Store用512x512高解像度アイコンへの
  変換作業が発生する。

## Related

- [0087-store-listing-assets-own-issue](./0087-store-listing-assets-own-issue.md)
