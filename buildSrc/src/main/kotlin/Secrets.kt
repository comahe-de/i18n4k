import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.util.Properties

/**
 * Loads secret system properties needed for signing and publishing to Maven Central and Gradle Plugin Portal
 *
 * The loaded "/secret.properties" file is in ".gitignore"!!!
 */
fun loadSecretProperties(project: Project) {
    {}::class.java.getResourceAsStream("/secret.properties").use {

        val secretProperties = Properties()
        secretProperties.load(it)
        secretProperties.forEach { key, value ->
            project.extra[key.toString()] = value
        }
    }

}