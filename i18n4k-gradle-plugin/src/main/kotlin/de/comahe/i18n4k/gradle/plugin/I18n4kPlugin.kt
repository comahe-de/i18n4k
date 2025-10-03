package de.comahe.i18n4k.gradle.plugin

import de.comahe.i18n4k.generator.GenerationTargetPlatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

open class I18n4kPlugin : Plugin<Project> {

    private lateinit var config: I18n4kExtension


    override fun apply(project: Project) {
        logger.info("Applying i18n4k plugin...")
        config = project.extensions.create("i18n4k", I18n4kExtension::class.java)

        if (config.generationTargetPlatform == null) {
            config.generationTargetPlatform = when {
                project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") ->
//                    if (project.plugins.hasPlugin("org.jetbrains.kotlin.plugin.compose")
//                        // resources extension available?
//                        && (project.extensions.findByName("compose") as? ExtensionAware)
//                            ?.extensions?.findByName("resources") != null
//                    )
//                        GenerationTargetPlatform.COMPOSE_MULTI_PLATFORM
//                    else
                        GenerationTargetPlatform.MULTI_PLATFORM

                project.plugins.hasPlugin("org.jetbrains.kotlin.js") ->
                    GenerationTargetPlatform.JS

                project.plugins.hasPlugin("org.jetbrains.kotlin.native") ->
                    GenerationTargetPlatform.NATIVE

                project.plugins.hasPlugin("org.jetbrains.kotlin.jvm") ->
                    GenerationTargetPlatform.JVM

                project.plugins.hasPlugin("org.jetbrains.kotlin.android") ->
                    GenerationTargetPlatform.ANDROID

                else -> throw IllegalStateException(
                    "No of the supported Kotlin-Plugins (multiplatform, js, native, jvm, android) " +
                        "has be applied to the project.\nPlugins: "
                        + project.plugins.toString()
                )
            }
        }


        addNeededPlugins(project)
        defineTasks(project)
        project.afterEvaluate { addTaskDependencies(project) }



        addGeneratedSourcesDirectoryToSourceSets(project)
        addGeneratedLanguageFilesDirectoryToResources(project)
        markGeneratedSourcesDirectoryAsGeneratedInIde(
            project,
            project.extensions.findByType(IdeaModel::class.java)
        )


        // make sure the generated directory is marked "generated"
        project.plugins.withType(IdeaPlugin::class.java) {
            markGeneratedSourcesDirectoryAsGeneratedInIde(project, it.model)
        }
    }

    /** adds other needed plugins. */
    private fun addNeededPlugins(project: Project) {
        // we also need the "idea" plugin to mark the directory with the generated sources as "generated sources root"
        project.pluginManager.apply("org.gradle.idea")
    }

    /** defines the task for this plugin */
    private fun defineTasks(project: Project) {
        // generateI18nSources task
        project.tasks.register(
            GENERATE_I18N_SOURCES_TASK_NAME,
            GenerateI18n4kFilesTask::class.java
        )
        {
            it.config = this@I18n4kPlugin.config
            it.description = "Generate i18n4k files"
            it.group = "i18n"
        }

        // clearI18nSources task
        project.tasks.register(
            CLEAR_I18N_SOURCES_TASK_NAME,
            ClearI18n4kFilesTask::class.java
        )
        {
            it.config = this@I18n4kPlugin.config
            it.description = "Clear i18n4k files"
            it.group = "i18n"
        }
    }

