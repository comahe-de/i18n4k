# Build system

## Overview

| Part | Build system | Why |
|---|---|---|
| `i18n4k-core` | Kotlin Toolchain 0.14.1 (`kmp/lib`) | Multiplatform library |
| `i18n4k-cldr-plural-rules` | Kotlin Toolchain 0.14.1 (`kmp/lib`) | Multiplatform library |
| `i18n4k-generator` | Kotlin Toolchain 0.14.1 (`jvm/lib`, `layout: maven-like`) | JVM library |
| `i18n4k-gradle-plugin` | **Gradle** (nested build) | The Toolchain has no product type for Gradle plugins |
| `examples/*` | **Gradle** | Consumer samples for the Gradle plugin — do not modify |

The Kotlin Toolchain is JetBrains' declarative `kotlin` CLI (formerly Amper). Configuration is
YAML, not Gradle Kotlin DSL.

```
project.yaml                     # Toolchain module list
libs.versions.toml               # dependency catalog (read by the Toolchain and by Gradle)
kotlin / kotlin.bat              # Toolchain wrapper (pins the Toolchain version)
templates/
  kmp-build.module-template.yaml # shared KMP settings (JDK 17, JVM release 8, Android, JUnit 4)
  publish.module-template.yaml   # shared Maven Central publishing settings
i18n4k-core/module.yaml
i18n4k-cldr-plural-rules/module.yaml
i18n4k-generator/module.yaml
i18n4k-gradle-plugin/            # nested, self-contained Gradle build
```

The wrapper pins **Kotlin Toolchain 0.14.1** (distribution `0.14.0-dev-4630`). The
`kotlin_cli_version` / `kotlin_cli_sha256` lines in `kotlin` and `kotlin.bat` must stay in sync.
To bump the version, run `./kotlin update`; do not hand-edit the checksum.

## Everyday commands

```sh
./kotlin build                     # compile everything for host-supported platforms
./kotlin test                      # run all tests
./kotlin publish mavenLocal        # install the libraries into ~/.m2
./kotlin show modules              # list modules
./kotlin show settings -m i18n4k-core
./kotlin show dependencies -m i18n4k-core
./kotlin show tasks
```

Scoping:

```sh
./kotlin build -p jvm -p android
./kotlin test  -p jvm
./kotlin build -m i18n4k-core -p js -p wasmJs -p wasmWasi
./kotlin publish mavenLocal -m i18n4k-generator --transitive
```

`build` and `test` automatically restrict themselves to platforms that can be handled on the
current host. Apple targets are cross-compiled to klibs for publishing, but Apple **tests and
binaries** still require macOS; they are covered by the macOS entry in `run_checks.yml`.

`-m` with a module that has local dependencies requires `--transitive` or `--non-transitive`.

## Source layout

The Toolchain does not use `src/commonMain/kotlin`. Sources live in platform-qualified
directories and the `kotlin` level is dropped:

| Gradle | Kotlin Toolchain |
|---|---|
| `src/commonMain/kotlin` | `src` |
| `src/commonTest/kotlin` | `test` |
| `src/commonTest/resources` | `testResources` |
| `src/jvmMain/kotlin` | `src@jvm` |
| `src/jvmAndroidMain/kotlin` | `src@jvmAndroid` (alias in `i18n4k-core/module.yaml`) |
| `src/androidMain/kotlin` | `src@android` |
| `src/jsMain/kotlin` | `src@js` |
| `src/jsTest/kotlin` | `test@js` |
| `src/wasmJsMain/kotlin` | `src@wasmJs` |
| `src/wasmWasiMain/kotlin` | `src@wasmWasi` |
| `src/nativeMain/kotlin` | `src@native` |
| `src/androidNativeMain/kotlin` | `src@androidNative` |
| `src/androidNativeMain/cinterop` | `cinterop@androidNative` (`.def` files) |
| `src/appleMain/kotlin` | `src@apple` |
| `src/linuxMain/kotlin` | `src@linux` |
| `src/mingwMain/kotlin` | `src@mingw` |

`i18n4k-generator` is JVM-only and keeps `layout: maven-like`, so
`src/main/kotlin`, `src/test/kotlin` and `src/test/files` are unchanged.

