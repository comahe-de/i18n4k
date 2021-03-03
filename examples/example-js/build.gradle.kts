group = "examples"
version = "1.0.0-SNAPSHOT"

plugins {
    kotlin("js") version "1.4.30"
    id("de.comahe.i18n4k") version "0.1.0"
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
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
}


dependencies {
    testImplementation(kotlin("test-js"))
    implementation("org.jetbrains:kotlin-react:16.13.1-pre.113-kotlin-1.4.0")
    implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.113-kotlin-1.4.0")

    implementation("de.comahe.i18n4k:i18n4k-core-js:0.1.0")
}