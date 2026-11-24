# agent.md — i18n4k build system

Guidance for AI agents (and humans) working on this repository.

## What this project is

**i18n4k** is a multiplatform (JVM, Android, JS, Wasm, native) internationalization
library and code generator for Kotlin. Group: `de.comahe.i18n4k`, version `0.11.2`.

It consists of:

| Directory | Product type | What it is |
|---|---|---|
| `i18n4k-core` | `kmp/lib` | The runtime library (`Locale`, `LocalizedString`, message bundles, …) |
| `i18n4k-cldr-plural-rules` | `kmp/lib` | CLDR plural-rule formatters (depends on `i18n4k-core`) |
| `i18n4k-generator` | `jvm/lib` | The code generator (`I18n4kGenerator`, built on KotlinPoet) |
| `i18n4k-gradle-plugin` | **Gradle plugin** | Gradle plugin that wires the generator into consumer builds |
| `examples/*` | Gradle builds | Example consumer projects — **do not change these** |

## Build system: Kotlin Toolchain (formerly Amper)

The three library modules are built with the **Kotlin Toolchain** (JetBrains' unified
`kotlin` CLI, formerly Amper). Configuration is declarative YAML, not Gradle scripts.

```
project.yaml                     # lists the Toolchain modules
libs.versions.toml               # version catalog (Gradle format, read as $libs.*)
kotlin / kotlin.bat              # Toolchain wrapper (pins the Toolchain version)
templates/
  kmp-build.module-template.yaml # shared KMP config (was the i18n4k.kmp-build convention plugin)
  publish.module-template.yaml   # shared publishing config (was i18n4k.publish-build + root subprojects {})
i18n4k-core/module.yaml
i18n4k-cldr-plural-rules/module.yaml
i18n4k-generator/module.yaml
i18n4k-gradle-plugin/            # <-- nested, self-contained GRADLE build (see below)
```

The wrapper pins Kotlin Toolchain **0.14.1** (the `kotlin_cli_version` variable at the top of
`kotlin`/`kotlin.bat` is `0.14.0-dev-4630`, which is the distribution shipped with the
`v0.14.1` release). To bump it, run `./kotlin update`. Do **not** hand-edit the
checksum line unless you know what you are doing.

### Everyday commands

```sh
./kotlin build                 # compile everything
./kotlin test                  # run all tests
./kotlin publish mavenLocal    # install the libraries to ~/.m2 for the examples & the Gradle plugin
./kotlin show modules          # inspect the project model
./kotlin show dependencies -m i18n4k-core
```

Examples of scoping:

```sh
./kotlin test -m i18n4k-core
```

### The examples and the Gradle plugin still use Gradle

`examples/*` are Gradle builds and must stay that way (they are consumer samples that
demonstrate the **Gradle plugin**). Likewise `i18n4k-gradle-plugin` is a Gradle build,
because the Kotlin Toolchain has no product type for producing Gradle plugins.

Because the Gradle builds consume the published artifacts, publish them first:

```sh
./kotlin publish mavenLocal
cd i18n4k-gradle-plugin && ./gradlew build     # or gradlew.bat on Windows
```

## Source-set / directory layout

The Kotlin Toolchain does **not** use `src/commonMain/kotlin`. For multiplatform modules
the sources live in `@platform`-qualified directories and the `kotlin` level is dropped.
`i18n4k-core` and `i18n4k-cldr-plural-rules` were re-laid-out accordingly
(moved with `git mv`, history preserved):

| Old (Gradle) | New (Kotlin Toolchain) |
|---|---|
| `src/commonMain/kotlin` | `src` |
| `src/commonTest/kotlin` | `test` |
| `src/commonTest/resources` | `testResources` |
| `src/jvmMain/kotlin` | `src@jvm` |
| `src/jvmAndroidMain/kotlin` | `src@jvmAndroid` (via the `jvmAndroid` alias) |
| `src/androidMain/kotlin` | `src@android` |
| `src/jsMain/kotlin` | `src@js` |
| `src/jsTest/kotlin` | `test@js` |
| `src/wasmJsMain/kotlin` | `src@wasmJs` |
| `src/wasmWasiMain/kotlin` | `src@wasmWasi` |
| `src/nativeMain/kotlin` | `src@native` |
| `src/androidNativeMain/kotlin` | `src@androidNative` |
| `src/androidNativeMain/cinterop` | `cinterop@androidNative` (`.def` files, no YAML) |
| `src/appleMain/kotlin` | `src@apple` |
| `src/linuxMain/kotlin` | `src@linux` |
| `src/mingwMain/kotlin` | `src@mingw` |

