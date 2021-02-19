plugins {
    kotlin("jvm") // version from main build.gradle.kts
    `maven-publish`
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":i18n4k-core"))

    implementation(Dependencies.kotlinPoet)
    implementation(Dependencies.slf4jApi)

    testImplementation(Dependencies.logbackClassic)
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
