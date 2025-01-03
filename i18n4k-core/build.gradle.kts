@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("i18n4k.kmp-build")
    id("i18n4k.publish-build")
}


kotlin {
    // the sources of all the targets
    @Suppress("UnusedPrivateMember")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinxAtomicfu)
                implementation(libs.kotlinxCollectionsImmutable)
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
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val androidMain by getting {
            dependencies {
                // needed for androidx.core.os.ConfigurationCompat.getLocales()
                implementation(libs.androidxCoreKtx)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation(libs.kotlinxCoroutinesCore)
                implementation(libs.kotlinxCoroutinesTest)

            }
        }
        val wasmJsMain by getting {
            dependencies {
            }
        }
        val wasmJsTest by getting {
            dependencies {
                implementation(kotlin("test-wasm-js"))
                implementation(libs.kotlinxCoroutinesCore)
                implementation(libs.kotlinxCoroutinesTest)

            }
        }
        val wasmWasiMain by getting {
            dependencies {
            }
        }
        val wasmWasiTest by getting {
            dependencies {
                implementation(kotlin("test-wasm-wasi"))
                implementation(libs.kotlinxCoroutinesCore)
                implementation(libs.kotlinxCoroutinesTest)

            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
