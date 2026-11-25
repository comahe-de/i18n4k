---
name: i18n4k build
description: Load when building, testing, changing the build configuration, or troubleshooting i18n4k. Covers the Kotlin Toolchain layout, the nested Gradle plugin build, examples, Windows specifics and known pitfalls.
---

# i18n4k build

Repository-specific build knowledge. Full details: [`doc-dev/build-system.md`](../../../doc-dev/build-system.md).

## Non-negotiable rules

- `i18n4k-core`, `i18n4k-cldr-plural-rules`, `i18n4k-generator` are **Kotlin Toolchain**
  modules. Never add `build.gradle.kts`/`settings.gradle.kts` there; never run Gradle for them.
- Use `./kotlin` on Linux/macOS and `./kotlin.bat` on Windows (the shell wrapper cannot run
  under PowerShell). Same for `./gradlew` vs `.\gradlew.bat`.
- **Never modify `examples/*`** — they are consumer samples for the Gradle plugin.
- `i18n4k-gradle-plugin` stays a **nested Gradle build**; it needs the libraries in
  `~/.m2` first (`./kotlin publish mavenLocal`).
- Do not publish from several CI hosts. The Toolchain publishes all platforms from one host;
  Maven Central rejects duplicates.

## Common tasks

```sh
./kotlin show modules
./kotlin build                          # host-supported platforms
./kotlin test
./kotlin test -p jvm                    # fast feedback
./kotlin publish mavenLocal
./kotlin build -m i18n4k-core -p js -p wasmJs -p wasmWasi

cd i18n4k-gradle-plugin
./gradlew build                         # compile + validatePlugins
./gradlew publishToMavenLocal
./gradlew dokkaGeneratePublicationHtml  # build/dokka/html
```

On Windows replace `./kotlin` with `./kotlin.bat` and `./gradlew` with `.\gradlew.bat`.

## Layout facts

- Sources: `src` (common), `test`, `src@jvm`, `src@android`, `src@js`, `src@wasmJs`,
  `src@androidNative`, `cinterop@androidNative`, … (no `src/commonMain/kotlin`).
- `i18n4k-generator` uses `layout: maven-like`: `src/main/kotlin`, `src/test/kotlin`.
- Dependencies: `$libs.<key>` from `libs.versions.toml`; local modules as `//i18n4k-core`.
- Templates must not contain `product:`, `apply:`, `aliases:` or `description:`.

## Known pitfalls

| Symptom | Cause / fix |
|---|---|
| `InvalidPathException: Illegal char <:>` on JS/Wasm (Windows) | Missing `-Xir-module-name` workaround in the KMP `module.yaml`; see `doc-dev/build-system.md` |
| `argument -Xir-module-name is passed multiple times` | Expected on test compilations; harmless |
| `Selected modules have dependencies on … local modules` | Add `--transitive` or `--non-transitive` |
| `Could not find de.comahe.i18n4k:i18n4k-core-jvm` in the plugin build | Run `./kotlin publish mavenLocal` |
| `validatePlugins` fails | Task input/output annotations need fixing in `i18n4k-gradle-plugin/src` |
| Dokka crashes with `KaFirDefaultPropertyGetterSymbol` | Do not put `i18n4k-core-jvm`/`i18n4k-generator` on the Dokka source-set classpath; use `docsClasspath` |
| `./kotlin` does nothing on Windows | Use `./kotlin.bat` |

## Before claiming success

Run `./kotlin test` (or at least `-p jvm`) and `cd i18n4k-gradle-plugin && ./gradlew build`.
Report which OS/targets were actually exercised; Apple binaries/tests require macOS.
