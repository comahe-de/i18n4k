# Migration from Gradle to the Kotlin Toolchain

History and mapping of the migration to Kotlin Toolchain 0.14.1 (formerly Amper). For the
current build instructions see [`build-system.md`](build-system.md); for releasing see
[`deployment.md`](deployment.md).

## Scope

| Module | Migrated? | Notes |
|---|---|---|
| `i18n4k-core` | yes | `kmp/lib`, 23 platforms |
| `i18n4k-cldr-plural-rules` | yes | `kmp/lib`, 23 platforms |
| `i18n4k-generator` | yes | `jvm/lib`, `layout: maven-like` |
| `i18n4k-gradle-plugin` | **no** | No Gradle-plugin product type in the Toolchain; stays a nested Gradle build |
| `examples/*` | **no** | Consumer samples for the Gradle plugin; intentionally untouched |

## Gradle files removed

- root `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`
- root `gradle/`, `gradlew`, `gradlew.bat`
- the whole `buildSrc/`
- **module-level** `build.gradle.kts` files of `i18n4k-core`,
  `i18n4k-cldr-plural-rules` and `i18n4k-generator`

The Gradle wrapper moved into `i18n4k-gradle-plugin/` for its standalone build.
`i18n4k-generator/.gitignore` (`/temp/`, `/log/`) was kept because the generator tests still
create those directories.

## `buildSrc` convention plugins → templates

| Gradle | Kotlin Toolchain |
|---|---|
| `i18n4k.kmp-build` (KMP targets, JDK target, `-Xexpect-actual-classes`) | `templates/kmp-build.module-template.yaml` |
| `i18n4k.publish-build` (Maven publish, Dokka Javadoc jar, duplicate filtering) | `templates/publish.module-template.yaml` |
| root `subprojects { }` (signing + POM metadata + staging repo) | `templates/publish.module-template.yaml` (`settings.publishing.*`) |
| root `allprojects { }` (group/version/repositories) | `settings.publishing.group` / `.version`; Maven Central/Google repos are built in |

Templates in Toolchain 0.14 cannot contain `product:`, `apply:`, `aliases:` or `description:`.
Those stay in the individual `module.yaml` files (`jvmAndroid` alias, POM description, …).

## Dependencies

| Gradle | Kotlin Toolchain |
|---|---|
| `implementation(libs.x)` | `- $libs.x` under `dependencies:` |
| `testImplementation(libs.x)` | `- $libs.x` under `test-dependencies:` |
| `implementation(project(":i18n4k-core"))` | `- //i18n4k-core` |
| `api(...)` | suffix `: exported` |
| `compileOnly(...)` | suffix `: compile-only` |
| `kotlin("test")` / `kotlin("test-junit")` / … | added automatically (`settings.junit`) |

`libs.versions.toml` is read by the Toolchain as a library catalog (`$libs.*`). The `[plugins]`
section is only used by the nested Gradle build.

## Version pinning

- Kotlin `2.3.20` is pinned in each module (`settings.kotlin.version: 2.3.20`) and in the
  version catalog (`kotlin = "2.3.20"`, used by the Gradle plugin).
- The Toolchain wrapper pins version 0.14.1 (distribution `0.14.0-dev-4630`).
- The project version (`0.11.2`) is duplicated in
  `templates/publish.module-template.yaml` and `i18n4k-gradle-plugin/build.gradle.kts`.

## JVM targets

Gradle used `jvmTarget = JVM_1_8` + `sourceCompatibility = 1.8`. The Toolchain uses
`settings.jvm.release: 8` with a provisioned JDK 17 (`settings.jvm.jdk.version: 17`), which
applies the same constraints to the Kotlin and Java compilers.

## Publishing

| Old Gradle | New |
|---|---|
| `publishToMavenLocal` | `./kotlin publish mavenLocal` |
| `publishToMavenCentral` (local staging → JReleaser → Central Portal) | `./kotlin publish mavenCentral` |
| `-PavoidDuplicatePublications=true` + per-OS publication filters | **Not needed**: the Toolchain publishes all platforms from one host |
| Gradle properties / `CI_SECRET_PROPERTIES_FILE` | Environment variables (`KOTLIN_TOOLCHAIN_*`), see [`deployment.md`](deployment.md) |
| `publishPlugins` (Gradle plugin) | unchanged, from the nested Gradle build |

## Source-set layout

See the table in [`build-system.md`](build-system.md#source-layout). Files were moved with
`git mv`, so history is preserved.

## Windows Web-target workaround

`js` / `wasmJs` / `wasmWasi` compilation used to fail on Windows because the klib name
`group:artifact` contains a colon. Both KMP modules now pass `-Xir-module-name`; details in
[`build-system.md`](build-system.md#windows-web-target-workaround).

## Known limitations

1. **`watchosX64` target dropped.** The Gradle build targeted it; the Toolchain does not expose
   that leaf platform. All other targets are present; several are deprecated in Toolchain 0.14 /
   Kotlin 2.3.20 and emit warnings.
2. **JS / Wasm test runners.** Gradle configured Karma/ChromeHeadless and Mocha/NodeJS. The
   Toolchain has a fixed JS/Wasm test host; the browser/node configuration was dropped.
3. **Dokka / Javadoc removed.** The Toolchain adds an almost empty `-javadoc.jar` and has no
   Dokka integration; a Dokka-based workaround was tried in the nested Gradle build and dropped
   again (Dokka 2.3 crashes on the multiplatform sources plus module classpath, and its Javadoc
   format cannot represent several source sets). No API documentation is generated for now; see
   [`deployment.md`](deployment.md#javadoc-artifacts).
4. **Android `compileSdk`.** Gradle used 36; Toolchain 0.14 defaults to 37, so it is pinned back
   to 36 in the template.
5. **`-P` properties.** `publishSnapshots`, `publishReleases`, `avoidDuplicatePublications` and
   their command-line overrides have no Toolchain equivalent. Release gating is now the
   `on: push: tags` trigger.
6. **JReleaser.** The JReleaser staging upload for Maven Central was replaced by
   `./kotlin publish mavenCentral`. It was not re-added for the Gradle plugin (Portal-only).
7. **CI secrets renamed.** `CI_SECRET_KEY_RING_FILE` / `CI_SECRET_PROPERTIES_FILE` became
   `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD`, `SIGNING_KEY`,
   `SIGNING_KEY_PASSPHRASE` (plus `GRADLE_PUBLISH_KEY` / `GRADLE_PUBLISH_SECRET`). Repository
   secrets must be updated.
8. **Wasm artifact IDs.** The Toolchain publishes `-wasmjs` / `-wasmwasi`; Gradle used
   `-wasm-js` / `-wasm-wasi`. The root `.module` metadata points at the new names, so consumers
   resolve the right files.
9. **Gradle Plugin Portal signing.** The Portal does not require signatures; the nested build
   signs the Maven publications when a key is available, but the Portal's own upload format is
   controlled by `com.gradle.plugin-publish`.
10. **Maven Central is manual by default.** `publishingMode` defaults to `manual`: a human must
    release the deployment in the Central Portal UI.
