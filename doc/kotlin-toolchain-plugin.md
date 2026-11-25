# i18n4k Kotlin Toolchain plugin

The Kotlin Toolchain counterpart of the [i18n4k Gradle plugin](../i18n4k-gradle-plugin).
It runs the i18n4k code generator as part of a Kotlin Toolchain build and
contributes the generated Kotlin sources and language resource files to the
module.

> **Status:** The Kotlin Toolchain plugin system is a preview. In particular,
> custom plugins **cannot be packaged and published yet** ("Packaging and
> publishing plugins" on the
> [roadmap](https://kotlin-toolchain.org/latest/user-guide/plugins/overview/#planned-for-upcoming-releases),
> as of 2026-11-25). Until then, use the thin-wrapper approach described in
> [Sharing the plugin](#sharing-the-plugin) below.

## What it does

For every message bundle (`*.properties` file) in the configured input
directory the plugin generates:

* a Kotlin object with one typed property per message key,
* optionally one private object per locale with the translations inlined into
  the Kotlin code (`sourceCodeLocales`),
* one `*.i18n4k.txt` resource file per locale that is not inlined.

The outputs are declared in the plugin's `plugin.yaml` under
`generated.sources` and `generated.resources`. The Kotlin Toolchain therefore

* compiles the generated Kotlin code as part of the module,
* packages the generated language files as module resources,
* shows the generated directories in IntelliJ IDEA as source/resource folders
  of the module (they are imported through the Kotlin Toolchain, which knows
  about the `generated.*` contributions),
* re-runs the generator when the properties files or the settings change
  (execution avoidance based on the declared task inputs/outputs).

## Requirements

* Kotlin Toolchain **0.14.1** (the version pinned by `kotlin`/`kotlin.bat` in
  this repository).
* `i18n4k-core` and `i18n4k-generator` **0.11.2** or newer.

## Sharing the plugin

A Kotlin Toolchain project can only use *local* plugin modules, so the plugin
has to live inside the consuming project. The plugin here is deliberately thin:
all generation logic is in the published `i18n4k-generator` library, which is
exactly the workaround recommended by JetBrains for sharing plugins before
packaging is supported.

The reference copy lives in
[`examples-kotlin-toolchain/example-mpp/plugins/i18n4k`](../examples-kotlin-toolchain/example-mpp/plugins/i18n4k)
and is used by that example. `example-compose` contains an identical copy, as
every standalone project needs its own local plugin. **Copy that directory** to
`plugins/i18n4k` of your project:

```
your-project/
├─ project.yaml
├─ plugins/
│  └─ i18n4k/          # copied from examples-kotlin-toolchain/example-mpp/plugins/i18n4k
│     ├─ module.yaml   # already depends on the published Maven artifacts
│     ├─ plugin.yaml
│     └─ src/          # I18n4kSettings.kt, GenerationTarget.kt, tasks/*.kt
├─ app/
│  ├─ module.yaml
│  └─ src/
└─ ...
```

A git submodule pointing at the example project works as well. Its
`module.yaml` uses the published artifacts:

```yaml
# plugins/i18n4k/module.yaml
product: jvm/amper-plugin

pluginInfo:
  id: i18n4k
  settingsClass: de.comahe.i18n4k.toolchain.I18n4kSettings

settings:
  jvm:
    jdk:
      version: 17

dependencies:
  # Core logic, published as a normal JVM library.
  - de.comahe.i18n4k:i18n4k-generator:0.11.2
  # Types used in the task-action signature (MessageFormatter, forLocaleTag).
  # i18n4k-generator declares i18n4k-core with runtime scope only, so it has to
  # be added explicitly for compilation.
  - de.comahe.i18n4k:i18n4k-core-jvm:0.11.2
  - org.slf4j:slf4j-api:2.0.17
```

`i18n4k-generator` and `i18n4k-core-jvm` are on Maven Central, which is the
default repository of the Kotlin Toolchain. Add `mavenLocal` while testing
locally built versions:

```yaml
# plugins/i18n4k/module.yaml (excerpt)
repositories:
  - mavenLocal
```

Register the plugin and enable it per module:

```yaml
# project.yaml
modules:
  - app
  - plugins/i18n4k

plugins:
  - //plugins/i18n4k
```

```yaml
# app/module.yaml
plugins:
  i18n4k:
    enabled: true
```

`plugins: i18n4k: enabled` is enough for the default configuration.

## Configuration

All settings live under `plugins.i18n4k` in `module.yaml`:

| Setting | Type | Default | Description |
|---|---|---|---|
| `enabled` | boolean | `false` | Enables the plugin in this module. |
| `inputDirectory` | string | `"i18n"` | Directory with the message properties files, relative to the module root. Use `src/main/i18n` for `layout: maven-like`. |
| `packageName` | string? | `null` | Package of the generated classes. `null` derives it from the path below `inputDirectory`. |
| `commentLocale` | string? | `"en"` | Locale whose texts are added as KDoc comments. `null` disables comments. |
| `sourceCodeLocales` | list? | `null` | Locales inlined into the Kotlin code. `null` = all, empty list = none. |
| `generationTarget` | enum | `MULTI_PLATFORM` | `MULTI_PLATFORM`, `JVM`, `ANDROID`, `JS` or `NATIVE`. |
| `valueTypesEnabled` | boolean | `true` | Generate typed factories for messages with value types (`{0:int}`). |
| `valueTypeMapping` | map | `{}` | Map value type names to fully qualified class names. |
| `enableJsExportAnnotation` | boolean | `false` | **Experimental:** add `@JsExport` to generated bundle objects. |

Example:

```yaml
# app/module.yaml
product: jvm/app

plugins:
  i18n4k:
    enabled: true
    sourceCodeLocales:
      - en
      - de

settings:
  jvm:
    mainClass: example.MainKt
    jdk:
      version: 17

dependencies:
  - de.comahe.i18n4k:i18n4k-core-jvm:0.11.2
```

For a Kotlin Multiplatform module use `de.comahe.i18n4k:i18n4k-core` (without the
`-jvm` suffix), as in
[`examples-kotlin-toolchain/example-mpp/shared/module.yaml`](../examples-kotlin-toolchain/example-mpp/shared/module.yaml).
Mark it `exported` if the generated accessors are part of the module API.

## Message bundles

By default the bundles are searched in `i18n` at the module root (Toolchain
layout) or `src/main/i18n` (with `layout: maven-like`, configured via
`inputDirectory`). `i18n` is a build input directory, not a source or resource
directory: the `.properties` files are **not** added to the module's resources
and do not end up in the artifact. Only the generated Kotlin code is compiled,
and — for locales that are not inlined — the generated `*.i18n4k.txt` files are
added through `generated.resources`. Subdirectories become packages:

```
app/i18n/x/y/MyMessages_en.properties
app/i18n/x/y/MyMessages_de.properties
```

generates the `x.y.MyMessages` object. The generated language files are put at
the root of the generated resources, keeping the package path:

```
x/y/MyMessages_de.i18n4k.txt      -> classpath resource "/x/y/MyMessages_de.i18n4k.txt"
```

## Using the generated code

```kotlin
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.provider.MessagesProviderViaResource
import x.y.MyMessages

fun main() {
    val config = I18n4kConfigDefault()
    i18n4k = config

    // Locales not listed in sourceCodeLocales are loaded from the generated resources.
    MyMessages.registerTranslation(
        MessagesProviderViaResource(pathToResource = "/x/y/MyMessages_de.i18n4k.txt")
    )

    config.locale = forLocaleTag("de")
    println(MyMessages.sayHello("Kotlin"))
}
```

The language files are contributed with `generated.resources`, so they are part
of the module's runtime classpath. Nothing has to be copied into `resources`
manually, and no `resources` directory is required.

## Tasks and commands

| Command | Description |
|---|---|
| `./kotlin do generateI18n4kFiles` | Runs the generation task explicitly (skipped when up to date). |
| `./kotlin do clearI18n4kFiles` | Deletes the generated sources and resources. |

The generation task itself (`:app:generate@i18n4k`) is not meant to be invoked
directly. It runs automatically whenever its outputs are needed, and is skipped
when the properties files and the settings are unchanged.

Add `-m <module>` to restrict a command to one module, e.g.
`./kotlin do clearI18n4kFiles -m app`.

## Kotlin Multiplatform

The default `generationTarget: MULTI_PLATFORM` produces common Kotlin code:

* `@JvmField` is added for JVM/Android (ignored by the JS/Native compilers),
* `@JsExport` is added for `JS`/`MULTI_PLATFORM` if
  `enableJsExportAnnotation` is on.

The `generated.sources`/`generated.resources` entries have no `fragment`, so
the Kotlin Toolchain attributes them to the common part of the module. For
platform-specific generation use the `fragment` clause in `plugin.yaml`:

```yaml
generated:
  sources:
    - language: kotlin
      directory: ${tasks.generate.action.generatedSourcesDir}
      fragment:
        modifier: jvm
```

At the moment the plugin registers one generation task per module, so a single
`fragment`/`generationTarget` applies to the whole module. Generating different
code per platform (e.g. `JS` and `NATIVE` variants of the same bundles) needs
separate modules or separate task registrations.

## Android

`generationTarget: ANDROID` makes the generator write the language files in the
"raw Android resource" style (flat directory, lower-case snake_case names), like
the Gradle plugin. The files are still contributed as normal module resources.
The Kotlin Toolchain plugin API currently offers no way to register generated
directories as Android `res/raw` folders, so the Android-specific
`R.raw.<name>` lookup of the Gradle plugin is **not** available. Treat Android
support as experimental until it has been validated with a Toolchain Android
app.

## Differences from the Gradle plugin

| Gradle plugin | Kotlin Toolchain plugin |
|---|---|
| Applied with `id("de.comahe.i18n4k")` | Copied as a local plugin module; enabled with `plugins: i18n4k: enabled`. |
| `i18n4k { ... }` extension | `plugins.i18n4k` block in `module.yaml`. |
| Auto-detects the Kotlin/Android plugin type | No plugin introspection: `generationTarget` defaults to `MULTI_PLATFORM` (works for common code and JVM). |
| `sourceCodeOutputDirectory` / `languageFilesOutputDirectory` | Outputs are always the task's output directory (`${taskOutputDir}`). |
| `messageFormatter` (object reference) | Fixed to `MessageFormatterDefault` (configurable types cannot reference runtime objects). |
| Wires the generator before every compile/resource/Jar task | The Toolchain derives the dependencies from `generated.sources`/`generated.resources`. |
| `clearI18n4kFiles` task | `./kotlin do clearI18n4kFiles` command. |
| Compose/Android source-set integration | Not available in the plugin API (yet). |
| `-P` properties / Gradle cache configuration | No equivalent; the Toolchain handles execution avoidance. |

## Example projects

### `example-mpp`

[`examples-kotlin-toolchain/example-mpp`](../examples-kotlin-toolchain/example-mpp)
is a standalone Kotlin Toolchain project (its own `project.yaml` and
`kotlin`/`kotlin.bat` wrapper) and the counterpart of
[`examples/example-mpp`](../examples/example-mpp). Like the Gradle examples, it
is not part of the repository's root project and consumes the published
artifacts:

* `shared` — a `kmp/lib` (JVM, JS, Native) with the plugin enabled, the message
  bundles in `i18n`, inline translations for `en`, `en_US` and `de`, and
  generated resources for the other locales.
* `jvm-app` — a `jvm/app` Swing desktop application; it also loads the
  generated resource files.
* `js-app` — a `js/app` browser application (compile-only in Toolchain 0.14.1,
  see below).
* `windows-app`, `linux-app`, `macos-app` — console apps for the three native
  platforms.
* `plugins/i18n4k` — the plugin.

Run it from the example project directory:

```sh
cd examples-kotlin-toolchain/example-mpp

./kotlin build
./kotlin test -m shared -p jvm
./kotlin run -m jvm-app              # Swing app
./kotlin run -m windows-app          # or linux-app / macos-app
```

On Windows use `./kotlin.bat`. To test the current sources instead of the
released artifacts, run `./kotlin publish mavenLocal` in the repository root
first; the example's plugin module has `mavenLocal` in its repository list.

The `js/app` product type is an incomplete preview in Kotlin Toolchain 0.14.1:
`build` compiles it, but the CLI neither links a runnable `.mjs` file nor runs
it. See [`example-mpp/README.md`](../examples-kotlin-toolchain/example-mpp/README.md).

### `example-compose`

[`examples-kotlin-toolchain/example-compose`](../examples-kotlin-toolchain/example-compose)
is the standalone Compose Multiplatform port of
[`examples/example-compose`](../examples/example-compose) and demonstrates
typed parameters, plural/ordinal selection, message attributes and value types:

* `shared` — a `kmp/lib` with the Compose UI, Compose resources and the plugin.
  **All locales are inlined** into the generated code, so no language files
  have to be loaded at runtime.
* `desktopApp` (`jvm/app`), `webApp` (`wasm-js/app`), `androidApp`
  (`android/app`) and `iosApp` (`ios/app`, macOS/Xcode only).
* `plugins/i18n4k` — the plugin.

```sh
cd examples-kotlin-toolchain/example-compose
./kotlin build
./kotlin test -m shared -p jvm
./kotlin run -m desktopApp
```

The Gradle plugin writes non-inlined language files into
`composeResources/files/i18n`. The Toolchain plugin always writes its outputs
below the task output directory, so this project inlines every locale instead.
See [`example-compose/README.md`](../examples-kotlin-toolchain/example-compose/README.md).

## Verified

Verified on Windows with Kotlin Toolchain 0.14.1.

### `example-mpp`

Run from `examples-kotlin-toolchain/example-mpp`:

| Command | Result |
|---|---|
| `./kotlin show modules` | plugin, shared and 5 app modules |
| `./kotlin build` | all host-supported modules/platforms (incl. native apps) |
| `./kotlin test` | 4 JVM tests + 3 native tests pass |
| `./kotlin test -m shared -p jvm` | inline translations and generated resources pass |
| `./kotlin run -m jvm-app` | Swing app starts |
| `./kotlin run -m windows-app` | native console app prints the locales |
| `./kotlin do clearI18n4kFiles -m shared` | removes both output directories |
| Repository root: `./kotlin publish mavenLocal` | provides the artifacts the example consumes |

### `example-compose`

Run from `examples-kotlin-toolchain/example-compose`:

| Command | Result |
|---|---|
| `./kotlin build` | desktop, Wasm and Android apps build (iOS is skipped off macOS) |
| `./kotlin test -m shared -p jvm` | 5 translation tests (incl. plurals, ordinals, attributes) pass |
| `./kotlin run --no-compose-hot-reload -m desktopApp` | Compose Desktop app starts |

The iOS app requires macOS/Xcode and was not verified on Windows.
