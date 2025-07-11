package de.comahe.i18n4k.generator.tests

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.generator.GenerationTargetPlatform
import de.comahe.i18n4k.generator.I18n4kProcessor
import de.comahe.i18n4k.i18n4k
import org.junit.Assert.assertEquals
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File

internal val packageName = null
internal val commentLocale = Locale("en")
internal val sourceCodeLocales = listOf(
    forLocaleTag("en"),
    forLocaleTag("en_x_attrib_gender"),
    forLocaleTag("en_US"),
    forLocaleTag("en_US_texas"),
    forLocaleTag("de"),
    forLocaleTag("de_x_attrib_gender"),)

internal val inputDirectory = File("src/test/files/source_text")

internal val expectedGeneratedSourcesDirectory = File("src/test/files/generated_code")
internal val expectedGeneratedLanguageFilesDirectory = File("src/test/files/generated_text")

internal val actualGeneratedSourcesDirectory = File("temp/generated_code")
internal val actualGeneratedLanguageFilesDirectory = File("temp/generated_text")

internal val valueTypeMapping = mapOf(
    "AC" to "kotlin.AutoCloseable",
    "Locale" to "de.comahe.i18n4k.Locale",
)

class GeneratorTest {

    /** Test that the generated files are equal to the expected files */
    @Test
    fun compareGeneratedFiles() {

        for (target in GenerationTargetPlatform.entries) {
            val processor = I18n4kProcessor(
                inputDirectory = inputDirectory,
                generatedSourcesDirectory = File(actualGeneratedSourcesDirectory, target.name),
                generatedLanguageFilesDirectory = actualGeneratedLanguageFilesDirectory,
                generatedLanguageFilesDirAndroidRawResourceStyle = false,
                packageName = null,
                commentLocale = commentLocale,
                sourceCodeLocales = sourceCodeLocales,
                messageFormatter = i18n4k.messageFormatter,
                generationTarget = target,
                valueTypesEnabled = true,
                valueTypeMapping = valueTypeMapping,
                logger = LoggerFactory.getLogger("I18n4k-Processor")
            )
            processor.execute()

            val actualGeneratedSources =
                readDirectoryFiles(File(actualGeneratedSourcesDirectory, target.name))
            val expectedGeneratedSources =
                readDirectoryFiles(File(expectedGeneratedSourcesDirectory, target.name))

            val actualGeneratedLanguageFiles =
                readDirectoryFiles(actualGeneratedLanguageFilesDirectory)
            val expectedGeneratedLanguageFiles =
                readDirectoryFiles(expectedGeneratedLanguageFilesDirectory)

            assertSameContent(expectedGeneratedSources, actualGeneratedSources)
            assertSameContent(expectedGeneratedLanguageFiles, actualGeneratedLanguageFiles)
        }
    }

    /** Reads all text file in a directory
     *
     * @return A map with key: relative path to the start directory, value: content of the file*/
    private fun readDirectoryFiles(directory: File): Map<String, String> {
        val files = mutableMapOf<String, String>()
        directory.walk().forEach { file ->
            if (file.isFile) {
                files[file.relativeTo(directory).path] = file.readText().replace("\r\n", "\n")
            }
        }
        return files
    }

    /** Checks that the map created wth [readDirectoryFiles] have the same content */
    private fun assertSameContent(
        expectedFiles: Map<String, String>,
        actualFiles: Map<String, String>
    ) {

        // check for same files
        assertEquals("File names are not equal - ", expectedFiles.keys, actualFiles.keys)

        // check same content
        for (expectedFile in expectedFiles) {
            assertEquals(
                "Checked File is different: ${expectedFile.key} - ",
                expectedFile.value, actualFiles[expectedFile.key]
            )
        }

    }
}