`i18n4k-generator` is JVM-only and uses `layout: maven-like`, so its
`src/main/kotlin`, `src/test/kotlin` and `src/test/files` directories are **unchanged**.

`jvmAndroidMain`/`jvmAndroidTest` were custom intermediate source sets in Gradle; they are
expressed as the alias `jvmAndroid: [jvm, android]` in `i18n4k-core/module.yaml` (Toolchain
0.14 rejects `aliases:` inside templates), enabling `src@jvmAndroid` / `test@jvmAndroid`.
`i18n4k-cldr-plural-rules` does not use that source set, so it needs no alias.

## Migration mapping (what replaced what)

### Gradle files removed

`settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`, `gradle/`, `gradlew`,
`gradlew.bat` (moved into the Gradle plugin), and the whole `buildSrc/` directory.

### `buildSrc` convention plugins → templates

| Gradle | Kotlin Toolchain |
|---|---|
| `i18n4k.kmp-build` (KMP targets, JDK target, `-Xexpect-actual-classes`) | `templates/kmp-build.module-template.yaml` (aliases stay in `module.yaml`) |
| `i18n4k.publish-build` (Maven publish, Dokka javadoc jar, dedup) | `templates/publish.module-template.yaml` |
| root `subprojects { }` (signing + POM metadata + staging repo) | `templates/publish.module-template.yaml` (`settings.publishing.*`) |
| root `allprojects { }` (group/version/repositories) | `settings.publishing.group` / `.version`; Maven Central + Google repos are built in |

### Dependencies

| Gradle | Kotlin Toolchain |
|---|---|
| `implementation(libs.x)` | `- $libs.x` in `dependencies:` |
| `testImplementation(libs.x)` | `- $libs.x` in `test-dependencies:` |
| `implementation(project(":i18n4k-core"))` | `- //i18n4k-core` |
| `api(...)` | suffix `: exported` |
| `compileOnly(...)` | suffix `: compile-only` |
| `kotlin("test")` / `kotlin("test-junit")` etc. | added automatically (`settings.junit`) |

### Toolchain-version pinning

The Gradle build applied KGP version `2.3.20` (`libs.versions.toml` → `kotlin`). Each module
pins `settings.kotlin.version: 2.3.20`. When bumping Kotlin, update the catalog **and** the
module settings (or move the value into a shared template).

### JVM targets

`jvmTarget = JVM_1_8` + `sourceCompatibility = 1.8` map to `settings.jvm.release: 8`
applied by **both** the Kotlin and Java compilers. A JDK 17 is pinned via
`settings.jvm.jdk.version: 17` (release 8 was dropped by newer JDKs).

## Exception: `i18n4k-gradle-plugin` stays on Gradle

The Kotlin Toolchain can produce `jvm/app`, `jvm/lib`, `kmp/lib`, `jvm/amper-plugin`, etc.,
but there is **no product type that produces a Gradle plugin**. `i18n4k-gradle-plugin`
publishes to the Gradle Plugin Portal via `java-gradle-plugin`/`com.gradle.plugin-publish`,
which has no Toolchain equivalent. It therefore remains a **self-contained Gradle build**:

- `i18n4k-gradle-plugin/settings.gradle.kts` — own settings, reads `../libs.versions.toml`
- `i18n4k-gradle-plugin/build.gradle.kts` — standalone (no `buildSrc`)
- `i18n4k-gradle-plugin/gradlew(.bat)` + `gradle/wrapper/` — its own wrapper

It depends on the Toolchain-built artifacts **from `mavenLocal`**, so publish them first.
Naming note: `i18n4k-core` is a `kmp/lib`, so its JVM artifact is `i18n4k-core-jvm`.

### Gradle-plugin build-system deltas

- `buildSrc` is gone. The Gradle-plugin build no longer uses `BuildTools`/`Secrets`; POM
  metadata and conditional signing were inlined in its `build.gradle.kts`.
- JReleaser-based Maven Central upload for the Gradle plugin was **not** re-created. The
  Gradle plugin is distributed via the Gradle Plugin Portal (`./gradlew publishPlugins`).
  If Central publication of the plugin is still required, re-add JReleaser (or migrate the
  plugin to a `jvm/lib` and publish it through the Toolchain — but you lose the auto-generated
  plugin descriptors and Portal publishing).

