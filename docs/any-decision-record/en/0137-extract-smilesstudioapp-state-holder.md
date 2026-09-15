# 0137: Split SmilesStudioApp's state management and logic into a lightweight State Holder class

- Date: 2026-09-16
- Status: Accepted
- Category: architecture
- Deciders: the user, Claude Code

## Context

`android-app`'s `SmilesStudioApp.kt` had grown into a single `@Composable fun SmilesStudioApp()`
carrying over 240 lines of logic. Specifically, all of the following lived inside one Composable
function:

- 10+ pieces of state (selected tab, SMILES text, API key, several dialog/sheet visibility flags,
  a recognizing flag, a pending capture URI, etc.)
- Wiring for `ApiKeyStore` and `ImageRecognitionCoordinator` (assembling their concrete
  implementations)
- The image-recognition business logic (`runRecognition`: read image → recognize → branch on
  outcome → snackbar/paywall control)
- Management of Android `ActivityResultLauncher`s
- UI tree construction (Scaffold/TopBar/BottomBar/AnimatedContent + four dialogs)

In particular, `runRecognition`'s outcome branching (Recognized/Failed/FreeTierExhausted) was
buried inside a local function within the Composable and couldn't be unit-tested without the UI.
Given that decisions around recognition results ([[0135-outcome-nullable-remaining-count]] and
similar) were expected to keep accumulating, leaving this logic untestable was judged a growing
risk.

## Decision

Adopted the approach (Option A) of extracting the state and logic into a plain `@Stable` Kotlin
class, `SmilesStudioAppState`, created via a `rememberSmilesStudioAppState()` factory Composable.
`SmilesStudioApp` itself is now dedicated purely to building the UI tree.

- `SmilesStudioAppState` holds the state (`selectedTab`/`smilesText`/the various visibility
  flags/`isRecognizing`/`pendingCaptureUri`, etc.) along with `saveApiKey`/`deleteApiKey`/
  `runRecognition`.
- Wiring for `ApiKeyStore`/`ImageRecognitionCoordinator` is centralized inside
  `rememberSmilesStudioAppState()`.
- `runRecognition` was made a suspend function that takes an already-resolved `ByteArray?`, with
  no Android dependency (`Context`/`Uri`) of its own. This confirmed that the Compose runtime's
  `mutableStateOf` works fine under plain JVM unit tests.
- Filed Issue #40 first, then carried out the change through the 5-step TDD cycle (write the
  test → compile via a skeleton → confirm behavioral RED → implement to GREEN → refactor).

## Alternatives

- **Option B: introduce `androidx.lifecycle.ViewModel`**: would give standard Android-provided
  state retention across configuration changes, but requires new dependencies
  (`lifecycle-viewmodel-compose`, etc.) and boilerplate to move to `StateFlow`. Rejected as not
  worth the effort given the Shipaton deadline (2026-09-30).
- **Option C: split only the UI tree into smaller Composables, leaving state untouched**: lowest
  risk and smallest diff, but doesn't solve the core problem — `runRecognition` would remain
  untestable. Rejected as merely cosmetic.

## Consequences

- `runRecognition`'s outcome branching can now be verified with plain JVM unit tests
  (`SmilesStudioAppStateTest.kt`) that don't depend on Compose.
- `SmilesStudioApp.kt`: 322 lines → 239 lines; `SmilesStudioAppState.kt`: 105 lines (new).
- No new dependencies were introduced (no ViewModel, no DI framework).
- State retention across configuration changes (e.g. screen rotation) still relies on `remember`
  as before; this refactor neither fixes nor regresses that.

## Related

- [0027-android-app-module-for-shipaton-2026](./0027-android-app-module-for-shipaton-2026.md)
- [0135-outcome-nullable-remaining-count](./0135-outcome-nullable-remaining-count.md)
