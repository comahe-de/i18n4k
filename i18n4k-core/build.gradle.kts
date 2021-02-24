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
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


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
        val jsMain by getting{
            dependencies {
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by getting {
            dependencies {
            }
        }
        val nativeTest by getting
    }

    // Publishing....
    /*
     * When used with maven-publish, the Kotlin plugin automatically creates publications for each
     * target that can be built on the current host, except for the Android target, which needs an
     *  additional step to configure publishing.
     *
     * https://kotlinlang.org/docs/mpp-publish-lib.html#structure-of-publications
     */

    /*
     * To avoid duplicate publications of modules that can be built on several platforms
     * (like JVM and JS), configure the publishing tasks for these modules to run conditionally.
     *
     * https://kotlinlang.org/docs/mpp-publish-lib.html#avoid-duplicate-publications
     */
    /*
    val publicationsFromMainHost = listOf(jvm(), js()).map { it.name } + "kotlinMultiplatform"
    publishing {
        publications {
            matching { it.name in publicationsFromMainHost }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
            }
        }
    }
    */

}