## Windows: Web-target klib-name workaround

Kotlin's IR backend derives the klib name from `group:artifact`
(`de.comahe.i18n4k:i18n4k-core`). On Windows `:` is an illegal path character, and the compiler
uses that name as a temp-directory prefix, so `js`/`wasmJs`/`wasmWasi` compilation failed with:

```
java.nio.file.InvalidPathException: Illegal char <:> at index 16: de.comahe.i18n4k:i18n4k-core…
```

Fix: pass `-Xir-module-name` per web platform, and a distinct `_test` name via
`test-settings@<platform>` (otherwise the main and test klibs share a `unique_name`). Both KMP
`module.yaml` files carry it:

```yaml
settings@js:
  kotlin:
    freeCompilerArgs: [ -Xir-module-name=i18n4k-core ]
test-settings@js:
  kotlin:
    freeCompilerArgs: [ -Xir-module-name=i18n4k-core_test ]
# …repeated for wasmJs and wasmWasi
```

The "`-Xir-module-name` is passed multiple times" warning on test compilations is expected and
harmless (the last value wins). Do **not** use `-module-name` — the Wasm compiler rejects it.

## Known limitations / things that could not be migrated 1:1

These are honest gaps; each should be reviewed on the first real CI run.

1. **`watchosX64` target dropped.** The Gradle build targeted `watchosX64`; the Kotlin
   Toolchain does not expose that leaf platform. All other targets
   (`iosX64`, `macosX64`, `tvosX64`, `watchosArm32`, …) exist but several are *deprecated*
   in Toolchain 0.14 / Kotlin 2.3.20 — expect deprecation warnings.
2. **JS / Wasm test runners.** Gradle configured Karma + ChromeHeadless and Mocha + NodeJS.
   The Toolchain uses its own fixed JS/Wasm test host; the browser/node configuration has no
   YAML equivalent and was dropped.
3. **Dokka.** The Gradle build published a Dokka-generated Javadoc JAR. The Toolchain adds an
   (empty) Javadoc JAR automatically; Dokka is not wired. Update `publishSources`/docs tooling
   if real Javadoc is required.
4. **Android `compileSdk`.** Gradle used `compileSdk = 36`; Toolchain 0.14 defaults to 37. It
   is pinned back to `36` in the template for fidelity.
5. **`-P` properties / `avoidDuplicatePublications`.** `gradle.properties` flags
   (`publishSnapshots`, `publishReleases`, `avoidDuplicatePublications`) and the `-P` CLI
   overrides have no Toolchain equivalent. Multi-platform publishing now relies on the
   Toolchain's own per-host platform selection. The release/publish-on-tag gating that these
   flags implemented in old CI is now implicit in the `on: push: tags` workflow triggers.
6. **JReleaser staging upload.** The `publishToMavenCentral` task (local staging dir →
   JReleaser → Central Portal) was replaced by the native `./kotlin publish mavenCentral`.
7. **CI secrets renamed.** The old workflows used `CI_SECRET_KEY_RING_FILE` (base64 GPG) and
   `CI_SECRET_PROPERTIES_FILE`. The new workflows expect `MAVEN_CENTRAL_USERNAME`,
   `MAVEN_CENTRAL_PASSWORD`, `SIGNING_KEY` (ASCII-armored PGP key) and
   `SIGNING_KEY_PASSPHRASE` GitHub secrets. **The repository secrets must be updated.**
8. ~~**Test working directory.**~~ Resolved: verified that the Toolchain runs tests with the
   module directory as CWD, so `i18n4k-generator`'s `File("src/test/files/...")` reads work.
9. **Maven Central is published from macOS only.** Unlike Gradle (which published per-host
   platform fragments), the Toolchain publishes *every* platform from one host — verified: on
   Windows, `publish mavenLocal` produced Apple klibs (`ios*`, `macos*`, `watchos*`) as well.
   Maven Central rejects duplicate artifacts, so only `publish_artifacts_macos.yml` runs
   `publish mavenCentral`; the Linux and Windows workflows are release checks (Linux also
   publishes the Gradle plugin to the Plugin Portal).
