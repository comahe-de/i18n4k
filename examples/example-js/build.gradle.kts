group = "examples"
version = "1.0.0-SNAPSHOT"

plugins {
    kotlin("js") version "1.7.0"
    id("de.comahe.i18n4k") version "0.5.0"
}

// ####################################
// configuration

i18n4k {
    sourceCodeLocales = listOf("en","de")
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
    implementation("org.jetbrains:kotlin-react:16.13.1-pre.113-kotlin-1.4.0")
    implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.113-kotlin-1.4.0")

    implementation("de.comahe.i18n4k:i18n4k-core-js:0.5.0")
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