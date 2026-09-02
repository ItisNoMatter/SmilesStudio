# Claude Code WIP メモ (SmilesStudio プロジェクト基盤構築)

最終更新: 2026-09-03

このファイルはClaude Codeとの作業セッションが中断された際の再開用メモ。
セッション再起動後は、まずこのファイルを読んでから作業を再開すること。

## ステータス: Issue #5（2Dレイアウト計算）実装・クローズ済み。次はIssue #6かIssue #14

前回セッションでShipaton方針更新（課金B/C案・三層防御OSS戦略）をAnyDR 0035〜0038として記録
（コミット 6828338 ・ d7ecf87 ）。今回セッションは3つの作業を行った。
(1) BuildInPublicツイート作成Skill（`buildinpublic-tweet`、グローバル配置）を`/grill-with-docs`で
設計・実装（AnyDR 0039〜0044）。
(2) 実際にAnyDR 0028のツイートを試作し、Skill出力の品質フィードバック（文字数に余裕があっても
自然な文章を優先すべき）をSKILL.mdと個人メモリに反映。
(3) Issue #5（2Dレイアウト計算）を`/grill-with-docs`で設計しTDD実装、クローズ（AnyDR 0045〜0047、
コミット 899571e ）。

**⚠️ 注意**: 作業ツリーに、このセッションが作成していない未コミットの変更がある
（`CLAUDE.md`の受賞戦略・BuildInPublic運用に関する追記、`docs/any-decision-record/0048`・`0049`）。
別セッションまたはユーザーによる作業と判断し、あえて触れていない。次セッションで状況を再確認し、
必要なら経緯を確認してからコミットすること。

## 直近セッションでやったこと（2026-09-03）

1. BuildInPublicツイート作成Skillの設計・実装（`/grill-with-docs`）:
   - AnyDR 0039〜0044を記録（グローバル配置／英語版AnyDRは`docs/any-decision-record/en/`に格納・
     欠番許容／明示的呼び出しのみ／英語のみ／ハッシュタグ`#Shipaton #BuildInPublic`はShipaton公式
     ルールで確認済み／クリップボードへのベストエフォートコピー）。コミット c6b1043 。
   - `~/.claude/skills/buildinpublic-tweet/SKILL.md`を実装（グローバル、このリポジトリには
     含まれない）。SmileStudio側CLAUDE.mdにツイート運用規約セクションを追記。
2. `/buildinpublic-tweet 0028`で実際にツイート文を試作。
   - `docs/any-decision-record/en/0028-handdrawn-structure-recognition-mvp.md`をオンデマンド生成
     （コミット 13427e8 ）。
   - 最初のドラフトが280字制限に対して余裕があったにもかかわらず、電報調の断片的な文章になり
     「なぜその決定をしたか」が本文から失われ`link`任せになっていたとユーザーから指摘。自然な
     文章を優先するようSKILL.md（STEP 4）を修正し、個人メモリ`feedback_dont_overcompress_for_char_limits`
     にも記録。
3. Issue #5（core-smiles: 2Dレイアウト計算）を`/grill-with-docs`で設計:
   - AnyDR 0045: `computeLayout(molecule): Map<AtomId, Point2D>`を独立関数として実装（`Molecule`の
     プロパティにはしない。レイアウトは構造そのものでなく「描画のための一つの解釈」のため）。
   - AnyDR 0046: 鎖状部分はジグザグ配置（結合角を交互に反転）。
   - AnyDR 0047: 分岐点は対称なY字分岐（入ってきた結合を基準に±120度）。`Bond`に由来を示す
     フラグを追加する複雑さを避けるため。
   - TDDで実装（`Point2D.kt`・`Layout.kt`・`LayoutTest.kt`新規）。環（`Molecule.rings`）は正多角形、
     置換基は環の中心から外向きに配置。テストは正確な座標一致ではなく結合長・角度を許容誤差付きで
     検証する方式にし、この方式のおかげで「環の置換基が外向きでなく真横に配置される」バグを
     実装中に発見・修正できた（`placeOutgoing`の±120度オフセットをそのまま流用していたのが原因）。
   - コミット 899571e 。Issue #5にコメント＋クローズ、マップIssue #1を更新。

## 確定した決定事項（AnyDRに記録済み）

