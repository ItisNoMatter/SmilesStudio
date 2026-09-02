# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-02

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Shipaton 2026対応の方針決定・Issue化が完了。2つの独立したロードマップが並行稼働中

前回セッションでIssue #4（Ring検出）を実装しクローズ（コミット f7bc955 ）。今回セッションは、
セッション外でユーザーが決定したShipaton 2026（RevenueCatハッカソン、締切2026-09-30）参加方針を
AnyDR化し、Wayfinder方式で新しいマップIssue #11を作成した。**コード変更はなし**（ドキュメント・
GitHub Issueのみ）。デスクトップv1ロードマップ（Issue #1）とShipatonロードマップ（Issue #11）が
別々のマップとして並行稼働している点に注意。

## 直近セッションでやったこと（2026-09-02）

1. ユーザーがセッション外で決定したShipaton 2026対応方針（Android対応、Koog手描き構造式認識、
   BYOKハイブリッド課金、型安全OSS戦略等）をヒアリングし、懸念点を確認：
   - CLAUDE.mdの更新タイミング → 今回あわせて更新することで合意
   - 有料プランのAPIコスト上限（レート制限なし） → MVPとしてリスク受入で合意
   - OSSライセンス・具体的な価格帯は「未決定」として、AnyDR化せず未決定事項のまま記録
2. AnyDR 0027〜0032を記録（android-app追加／手描き認識MVP／Gemini採用／BYOKハイブリッド課金／
   型システムのOSS安全網戦略・FIR/K2先送り／AIレビューCI低優先度）。CLAUDE.mdも同時に更新
   （Android対応・Koog近日実装・課金方針を追記）。コミット c2475b7 。
3. Issue化の方法について選択肢を提示（既存マップIssue #1に追加 vs 新しい別マップ）。ユーザーは
   別マップを選択し、AnyDR 0033として記録（コミット a6d5c19 ）。
4. Wayfinderマップ [Issue #11「SmilesStudio: Shipaton 2026対応」](https://github.com/ItisNoMatter/SmilesStudio/issues/11) + 子Issue10件（#12〜#21）を作成。依存関係は
   #12(android-app追加)→#13(ui-composeモバイル調整)、#14(Koog+Gemini+BYOK呼び出し)は独立、
   #15(手描き認識UI)は#13・#14に加えて**クロスマップで既存Issue #7（MoleculeCanvas描画実装）にも
   blocked_by**、#16(BYOK設定画面)→#17(RevenueCat課金)→#18(Playストア申請)→#19(Devpost提出)、
   #20(AIレビューCI低優先度)・#21(iOS/年額プラン低優先度)は#15・#17完了後。
   現在のフロンティア（依存なし）は**#12・#14の2つ**。
5. マップIssue #1のFogセクションに、Shipaton方針転換とIssue #11への参照を追記。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0026`: 前回までに反映済み（詳細は割愛）。
- `0027`（android-app追加、iOS後回し）: **未実装**（Issue #12）。
- `0028`（手描き構造式パースMVP、既存テキスト入力パイプライン再利用）: **未実装**（Issue #14, #15）。
- `0029`（Gemini採用、Koogマルチプロバイダ維持）: **未実装**（Issue #14）。
- `0030`（BYOKハイブリッド課金、RevenueCat、コスト上限は未設計のまま受入）: **未実装**（Issue #16, #17）。
- `0031`（型システムをOSS安全網に、FIR/K2は将来構想）: 既存の型設計（0001/0003/0025/0026）に
  すでに体現されている。新規実装作業は発生しない。
- `0032`（AIレビューCI、低優先度）: **未実装**（Issue #20）。
- `0033`（Shipatonは別マップIssue #11で管理）: **実装済み**（Issue #11作成・運用中）。

## 現在のプロジェクト構成

コードは前回セッション（Issue #4完了時点）から変更なし。詳細は1つ前のWIPメモ版を参照、または
`core-smiles/src/commonMain/kotlin/com/smilestudio/core/`を直接確認。

```
docs/any-decision-record/  0001〜0033
CONTEXT.md                 5用語。変更なし
GitHub Issues（2マップ体制）:
  Issue #1  マップ「SmilesStudio v1: 最小構成でのユーザーリリース」（デスクトップ）
    #2,#3,#4 クローズ済み。フロンティア: #5「2Dレイアウト計算」
  Issue #11 マップ「SmilesStudio: Shipaton 2026対応」（モバイル・Koog・課金）【新規】
    フロンティア: #12「android-appモジュールの追加」、#14「Koog SDK導入とVision LLM呼び出し」
.git/hooks/pre-commit       コミットSHA表記チェック用の非ブロッキング警告（リポジトリ追跡外）
```

## ⚠️ コードと決定のズレ

- デスクトップ側（Issue #1）: `0019`後半（レイアウト計算本体）→ Issue #5。`0020`（Kekulé描画）
  → Issue #6。`0017`実描画・SMILES入力欄 → Issue #7, #8。`0021`パッケージング → Issue #9。
  `0023`CI → Issue #10。
- Shipaton側（Issue #11）: `0027`〜`0032`はすべて未実装（対応するIssue #12〜#21参照）。

## 既知の注意点（未対応・要フォローアップ）

1. `compose.runtime`等のバージョンカタログ経由アクセサが非推奨警告（優先度低、未着手）。
2. `Element`に`B`（ホウ素）がなく、芳香族小文字の`b`は未対応のまま。
3. `Molecule.rings`のDFS背後辺方式は縮合環・橋かけ環を正しく扱えない（AnyDR 0026）。
4. OSSライセンス（MIT/Apache 2.0）・有料プランの具体的価格・有料プランの使用上限（レート制限）は
   いずれも未決定のまま。決まり次第AnyDR化する。

## 次にやりそうなこと（未着手）

- **Issue #11（Shipaton 2026対応）の子Issueを優先する**（[AnyDR 0034](https://github.com/ItisNoMatter/SmilesStudio/blob/main/docs/any-decision-record/0034-prioritize-shipaton-map-over-desktop-v1.md)、2026-09-03決定）。
  デスクトップ側ロードマップ（Issue #1、フロンティアは
  [Issue #5「2Dレイアウト計算」](https://github.com/ItisNoMatter/SmilesStudio/issues/5)）は
  一時停止。
- 次の着手先はShipaton側のフロンティア2件のいずれか:
  [Issue #12「android-appモジュールの追加」](https://github.com/ItisNoMatter/SmilesStudio/issues/12)、
  [Issue #14「Koog SDK導入とVision LLM呼び出し」](https://github.com/ItisNoMatter/SmilesStudio/issues/14)。
  どちらから着手するかはユーザー指示待ち。
- Play Store申請は2026-09-20頃を目標（審査バッファ）。
