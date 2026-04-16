import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    id("de.comahe.i18n4k")
}

i18n4k {
    sourceCodeLocales =
        listOf("en", "en_US", "de", "de_x_attr_gender", "de_x_attr_decl_genitive", "en_x_attr_gender", "x_attr_emoji")
    // Add "LS" as shortcut for LocalizedString class
    valueTypeMapping = mapOf("LS" to "de.comahe.i18n4k.strings.LocalizedString")
    // Create the resource files inside the compose resources folder for files
    // Hint: Compose resources plugin currently does not support multiple directories for one source-set.
    //       Therefore, the I18n4k-Plugin cannot add generated folder to the resources!
    languageFilesOutputDirectory = "{projectDir}/src/commonMain/composeResources/files/i18n"
}

kotlin {
    // Android target configured via androidLibrary block (replaces androidTarget + android{})
    androidLibrary {
        namespace = "example.compose.composeapp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        // Required for Compose Multiplatform resources to be bundled into the AAR
        androidResources {
            enable = true
        }
    }

    // build iOS only on Mac, as it hangs in the GitHub-Pipeline for Linux.
    if (isMacOs()) {
        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
            }
        }
    }
    
    jvm()
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {

            implementation("de.comahe.i18n4k:i18n4k-core:0.11.2-SNAPSHOT")
            implementation("de.comahe.i18n4k:i18n4k-cldr-plural-rules:0.11.2-SNAPSHOT")

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "example.compose.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "example.compose"
            packageVersion = "1.0.0"
        }
    }
}

fun isMacOs() = org.gradle.internal.os.OperatingSystem.current().isMacOsX