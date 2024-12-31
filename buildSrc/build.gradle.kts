plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
    // so that external plugins can be resolved in dependencies section
    gradlePluginPortal()
    google()
}

dependencies {
   implementation(libs.kotlinGradlePlugin)
   implementation(libs.dokkaPlugin)
   implementation(libs.androidGradlePlugin)
}