10. **Wasm artifact IDs.** The Toolchain publishes the web artifacts as `i18n4k-core-wasmjs`
    and `i18n4k-core-wasmwasi`; the old Gradle build used `-wasm-js` / `-wasm-wasi`. The root
    `.module` file points at the new names, so Gradle/Maven consumers resolve the right files.
11. **Windows uses `kotlin.bat` in CI.** GitHub's default Windows shell is PowerShell, which
    cannot execute the extension-less `./kotlin` shell script. The workflows select the wrapper
    per OS (`./kotlin` vs `./kotlin.bat`) and run Gradle through `shell: bash`.

## Verification status

Verified on **Windows** with Kotlin Toolchain **0.14.1** (distribution `0.14.0-dev-4630`):

| Command | Result |
|---|---|
| `./kotlin show modules` | ✅ 3 modules listed |
| `./kotlin build` | ✅ all host-supported targets (JVM, Android, JS, WasmJs, WasmWasi, mingwX64, linuxX64/linuxArm64, androidNative*) |
| `./kotlin test` | ✅ all host-supported targets (112 JVM tests, incl. the generator's file-comparison tests) |
| `./kotlin publish mavenLocal` | ✅ all 3 modules published (`i18n4k-core` incl. every native klib) |
| `cd i18n4k-gradle-plugin && ./gradlew build` | ✅ compiles against the Maven-Local artifacts |

Apple targets (`ios*`, `macos*`, `tvos*`, `watchos*`) are not buildable on Windows, so the
Toolchain skips them there; they are covered by the macOS CI job. `build`/`test` auto-filter to
the current host unless you pin platforms:

```sh
./kotlin build -p jvm -p android
./kotlin test  -p jvm
./kotlin build -m i18n4k-core -p js -p wasmJs -p wasmWasi
```

Still to verify in CI: `./kotlin publish mavenCentral` (needs the Central Portal credentials).

The nested Gradle build has no `buildSrc`, so the Kotlin plugin version is declared through the
version catalog (`libs.plugins.kotlinJvm`, added to `libs.versions.toml`). Running its `build`
also exercises Gradle's `validatePlugins`, which previously was never run in CI and exposed
missing task annotations — `GenerateI18n4kFilesTask` and `ClearI18n4kFilesTask` now carry
`@DisableCachingByDefault`, and the input directory declares `@PathSensitive(RELATIVE)`.

## For the next agent

- **Do not** reintroduce `build.gradle.kts` / `gradlew` at the repository root, and **do not**
  run `gradle` for the library modules — use `./kotlin`.
- **Do not** move `examples/*` to the Kotlin Toolchain; they intentionally demonstrate the
  Gradle plugin.
- When adding a Kotlin library dependency, add it to `libs.versions.toml` and reference it as
  `$libs.<key>` (never a literal coordinate, and never a `[bundles]` entry — bundles are not
  supported).
- The version catalog's `[plugins]` section is only used by the nested Gradle build of the
  Gradle plugin; the Toolchain ignores it. It contains `kotlinJvm` because that build no longer
  inherits the Kotlin plugin from a `buildSrc` classpath.
- Templates cannot declare `product:` or `apply:`. In Toolchain 0.14 they also reject
  `aliases:` and `description:` (the parser reports them as *Unknown property*). Keep those
  in `module.yaml`; put the published description under `settings.publishing.pom.description`.

## References

Official Kotlin Toolchain documentation:

- Getting started / migrating from Gradle: <https://kotlin-toolchain.org/dev/getting-started/migrating-from-gradle/>
- `module.yaml` reference: <https://kotlin-toolchain.org/dev/reference/module/>
- Multiplatform modules: <https://kotlin-toolchain.org/dev/user-guide/multiplatform/>
- Publishing: <https://kotlin-toolchain.org/dev/user-guide/publishing/>

Agent skills consulted while producing this migration:

- `kotlin-tooling-kotlin-toolchain` — <https://github.com/Kotlin/kotlin-agent-skills> (Toolchain syntax, product types, layouts, pitfalls)
- `kotlin-tooling-gradle-to-kotlin-toolchain-project` — whole-project Gradle → Toolchain migration workflow
- `kotlin-tooling-gradle-to-kotlin-toolchain-plugin` — Gradle plugin → Toolchain plugin porting (reference for the `i18n4k-gradle-plugin` exception)
- `singleton11/kotlin-toolchain-skills` — <https://github.com/singleton11/kotlin-toolchain-skills>
