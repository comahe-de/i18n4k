group = "examples"
version = "1.0.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.9.10"
    id("de.comahe.i18n4k") version "0.7.0-SNAPSHOT"
}

// ####################################
// configuration

i18n4k {
    sourceCodeLocales = listOf("en", "en_US", "de")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

// ####################################
// dependencies

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("de.comahe.i18n4k:i18n4k-core-jvm:0.7.0-SNAPSHOT")
    implementation("com.miglayout:miglayout-swing:5.2")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit", "junit", "4.13")
}



