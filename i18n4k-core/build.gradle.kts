plugins {
    kotlin("multiplatform") // version from main build.gradle.kts
}

repositories {
    mavenCentral()
    jcenter()
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
    js(BOTH) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
    // #####  native targets...
    // out commented targets are not supported by a used library
    //androidNativeArm32() // not supported by "atomicfu"
    //androidNativeArm64() // not supported by "atomicfu"
    iosArm32()
    iosArm64()
    iosX64()
    macosX64()
    mingwX64()
    //mingwX86()// not supported by "atomicfu"
    tvosArm64()
    tvosX64()
    //wasm32() // not supported by "atomicfu"
    //linuxArm32Hfp() // not supported by "atomicfu"
    //linuxArm64() // not supported by "atomicfu"
    //linuxMips32() // not supported by "atomicfu"
    //linuxMipsel32() // not supported by "atomicfu"
    linuxX64()
    watchosArm32()
    watchosArm64()
    watchosX86()

    // the sources of all the targets
    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(Dependencies.kotlinxAtomicfu)
                implementation(Dependencies.kotlinxCollectionsImmutable)
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
        val jsMain by getting {
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        // ##### configure all the native targets...

        // default native sources
        val nativeCommonMain = create("nativeMain")
        val nativeCommonTest = create("nativeTest")

        //val androidNativeArm32Main // not supported by "atomicfu"
        //val androidNativeArm64Main // not supported by "atomicfu"

        val iosArm32Main by getting {
            dependsOn(nativeCommonMain)
        }
        val iosArm32Test by getting {
            dependsOn(nativeCommonTest)
        }
        val iosArm64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val iosArm64Test by getting {
            dependsOn(nativeCommonTest)
        }
        val iosX64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val iosX64Test by getting {
            dependsOn(nativeCommonTest)
        }
        val macosX64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val macosX64Test by getting {
            dependsOn(nativeCommonTest)
        }
        val mingwX64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val mingwX64Test by getting {
            dependsOn(nativeCommonTest)
        }
        //val mingwX86Main // not supported by "atomicfu"

        val tvosArm64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val tvosArm64Test by getting {
            dependsOn(nativeCommonTest)
        }
        val tvosX64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val tvosX64Test by getting {
            dependsOn(nativeCommonTest)
        }

        //val wasm32Main // not supported by "atomicfu"
        //val linuxArm32HfpMain // not supported by "atomicfu"
        //val linuxArm64Main // not supported by "atomicfu"
        //val linuxMips32Main // not supported by "atomicfu"
        //val linuxMipsel32Main // not supported by "atomicfu"

        val linuxX64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val linuxX64Test by getting {
            dependsOn(nativeCommonTest)
        }

        val watchosArm32Main by getting {
            dependsOn(nativeCommonMain)
        }
        val watchosArm32Test by getting {
            dependsOn(nativeCommonTest)
        }
        val watchosArm64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val watchosArm64Test by getting {
            dependsOn(nativeCommonTest)
        }
        val watchosX86Main by getting {
            dependsOn(nativeCommonMain)
        }
        val watchosX86Test by getting {
            dependsOn(nativeCommonTest)
        }

    }

    // Publishing....
    /**
     * When used with maven-publish, the Kotlin plugin automatically creates publications for each
     * target that can be built on the current host, except for the Android target, which needs an
     *  additional step to configure publishing.
     *
     * https://kotlinlang.org/docs/mpp-publish-lib.html#structure-of-publications
     */

    /**
     * To avoid duplicate publications of modules that can be built on several platforms:
     *
     * What can be build on with platform:
     * https://patrickjackson.dev/publishing-multiplatform-kotlin-libraries/
     *
     *
     *  |                 | Linux  | Windows | Macos |
     *  |-----------------|--------|--------|--------|
     *  | androidNative32 |   ✅   |    ✅   |   ✅   |
     *  | androidNative64 |   ✅   |    ✅   |   ✅   |
     *  | jvm             |   ✅   |    ✅   |   ✅   |
     *  | js              |   ✅   |    ✅   |   ✅   |
     *  | iosArm32        |   ❌   |    ❌   |   ✅   |
     *  | iosArm64        |   ❌   |    ❌   |   ✅   |
     *  | iosX64          |   ❌   |    ❌   |   ✅   |
     *  | macosX64        |   ❌   |    ❌   |   ✅   |
     *  | mingwx64        |   ❌   |    ✅   |   ❌   |
     *  | mingwx86        |   ❌   |    ✅   |   ❌   |
     *  | wasm32          |   ✅   |    ❌   |   ✅   |
     *  | linuxArm32Hfp   |   ✅   |    ✅   |   ✅   |
     *  | linuxArm64      |   ✅   |    ✅   |   ✅   |
     *  | linuxMips32     |   ✅   |    ❌   |   ❌   |
     *  | linuxMipsel32   |   ✅   |    ❌   |   ❌   |
     *  | linuxX64        |   ✅   |    ✅   |   ✅   |
     */

    val hostOs = System.getProperty("os.name")
    val publicationsToSkip = when {
        hostOs.startsWith("Windows") -> listOf(
            "linuxArm32Hfp",
            "linuxArm64",
            "linuxX64"
        )
        hostOs.startsWith("Linux") -> listOf(
            "metadata",
            "jvm",
            "js",
            "androidNativeArm32",
            "androidNativeArm64"
        )
        hostOs.startsWith("Mac OS") -> listOf(
            "metadata",
            "androidNativeArm32",
            "androidNativeArm64",
            "jvm",
            "js",
            "wasm32",
            "linuxArm32Hfp",
            "linuxArm64",
            "linuxX64"
        )
        else -> emptyList()
    }

    publishing {
        publications {
            println("########### Host OS is: $hostOs ##################")
            println("### Having publications: ")
            forEach { targetPublication ->
                val isDeploy = !(targetPublication.name in publicationsToSkip)
                println("### * \"${targetPublication.name}\" - publish: $isDeploy")
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach {
                        onlyIf { isDeploy }
                    }
            }
        }
    }
}
