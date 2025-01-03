rootProject.name = "example-compose"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenLocal() // needed testing local plugin deployments
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()  // needed testing local artefact deployments
        google()
        mavenCentral()
    }
}

include(":composeApp")
