plugins {
    id("i18n4k.kmp-build")
    id("i18n4k.publish-build")
}


kotlin {
    android {
        namespace = "de.comahe.i18n4k.core"
    }

    // the sources of all the targets
    sourceSets {

        commonMain {
            dependencies {
                implementation(libs.kotlinxAtomicfu)
                implementation(libs.kotlinxCollectionsImmutable)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvmMain {
            dependencies {
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        androidMain {
            dependencies {
                // needed for androidx.core.os.ConfigurationCompat.getLocales()
                implementation(libs.androidxCoreKtx)
            }
        }
        androidUnitTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        jsMain {
            dependencies {
            }
        }
        jsTest {
            dependencies {
                implementation(kotlin("test-js"))
                implementation(libs.kotlinxCoroutinesCore)
                implementation(libs.kotlinxCoroutinesTest)

            }
        }
        wasmJsMain {
            dependencies {
            }
        }
        wasmJsTest {
            dependencies {
                implementation(kotlin("test-wasm-js"))
                implementation(libs.kotlinxCoroutinesCore)
                implementation(libs.kotlinxCoroutinesTest)

            }
        }
        wasmWasiMain {
            dependencies {
            }
        }
        wasmWasiTest {
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
