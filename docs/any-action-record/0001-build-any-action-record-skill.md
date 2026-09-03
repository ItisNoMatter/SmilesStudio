# 0001: any-action-record Skillを設計・実装・検証した

- Date: 2026-09-03
- Related AnyDR: 0050, 0051, 0052, 0053, 0054
- Related Issue: なし

## Objective

AnyDR（Any Decision Record、「なぜその決定に至ったか」を記録する）と対を成す記録媒体として、
「何をして、何が起きたか」を記録するAny Action Record（AnyAR）のSkillを新設する。加えて、作業が
ひと段落したタイミングで自動発火する仕組みも備える。

## Action

`/grill-with-docs`でAnyARの主目的・自動発火方式・採番方式・テンプレート構成を設計し、AnyDR
0050〜0054として記録した（主目的＝BuildInPublic向け学び共有材料、ハードフック検知＋Claude判断の
ハイブリッド発火方式、AnyDRとは独立した採番空間、Objective/Action/Result/Reflectionsテンプレート、
`git commit`実行後毎回のフック発火条件）。

その後、実装に着手した。

- `~/.claude/skills/any-action-record/SKILL.md`（グローバル）を作成。
- `update-config` Skill経由で`~/.claude/settings.json`にPostToolUseフックを追加。当初は
  `if: "Bash(git commit*)"`という許可ルール構文でのフィルタリングを試みたが、pipe-testでは
  検出できない不具合があり、実際にBashツールを呼び出して検証したところ、`echo`のような無関係な
  コマンドでも無条件に発火してしまうことが判明した。
- 対策として、フック本体は`matcher: "Bash"`のみに単純化し、標準入力の`tool_input.command`を
  正規表現でチェックする`~/.claude/hooks/anyar-git-commit-reminder.py`を新規作成。`git commit`
  で始まる（または`;`/`&`/`|`/改行の後に続く）場合のみAnyARリマインドのJSONを出力する方式に
  切り替えた。
- ポジティブ・ネガティブ双方のケースを実際のBashツール呼び出しでライブ検証した（`echo`等の
  無関係なコマンドでは発火せず、`git commit --help`という実コミットを伴わない安全なコマンドでは
  正しく発火することを確認）。

## Result

フックが意図通りに機能することを確認した。AnyDR 0050〜0054をコミット（cadbc87）。CLAUDE.mdや
AnyDR 0048/0049/0055等、並行して外部で進行中の変更には一切触れず、意図的にコミット対象から
除外した。

## Reflections

`update-config` Skillが例示する許可ルール構文ベースの`if`フィルタは、ドキュメント通りには機能
しない場合があった（実際には無条件に発火していた）。pipe-testで単体のJSONを通しただけでは
この不具合は検出できず、`settings.json`に反映した状態で実際にBashツールを呼び出してポジティブ・
ネガティブ両方のケースを確認して初めて発覚した。「動くはずのコマンドを一度試して終わり」ではなく、
設定を反映した後に実ツール呼び出しで再検証するステップを省略しないことの重要性を再認識した。
