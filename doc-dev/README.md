# doc-dev — developer & agent documentation

This directory contains the **internal** documentation for developing, testing and releasing
i18n4k. User-facing documentation (formatters, message syntax, …) lives in [`../doc`](../doc)
and [`../readme.md`](../readme.md).

The build was migrated from Gradle to the **Kotlin Toolchain 0.14.1** (formerly Amper).
`examples/*` and the `i18n4k-gradle-plugin` intentionally remain Gradle builds.

## Start here

| If you want to… | Read |
|---|---|
| Build, test, or understand the repository layout | [`build-system.md`](build-system.md) |
| Release to Maven Central / the Gradle Plugin Portal | [`deployment.md`](deployment.md) |
| Understand what changed during the Gradle → Toolchain migration | [`migration-from-gradle.md`](migration-from-gradle.md) |
| Work as / with an AI agent | [`../agent.md`](../agent.md) and [`agent-skills.md`](agent-skills.md) |
| Refresh the vendored Kotlin agent skills | [`agent-skills.md`](agent-skills.md); monthly via [`../.github/workflows/update_agent_skills.yml`](../.github/workflows/update_agent_skills.yml) or `./update-skills.sh` / `./update-skills.ps1` |

## Quick commands

```sh
./kotlin build                    # build the library modules (host-supported platforms)
./kotlin test                     # run the tests
./kotlin publish mavenLocal       # install the libraries to ~/.m2 (needed by the Gradle builds)
./kotlin show modules             # inspect the Toolchain project model

# Gradle plugin (nested Gradle build)
cd i18n4k-gradle-plugin
./gradlew build                   # validate + test the plugin
```

On Windows use `./kotlin.bat` and `.\gradlew.bat`.

## Rules of thumb

- **Never** add Gradle build files to `i18n4k-core`, `i18n4k-cldr-plural-rules` or
  `i18n4k-generator`; they are Kotlin Toolchain modules.
- **Never** change `examples/*`; they are consumer samples for the Gradle plugin.
- Keep `i18n4k-gradle-plugin` on Gradle; the Toolchain has no Gradle-plugin product type.
- The Kotlin Toolchain publishes **all** platforms from **one** host. Do not publish from
  several CI hosts (Maven Central rejects duplicates).
