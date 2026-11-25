# i18n4k Kotlin Toolchain plugin

Kotlin Toolchain plugin that generates the i18n4k accessor code and language
resource files for a module.

```yaml
# module.yaml
plugins:
  i18n4k:
    enabled: true
    sourceCodeLocales: [ en, de ]
```

The generated Kotlin sources and resources are contributed to the build through
`generated.sources` / `generated.resources` in [`plugin.yaml`](plugin.yaml), so
they are compiled, packaged, and shown in IntelliJ IDEA as source/resource
folders.

* Settings: [`src/de/comahe/i18n4k/toolchain/I18n4kSettings.kt`](src/de/comahe/i18n4k/toolchain/I18n4kSettings.kt)
* Task actions: [`src/de/comahe/i18n4k/toolchain/tasks/`](src/de/comahe/i18n4k/toolchain/tasks)
* Documentation: [`doc/kotlin-toolchain-plugin.md`](../../../../doc/kotlin-toolchain-plugin.md)

## Why is the plugin part of the example project?

The Kotlin Toolchain currently cannot package and publish custom plugins
([roadmap](https://kotlin-toolchain.org/latest/user-guide/plugins/overview/#planned-for-upcoming-releases)).
The supported workaround is to keep a thin local plugin wrapper in the consuming
project and put all logic into a published library. That is exactly what this
directory is: the thin wrapper around the published `i18n4k-generator` library.

This copy is used by the example project in [`../..`](../..) (module `shared`).
To use it in your own project, copy this directory to `plugins/i18n4k` and keep
[`module.yaml`](module.yaml) (it depends on the published Maven artifacts).
