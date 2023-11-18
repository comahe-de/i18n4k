plugins {
    kotlin("multiplatform") // version from main build.gradle.kts
    alias(libs.plugins.dokkaPlugin)
}

repositories {
    mavenCentral()
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
    //androidNativeArm32() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //androidNativeArm64() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //mingwX86()// not supported by "atomicfu", "kotlinx-collections-immutable"
    //wasm32() // not supported by "atomicfu"
    //linuxArm32Hfp() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //linuxArm64() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //linuxMips32() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //linuxMipsel32() // not supported by "atomicfu", "kotlinx-collections-immutable"

    // the sources of all the targets
    @Suppress("UNUSED_VARIABLE", "UnusedPrivateMember")
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

        // ##### configure all the native targets...
        // # out commented targets are not supported by a used library

        // default native sources
        val nativeCommonMain = create("nativeMain") {
            dependsOn(commonMain)
        }
        val nativeCommonTest = create("nativeTest") {
            dependsOn(commonTest)
        }

        // windows native sources


        val windowsCommonMain = create("windowsCommonMain") {
            dependsOn(nativeCommonMain)
        }
        val windowsCommonTest = create("windowsCommonTest") {
            dependsOn(nativeCommonTest)
        }

        val mingwX64Main by getting {
            dependsOn(windowsCommonMain)
        }
        val mingwX64Test by getting {
            dependsOn(windowsCommonTest)
        }

        // linux native sources

        val linuxCommonMain = create("linuxCommonMain") {
            dependsOn(nativeCommonMain)
        }
        val linuxCommonTest = create("linuxCommonTest") {
            dependsOn(nativeCommonTest)
        }

        val linuxX64Main by getting {
            dependsOn(linuxCommonMain)
        }
        val linuxX64Test by getting {
            dependsOn(linuxCommonTest)
        }

        // Apple native sources

        val appleCommonMain = create("appleCommonMain") {
            dependsOn(nativeCommonMain)
        }
        val appleCommonTest = create("appleCommonTest") {
            dependsOn(nativeCommonTest)
        }

        // Apple / iOS
        val iosArm64Main by getting {
            dependsOn(appleCommonMain)
        }
        val iosArm64Test by getting {
            dependsOn(appleCommonTest)
        }
        val iosX64Main by getting {
            dependsOn(appleCommonMain)
        }
        val iosX64Test by getting {
            dependsOn(appleCommonTest)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(appleCommonMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(appleCommonTest)
        }

        // Apple /  Mac OS
        val macosX64Main by getting {
            dependsOn(appleCommonMain)
        }
        val macosX64Test by getting {
            dependsOn(appleCommonTest)
        }
        val macosArm64Main by getting {
            dependsOn(appleCommonMain)
        }
        val macosArm64Test by getting {
            dependsOn(appleCommonTest)
        }

        // Apple /  Watch OS
        val watchosArm64Main by getting {
            dependsOn(appleCommonMain)
        }
        val watchosArm64Test by getting {
            dependsOn(appleCommonTest)
        }
        val watchosX64Main by getting {
            dependsOn(appleCommonMain)
        }
        val watchosX64Test by getting {
            dependsOn(appleCommonTest)
        }
        val watchosSimulatorArm64Main by getting {
            dependsOn(appleCommonMain)
        }
        val watchosSimulatorArm64Test by getting {
            dependsOn(appleCommonTest)
        }

        /* tvos */
        val tvosArm64Main by getting {
            dependsOn(appleCommonMain)
        }
        val tvosArm64Test by getting {
            dependsOn(appleCommonTest)
        }
        val tvosX64Main by getting {
            dependsOn(appleCommonMain)
        }
        val tvosX64Test by getting {
            dependsOn(appleCommonTest)
        }
        val tvosSimulatorArm64Main by getting {
            dependsOn(appleCommonMain)
        }
        val tvosSimulatorArm64Test by getting {
            dependsOn(appleCommonTest)
        }


        /*
        val androidNativeArm32Main by getting {
            dependsOn(nativeCommonMain)
        }
        val androidNativeArm32Test by getting {
            dependsOn(nativeCommonTest)
        }

        val androidNativeArm64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val androidNativeArm64Test by getting {
            dependsOn(nativeCommonTest)
        }

        val mingwX86Main by getting {
            dependsOn(nativeCommonMain)
        }
        val mingwX86Test by getting {
            dependsOn(nativeCommonTest)
        }

        val wasm32Main by getting {
            dependsOn(nativeCommonMain)
        }
        val wasm32Test by getting {
            dependsOn(nativeCommonTest)
        }

        val linuxArm32Main by getting {
            dependsOn(nativeCommonMain)
        }
        val linuxArm32Test by getting {
            dependsOn(nativeCommonTest)
        }
        val linuxArm64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val linuxArm64Test by getting {
            dependsOn(nativeCommonTest)
        }
        val linuxMips32Main by getting {
            dependsOn(nativeCommonMain)
        }
        val linuxMips32Test by getting {
            dependsOn(nativeCommonTest)
        }
        val linuxMipsel32Main by getting {
            dependsOn(nativeCommonMain)
        }
        val linuxMipsel32Test by getting {
            dependsOn(nativeCommonTest)
        }

        */
    }


}

// from https://stackoverflow.com/a/66352905/2611134
tasks {
    create<Jar>("javadocJar") {
        // we cannot use `dokkaJavadoc`, as it does not support multiplatform projects
        // https://slack-chats.kotlinlang.org/t/484606/is-there-a-workaround-for-getting-the-dokka-javadoc-plugin-t
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaHtml.get().outputDirectory)
    }
}

// Publishing....
afterEvaluate {
    publishing {
        publications {
            /**
             * When used with maven-publish, the Kotlin plugin automatically creates
             * publications for each target that can be built on the current host,
             * except for the Android target, which needs an additional step to
             * configure publishing.
             *
             * Therefore no additional publication declaractions are needed here!
             *
             * https://kotlinlang.org/docs/mpp-publish-lib.html#structure-of-publications
             */
            // avoid duplicate publication for multi platform builds
            BuildTools.avoidDuplicateMultiPlatformPublications(project, this)

            // add JavaDoc
            all {
                val mavenPublication = this as? MavenPublication
                mavenPublication?.artifact(tasks["javadocJar"])
            }
        }
    }
}
