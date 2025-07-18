import org.apache.tools.ant.filters.StringInputStream
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Base64
import java.util.Properties

/** true, if [loadSecretProperties] was executed successfully. */
var secretPropertiesLoaded = false
/**
 * Loads secret system properties needed for signing and publishing to Maven Central and Gradle
 * Plugin Portal
 *
 * The loaded "/secret.properties" file is in ".gitignore"!!!
 */
fun loadSecretProperties(project: Project) :Boolean {

    try {
        var secretPropertiesFile = {}::class.java.getResourceAsStream("/secret.properties")
        if (secretPropertiesFile == null) {// read environment variable in CI
            val secretPropertiesFileString = System.getenv()["CI_SECRET_PROPERTIES_FILE"]
                ?: throw IllegalStateException("'/secret.properties' not found and no 'CI_SECRET_PROPERTIES_FILE'  environment variable")
            secretPropertiesFile = StringInputStream(secretPropertiesFileString)
        }

        secretPropertiesFile.use {

            val secretProperties = Properties()
            secretProperties.load(it)
            secretProperties.forEach { key, value ->
                project.extra[key.toString()] = value
            }
        }
        project.extra["signing.secretKeyRingFile"] = secretKeyRingFile.value.absolutePath
        secretPropertiesLoaded = true
        return true
    } catch (ex: Exception) {
        project.logger.warn("Could not load secrets for signing and publishing: " + ex.message)
        secretPropertiesLoaded = false
        return false
    }
}

private var secretKeyRingFile = lazy<File> {
    //dumpSecretKeyRingFileAsBase64()
    var secretKeyRingFileInput = {}::class.java.getResourceAsStream("/comahe-de.gpg")
    if (secretKeyRingFileInput == null) {// read environment variable in CI
        val secretKeyRingInputString = System.getenv()["CI_SECRET_KEY_RING_FILE"]
            ?: throw IllegalStateException("'comahe-de.gpg' not found and no 'CI_SECRET_KEY_RING_FILE' environment variable")
        secretKeyRingFileInput =
            ByteArrayInputStream(Base64.getDecoder().decode(secretKeyRingInputString))
    }
    secretKeyRingFileInput.use {
        val secretKeyRingFile = File.createTempFile("secretKeyRingFile-", ".gpg")
        secretKeyRingFile.deleteOnExit()

        FileOutputStream(secretKeyRingFile).use {
            secretKeyRingFileInput.copyTo(it)
        }
        return@lazy secretKeyRingFile
    }

}
/*

fun dumpSecretKeyRingFileAsBase64() {
    // Print secretKeyRingFileInput as BASE64
    val secretKeyRingFileInput: InputStream = {}::class.java.getResourceAsStream("/comahe-de.gpg")
        ?: throw IllegalStateException("'comahe-de.gpg' not found")
    secretKeyRingFileInput.use {
        println("-----------------")
        println(Base64.getEncoder().encodeToString(secretKeyRingFileInput.readAllBytes()))
        println("-----------------")
    }
}
*/
