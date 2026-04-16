plugins {
    id("i18n4k.kmp-build")
    id("i18n4k.publish-build")
}


kotlin {
    android {
        namespace = "de.comahe.i18n4k.cldr"
    }
    
    // the sources of all the targets
    sourceSets {

        commonMain {
            dependencies {
                implementation(project(":i18n4k-core"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                // to parse JSON data
                implementation (libs.kotlinxSerializationJson)
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
            }
        }
        wasmJsMain {
            dependencies {
            }
        }
        wasmJsTest {
            dependencies {
                implementation(kotlin("test-wasm-js"))
            }
        }
        wasmWasiMain {
            dependencies {
            }
        }
        wasmWasiTest {
            dependencies {
                implementation(kotlin("test-wasm-wasi"))
            }
        }

    }
}
