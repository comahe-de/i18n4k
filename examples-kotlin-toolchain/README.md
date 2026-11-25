# i18n4k — Kotlin Toolchain examples

Kotlin Toolchain counterparts of the Gradle examples in [`../examples`](../examples).

Each entry is a **standalone Kotlin Toolchain project** with its own
`project.yaml` and `kotlin`/`kotlin.bat` wrapper. Like the Gradle examples, they
do **not** belong to the repository's root `project.yaml`; they consume the
published i18n4k artifacts (Maven Central, or Maven Local after
`./kotlin publish mavenLocal`).

## [`example-mpp`](example-mpp)

Kotlin Multiplatform application and counterpart of
[`../examples/example-mpp`](../examples/example-mpp):

* `shared` — `kmp/lib` (JVM, JS, Native) with the i18n4k plugin enabled and the
  message bundles.
* `jvm-app` — Swing desktop app.
* `js-app` — browser app.
* `windows-app` / `linux-app` / `macos-app` — console apps for the three native
  platforms.
* `plugins/i18n4k` — the i18n4k Kotlin Toolchain plugin (thin wrapper around the
  published `i18n4k-generator` library).

See [`example-mpp/README.md`](example-mpp/README.md) for run instructions.

## [`example-compose`](example-compose)

Compose Multiplatform application and counterpart of
[`../examples/example-compose`](../examples/example-compose):

* `shared` — `kmp/lib` with the Compose UI, the i18n4k plugin, message bundles
  and Compose resources.
* `desktopApp` — Compose Desktop app.
* `webApp` — Compose Multiplatform browser app (Kotlin/Wasm).
* `androidApp` — Android app.
* `iosApp` — iOS app (macOS/Xcode only).
* `plugins/i18n4k` — the i18n4k Kotlin Toolchain plugin (thin wrapper around the
  published `i18n4k-generator` library).

See [`example-compose/README.md`](example-compose/README.md) for run instructions.