    /** Add `dependsOn()` to external task where the t184k tasks should run before */
    private fun addTaskDependencies(project: Project) {

        // add it to all "compile*"-tasks, like "compileKotlin", "compileKotlinJvm"
        // "compileJava", "compileKotlinJs", "compileKotlinNative",
        // "compileKotlinMetadata", ...
        project.tasks.matching { it.name.contains("compile", true) }
            .configureEach { it.dependsOn(GENERATE_I18N_SOURCES_TASK_NAME) }
        project.tasks.withType(KotlinCompile::class.java)
            .configureEach { it.dependsOn(GENERATE_I18N_SOURCES_TASK_NAME) }
        project.tasks.withType(JavaCompile::class.java)
            .configureEach { it.dependsOn(GENERATE_I18N_SOURCES_TASK_NAME) }

        // Resource processing for normal projects
        project.tasks.withType(ProcessResources::class.java)
            .configureEach { it.dependsOn(GENERATE_I18N_SOURCES_TASK_NAME) }

        // Jar-Tasks...
        project.tasks.withType(org.gradle.jvm.tasks.Jar::class.java)
            .configureEach { it.dependsOn(GENERATE_I18N_SOURCES_TASK_NAME) }
        project.tasks.withType(org.gradle.api.tasks.bundling.Jar::class.java)
            .configureEach { it.dependsOn(GENERATE_I18N_SOURCES_TASK_NAME) }

        // Android resource processing:
        // - packageDebugResources, packageReleaseResources, ...
        // - mergeDebugResources, mergeReleaseResources, ...
        // Android misc tasks
        // - extractDeepLinksDebug, extractDeepLinksRelease, ...
        // Compose resource processing:
        // - copyNonXmlValueResourcesForCommonMain
        // - generateExpectResourceCollectorsForCommonMain
        // Compose misc tasks
        // - commonizeCInterop
        // - generateComposeResClass
        // - generateDebugResValues, generateReleaseResValues
        // - checkDebugAarMetadata, checkReleaseAarMetadata
        // - checkDebugDuplicateClasses, checkReleaseDuplicateClasses
        // - writeDebugSigningConfigVersions, writeReleaseSigningConfigVersions
        // - mergeDebugShaders, mergeReleaseShaders
        // - unpackSkikoWasmRuntime
        // - extractProguardFiles
        // - wasmJsPublicPackageJson
        // - and many more...
        project.tasks.matching { task ->
            var match = false
            for (name in listOf(
                "Resource",
                "commonizeCInterop",
                "unpack", "extract",
                "ResClass", "ResValues", "Metadata", "Classes", "Signing",
                "merge", "package",
                "Dependencies", "Assets",
                "debug", "release"
            )) {
                if (task.name.contains(name, true)) {
                    match = true
                    break
                }
            }
            return@matching match
        }
            .configureEach { it.dependsOn(GENERATE_I18N_SOURCES_TASK_NAME) }

    }


    /** Finds the [SourceDirectorySet] depending on the project type (jvm, multiplatform, ...) */
    private fun findSourceDirectorySet(
        project: Project,
        type: SourceDirectoryType
    ): SourceDirectorySet {
        ////// find correct sourceDirectorySet
        val sourceSets = project.extensions
            // InteliJ cannot find this `KotlinProjectExtension` but it compiles!
            .getByType(KotlinProjectExtension::class.java).sourceSets

        val sourceSet = sourceSets.getByName(
            if (config.generationTargetPlatform == GenerationTargetPlatform.MULTI_PLATFORM
                // || config.generationTargetPlatform == GenerationTargetPlatform.COMPOSE_MULTI_PLATFORM
                )
                "commonMain"
            else
                "main"
        ) as KotlinSourceSet

        return when (type) {
            SourceDirectoryType.KOTLIN -> sourceSet.kotlin
            SourceDirectoryType.RESOURCES -> sourceSet.resources
        }

    }

    /** Add a directory to a [SourceDirectorySet] */
    private fun addDirectoryToSourceDirectorySet(
        sourceDirectorySet: SourceDirectorySet,
        directory: File
    ) {
        // Adds the given source directories to the set.
        sourceDirectorySet.srcDirs(directory)
        if (logger.isDebugEnabled) {
            sourceDirectorySet.srcDirs.forEach {
                logger.debug("{} - {}", sourceDirectorySet.name, it)
            }
        }
    }

