@file:Suppress("unused")

package de.comahe.i18n4k.gradle.plugin

import org.gradle.api.Project
import org.slf4j.LoggerFactory


internal val logger = LoggerFactory.getLogger("i18n")

const val GENERATE_I18N_SOURCES_TASK_NAME = "generateI18n4kFiles"
const val CLEAR_I18N_SOURCES_TASK_NAME = "clearI18n4kFiles"


/**
 * Configures the [I18n4kPlugin] for this project.
 *
 * Executes the given configuration block against the [I18n4kExtension] for this
 * project.
 *
 * @param configuration the configuration block.
 */
fun Project.i18n4k(configuration: I18n4kExtension.() -> Unit) =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("i18n4k", configuration)



