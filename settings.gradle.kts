rootProject.name = "i18n4k"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include("i18n4k-core")
include("i18n4k-generator")
include("i18n4k-gradle-plugin")
