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
    alias(libs.plugins.dokkaPlugin)
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

// Classpath used only by Dokka when documenting the library sources. It must NOT contain
// `i18n4k-core-jvm` / `i18n4k-generator`: those sources are documented themselves, and
// having the compiled classes on the classpath makes Dokka's K2 analysis crash with
// "Error in translating of symbol ... KaFirDefaultPropertyGetterSymbol".
val docsClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    docsClasspath(kotlin("stdlib"))
    docsClasspath(libs.kotlinxAtomicfu)
    docsClasspath(libs.kotlinxCollectionsImmutable)
    docsClasspath(libs.kotlinxCoroutinesCore)
    docsClasspath(libs.kotlinPoet)
    docsClasspath(libs.slf4jApi)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    // `java-gradle-plugin` already registers a `javadocJar` artifact. The Java `javadoc`
    // task has no Kotlin sources, so we add the Dokka HTML output to that same JAR instead
    // of creating a second artifact with the same classifier.
}

// The task is registered lazily by the plugin-publish plugin, so match it instead of using
// `tasks.named`.
tasks.withType<Jar>().matching { it.name == "javadocJar" }.configureEach {
    description = "Assembles a JAR containing the Dokka API documentation"
    dependsOn(tasks.named("dokkaGeneratePublicationHtml"))
    from(layout.buildDirectory.dir("dokka/html"))
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

// Sign the publications when a signing key is available.
//
// The CI supplies the same variables as for the library publication through the Kotlin
// Toolchain:
//   KOTLIN_TOOLCHAIN_SIGNING_KEY            ASCII-armored PGP private key
//   KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE optional passphrase
//
// Locally, the classic Gradle signing properties also work:
//   signing.keyId / signing.password / signing.secretKeyRingFile
val inMemorySigningKey = providers.environmentVariable("KOTLIN_TOOLCHAIN_SIGNING_KEY").orNull
val inMemorySigningPassphrase = providers.environmentVariable("KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE").orNull

signing {
    isRequired = false
    if (inMemorySigningKey != null) {
        useInMemoryPgpKeys(inMemorySigningKey, inMemorySigningPassphrase)
    }
}

afterEvaluate {
    if (inMemorySigningKey != null || project.hasProperty("signing.keyId")) {
        signing {
            sign(publishing.publications)
        }
    }
}

// ---------------------------------------------------------------------------
// API documentation for the Kotlin Toolchain library modules
//
// The Kotlin Toolchain 0.14 intentionally publishes an empty `-javadoc.jar`
// placeholder and has no Dokka integration yet. This build generates real API
// documentation directly from the Toolchain source layout; the release workflow
// publishes it. See doc-dev/deployment.md.
// ---------------------------------------------------------------------------
dokka {
    dokkaPublications.html {
        moduleName.set("i18n4k")
        moduleVersion.set(project.version.toString())
        outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
        failOnWarning.set(false)
    }

    dokkaSourceSets {
        val coreSourceSet = create("core") {
            displayName.set("i18n4k-core")
            sourceRoots.from(file("../i18n4k-core/src"))
            classpath.from(docsClasspath)
        }
        create("cldr") {
            displayName.set("i18n4k-cldr-plural-rules")
            sourceRoots.from(file("../i18n4k-cldr-plural-rules/src"))
            classpath.from(docsClasspath)
            dependentSourceSets.add(coreSourceSet.sourceSetId.get())
        }
        create("generator") {
            displayName.set("i18n4k-generator")
            sourceRoots.from(file("../i18n4k-generator/src/main/kotlin"))
            classpath.from(docsClasspath)
            dependentSourceSets.add(coreSourceSet.sourceSetId.get())
        }
    }
}
