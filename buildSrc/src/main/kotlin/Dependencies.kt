import org.gradle.kotlin.dsl.version

object Versions {
    const val kotlin = "1.6.21"
    const val kotlinxAtomicfu = "0.17.2"
    const val kotlinxCollectionsImmutable = "0.3.5"
    const val kotlinPoet = "1.11.0"
    const val slf4j = "1.7.32"
    const val logback = "1.2.6"
    const val junit = "4.13"

    // Plugins
    const val gradlePluginPublish = "0.16.0"
}

object Dependencies {
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinxAtomicfu =
        "org.jetbrains.kotlinx:atomicfu:${Versions.kotlinxAtomicfu}"
    const val kotlinxCollectionsImmutable =
        "org.jetbrains.kotlinx:kotlinx-collections-immutable:${Versions.kotlinxCollectionsImmutable}"
    const val kotlinPoet =
        "com.squareup:kotlinpoet:${Versions.kotlinPoet}"
    const val slf4jApi =
        "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val logbackClassic =
        "ch.qos.logback:logback-classic:${Versions.logback}"
    const val junit =
        "junit:junit:${Versions.junit}"
}

// Plugins...
@Suppress("ObjectPropertyName")
inline val org.gradle.plugin.use.PluginDependenciesSpec.`gradle-plugin-publish`: org.gradle.plugin.use.PluginDependencySpec
    get() = id("com.gradle.plugin-publish") version  Versions.gradlePluginPublish