- `0001`〜`0038`: 前回までに反映済み（詳細は割愛）。
- `0039`〜`0044`（BuildInPublicツイート作成Skillの設計）: **実装済み**（グローバルSkill、
  コミット c6b1043 ）。
- `0045`〜`0047`（2Dレイアウト計算の設計）: **実装済み**（Issue #5、コミット 899571e ）。
- `0048`・`0049`: このセッションでは未確認・未着手（上記「注意」参照、別セッションの可能性）。

## 現在のプロジェクト構成

```
core-smiles/src/commonMain/kotlin/com/smilestudio/core/
  Point2D.kt         【新規】data class(x, y) + plus/minus/times演算子
  Layout.kt          【新規】fun computeLayout(molecule): Map<AtomId, Point2D>
                     鎖=ジグザグ(120度交互反転)、分岐=Y字(±120度対称)、環=正多角形+置換基は外向き
  （Ring.kt, Molecule.kt等、Issue #4完了時点から変更なし）
core-smiles/src/commonTest/kotlin/com/smilestudio/core/
  LayoutTest.kt       【新規】7件。結合長・角度を許容誤差付きで検証（厳密な座標一致ではない）

docs/any-decision-record/  0001〜0047（0048・0049は別セッション作成、内容未確認）
                            en/0028-handdrawn-structure-recognition-mvp.md（オンデマンド生成済み）
~/.claude/skills/buildinpublic-tweet/SKILL.md  【新規、グローバル】このリポジトリには含まれない

GitHub Issues（2マップ体制、Issue #11優先＋#5〜#7例外）:
  Issue #1  マップ「SmilesStudio v1: 最小構成でのユーザーリリース」
    #2,#3,#4,#5 クローズ済み。フロンティア: #6「芳香族結合のKekulize変換」
  Issue #11 マップ「SmilesStudio: Shipaton 2026対応」（優先中、子Issue11件）
    #12 クローズ済み。フロンティア: #14「Koog SDK導入とVision LLM呼び出し」
    （#13は#5〜#7完了待ちで実質保留、#6・#7が残っている）
.git/hooks/pre-commit       コミットSHA表記チェック用の非ブロッキング警告（リポジトリ追跡外）
```

## ⚠️ コードと決定のズレ

- デスクトップ側（Issue #1）: `0020`（Kekulé描画）→Issue #6（未実装）。`0017`実描画・SMILES入力欄
  →Issue #7, #8（未実装）。`0021`パッケージング→Issue #9。`0023`CI→Issue #10。
- Shipaton側（Issue #11）: `0028`・`0029`（Koog連携）→Issue #14。`0036`（B/C課金）→Issue #16,
  #17。`0037`のテストハーネス層→Issue #22。いずれも未実装。

## 既知の注意点（未対応・要フォローアップ）

1. `compose.runtime`等のバージョンカタログ経由アクセサが非推奨警告（優先度低、未着手）。
2. `Element`に`B`（ホウ素）がなく、芳香族小文字の`b`は未対応のまま。
3. `Molecule.rings`のDFS背後辺方式・`computeLayout`の固定角度配置は、いずれも縮合環・橋かけ環を
   正しく扱えない（AnyDR 0026・0019）。v1スコープでは問題ない。
4. `computeLayout`の3方向以上の分岐（3+outgoing）・環の2箇所以上の置換基は、フォールバック実装
   のみでテストカバレッジがない（v1文法スコープでは基本的に発生しない想定）。
5. 有料プランの具体的価格・使用上限（レート制限）は未決定のまま。
6. このマシンのAndroid SDKは`cmdline-tools`を手動追加済み。AVD`SmileStudio_Test`
   （API 36, Pixel 6）が1件作成済み。

## 次にやりそうなこと（未着手）

- **Issue #6「core-smiles: 芳香族結合のKekulize変換」**（Issue #5完了により依存解消。AnyDR 0035の
  レンダリングパイプライン優先順序の次のステップ）。
- 並行して着手可能: [Issue #14「Koog SDK導入とVision LLM呼び出し」](https://github.com/ItisNoMatter/SmilesStudio/issues/14)
  （依存なし）。
- どちらから着手するかはユーザー指示待ち。
- 未コミットの`CLAUDE.md`変更・AnyDR 0048/0049の扱いをユーザーに確認する。
- Play Store申請は2026-09-20頃を目標（審査バッファ）。
