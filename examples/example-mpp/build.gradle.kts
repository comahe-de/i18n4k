import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "examples"
version = "1.0.0-SNAPSHOT"


plugins {
    kotlin("multiplatform") version "2.2.0"
    id("de.comahe.i18n4k") version "0.11.0"
}

// ####################################
// configuration


i18n4k {
    sourceCodeLocales = listOf("en", "en_US", "de")
}



kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }

        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js(IR) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport {
                    enabled.set(true)
                }
            }
            runTask {
                cssSupport {
                    enabled.set(true)
                }
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport {
                        enabled.set(true)
                    }
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
                implementation("de.comahe.i18n4k:i18n4k-core:0.11.0")
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
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.349")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.349")
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
    mavenLocal()  // needed testing local deployments
    mavenCentral()
}

// Fixes webpack-cli incompatibility by pinning the newest version.
// https://stackoverflow.com/a/72731728/2611134
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}