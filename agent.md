# agent.md — i18n4k

Entry point for AI agents (and humans) working on this repository. Keep this file short;
put details in [`doc-dev/`](doc-dev/) and load the matching skill when needed.

## What this repository is

**i18n4k** — multiplatform (JVM, Android, JS, Wasm, native) internationalization library and code
generator for Kotlin. Group `de.comahe.i18n4k`, version `0.11.2`.

| Directory | Build | Content |
|---|---|---|
| `i18n4k-core` | Kotlin Toolchain `kmp/lib` | runtime library |
| `i18n4k-cldr-plural-rules` | Kotlin Toolchain `kmp/lib` | CLDR plural formatters |
| `i18n4k-generator` | Kotlin Toolchain `jvm/lib` | code generator (KotlinPoet) |
| `i18n4k-gradle-plugin` | **Gradle** (nested build) | Gradle plugin |
| `examples/*` | **Gradle** | Gradle consumer samples — **not** part of the root project |
| `examples-kotlin-toolchain/*` | Kotlin Toolchain (standalone projects) | Kotlin Toolchain consumer samples; **not** part of the root project |

Build system: **Kotlin Toolchain 0.14.1** (formerly Amper), wrapper pinned in `kotlin`/`kotlin.bat`.

## Hard rules

1. Never add Gradle files to the Toolchain modules and never run Gradle for them.
2. `examples/*`. and  `examples-kotlin-toolchain/*` are standalone projects consuming the published artifacts —
   do not add them to the root `project.yaml` or make them depend on local modules.
3. Keep `i18n4k-gradle-plugin` on Gradle: the Toolchain has no Gradle-plugin product type.
4. On Windows use `./kotlin.bat` / `.\gradlew.bat`; the extension-less shell wrappers do not run
   under PowerShell.
5. Publish to Maven Central from **one** host only. The Toolchain publishes all platforms
   (incl. cross-compiled Apple klibs) from a single machine; Central rejects duplicates.
6. Do not commit generated build output; `build/`, `.kotlin/`, `.gradle/` are ignored.

## Everyday commands

```sh
./kotlin show modules
./kotlin build
./kotlin test
./kotlin test -p jvm
./kotlin publish mavenLocal

cd i18n4k-gradle-plugin
./gradlew build

cd ../examples-kotlin-toolchain/example-mpp
./kotlin build
./kotlin run -m jvm-app

cd ../example-compose
./kotlin build
./kotlin run -m desktopApp
```

## Where to look

| Topic | Document |
|---|---|
| Build layout, commands, source dirs, Pitfalls | [`doc-dev/build-system.md`](doc-dev/build-system.md) |
| Kotlin Toolchain plugin usage and limitations | [`doc/kotlin-toolchain-plugin.md`](doc/kotlin-toolchain-plugin.md) |
| Maven Central + Plugin Portal, secrets, signing, release checklist | [`doc-dev/deployment.md`](doc-dev/deployment.md) |
| What changed in the Gradle → Toolchain migration, limitations | [`doc-dev/migration-from-gradle.md`](doc-dev/migration-from-gradle.md) |
| Agent skills, how to refresh upstream skills | [`doc-dev/agent-skills.md`](doc-dev/agent-skills.md) |
| Human-facing docs | [`doc/`](doc/), [`readme.md`](readme.md) |

## Skills

Project skills live in [`.opencode/skills/`](.opencode/skills):

- `i18n4k-build` — repository-specific build/test/troubleshooting.
- `i18n4k-release` — release/publishing process.
- `kotlin-tooling-kotlin-toolchain`, `kotlin-tooling-gradle-to-kotlin-toolchain-project`,
  `kotlin-tooling-gradle-to-kotlin-toolchain-plugin` — vendored JetBrains skills.

Refresh the upstream skills with `./doc-dev/update-skills.sh` (or `.ps1`).

## Current verified state (Windows)

Kotlin Toolchain 0.14.1 (`0.14.0-dev-4630`):

- `./kotlin show modules` → 3 modules
- `./kotlin build` → all host-supported targets
- `./kotlin test` → all host-supported targets, 112 JVM tests
- `./kotlin publish mavenLocal` → all modules/platforms
- nested Gradle plugin `build`, `publishToMavenLocal` → OK
- `examples-kotlin-toolchain/example-mpp`: `./kotlin build`, `./kotlin test` (4 JVM + 3 native
  tests), and the JVM/native apps work
- `examples-kotlin-toolchain/example-compose`: `./kotlin build` (desktop, web and Android;
  iOS is skipped off macOS), `./kotlin test -m shared -p jvm` (5 tests), and the desktop app
  starts with `./kotlin run --no-compose-hot-reload -m desktopApp`

Apple binaries/tests still require macOS; `run_checks.yml` covers that host.

## Important gotchas

- **Windows Web builds** need the `-Xir-module-name` workaround (already in both KMP
  `module.yaml` files). Details: [`doc-dev/build-system.md`](doc-dev/build-system.md#windows-web-target-workaround).
- `-m` with local dependencies requires `--transitive` or `--non-transitive`.
- The nested Gradle plugin build resolves `i18n4k-core-jvm` / `i18n4k-generator` from Maven
  Local: run `./kotlin publish mavenLocal` first.
- The Kotlin Toolchain example is a standalone project too. It consumes the published 0.11.2
  artifacts (Maven Local first, then Maven Central); run `./kotlin publish mavenLocal` in the
  repository root before building it against the current sources.
- Kotlin Toolchain native apps (`windows/app`, `linux/app`, `macos/app`) default to a top-level
  `main` in the **root package**. With a package declaration, set
  `settings.native.entryPoint: <package>.main` (as the example's `*-app/module.yaml` files do),
  otherwise linking fails with `could not find '/main' function`.
- Library `-javadoc.jar` files are Toolchain placeholders (a single `README.md`). The Toolchain
  has no Dokka integration, and no API documentation is generated at the moment.
- The tooling expects the current Toolchain syntax: templates reject `aliases:`/`description:`;
  use `module.yaml` for those.

## References

- Kotlin Toolchain docs: <https://kotlin-toolchain.org/>
- Migration guide: <https://kotlin-toolchain.org/dev/getting-started/migrating-from-gradle/>
- Kotlin agent skills: <https://github.com/Kotlin/kotlin-agent-skills>
