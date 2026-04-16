package de.comahe.i18n4k.gradle.plugin

import de.comahe.i18n4k.gradle.plugin.I18n4kPlugin.SourceDirectoryType
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import java.io.File

/**
 * Code for Android Grade Plugin as an extra class.
 *
 * Needed to avoid class not found exception when this is in a class use
 * by a non Android project.
 */
internal object AndroidSupport {
    internal fun addGeneratedLanguageFilesDirectoryToResources(project: Project, genResDir: File) {
        val androidExtension = project.extensions.getByName("android")
        val fileCollection = project.files(genResDir)

        when (androidExtension) {
            is com.android.build.gradle.AppExtension -> {
                logger.info("Adding i18n4k generated resources directory to Android resources - via AppExtension...")
                androidExtension.applicationVariants.all {
                    it.registerGeneratedResFolders(fileCollection)
                }
            }

            is com.android.build.gradle.LibraryExtension -> {
                logger.info("Adding i18n4k generated resources directory to Android resources - via LibraryExtension...")

                androidExtension.libraryVariants.all {
                    it.registerGeneratedResFolders(fileCollection)
                }
            }

            is com.android.build.gradle.TestExtension -> {
                logger.info("Adding i18n4k generated resources directory to Android resources - via TestExtension...")

                androidExtension.applicationVariants.all {
                    it.registerGeneratedResFolders(fileCollection)
                }
            }

            is com.android.build.api.dsl.CommonExtension -> {
                logger.info("Adding i18n4k generated resources directory to Android resources - via CommonExtension...")

                try {
                    androidExtension.sourceSets.getByName("main").res.directories.add(genResDir.absolutePath)
                } catch (e: Exception) {
                    logger.warn("Unable to add generated resources to 'main' source set!", e)
                }
            }

        }
    }

    /** Finds the [SourceDirectorySet].
     *
     * Android projects – use the Android extension, since AGP 9.0 - amd not the default source sets.
     *
     * @return the found Android source set. Null if not found, the default source set should be tried in this case.
     */
    internal fun findSourceDirectorySet(
        project: Project,
        type: SourceDirectoryType
    ): SourceDirectorySet? {

        val androidExt = project.extensions.findByName("android")
        // The concrete type can be ApplicationExtension, LibraryExtension, etc.
        // All of them inherit from CommonExtension which exposes `sourceSets`.
        if (androidExt != null && androidExt is com.android.build.api.dsl.CommonExtension) {

            val androidSourceSet = androidExt.sourceSets.getByName("main")

            return AndroidSourceSetToSourceDirectorySet(
                when (type) {
                    SourceDirectoryType.KOTLIN -> androidSourceSet.kotlin   // KotlinSourceDirectorySet
                    SourceDirectoryType.RESOURCES -> androidSourceSet.resources // ResourceSourceDirectorySet
                }
            )
        }

        return null
    }
}