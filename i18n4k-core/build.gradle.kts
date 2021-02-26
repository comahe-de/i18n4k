plugins {
    kotlin("multiplatform") // version from main build.gradle.kts

    // https://stackoverflow.com/a/66352905/2611134
    // "I use older version of dokka library (1.4.0-rc), as newer version could not generate
    // javadocs for all platforms"
    id("org.jetbrains.dokka") version "1.4.0-rc"
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
    // # out commented targets are not supported by a used library
    iosArm32()
    iosArm64()
    iosX64()
    macosX64()
    mingwX64()
    linuxX64()
    //androidNativeArm32() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //androidNativeArm64() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //mingwX86()// not supported by "atomicfu", "kotlinx-collections-immutable"
    //tvosArm64() // not supported by "kotlinx-collections-immutable"
    //tvosX64() // not supported by "kotlinx-collections-immutable"
    //wasm32() // not supported by "atomicfu"
    //linuxArm32Hfp() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //linuxArm64() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //linuxMips32() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //linuxMipsel32() // not supported by "atomicfu", "kotlinx-collections-immutable"
    //watchosArm32() // not supported by "kotlinx-collections-immutable"
    //watchosArm64() // not supported by "kotlinx-collections-immutable"
    //watchosX86() // not supported by  "kotlinx-collections-immutable"

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
        // # out commented targets are not supported by a used library

        // default native sources
        val nativeCommonMain = create("nativeMain")
        val nativeCommonTest = create("nativeTest")

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

        val linuxX64Main by getting {
            dependsOn(nativeCommonMain)
        }
        val linuxX64Test by getting {
            dependsOn(nativeCommonTest)
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

        */
    }


}

// from https://stackoverflow.com/a/66352905/2611134
tasks {
    create<Jar>("javadocJar") {
        dependsOn(dokkaJavadoc)
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc.get().outputDirectory)
    }

    dokkaJavadoc {
        dokkaSourceSets {
            create("commonMain") {
                displayName = "common"
                platform = "common"
            }
        }
    }
}


tasks {
    dokkaJavadoc {
        dokkaSourceSets.forEach {
            println("#### dokkaa")
            println(it.name)
            println(it.sourceRoots)
            println(it.platform)
        }
    }

}

// Publishing....
afterEvaluate {
    publishing {
        publications {
            /**
             * When used with maven-publish, the Kotlin plugin automatically creates publications for each
             * target that can be built on the current host, except for the Android target, which needs an
             *  additional step to configure publishing.
             *
             *  Therefore no additional publication declaractions are needed here!
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
