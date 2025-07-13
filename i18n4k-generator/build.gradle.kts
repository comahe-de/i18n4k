import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":i18n4k-core"))

    implementation(libs.kotlinPoet)
    implementation(libs.slf4jApi)

    testImplementation(libs.logbackClassic)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinCompileTesting)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }

        // avoid duplicate publication for multi platform builds
        BuildTools.avoidDuplicatePublications(project,this)
    }
}
