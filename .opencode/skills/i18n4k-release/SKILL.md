---
name: i18n4k release
description: Load when releasing i18n4k, bumping its version, publishing to Maven Central or the Gradle Plugin Portal, or debugging the publish workflow. Covers signing keys, environment variables, single-host publishing and Javadoc placeholders.
---

# i18n4k release

Full details: [`doc-dev/deployment.md`](../../../doc-dev/deployment.md).

## What is published where

| Artifact | Repository | Command |
|---|---|---|
| libraries (all platforms) | Maven Central (Central Portal) | `./kotlin publish mavenCentral` |
| `i18n4k-gradle-plugin` + marker | Gradle Plugin Portal | `./gradlew publishPlugins` (nested build) |

The tag `v*.*.*` triggers `.github/workflows/publish_artifacts.yml`, which runs on **Windows
only**. This is intentional: the Kotlin Toolchain cross-compiles and publishes all platforms
(including Apple klibs) from one host, and Maven Central rejects duplicate coordinates.
macOS is not required for publishing.

## Secrets (GitHub Actions)

| Secret | Environment variable |
|---|---|
| `MAVEN_CENTRAL_USERNAME` | `KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_USERNAME` |
| `MAVEN_CENTRAL_PASSWORD` | `KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_PASSWORD` |
| `SIGNING_KEY` (ASCII-armored PGP private key) | `KOTLIN_TOOLCHAIN_SIGNING_KEY` |
| `SIGNING_KEY_PASSPHRASE` | `KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE` |
| `GRADLE_PUBLISH_KEY` | `GRADLE_PUBLISH_KEY` |
| `GRADLE_PUBLISH_SECRET` | `GRADLE_PUBLISH_SECRET` |

Libraries are signed because `templates/publish.module-template.yaml` sets
`signArtifacts: true`. The Gradle plugin signs its Maven publications when
`KOTLIN_TOOLCHAIN_SIGNING_KEY` is present.

## Release checklist

1. Bump the version in **both** places:
   - `templates/publish.module-template.yaml` (`settings.publishing.version`)
   - `i18n4k-gradle-plugin/build.gradle.kts` (`version = …`)
2. Local verification:
   ```sh
   ./kotlin test
   ./kotlin publish mavenLocal
   cd i18n4k-gradle-plugin && ./gradlew build
   ```
3. Commit, tag (`vX.Y.Z`), push the tag.
4. Watch `Publish artifacts`.
5. Maven Central: release the pending deployment in the Central Portal UI
   (`publishingMode` defaults to `manual`).
6. Gradle Plugin Portal: wait for Gradle's review.

## Caveats

- **Do not** add a second publishing host; see above.
- The Toolchain's library `-javadoc.jar` files are **empty placeholders** (a single
  `README.md`, required by Maven Central). There is no Dokka integration and no API
  documentation is generated at the moment.
- `publish mavenCentral` with `publishingMode: manual` stops after uploading; artefacts are not
  public until released in the Portal.
- Never reuse a release version: Maven Central forbids deleting/overwriting artifacts.
