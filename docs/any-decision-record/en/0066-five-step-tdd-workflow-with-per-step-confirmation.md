# 0066: Redefine the TDD workflow as 5 steps with mandatory per-step confirmation

- Date: 2026-09-04
- Status: Accepted
- Category: process
- Deciders: the user

## Context

CLAUDE.md's TDD workflow used to be a 4-step Test First / Red / Green / Refactor cycle, where work
proceeded from writing the test all the way to the implementation in one go. The user gave an
explicit instruction to redefine this into a stricter 5-step process that clearly separates
"structural RED" (a compile error) from "behavioral RED" (a failing test run), and that pauses for a
status report and confirmation after each step completes.

## Decision

Redefine CLAUDE.md's "Development Workflow (Thorough TDD Cycle)" section as the following 5 steps.
After each step completes, always report status and wait for confirmation before moving to the next
step (never proceed straight through to the implementation in one pass).

1. Write the test code (expected to be a compile error at this point — "structural RED")
2. Resolve the compile error (skeleton implementation: empty classes/methods, dummy return values or
   `TODO()`; behavior is not implemented yet)
3. Confirm "behavioral RED" (run the test, confirm it fails — either a value mismatch or an
   exception — and present the failure output)
4. Implement (GREEN: minimal logic to make the test pass)
5. Refactor (revisit readability, clean up into idiomatic Kotlin style)

## Alternatives

No explicit comparison of alternatives appears in the discussion. The user directly specified this as
the concrete procedure to follow, and it was adopted as-is (this wasn't a design fork weighing
multiple approaches, but a specific procedure being instructed directly).

## Consequences

- Future TDD implementation work will no longer bundle test-writing and implementation together as
  before; it will pause at each of the 5 steps to wait for the user's confirmation. This increases
  the number of back-and-forth exchanges needed to complete a single feature.
- Explicitly separating "structural RED" (compile error) from "behavioral RED" (test failure) more
  strongly enforces the order of fixing the signature under test (argument and return types) before
  implementing its behavior.
- The existing operating rule of confirming GREEN via `./gradlew allTests` and autonomously
  analyzing and fixing the cause on failure remains unchanged.

## Related

(none)
