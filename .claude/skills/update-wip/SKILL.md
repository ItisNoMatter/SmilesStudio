---
name: update-wip
description: Refresh .agent/claude-wip.md, this project's session-resume note, so the next Claude Code session (or a resumed one) can pick up work without re-deriving context. Use at the end of a substantial work session, after a batch of commits, or whenever the user asks to update the WIP notes.
---

# update-wip

`.agent/claude-wip.md` is this repo's session-resume memo: whoever (or whatever) picks up work
next reads it first. It is **not** a running log — git history and `docs/any-decision-record/`
already are the durable history. This file is a snapshot: current status, what just happened, what
is known-broken or half-done, and what to do next. Keep it lean; don't let it grow forever.

## Before writing, gather real facts — never guess

Read the current `.agent/claude-wip.md` first (if it doesn't exist yet, treat this as first-time
creation and use the "File shape" below as the template). Then check what's actually true right
now, don't trust the old file's claims:

- `git log --oneline <since the date/commit the current file references>..HEAD` and `git status` —
  what actually happened since the last update, and what's uncommitted.
- `ls docs/any-decision-record/` (if it exists) — any AnyDR files newer than the ones already
  listed in the current WIP note.
- Whether `CONTEXT.md` exists and what terms it currently defines.
- The actual module/file structure (`core-smiles`, `ui-compose`, `desktop-app` source trees) —
  regenerate the structure snapshot from what's really on disk, don't just carry the old one
  forward. Note anything the old file claimed that turned out to be stale (e.g. "TODO()" that's
  since been implemented, or a decision recorded as pending that's since landed in code).
- Run `./gradlew allTests` if code changed since the last update and the result isn't already known
  from this session, so the status line is accurate (green/red, not assumed).

## File shape

Match the structure already established in this file (don't invent a new one):

```markdown
# Claude Code WIP メモ (SmilesStudio ...)

最終更新: <today>

このファイルは...(the fixed preamble already in the file — keep it verbatim)

## ステータス: <one-line current phase>

<short paragraph: where things stand right now>

## 直近セッションでやったこと (<date range or session date>)

<bullets for what changed since the last update — replace the previous session's bullets here,
don't stack every session's bullets forever. If an item from the previous "直近セッション" section
is still unresolved/relevant, fold it into a longer-lived section (below) instead of just carrying
the bullet forward unchanged.>

## 確定した決定事項（AnyDRに記録済み）

<one line per AnyDR file: number, title, one-sentence summary, and — this is the part that goes
stale fastest — whether the decision is actually reflected in the current code yet>

## 現在のプロジェクト構成

<regenerated file/module tree, not carried over from the old file>

## ⚠️ コードと決定のズレ (omit this section if there is none)

<any AnyDR whose decision isn't yet implemented in code — this is the single most useful section
for a resuming session, don't skip checking for it>

## 既知の注意点（未対応・要フォローアップ）

<long-lived known issues; prune ones that were actually resolved>

## 次にやりそうなこと（未着手）

<short prioritized list; put the "コードと決定のズレ" items first if any exist>
```

## Pruning discipline

When a bullet from "既知の注意点" or a past "直近セッションでやったこと" has been fully resolved
(confirmed by an actual check, not assumption), remove it — don't leave a `~~strikethrough~~`
trail accumulating indefinitely. One past example in this file was kept as a `~~...~~ → 解消済み`
note; that's acceptable for a single notable reversal, but don't let this become the default — the
git history is the place for a full audit trail, not this file.

## After writing

Report a short diff-style summary of what changed in the note (new status, newly-flagged
code/decision gaps, updated next-steps) — don't just say "updated it." Don't commit automatically;
ask first, same as any other change to tracked files.
