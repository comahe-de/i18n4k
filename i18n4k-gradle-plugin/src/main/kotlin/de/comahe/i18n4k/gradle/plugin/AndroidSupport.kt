package de.comahe.i18n4k.gradle.plugin

import org.gradle.api.Project
import java.io.File

/**
 * Code for Android Grade Plugin as an extra class.
 *
 * Needed to avoid class not found exception when this is in a class use
 * by a non Android project.
 */
object AndroidSupport {
    fun addGeneratedLanguageFilesDirectoryToResources(project: Project, genResDir: File) {
        val androidExtension = project.extensions.getByName("android")
        val fileCollection = project.files(genResDir)
        when (androidExtension) {
            is com.android.build.gradle.AppExtension -> androidExtension.applicationVariants.all {
                it.registerGeneratedResFolders(fileCollection)
            }
            is com.android.build.gradle.LibraryExtension -> androidExtension.libraryVariants.all {
                it.registerGeneratedResFolders(fileCollection)
            }
            is com.android.build.gradle.TestExtension -> androidExtension.applicationVariants.all {
                it.registerGeneratedResFolders(fileCollection)
            }
        }
    }
}