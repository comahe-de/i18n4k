plugins {
    kotlin("jvm") // version from main build.gradle.kts
    `java-gradle-plugin`
    `gradle-plugin-publish`
}


repositories {
    mavenCentral()
    jcenter()
    maven { setUrl("https://jitpack.io") }
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":i18n4k-core"))
    implementation(project(":i18n4k-generator"))

    compileOnly(gradleApi())
    compileOnly(Dependencies.kotlinGradlePlugin)

    testImplementation(Dependencies.junit)
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

gradlePlugin {
    plugins {
        create("i18n4k") {
            id = "de.comahe.i18n4k"
            implementationClass = "de.comahe.i18n4k.gradle.plugin.I18n4kPlugin"
            displayName = "i18n4k"
            description = "i18n4k - Internationalization for Kotlin - Code Generator"

        }
    }
}

pluginBundle {
    website = "https://comahe-de.github.com/i18n4k"
    vcsUrl = "https://github.com/comahe-de/i18n4k"
    tags = listOf(
        "kotlin", "kotlin/multiplatform", "kotlin/js", "kotlin/jvm", "kotlin/native",
        "i18n", "internationalization", "code generator"
    )
}

java {
    withSourcesJar()
    withJavadocJar()
}


// Maven publications are added automatically during evaluation (pluginBundle)
// So call avoidDuplicatePublications() after evaluation
afterEvaluate {
    publishing {
        publications {
            // avoid duplicate publication for multi platform builds
            BuildTools.avoidDuplicatePublications(project, this)
        }
    }
}