`jvmAndroidMain` / `jvmAndroidTest` were custom intermediate Gradle source sets. They are
expressed with `aliases: - jvmAndroid: [jvm, android]` in `i18n4k-core/module.yaml` (Toolchain
0.14 rejects `aliases:` in templates).

## Dependency catalog

Dependencies and versions live in `libs.versions.toml` and are referenced from YAML as `$libs.<key>`:

```yaml
dependencies:
  - $libs.kotlinxAtomicfu
  - //i18n4k-core            # local module dependency
  - $libs.kotlinxCoroutinesCore: compile-only
```

`[bundles]` are not supported by the Toolchain. The `[plugins]` section is only for the nested
Gradle build.

## Kotlin / JVM / Android settings

- Kotlin `2.3.20` is pinned per module (`settings.kotlin.version`).
- JVM: `settings.jvm.jdk.version: 17` + `settings.jvm.release: 8` reproduce the old
  `jvmTarget = 1.8` / `sourceCompatibility = 1.8`.
- Android: `minSdk = 21`, `compileSdk = 36` (Toolchain 0.14 defaults to 37).
- Tests use JUnit 4 (`settings.junit: junit-4`), matching the old Gradle setup.

## Windows: Web-target workaround

Kotlin's IR backend derives the klib name from `group:artifact`
(`de.comahe.i18n4k:i18n4k-core`). On Windows `:` is illegal in paths, and the compiler uses that
name as a temp-directory prefix. Without a workaround, `js` / `wasmJs` / `wasmWasi` compilation
fails with:

```
java.nio.file.InvalidPathException: Illegal char <:> at index 16: de.comahe.i18n4k:i18n4k-core…
```

Both KMP `module.yaml` files therefore pass `-Xir-module-name` per web platform, plus a
distinct `_test` name:

```yaml
settings@js:
  kotlin:
    freeCompilerArgs: [ -Xir-module-name=i18n4k-core ]
test-settings@js:
  kotlin:
    freeCompilerArgs: [ -Xir-module-name=i18n4k-core_test ]
# …repeated for wasmJs and wasmWasi
```

The warning `-Xir-module-name is passed multiple times` during test compilation is expected.
Do **not** use `-module-name`: the Wasm compiler rejects it.

## Windows / Unix wrappers

- Linux/macOS: `./kotlin`
- Windows CI and PowerShell: `./kotlin.bat` (the shell wrapper cannot run under PowerShell)
- The Gradle wrapper is `./gradlew` (Git Bash/Linux/macOS) or `.\gradlew.bat` (PowerShell).

## Tests

```sh
./kotlin test -p jvm          # fastest useful subset
./kotlin test                 # all host-supported targets
```

Verified: 112 JVM tests pass, including `i18n4k-generator` tests that read
`src/test/files/...` relative to the module directory (the Toolchain runs tests with the module
directory as CWD).

## API documentation / Javadoc

There is **no Dokka integration** in this repository. The Kotlin Toolchain always adds an
**almost empty** `-javadoc.jar` (a single `README.md`) to every publication; Maven Central
requires the JAR to exist, but its content cannot be replaced because the Toolchain exposes no
hook for it.

Generating API documentation for the Toolchain source layout with Dokka was tried and removed:
Dokka 2.3 crashes on the combination of multiplatform source sets and the compiled module
classpath (`KaFirDefaultPropertyGetterSymbol`), and its Javadoc output format cannot represent
multiple source sets. If real API docs are needed, either publish them as a separate (non
`-javadoc.jar`) artifact or move publishing back to Gradle.

## Verification status

Verified on Windows with Kotlin Toolchain 0.14.1:

| Command | Result |
|---|---|
| `./kotlin show modules` | 3 modules |
| `./kotlin build` | all host-supported targets |
| `./kotlin test` | all host-supported targets (112 JVM tests) |
| `./kotlin publish mavenLocal` | all 3 modules, all platforms (incl. Apple klibs) |
| `i18n4k-gradle-plugin/gradlew build` | OK |
| `i18n4k-gradle-plugin/gradlew publishToMavenLocal` | OK |

See [`migration-from-gradle.md`](migration-from-gradle.md) for the full list of differences from
the old Gradle build.
