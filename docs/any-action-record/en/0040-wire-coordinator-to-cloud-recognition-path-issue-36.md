# 0040: Wired ImageRecognitionCoordinator to the non-BYOK cloud recognition path

- Date: 2026-09-14
- Related AnyDR: 0133, 0134, 0135, 0136
- Related Issue: #36

## Objective
With Issue #34 (the Cloud Functions image recognition proxy) and Issue #35 (Firebase Auth /
RevenueCat SDK integration) done, extend `ImageRecognitionCoordinator` so that when no BYOK key
is set, recognition goes through the serverless function instead. This also covers showing the
remaining free-tier count via Snackbar and showing the paywall once the free tier is exhausted.

## Action
- Used `/grill-with-docs` to work through the response contract (AnyDR 0133), the exhaustion-
  detection strategy (AnyDR 0134), the `ImageRecognitionOutcome` shape (AnyDR 0135), and whether
  to wait for Firebase Auth before calling the function (AnyDR 0136) one question at a time.
- TDD'd a change on the Cloud Functions side (`entitlementLogic.ts`, `recognizeImage.ts`) to
  include `remainingFreeCount` in the response.
- Split `ImageRecognitionCoordinator` into two paths (BYOK vs. cloud), adding a new imperative
  shell `FirebaseCloudRecognizer` (wrapping the Firebase Functions SDK call) and a
  `CloudRecognitionResult` type. Removed `MissingApiKey` from `ImageRecognitionOutcome`, added a
  nullable `remainingFreeCount` on `Recognized`, and a new `FreeTierExhausted` case.
- Wired the paywall (Issue #35's `SubscriptionPaywall`, wrapped in a full-screen `Dialog`) and the
  remaining-count Snackbar into `SmilesStudioApp.kt`.
- Tried to redeploy the changed Cloud Functions with `firebase deploy --only functions`, and hit a
  Google Cloud authorization inconsistency: the IAM console showed "Owner" for the account, but
  `testIamPermissions` returned nothing. Checked and fixed several things along the way (upgrading
  to the Blaze plan, enabling the Firebase Management API), but the deploy itself hadn't succeeded
  yet by the end of the session.

## Result
Both the Android and Cloud Functions sides are implemented; `./gradlew allTests
:android-app:testDebugUnitTest` and `npx jest` are all green. Committed as e320ace and pushed.
The final on-device verification of the non-BYOK cloud path (actually exercising the deployed
function) is on hold until the Firebase deploy permission issue clears up; planned to retry the
next morning.

## Reflections
During the TDD cycle's "confirm behavioral RED" step, I first used a blanket `TODO()` that threw
an exception, and got pushed back on: does failing because of `TODO()` actually prove anything
about TDD? All five tests failing with the exact same exception proves nothing about whether each
test's `assertEquals` is actually checking a meaningful value. Instead, I swapped in an
implementation that always returns one fixed, obviously-wrong value, with zero knowledge of the
real branching logic. All five tests then failed with a real expected-vs-actual diff, proving each
assertion actually discriminates -- while staying black-box (no dependency on the real branch
structure being deliberately broken, which I'd proposed first and got vetoed as too white-box).
It's a technique worth keeping for future RED confirmations.

Separately, the Cloud Functions deploy trouble surfaced a real gotcha: a project's IAM console can
display "Owner" for an account while the actual authorization backend grants that account zero
permissions. Displayed role and effective permission aren't always the same thing -- a fact worth
remembering next time a similarly confusing permission error shows up.
