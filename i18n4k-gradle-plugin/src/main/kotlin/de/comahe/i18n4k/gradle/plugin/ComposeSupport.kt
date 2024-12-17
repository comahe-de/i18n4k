package de.comahe.i18n4k.gradle.plugin

import org.gradle.api.Project
import java.io.File

/**
 * Code for Compose Grade Plugin as an extra class.
 *
 * Needed to avoid class not found exception when this is in a class use by a non Compose project.
 *
 * TODO: currently the resource extension does not support multiple directories.
 *  Therefor, we cannot add the generated text file directory without overwriting the user resource directory.
 *  Therefor, we cannot use the code below actually :-(
 */
object ComposeSupport {
    fun addGeneratedLanguageFilesDirectoryToResources(project: Project, genResDir: File) {
        val composeExtension = project.extensions.findByName("compose")
        if (composeExtension == null) {
            logger.warn("Cannot find 'compose' extension")
            return
        }
        if (composeExtension !is org.jetbrains.compose.ComposeExtension) {
            logger.warn("Compose extension is not of type org.jetbrains.compose.ComposeExtension")
            return
        }
        val resourcesExtension = composeExtension.extensions.findByName("resources")
        if (resourcesExtension == null) {
            logger.warn("Cannot find 'compose.resources' extension")
            return
        }
        if (resourcesExtension !is org.jetbrains.compose.resources.ResourcesExtension) {
            logger.warn("Compose extension is not of type org.jetbrains.compose.resources.ResourcesExtension")
            return
        }
        logger.info("Adding '$genResDir' as 'customDirectory' for compose resources")
        resourcesExtension.customDirectory(
            "commonMain",
            project.layout.dir(project.providers.provider { genResDir })
        )

    }
}