group = "examples"
version = "1.0.0-SNAPSHOT"

plugins {
    kotlin("js") version "2.2.0"
    id("de.comahe.i18n4k") version "0.11.0-SNAPSHOT"
}

// ####################################
// configuration

i18n4k {
    sourceCodeLocales = listOf("en", "en_US", "de")
    enableJsExportAnnotation = true
}


kotlin {
    js(IR) {
        browser {
            binaries.executable()
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
}


// ####################################
// dependencies

repositories {
    mavenLocal()
    mavenCentral()
}


dependencies {
    testImplementation(kotlin("test-js"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.349")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.349")

    implementation("de.comahe.i18n4k:i18n4k-core-js:0.11.0-SNAPSHOT")
}

// Fix build error message "Entry index.html is a duplicate but no duplicate handling strategy has been set."
tasks.named<Copy>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.WARN
}

// Fixes webpack-cli incompatibility by pinning the newest version.
// https://stackoverflow.com/a/72731728/2611134
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}