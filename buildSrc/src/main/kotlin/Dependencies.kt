import org.gradle.kotlin.dsl.version

object Versions {
    // https://kotlinlang.org/docs/releases.html#release-details
    const val kotlin = "1.8.22"
    // https://github.com/Kotlin/kotlinx-atomicfu
    const val kotlinxAtomicfu = "0.21.0"
    // https://github.com/Kotlin/kotlinx.collections.immutable
    const val kotlinxCollectionsImmutable = "0.3.5"
    // https://github.com/square/kotlinpoet
    const val kotlinPoet = "1.14.2"
    // https://www.slf4j.org/news.html
    const val slf4j = "1.7.36"
    // https://logback.qos.ch/news.html
    const val logback = "1.2.11"
    // https://junit.org/junit4/
    const val junit = "4.13.2"

    ////////////// Plugins
    // https://plugins.gradle.org/plugin/com.gradle.plugin-publish
    const val gradlePluginPublish = "0.16.0"
    // https://mvnrepository.com/artifact/com.android.tools.build/gradle?repo=google
    const val androidPlugin = "7.4.2"
}

object Dependencies {
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val androidGradlePlugin =
        "com.android.tools.build:gradle:${Versions.androidPlugin}"
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
