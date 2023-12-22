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
                implementation(project(":i18n4k-core"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                // to parse JSON data
                implementation (libs.kotlinxSerializationJson)
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

    }
}
