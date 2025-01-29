import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}



kotlin {

    androidTarget() {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
        publishLibraryVariants("release")
    }
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
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport {
                        enabled.set(true)
                    }
                }
            }
        }
        // test in a nodeJS environment without browser
        nodejs {
            testTask {
                useMocha {
                }
            }
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmWasi{
        // test in a nodeJS environment without browser
        nodejs {
            testTask {
                useMocha {
                }
            }
        }
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs{
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport {
                        enabled.set(true)
                    }
                }
            }
        }
        // test in a nodeJS environment without browser
        nodejs {
            testTask {
                useMocha {
                }
            }
        }
    }


    // #####  native targets...
    // # out commented targets are not supported by a used library
    // iosArm32() - https://blog.jetbrains.com/kotlin/2023/02/update-regarding-kotlin-native-targets/
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    mingwX64()
    linuxX64()
    tvosArm64()
    tvosX64()
    tvosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    // watchosX86() - https://blog.jetbrains.com/kotlin/2023/02/update-regarding-kotlin-native-targets/
    watchosX64()
    watchosSimulatorArm64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()
    //mingwX86()// not supported by "atomicfu", "kotlinx-collections-immutable"
    //wasm32() // not supported by "atomicfu"
    //linuxArm32Hfp() // not supported by "atomicfu", "kotlinx-collections-immutable"
    linuxArm64() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //linuxMips32() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //linuxMipsel32() // not supported by "atomicfu", "kotlinx-collections-immutable"


    // Apply the default hierarchy again. It'll create, for example, the iosMain source set:
    // https://kotlinlang.org/docs/multiplatform-hierarchy.html#see-the-full-hierarchy-template
    // https://kotlinlang.org/docs/multiplatform-hierarchy.html#creating-additional-source-sets
    applyDefaultHierarchyTemplate()

    sourceSets {
        // create common sources sets for Android and JVM
        val commonMain by getting
        val commonTest by getting

        val jvmAndroidMain by creating {
            dependsOn(commonMain)
        }
        val jvmMain by getting {
            dependsOn(jvmAndroidMain)
        }
        val androidMain by getting {
            dependsOn(jvmAndroidMain)
        }
        val jvmAndroidTest by creating {
            dependsOn(commonTest)
        }
        val jvmTest by getting {
            dependsOn(jvmAndroidTest)
        }
        val androidUnitTest by getting {
            dependsOn(jvmAndroidTest)
        }
    }
}

android{
    namespace = "de.comahe.i18n4k"
    defaultConfig {
        // Android 5.0 (API-Level 21), 2015
        minSdk = 21
        // Android 15 (API level 35), 2024
        compileSdk = 35
    }
}