    /** Adds the [getGeneratedSourcesDirectory] to the Kotlin sources */
    private fun addGeneratedSourcesDirectoryToSourceSets(project: Project) {
        if (config.generationTargetPlatform == GenerationTargetPlatform.JVM) {
            val sourceSets = project.properties["sourceSets"] as SourceSetContainer

            val sourceDirectorySet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).java
            addDirectoryToSourceDirectorySet(
                sourceDirectorySet,
                getGeneratedSourcesDirectory(project, config)
            )
        } else {
            val sourceDirectorySet = findSourceDirectorySet(project, SourceDirectoryType.KOTLIN)
            logger.info("Adding i18n4k generated sources directory to source set '${sourceDirectorySet.name}'")
            addDirectoryToSourceDirectorySet(
                sourceDirectorySet,
                getGeneratedSourcesDirectory(project, config)
            )
        }
    }

    /** Adds the [getGeneratedLanguageFilesDirectory] to the resources */
    private fun addGeneratedLanguageFilesDirectoryToResources(project: Project) {
        val genResDir = getGeneratedLanguageFilesDirectory(project, config)
        genResDir.mkdirs()

        if (config.generationTargetPlatform == GenerationTargetPlatform.JVM) {
            val sourceSets = project.properties["sourceSets"] as SourceSetContainer

            val sourceDirectorySet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).resources
            addDirectoryToSourceDirectorySet(sourceDirectorySet, genResDir)

        } else if (config.generationTargetPlatform == GenerationTargetPlatform.ANDROID) {
            AndroidSupport.addGeneratedLanguageFilesDirectoryToResources(project, genResDir)
        } else {
            val sourceDirectorySet = findSourceDirectorySet(project, SourceDirectoryType.RESOURCES)
            logger.info("Adding i18n4k generated resources directory to source set '${sourceDirectorySet.name}'")
            addDirectoryToSourceDirectorySet(sourceDirectorySet, genResDir)
        }
    }

    /** Adds the [getGeneratedSourcesDirectory] to the IDEs (IDEA) set of generated sources */
    private fun markGeneratedSourcesDirectoryAsGeneratedInIde(
        project: Project,
        ideaModel: IdeaModel?
    ) {
        if (ideaModel == null) {
            logger.warn("Idea model not set? Missing idea plugin?")
            return
        }
        logger.info("Mark i18n4k generated sources directory as 'generated' in IDEA")
        ideaModel.module.generatedSourceDirs.add(getGeneratedSourcesDirectory(project, config))
        if (logger.isDebugEnabled) {
            ideaModel.module.generatedSourceDirs.forEach {
                logger.debug("generatedSourceDirs[IDEA] - {}", it)
            }
        }
    }
    private fun getGeneratedSourcesDirectory(project: Project, config: I18n4kExtension): File {
        return Companion.getGeneratedSourcesDirectory(project.layout.buildDirectory.get().asFile, config)
    }

    private fun getGeneratedLanguageFilesDirectory(project: Project, config: I18n4kExtension): File {
        return Companion.getGeneratedLanguageFilesDirectory(
            project.layout.buildDirectory.get().asFile,
            project.layout.projectDirectory.asFile,
            config
        )
    }

    companion object {
        fun getGeneratedSourcesDirectory(buildDirectory: File, config: I18n4kExtension): File {
            val dir = File(
                config.sourceCodeOutputDirectory.replace(
                    "{buildDir}",
                    buildDirectory.absolutePath
                )
            )
            dir.mkdirs()
            return dir
        }

        fun getGeneratedLanguageFilesDirectory(
            buildDirectory: File,
            projectDirectory: File,
            config: I18n4kExtension
        ): File {

            val dir = File(
                config.languageFilesOutputDirectory.replace(
                    "{buildDir}",
                    buildDirectory.absolutePath
                ).replace(
                    "{projectDir}",
                    projectDirectory.absolutePath
                )
            )
            dir.mkdirs()
            return dir
        }
    }

    /** Types of [SourceDirectorySet] */
    internal enum class SourceDirectoryType {
        /** Kotlin or java code sources */
        KOTLIN,

        /** Resource files */
        RESOURCES,
    }
}



