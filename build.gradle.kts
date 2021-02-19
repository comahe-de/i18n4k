plugins {
    idea
    kotlin("jvm") version  Versions.kotlin
}

allprojects {
    group = "de.comahe.i18n4k"
    version = "0.1.0-SNAPSHOT"

    repositories{
        mavenLocal()
        mavenCentral()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }
}

