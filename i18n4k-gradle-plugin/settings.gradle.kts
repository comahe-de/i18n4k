// Self-contained Gradle build for the i18n4k Gradle plugin.
//
// The Kotlin Toolchain has no `java-gradle-plugin` product type, so this module
// cannot be built with the Kotlin Toolchain. It therefore keeps its own Gradle
// build. See `../agent.md` for the rationale.
//
// This build is intentionally independent of the Kotlin Toolchain project. It
// consumes the `i18n4k-core` and `i18n4k-generator` artifacts from the local
// Maven repository, so publish them first:
//
//     ./kotlin publish mavenLocal
//     ./i18n4k-gradle-plugin/gradlew build       # Linux/macOS
//     i18n4k-gradle-plugin\gradlew.bat build     # Windows

rootProject.name = "i18n4k-gradle-plugin"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
        maven { setUrl("https://jitpack.io") }
    }
}
