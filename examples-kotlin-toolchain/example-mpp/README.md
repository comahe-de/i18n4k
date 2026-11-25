# i18n4k — example-mpp (Kotlin Toolchain)

Standalone Kotlin Toolchain port of
[`../../examples/example-mpp`](../../examples/example-mpp). It is a full
multi-platform application, not a library:

| Module | Product | Description |
|---|---|---|
| `shared` | `kmp/lib` | Message bundles, generated i18n code and shared helpers (JVM, JS, Native). |
| `jvm-app` | `jvm/app` | Swing desktop application with a text field and locale buttons. |
| `js-app` | `js/app` | Plain-DOM browser application. |
| `windows-app` | `windows/app` | Kotlin/Native console application. |
| `linux-app` | `linux/app` | Kotlin/Native console application. |
| `macos-app` | `macos/app` | Kotlin/Native console application. |
| `plugins/i18n4k` | `jvm/amper-plugin` | The i18n4k Kotlin Toolchain plugin. |

Because the Kotlin Toolchain cannot publish custom plugins yet, the plugin is a
thin local wrapper around the published `i18n4k-generator` library. See
[`plugins/i18n4k`](plugins/i18n4k) and
[`doc/kotlin-toolchain-plugin.md`](../../doc/kotlin-toolchain-plugin.md).

## Prerequisites

The project uses the published `de.comahe.i18n4k:*:0.11.2` artifacts. To test
locally built versions, publish them first from the repository root:

```sh
./kotlin publish mavenLocal
```

The `repositories` section of `plugins/i18n4k/module.yaml` contains
`mavenLocal`; Maven Central is the Toolchain default. Remove `mavenLocal` if you
want to make sure only released artifacts are used.

## Build and run

Run the commands from this directory (`examples-kotlin-toolchain/example-mpp`).
On Windows use `./kotlin.bat`; the shell wrapper cannot run under PowerShell.

```sh
./kotlin build                     # all modules / host-supported platforms
./kotlin build -m shared -p jvm    # only the shared module on the JVM
./kotlin test -m shared -p jvm     # inline translations + generated resources
./kotlin run -m jvm-app            # Swing desktop app
```

The native console app for your host:

```sh
./kotlin run -m windows-app        # Windows
./kotlin run -m linux-app          # Linux
./kotlin run -m macos-app          # macOS
```

Kotlin/Native apps for other hosts are skipped by `build`; running them requires
the matching host.

The native app modules declare `settings.native.entryPoint` (for example
`example.windowsapp.main`): the Toolchain's default entry point is a `main`
function in the **root** package, but these apps use packages.

### Kotlin/JS

`./kotlin build` compiles `js-app`, but the `js/app` product type is an
incomplete preview in Kotlin Toolchain 0.14.1: the CLI neither links a runnable
`.mjs` file nor runs it. The module is still useful as a compile check for the
generated JS code and the shared module. When the Toolchain supports linking,
copy the generated `.mjs` (e.g. `build/tasks/_js-app_linkJs/js-app.mjs`) next to
[`js-app/resources/index.html`](js-app/resources/index.html) and serve that
directory.

## The i18n4k plugin

The plugin is enabled in [`shared/module.yaml`](shared/module.yaml):

```yaml
plugins:
  i18n4k:
    enabled: true
    sourceCodeLocales: [ en, en_US, de ]
```

The bundles live in [`shared/src/i18n/x/y`](shared/src/i18n/x/y). The generated
Kotlin sources and resource files are contributed to the build via
`generated.sources` / `generated.resources`, so they are compiled, packaged and
shown in IntelliJ IDEA as source/resource folders. `fr`, `nl` and
`en_US_texas` are loaded from the generated resources by `jvm-app`.

Commands:

```sh
./kotlin do generateI18n4kFiles -m shared
./kotlin do clearI18n4kFiles -m shared
```

See [`doc/kotlin-toolchain-plugin.md`](../../doc/kotlin-toolchain-plugin.md) for
the full settings reference and limitations.
