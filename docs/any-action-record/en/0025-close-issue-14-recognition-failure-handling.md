# 0025: Closing Issue #14 and fixing a gap in RecognitionResult.Failure

- Date: 2026-09-06
- Related AnyDR: 0077, 0078
- Related Issue: #14

## Objective
Review whether Issue #14 (Koog SDK integration and Vision LLM call implementation) could be
closed, and close the gap if it wasn't ready yet. In particular, the user asked that API limit
(rate-limit) errors be turned into an explicit `RecognitionResult.Failure`.

## Action
*   Cross-checked Issue #14's scope (Koog SDK integration, Gemini call, BYOK, MVP of one image →
    one SMILES candidate) against the implemented code. The surface-level scope was met, but a gap
    was found against [AnyDR 0073](../../any-decision-record/0073-recognition-result-sealed-type.md)'s
    decision (that Failure should represent things like network errors): the implementation let
    exceptions from the LLM call propagate uncaught instead.
*   The user chose "implement first, don't close yet." Added exception-to-Failure conversion in
    `runRecognition` via TDD (re-throwing `CancellationException` to preserve it).
*   The user then asked for rate limits to be handled explicitly. Presented implementation option A
    (string matching) vs. option B (restructuring `RecognitionResult` into a typed error) in a
    Pros/Cons format.
*   At the user's direction, queried Koog's official YouTrack (JetBrains/koog, the `KG` project) via
    its REST API and found an exact-match open issue,
    [KG-652](https://youtrack.jetbrains.com/issue/KG-652) (response headers are discarded, so
    provider rate-limit info can't be extracted from exceptions). Rather than filing a duplicate,
    went with option A (string matching) and implemented it via TDD.
*   Recorded the decisions as [AnyDR 0077](../../any-decision-record/0077-catch-exceptions-into-recognition-failure.md)
    and [0078](../../any-decision-record/0078-rate-limit-detection-via-message-matching.md)
    (Japanese only; no English translation exists for these yet).
*   The user posted a comment on KG-652 describing our current workaround (message string matching).
*   Confirmed `allTests` was green, then committed, pushed, and closed Issue #14.

## Result
*   All tests in the `vision-recognition` module passed (`./gradlew allTests` green).
*   Commit 833a62d pushed to origin/main.
*   Added AnyDR 0077 and 0078.
*   Posted a comment on [KG-652](https://youtrack.jetbrains.com/issue/KG-652/Support-HTTP-Headers-in-KoogHttpClientException#focus=Comments-27-14372140.0-0).
*   Closed Issue #14.

## Reflections
Reviewing whether an issue could actually be closed turned out to be a good way to surface a gap
between a documented design decision (0073) and what the code actually did. That kind of gap —
"the issue's wording is satisfied, but a related earlier design decision isn't" — wasn't visible
from the issue text alone.

It also turned out that Koog's `LLMClientException` doesn't expose HTTP status codes or response
headers, which forces error-type detection into string matching. That's not just our own problem —
it's a known open issue on JetBrains/koog's side too (KG-652, filed January 2025 and still
unresolved). Hitting a constraint in an upstream dependency was a good reminder to check the
upstream issue tracker before building a workaround from scratch.
