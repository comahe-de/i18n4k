import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// ****************************************************************************
//  i18n4k Gradle plugin
//
//  This module cannot be built by the Kotlin Toolchain (no `java-gradle-plugin`
//  product type), so it keeps a standalone Gradle build. See ../agent.md.
//
//  The `i18n4k-core` and `i18n4k-generator` dependencies are resolved from the
//  local Maven repository and must be published first with:
//      ./kotlin publish mavenLocal
// ****************************************************************************

plugins {
    // This build has no `buildSrc`, so the Kotlin plugin version must be
    // declared explicitly (previously it came from the `buildSrc` classpath).
    alias(libs.plugins.kotlinJvm)
    `java-gradle-plugin`
    `maven-publish`
    signing
    alias(libs.plugins.gradlePluginPublish)
}

group = "de.comahe.i18n4k"
version = "0.11.2"

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Built by the Kotlin Toolchain and published to Maven Local beforehand.
    implementation("de.comahe.i18n4k:i18n4k-core-jvm:0.11.2")
    implementation("de.comahe.i18n4k:i18n4k-generator:0.11.2")

    compileOnly(gradleApi())
    compileOnly(libs.kotlinGradlePlugin)
    compileOnly(libs.composeGradlePlugin)
    compileOnly(libs.androidGradlePlugin)

    testImplementation(libs.junit)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

gradlePlugin {
    website = "https://comahe-de.github.io/i18n4k"
    vcsUrl = "https://github.com/comahe-de/i18n4k"

    plugins {
        create("i18n4k") {
            id = "de.comahe.i18n4k"
            implementationClass = "de.comahe.i18n4k.gradle.plugin.I18n4kPlugin"
            displayName = "i18n4k"
            description = "i18n4k - Internationalization for Kotlin - Code Generator"
            tags = listOf(
                "kotlin", "kotlin/multiplatform", "kotlin/js", "kotlin/jvm", "kotlin/native",
                "i18n", "internationalization", "code generator"
            )
        }
    }
}

// POM metadata, previously applied by the root project's `subprojects { }` block.
publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set(project.name)
            description.set(
                "i18n4k is a multiplatform (JVM, JS, native) library and code generator for " +
                    "Kotlin to handle internationalisation (i18n) in your program."
            )
            url.set("https://comahe-de.github.io/i18n4k/")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("comahe.de")
                    name.set("Marcel Heckel")
                    email.set("info@comahede")
                }
            }
            scm {
                connection.set("scm:git@github.com:comahe-de/i18n4k.git")
                developerConnection.set("scm:git@github.com:comahe-de/i18n4k.git")
                url.set("https://github.com/comahe-de/i18n4k")
            }
        }
    }
}

// Sign Maven publications when a signing key is provided via Gradle properties:
//   signing.keyId, signing.password, signing.secretKeyRingFile
// (can also be supplied on the CI as ORG_GRADLE_PROJECT_signingKeyId, ...).
signing {
    isRequired = false
}

afterEvaluate {
    if (project.hasProperty("signing.keyId")) {
        signing {
            sign(publishing.publications)
        }
    }
}
