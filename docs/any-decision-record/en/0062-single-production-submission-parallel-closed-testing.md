# 0062: Collapse production submission to one, and run the 14-day closed test alongside feature work

- Date: 2026-09-04
- Status: Accepted
- Category: process
- Deciders: the user, Claude Code

## Context

AnyDR 0059–0061 had planned two production submissions: an "initial" one with a minimal build
([Issue #6](https://github.com/ItisNoMatter/SmilesStudio/issues/6),
[Issue #7](https://github.com/ItisNoMatter/SmilesStudio/issues/7),
[Issue #13](https://github.com/ItisNoMatter/SmilesStudio/issues/13)), then a "feature-added"
follow-up. But fact-finding during a `/grill-with-docs` session turned up that new personal Google
Play developer accounts (created after 2023-11-13) must run a closed test with 12+ testers,
opted in continuously for 14 days, before production access even unlocks. We confirmed the
SmilesStudio account is new and this requirement applies. That 14-day window can start and elapse
independent of how complete the build is, and runs fully in parallel with feature work — so the
real substance of "get store review moving early" turned out to be starting that window early, not
the order of production submissions. That prompted a rethink of the two-submission design.

## Decision

Once the minimal build ([Issue #6](https://github.com/ItisNoMatter/SmilesStudio/issues/6),
[Issue #7](https://github.com/ItisNoMatter/SmilesStudio/issues/7),
[Issue #13](https://github.com/ItisNoMatter/SmilesStudio/issues/13)) is done, start the Google Play
closed testing track and run the 14-day clock with 12+ testers. Implement
[Issue #14](https://github.com/ItisNoMatter/SmilesStudio/issues/14)–
[Issue #17](https://github.com/ItisNoMatter/SmilesStudio/issues/17) (hand-drawn recognition,
monetization) in parallel during that window. Submit to production only once — after both closed
testing (production access granted) and the feature work are done.

The issue split from AnyDR 0061 is kept, but its meaning changes:
- [Issue #18](https://github.com/ItisNoMatter/SmilesStudio/issues/18): redefined from "initial
  (minimal build) production submission" to "start and run Google Play closed testing (recruit
  testers, run the 14 days)." Still blocked by #6, #7, #13.
- New issue "Production submission (feature-complete)": a single production-submission issue,
  blocked by both #18 (closed testing done, production access granted) and #14–#17 (features done).

## Alternatives

- Keep AnyDR 0061 as-is: submit the minimal build to production on its own once closed testing
  ends, then submit the feature-added version as a second update. The upside was a safety net — if
  #14–#17 slipped, the minimal build would already be live, so Shipaton wouldn't be left with
  nothing shipped. Rejected because it means two production submissions (each with its own review
  wait for the update too), and a period where a text-only build is public and doesn't match
  Shipaton's actual pitch (hand-drawn structure recognition). Running the closed-testing window in
  parallel with feature work achieves the same underlying goal (start the review clock early)
  without that overhead.

## Consequences

- Update Issue #18's title/description to "start and run closed testing," and create the new issue
  "Production submission (feature-complete)" blocked by both #18 and #14–#17.
- Recruiting and retaining 12+ testers for 14 consecutive days remains a real operational risk. The
  user owns handling it.
- If #14–#17 aren't done within the 14 days, closed testing itself doesn't need to be redone, but
  the production submission timing slips relative to plan.

## Related

- [0059-phased-release-strategy-store-review-first](./0059-phased-release-strategy-store-review-first.md)
- [0060-minimal-build-scope-rendering-pipeline-only](./0060-minimal-build-scope-rendering-pipeline-only.md)
- [0061-split-store-submission-issue-and-rewire-dependencies](./0061-split-store-submission-issue-and-rewire-dependencies.md)
