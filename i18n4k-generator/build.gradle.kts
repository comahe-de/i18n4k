plugins {
    kotlin("jvm") // version from main build.gradle.kts
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":i18n4k-core"))

    implementation(libs.kotlinPoet)
    implementation(libs.slf4jApi)

    testImplementation(libs.logbackClassic)
    testImplementation(libs.junit)
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

java {
    withSourcesJar()
    withJavadocJar()
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
