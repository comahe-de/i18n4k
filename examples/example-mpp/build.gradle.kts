group = "examples"
version = "1.0.0-SNAPSHOT"


plugins {
    kotlin("multiplatform") version "1.6.21"
    id("de.comahe.i18n4k") version "0.4.0"
}

// ####################################
// configuration


i18n4k {
    sourceCodeLocales = listOf("en", "de")
}



kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js(IR) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("de.comahe.i18n4k:i18n4k-core:0.3.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.miglayout:miglayout-swing:5.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting{
            dependencies {
                implementation("org.jetbrains:kotlin-react:16.13.1-pre.113-kotlin-1.4.0")
                implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.113-kotlin-1.4.0")

            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by getting{
            dependencies {
            }
        }
        val nativeTest by getting
    }
}


// ####################################
// dependencies

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
}