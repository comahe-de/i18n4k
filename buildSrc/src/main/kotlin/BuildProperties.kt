import org.gradle.api.Project

@Suppress("MemberVisibilityCanBePrivate")
object BuildProperties {
    /** For multi platform builds, some publications should only run on specific OSs */
    var avoidDuplicatePublications: Boolean = false
        private set

    var publishSnapshots: Boolean = true
        private set

    var publishReleases: Boolean = true
        private set

    // read properties during configuration phase,
    // avoid use of `project` in task execution.
    fun init(mainProject: Project) {
        avoidDuplicatePublications = readBooleanProperty(mainProject, "avoidDuplicatePublications")
            ?: avoidDuplicatePublications
        publishSnapshots = readBooleanProperty(mainProject, "publishSnapshots")
            ?: publishSnapshots
        publishReleases = readBooleanProperty(mainProject, "publishSnapshots")
            ?: publishReleases
        printProperties()
    }

    private fun readStringProperty(mainProject: Project, name: String): String? {
        if (!mainProject.hasProperty(name))
            return null
        return mainProject.properties[name].toString()
    }

    private fun readBooleanProperty(mainProject: Project, name: String): Boolean? =
        readStringProperty(mainProject, name)?.toString()?.toBoolean()

    fun printProperties() {
        println("### Custom build properties")
        println("### * avoidDuplicatePublications = $avoidDuplicatePublications ")
        println("### * publishSnapshots = $publishSnapshots ")
        println("### * publishReleases = $publishReleases ")
    }
}