# Deployment

i18n4k publishes to two repositories:

| Artifact | Repository | Tool |
|---|---|---|
| `i18n4k-core`, `i18n4k-cldr-plural-rules`, `i18n4k-generator` (all platforms) | **Maven Central** (Central Portal) | `./kotlin publish mavenCentral` |
| `i18n4k-gradle-plugin` + plugin marker | **Gradle Plugin Portal** | `./gradlew publishPlugins` in `i18n4k-gradle-plugin/` |

Everything is triggered by pushing a `v*.*.*` (or `v*.*.*-*`) tag. The single workflow
[`.github/workflows/publish_artifacts.yml`](../.github/workflows/publish_artifacts.yml) runs on
**Windows** and does: tests → Maven Central → Gradle plugin.

## Why a single (Windows) publishing host?

Gradle's KMP plugin publishes only the targets that can be built on the current host, so the
old build split publications between Linux, Windows and macOS. The Kotlin Toolchain instead
**cross-compiles and publishes every platform from a single host** — verified on Windows
(Apple klibs such as `i18n4k-core-iosarm64` and `i18n4k-core-watchosarm32` are produced there).

Maven Central rejects duplicate coordinates, so publishing from several hosts would fail.
macOS is **not required** for publishing: Apple tests/binaries still need macOS, but the
published artifacts are klibs, which can be cross-compiled. Release checks on all three OSes
continue to run in [`run_checks.yml`](../.github/workflows/run_checks.yml) for every push.

## Required GitHub Actions secrets

Configure these repository secrets (`Settings → Secrets and variables → Actions`):

| Secret | Purpose | Exposed to the build as |
|---|---|---|
| `MAVEN_CENTRAL_USERNAME` | Central Portal user-token username | `KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_USERNAME` |
| `MAVEN_CENTRAL_PASSWORD` | Central Portal user-token password | `KOTLIN_TOOLCHAIN_MAVEN_CENTRAL_PASSWORD` |
| `SIGNING_KEY` | ASCII-armored PGP **private** key used to sign all artifacts | `KOTLIN_TOOLCHAIN_SIGNING_KEY` |
| `SIGNING_KEY_PASSPHRASE` | Passphrase of the PGP key (may be empty) | `KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE` |
| `GRADLE_PUBLISH_KEY` | Gradle Plugin Portal API key | `GRADLE_PUBLISH_KEY` |
| `GRADLE_PUBLISH_SECRET` | Gradle Plugin Portal API secret | `GRADLE_PUBLISH_SECRET` |

Optional:

| Variable | Purpose |
|---|---|
| `KOTLIN_TOOLCHAIN_SIGNING_KEY_ID` | Select a specific signing sub-key if the key contains several |

The library artifacts are signed because
[`templates/publish.module-template.yaml`](../templates/publish.module-template.yaml) sets
`signArtifacts: true`. The Gradle plugin is signed by the nested build when
`KOTLIN_TOOLCHAIN_SIGNING_KEY` is present (see `i18n4k-gradle-plugin/build.gradle.kts`).

### Creating / exporting the PGP key

```sh
# List keys
gpg --list-secret-keys --keyid-format LONG

# Export the private key in ASCII-armored form (this goes into SIGNING_KEY)
gpg --export-secret-keys --armor <KEY_ID> > signing-key.asc
```

Store the **whole file** (including `-----BEGIN PGP PRIVATE KEY BLOCK-----`) in `SIGNING_KEY`.
The public key must be published to a key server (e.g. `keys.openpgp.org`) so Maven Central can
verify the signatures.

### Publishing mode (manual vs. automatic)

`templates/publish.module-template.yaml` currently uses the Toolchain default (`manual`):
`./kotlin publish mavenCentral` uploads a bundle, waits for validation, and stops. A human must
release it in the Central Portal UI (`Deployments` → select deployment → `Publish`).

To release automatically:

```yaml
settings:
  publishing:
    mavenCentral:
      enabled: true
      publishingMode: auto   # or "manual" (default)
```

> Maven Central artifacts cannot be deleted. Use `auto` only after a proven release process.

## Local dry run (no credentials needed)

```sh
# Libraries -> ~/.m2/repository
./kotlin publish mavenLocal

# Gradle plugin -> ~/.m2/repository (also validates the plugin)
cd i18n4k-gradle-plugin
./gradlew publishToMavenLocal
```

`publish mavenLocal` does **not** sign (signatures are only needed for Maven Central). It also
produces Apple klibs on Windows/Linux, which is expected.

To check the signatures without uploading anything, build the Maven Central bundle instead:

```sh
KOTLIN_TOOLCHAIN_SIGNING_KEY="$(cat signing-key.asc)" \
KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE="…" \
./kotlin package --format=maven-central-bundle
```

Look for a `.asc` file next to every artifact in the bundle directory.

## Release checklist

1. Bump the version in **two** places (they must match the tag):
   - `templates/publish.module-template.yaml` → `settings.publishing.version`
   - `i18n4k-gradle-plugin/build.gradle.kts` → `version = "…"`
2. Run the local checks:
   ```sh
   ./kotlin test
   ./kotlin build
   ./kotlin publish mavenLocal
   cd i18n4k-gradle-plugin && ./gradlew build
   ```
3. Commit (`release: vX.Y.Z`).
4. Push a tag:
   ```sh
   git tag vX.Y.Z
   git push origin main --tags
   ```
5. Watch `Publish artifacts` in GitHub Actions.
6. **Maven Central**: open the Central Portal and release the pending deployment (unless
   `publishingMode: auto`).
7. **Gradle Plugin Portal**: the plugin is reviewed by Gradle before it appears publicly
   (usually a few days). Check the Portal for the new version.
8. Verify that the library POMs on Maven Central have the expected dependencies.

## Javadoc artifacts

The Kotlin Toolchain 0.14.1 has no Dokka integration. It creates the `-javadoc.jar` required by
Maven Central automatically, but the jar contains only a `README.md` placeholder. This cannot be
changed without replacing the Toolchain's publishing pipeline (it does not expose a hook), so no
API documentation is generated or published at the moment.

If real API documentation is needed again, publish it as a separate artifact or move publishing
back to Gradle; see [`migration-from-gradle.md`](migration-from-gradle.md#known-limitations).

## Troubleshooting

| Symptom | Fix |
|---|---|
| `Selected modules have dependencies on … local modules` | Add `--transitive` or `--non-transitive` |
| `InvalidPathException: Illegal char <:>` on Windows Web builds | The `-Xir-module-name` workaround is missing; see [`build-system.md`](build-system.md#windows-web-target-workaround) |
| `Could not find …i18n4k-core-jvm` in the Gradle plugin build | Run `./kotlin publish mavenLocal` first |
| `validatePlugins` fails | Task annotations changed; run `./gradlew build` locally |
| Zipping / publishing fails with a signature error | `SIGNING_KEY` is missing or not an ASCII-armored private key |
| Second publishing host fails with duplicate artifacts | Only publish from **one** host (the workflow does) |
