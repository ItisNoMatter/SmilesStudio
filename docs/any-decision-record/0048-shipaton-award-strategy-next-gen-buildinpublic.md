# 0048: Shipaton受賞戦略をNext Gen + #BuildInPublicの二枚看板に絞る

- Date: 2026-09-03
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

Shipaton 2026（[[0027]]）参加にあたり、21ある受賞カテゴリのうちどれを狙うかが未定だった。公式ルール（Devpost）とカテゴリ一覧を調査した結果、グランプリはトラクション・成長指標（インストール数・売上・保持率）が審査基準でソロ開発・短期間の開発体制には不利、Kotlin賞（JetBrains）はKMP+Koogという技術選定と評価軸が自然に一致するものの「iOS/Android両対応」が応募要件に含まれており、[[0027]]・[[0034]]で決めたAndroid専任方針と衝突することが判明した。一方、Next Gen Award（学生向け）はストア公開・有料開発者アカウントが不要で、OSSライセンス付き公開リポジトリとデモ動画のみで応募可能であることが分かり、いのまが現在学生であるため応募資格を満たす。#BuildInPublic Awardは既存のAnyDR運用・BuildInPublicツイートSkill（[[0039]]〜0044）をそのまま活用できる。

## Decision

iOS対応は復活させず、Android専任のまま（[[0027]]・[[0034]]を維持）、Next Gen Award + #BuildInPublic Awardの二枚看板に絞って狙う。Kotlin賞は狙わない。グランプリ・Design賞・Peace賞も積極的には狙わない。

## Alternatives

- Kotlin賞を狙ってiOS対応を復活させる: KMP+Koogという技術選定と評価軸が一致する魅力はあったが、[[0027]]・[[0034]]で確定したAndroid専任方針を覆すコストに見合わないと判断し不採用。
- グランプリを本命に据える: 賞金最大（$100k）だが、審査基準がトラクション・成長指標でありニッチな化学ツール＋ソロ開発＋短期間ではマーケティング抜きで数字を作るのが困難と判断し不採用。

## Consequences

- Next Gen Awardの応募条件はストア公開を必須としないため、Google Play Console審査待ちという外部リスクが受賞そのものの前提条件ではなくなる。ただし他カテゴリでは引き続きストア公開が前提と見られるため、Play申請準備（[Issue #18](https://github.com/ItisNoMatter/SmilesStudio/issues/18)）は引き続き必要。
- RevenueCat SDK連携（[Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17)）はNext Gen Awardでも必須要件のため、優先度は変わらない。
- OSSライセンス（[[0038]]）・三層防御フレーミング（[[0037]]）は既にNext Gen Awardの応募要件を満たす方向で進んでいたため、追加作業は主にデモ動画（[Issue #19](https://github.com/ItisNoMatter/SmilesStudio/issues/19)）に集約される。

## Related

- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
- [0034-prioritize-shipaton-map-over-desktop-v1](./0034-prioritize-shipaton-map-over-desktop-v1.md)
- [0037-three-layer-defense-oss-strategy](./0037-three-layer-defense-oss-strategy.md)
- [0038-mit-license](./0038-mit-license.md)
- [0039-global-buildinpublic-tweet-skill](./0039-global-buildinpublic-tweet-skill.md)
