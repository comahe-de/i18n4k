rootProject.name = "i18n4k"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}


include("i18n4k-core")
include("i18n4k-cldr-plural-rules")
include("i18n4k-generator")
include("i18n4k-gradle-plugin")
