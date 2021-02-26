@Suppress("MemberVisibilityCanBePrivate")
object BuildProperties {
    /** For multi platform builds, some publications should only run on specific OSs */
    val avoidDuplicatePublications: Boolean
        get() = readBooleanProperty("avoidDuplicatePublications") ?: false

    val publishSnapshots: Boolean
        get() = readBooleanProperty("publishSnapshots") ?: true

    val publishReleases: Boolean
        get() = readBooleanProperty("publishSnapshots") ?: true


    private fun readStringProperty(name: String): String? {
        if (!BuildTools.mainProject.hasProperty(name))
            return null
        return BuildTools.mainProject.properties[name].toString()
    }

    private fun readBooleanProperty(name: String): Boolean? =
        readStringProperty(name)?.toString()?.toBoolean()

    fun printProperties() {
        println("### Custom build properties")
        println("### * avoidDuplicatePublications = $avoidDuplicatePublications ")
        println("### * publishSnapshots = $publishSnapshots ")
        println("### * publishReleases = $publishReleases ")
    }
}