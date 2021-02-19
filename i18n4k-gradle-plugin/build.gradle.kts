plugins {
    kotlin("jvm") // version from main build.gradle.kts
    `maven-publish`
    `java-gradle-plugin`
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
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
