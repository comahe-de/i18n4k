group = "examples"
version = "1.0.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.0.0"
    id("de.comahe.i18n4k") version "0.9.0-SNAPSHOT"
}

// ####################################
// configuration

i18n4k {
    sourceCodeLocales = listOf("en", "en_US", "de", "de_x_attr_gender", "de_x_attr_decl_genitive", "en_x_attr_gender", "x_attr_emoji" )
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
    implementation("de.comahe.i18n4k:i18n4k-core-jvm:0.9.0-SNAPSHOT")
    implementation("de.comahe.i18n4k:i18n4k-cldr-plural-rules:0.9.0-SNAPSHOT")

    implementation("com.miglayout:miglayout-swing:5.2")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit", "junit", "4.13")
}



