![i18n4k](./doc/img/i18n4k-logo-small.png "i18n4k Logo")


![Version][badge-version]
![Kotlin][badge-kotlin]
![Kotlin][badge-gradle]
[![Kotlin][badge-maven]](https://mvnrepository.com/artifact/de.comahe.i18n4k)
[![License][badge-apache2.0]](./LICENSE)

![Kotlin/Multiplatform][badge-multiplatform]
![Kotlin/JVN][badge-jvm]
![Kotlin/JS][badge-js]
![Kotlin/Native][badge-native]

# i18n4k

_Internationalization for Kotlin_

**Home:** [github.com/comahe-de/i18n4k](https://github.com/comahe-de/i18n4k)

**Lastest release version:** 0.8.1

**Table of contents**

[//]: <> (generated with https://ecotrust-canada.github.io/markdown-toc/)

* [About](#about)
* [Supported platforms](#supported-platforms)
* [Artefact repository](#artefact-repository)
* [Gradle dependencies](#gradle-dependencies)
* [Gradle plugin](#gradle-plugin)
* [Code generation](#code-generation)
    + [Adding message bundles](#adding-message-bundles)
    + [Inline storing of translations](#inline-storing-of-translations)
    + [Optimized message files](#optimized-message-files)
* [Runtime configuration](#runtime-configuration)
* [Hints for Android](#hints-for-android)
* [Message format](#message-format)
* [Further dokumentation](#further-dokumentation)
* [Example](#example)
* [Example projects](#example-projects)
* [Contribute](#contribute)
* [Status](#status)

## About

**_i18n4k_** is a multiplatform (JVM, JS, native) library and code generator for Kotlin to handle
internationalisation (i18n) in your program.


It provides

* `Locale` class to store the selected language
* `LocalizedString` class that stores the reference to the translatable message. The `toString()`
  will return the message string in the currently selected language/locale.
* Parameter support via `LocalizedStringFatory*` that creates a `LocalizedString` with the supplied
  parameters using a parameter format like _"Hello, {0}!"_
* Code generators that creates...
    * ... access objects with a constant per message key
    * ... objects for inline storing of messages of specified locales (no need of loading resources
      at runtime)
    * ... optimized message files for loading message resources at runtime
* The code generators currently can load Java Properties files (but can be extended for additional
  formats).
* Gradle plugin to start the code generator

## Supported platforms

* Java (JVM) & Android
* JS (IR backend)
* Native:
   * iosArm64
   * iosX64
   * iosSimulatorArm64
   * macosX64
   * macosArm64
   * mingwX64
   * linuxX64
   * tvosArm64
   * tvosX64
   * tvosSimulatorArm64
   * watchosArm64
   * watchosX86
   * watchosX64
   * watchosSimulatorArm64
    
## Artefact repository

Ensure that you have Maven-Central (`mavenCentral()`) in your repository list

```kotlin
repositories {
    mavenCentral()
}
```

## Gradle dependencies

**For multiplatform projects:** add `de.comahe.i18n4k:i18n4k-core:<VERSION>` to `commonMain` source set.

```kotlin
val commonMain by getting {
    dependencies {
        implementation("de.comahe.i18n4k:i18n4k-core:0.8.1")
    }
}
```

**For Kotlin/JS:** add `de.comahe.i18n4k:i18n4k-core-js:<VERSION>` to the dependencies

```kotlin
dependencies {
    implementation("de.comahe.i18n4k:i18n4k-core-js:0.8.1")
}
```

**For Kotlin/Jvm:** add `de.comahe.i18n4k:i18n4k-core-jvm:<VERSION>` to the dependencies

```kotlin
dependencies {
    implementation("de.comahe.i18n4k:i18n4k-core-jvm:0.8.1")
}
```

## Gradle plugin

Apply the plugin "de.comahe.i18n4k", e.g:

```kotlin
plugins {
    id("de.comahe.i18n4k") version "0.8.1"
}
```

The configuration is done via the `i18n4k` object, e.g.:

```kotlin
i18n4k {
    sourceCodeLocales = listOf("en", "de")
}
```

For the full list of parameters,
see [I18n4kExtension](./i18n4k-gradle-plugin/src/main/kotlin/de/comahe/i18n4k/gradle/plugin/I18n4kExtension.kt)

## Code generation

The Gradle plugin can generate code to access the message via constants instead of string keys or
similar.

### Adding message bundles

By default, the language files of the message bundles are searched in the path `src/main/i18n`
(normal projects) respectively `src/commonMain/i18` (for multiplatform projects). See configuration
for other folder locations.

Any sub folder in this directory will be converted to a package in the Kotlin code.

### Inline storing of translations

To avoid loading of translations at runtime, there is also the possibility to include the messages
in the source code. So the translations are always available and no need for platform-specific
loading of resources is needed. Drawback is that the messages are hold in memory all the time. So
only the most important translations should be stored in the source code.

### Optimized message files

**_i18n4k_** has an internal format to store message files to be loaded at runtime. It is
index-based instead of key-bases as e.g. Java-Property files. This has the advantage that no memory
is wasted for storing the key in each language.

More details are described
here [MessagesProviderViaLoadingText](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/messages/providers/MessagesProviderViaLoadingText.kt)

## Runtime configuration

The current locale and other parameters are stored in the global
variable [i184k](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/I18n4k.kt) of type
[I18n4kConfig](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/config/I18n4kConfig.kt)

Predefined implementations of `I18n4kConfig` are:

* [I18n4kConfigDefault](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/config/I18n4kConfigDefault.kt)
* [I18n4kConfigImmutable](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/config/I18n4kConfigImmutable.kt)
* [I18n4kConfigDelegate](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/config/I18n4kConfigDelegate.kt)

The configuration can be changed by assigning (and editing) one of these implementation to
the `i184k` variable.

## Hints for Android

Generated resource files are not added to the "Java resource", but as "raw Android resources".
As Android does not allow sub-folders or uppercase letters, the resource name is adapted: packages
names are prefixed to the resource name and concatenated by "_". 
Also, camel case names are converted to sneak-case, 
e.g. "/x/y/MyMessages_fr.i18n4k.txt" will be changed to "x_y_my_messages_fr_i18n4k".

Generated raw resources can be loaded in the following way:

```kotlin
 MyMessages.registerTranslation(
    MessagesProviderViaText(
        text = String(
            context.resources.openRawResource(
                R.raw.x_y_my_messages_fr_i18n4k
            ).readBytes()
        )
    )
)
```

## Message format

The [default message format](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/messages/formatter/MessageFormatterDefault.kt)
is similar to the [Java-MessageFormat](https://docs.oracle.com/javase/8/docs/api/java/text/MessageFormat.html).
E.g. the text "Hello, {0}" has one parameter.
Any string can be used as parameter name.

The parameter values can also be formatted,
e.g. "{0,number, %.2}" formats parameter 0 as number with max. 2 fraction digits.
See [Formatters](./doc/formatters/index.adoc) for more information.

For messages with parameters, the generator plugin 
creates special [LocalizedStringFactories](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/strings/LocalizedStringFactories.kt)
where the parameters must be supplied as arguments to get the resulting string.

Other message formatters can be implemented and be configured in 
[I18n4kConfig](./i18n4k-core/src/commonMain/kotlin/de/comahe/i18n4k/config/I18n4kConfig.kt)`.messageFormatter`

## Further dokumentation

For more advanced features, have a look at the 
[documenation files](./doc/index.adoc)

## Example

Source files are the following message bundle as Java-Properties files located in
the `src/main/i18n/x/y/` folder

_MyMessages_en.properties_

```
sayHello=Hello, {0}!
title=Internationalisation example \uD83C\uDDEC\uD83C\uDDE7
```

_MyMessages_de.properties_

```
sayHello=Hallo, {0}!
title=Internationalisierung Beispiel \uD83C\uDDE9\uD83C\uDDEA
```

In the Gradle script, "en" is defined as source code locale (to be stored in source code)

```kotlin
i18n4k {
    sourceCodeLocaleCodes = listOf("en")
}
```

This results in the following generate Kotlin code

```kotlin
package x.y

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.MessageBundle
import de.comahe.i18n4k.messages.providers.MessagesProvider
import de.comahe.i18n4k.strings.LocalizedString
import de.comahe.i18n4k.strings.LocalizedStringFactory1
import kotlin.Array
import kotlin.Int
import kotlin.String

/**
 * Massage constants for bundle 'MyMessages'. Generated by i18n4k.
 */
public object MyMessages : MessageBundle("MyMessages", "x.y") {
    /**
     * Hello, {0}!
     */
    public val sayHello: LocalizedStringFactory1 = getLocalizedStringFactory1("sayHello", 0)

    /**
     * Internationalisation example 🇬🇧
     */
    public val title: LocalizedString = getLocalizedString0("title", 1)

    init {
        registerTranslation(MyMessages_en)
    }
}

/**
 * Translation of message bundle 'MyMessages' for locale 'en'. Generated by i18n4k.
 */
private object MyMessages_en : MessagesProvider {
    private val _data: Array<String?> = arrayOf(
        "Hello, {0}!",
        "Internationalisation example 🇬🇧"
    )

    public override val locale: Locale = Locale("en")

    public override val size: Int = _data.size

    public override fun `get`(index: Int): String? = _data[index]
}

```

And the following message file:

_MyMessages_de.i18n4k.txt_

```
de
^
Hallo, {0}!
^
Internationalisierung Beispiel 🇩🇪
^
```

The messages can than be accessed like this

```kotlin
println(MyMessages.sayHello("i18n4k"))
```

which prints

```
Hello, i18n4k!
```

The German message file can be loaded at runtime, e.g. like this (for JVM plattform)

```kotlin
MyMessages.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/MyMessages_de.i18n4k.txt"))
```

Changing the locale can be done like this:

```kotlin
val i18n4kConfig = I18n4kConfigDefault()
i18n4k = i18n4kConfig
i18n4kConfig.locale = Locale("de")
```

Afterwards the expression from above prints

```
Hallo, i18n4k!
```

## Example projects

Please also check the examples projects in the [examples](./examples) folder!

## Contribute

* Create an issue on [github](https://github.com/comahe-de/i18n4k/issues).
* Open a Pull requests on [github](https://github.com/comahe-de/i18n4k/pulls).
* [Trello board](https://trello.com/b/vFwkreA4/i18n4k)

## Status

The library is under development. So the API is not stable. Enhancements and feature request are 
very welcome. 


[badge-multiplatform]: http://img.shields.io/badge/platform-multiplatform-75E1FF.svg?style=flat
[badge-android]: http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat

[badge-native]: http://img.shields.io/badge/platform-native-6D6DFF.svg?style=flat

[badge-js]: http://img.shields.io/badge/platform-js-FFB100.svg?style=flat

[badge-jvm]: http://img.shields.io/badge/platform-jvm-79BF2D.svg?style=flat

[badge-linux]: http://img.shields.io/badge/platform-linux-important.svg?style=flat

[badge-windows]: http://img.shields.io/badge/platform-windows-informational.svg?style=flat

[badge-mac]: http://img.shields.io/badge/platform-macos-lightgrey.svg?style=flat

[badge-wasm]: https://img.shields.io/badge/platform-wasm-darkblue.svg?style=flat

[badge-apache2.0]:https://img.shields.io/badge/License-Apache/2.0-blue.svg?style=flat

[badge-version]:https://img.shields.io/badge/version-0.8.1-blueviolet?style=flat

[badge-maven]:https://img.shields.io/badge/Maven-Central-6262EC?style=flat

[badge-kotlin]:https://img.shields.io/badge/Kotlin-1.9.24-orange?style=flat

[badge-gradle]:https://img.shields.io/badge/Gradle-8.7.0-1DA2BD?style=flat
