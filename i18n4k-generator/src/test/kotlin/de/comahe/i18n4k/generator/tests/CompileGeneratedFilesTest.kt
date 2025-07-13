package de.comahe.i18n4k.generator.tests

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import de.comahe.i18n4k.generator.GenerationTargetPlatform
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class CompileGeneratedFilesTest {

    @Test
    fun testCompileGeneratedFiles_KMP() {
        asertCompilingWork(GenerationTargetPlatform.MULTI_PLATFORM)
    }

    @Test
    fun testCompileGeneratedFiles_JVM() {
        asertCompilingWork(GenerationTargetPlatform.JVM)
    }

    @Test
    fun testCompileGeneratedFiles_JS() {
        asertCompilingWork(GenerationTargetPlatform.JS)
    }

    @Test
    fun testCompileGeneratedFiles_Native() {
        asertCompilingWork(GenerationTargetPlatform.NATIVE)
    }


    @Test
    fun testCompileGeneratedFiles_Android() {
        asertCompilingWork(GenerationTargetPlatform.ANDROID)
    }

    @OptIn(ExperimentalCompilerApi::class)
    fun asertCompilingWork(target: GenerationTargetPlatform) {
        println("##########################################")
        println("## Start compilation of generated I18nFiles for: $target")

        val result = KotlinCompilation().apply {
            val baseDir = File(expectedGeneratedSourcesBaseDirectory, target.name)

            registerGeneratedSourcesDir(baseDir)
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }
}
