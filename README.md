# SmileStudio

A SMILES notation editor and structure-drawing tool for chemistry students, built with
Kotlin Multiplatform (KMP) and Compose Multiplatform.

**Status:** early development. Core SMILES parsing (chains, branches, ring closures, aromatic
notation) is implemented and tested; 2D structure rendering, the Android app, and AI-assisted
hand-drawn structure recognition are in progress. See `docs/any-decision-record/` for the
project's design decisions and current direction.

## Modules

- `core-smiles` — pure Kotlin. SMILES parsing and the chemistry domain model (atoms, bonds,
  molecules). No UI or platform dependencies.
- `ui-compose` — Compose Multiplatform. Depends on `core-smiles`; owns the structure-drawing
  canvas, shared across platforms.
- `desktop-app` — Compose for Desktop entry point.
- `android-app` — Android entry point.

## Building and running

```bash
# Build everything
./gradlew build

# Run all tests (core-smiles and ui-compose are Kotlin Multiplatform modules,
# so the standard `test` task doesn't run them -- use `allTests` instead)
./gradlew allTests

# Run just core-smiles' tests
./gradlew :core-smiles:jvmTest

# Launch the desktop app
./gradlew :desktop-app:run

# Build the Android app (or open the project in Android Studio and run it there)
./gradlew :android-app:assembleDebug
```

## License

MIT — see [LICENSE](./LICENSE).
