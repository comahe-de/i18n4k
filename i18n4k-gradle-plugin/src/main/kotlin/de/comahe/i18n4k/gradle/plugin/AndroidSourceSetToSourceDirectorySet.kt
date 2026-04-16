package de.comahe.i18n4k.gradle.plugin

import com.android.build.api.dsl.AndroidSourceDirectorySet
import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.DirectoryTree
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.provider.Provider
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.TaskDependency
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.util.PatternFilterable
import java.io.File
import java.util.function.Function

/**
 * An adapter class that wraps an [AndroidSourceDirectorySet] to implement the [SourceDirectorySet] interface.
 *
 * This class provides a bridge between Android-specific source set management and the standard
 * Gradle source directory set API, allowing Android source directories to be treated as generic
 * source directory sets.
 *
 * Only used methods are implemented!
 */
class AndroidSourceSetToSourceDirectorySet(val androidSources: AndroidSourceDirectorySet) : SourceDirectorySet {
    override fun getName(): String {
        return androidSources.getName()
    }

    override fun srcDir(srcPath: Any): SourceDirectorySet {
        androidSources.srcDir(srcPath)
        return this
    }

    override fun srcDirs(vararg srcPaths: Any?): SourceDirectorySet {
        srcPaths.forEach { it?.let { srcDir(it) } }
        return this
    }

    override fun getSrcDirs(): Set<File> {
        return androidSources.directories.map { File(it) }.toSet()
    }

    override fun setSrcDirs(srcPaths: Iterable<*>): SourceDirectorySet {
        androidSources.setSrcDirs(srcPaths)
        return this
    }

    override fun source(source: SourceDirectorySet): SourceDirectorySet {
        TODO()
    }

    override fun getSourceDirectories(): FileCollection {
        TODO()
    }

    override fun getSrcDirTrees(): Set<DirectoryTree> {
        TODO()
    }

    override fun getFilter(): PatternFilterable {
        TODO()
    }

    override fun getDestinationDirectory(): DirectoryProperty {
        TODO()
    }

    override fun getClassesDirectory(): Provider<Directory> {
        TODO()
    }

    override fun <T : Task> compiledBy(
        taskProvider: TaskProvider<T>,
        mapping: Function<T, DirectoryProperty>
    ) {
        TODO()
    }

    override fun matching(filterConfigClosure: Closure<*>): FileTree {
        TODO()
    }

    override fun matching(filterConfigAction: Action<in PatternFilterable>): FileTree {
        TODO()
    }

    override fun matching(patterns: PatternFilterable): FileTree {
        TODO()
    }

    override fun visit(visitor: FileVisitor): FileTree {
        TODO()
    }

    override fun visit(visitor: Closure<*>): FileTree {
        TODO()
    }

    override fun visit(visitor: Action<in FileVisitDetails>): FileTree {
        TODO()
    }

    override fun plus(fileTree: FileTree): FileTree {
        TODO()
    }

    override fun getAsFileTree(): FileTree {
        TODO()
    }

    override fun getFiles(): Set<File> {
        TODO()
    }

    override fun getSingleFile(): File {
        TODO()
    }

    override fun contains(file: File): Boolean {
        TODO()
    }

    override fun getAsPath(): String {
        TODO()
    }

    override fun plus(collection: FileCollection): FileCollection {
        TODO()
    }

    override fun minus(collection: FileCollection): FileCollection {
        TODO()
    }

    override fun filter(filterClosure: Closure<*>): FileCollection {
        TODO()
    }

    override fun filter(filterSpec: Spec<in File>): FileCollection {
        TODO()
    }

    override fun isEmpty(): Boolean {
        TODO()
    }

    override fun getElements(): Provider<Set<FileSystemLocation>> {
        TODO()
    }

    override fun addToAntBuilder(
        builder: Any,
        nodeName: String,
        type: FileCollection.AntType
    ) {
        TODO()
    }

    override fun addToAntBuilder(builder: Any, nodeName: String): Any {
        TODO()
    }

    override fun iterator(): MutableIterator<File> {
        TODO()
    }

    override fun getBuildDependencies(): TaskDependency {
        TODO()
    }

    override fun getIncludes(): Set<String> {
        TODO()
    }

    override fun getExcludes(): Set<String> {
        TODO()
    }

    override fun setIncludes(includes: Iterable<String>): PatternFilterable {
        TODO()
    }

    override fun setExcludes(excludes: Iterable<String>): PatternFilterable {
        TODO()
    }

    override fun include(vararg includes: String): PatternFilterable {
        TODO()
    }

    override fun include(includes: Iterable<String>): PatternFilterable {
        TODO()
    }

    override fun include(includeSpec: Spec<FileTreeElement>): PatternFilterable {
        TODO()
    }

    override fun include(includeSpec: Closure<*>): PatternFilterable {
        TODO()
    }

    override fun exclude(vararg excludes: String): PatternFilterable {
        TODO()
    }

    override fun exclude(excludes: Iterable<String>): PatternFilterable {
        TODO()
    }

    override fun exclude(excludeSpec: Spec<FileTreeElement>): PatternFilterable {
        TODO()
    }

    override fun exclude(excludeSpec: Closure<*>): PatternFilterable {
        TODO()
    }

    override fun getDisplayName(): String {
        TODO()
    }
}