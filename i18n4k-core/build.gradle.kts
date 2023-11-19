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


    // Apply the default hierarchy again. It'll create, for example, the iosMain source set:
    // https://kotlinlang.org/docs/multiplatform-hierarchy.html#see-the-full-hierarchy-template
    // https://kotlinlang.org/docs/multiplatform-hierarchy.html#creating-additional-source-sets
    applyDefaultHierarchyTemplate()

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
