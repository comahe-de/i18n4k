import org.gradle.api.Project
import org.gradle.api.publish.Publication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven

@Suppress("MemberVisibilityCanBePrivate")
object BuildTools {
    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var mainProject: Project

    /**
     *  When [BuildProperties.avoidDuplicatePublications] is true, this function can be used
     *  to restrict for multi platform projects, which target should be published from which OS
     * to avoid duplicate publications of modules that can be built on several platforms
     */
    fun avoidDuplicateMultiPlatformPublications(
        project: Project,
        targetPublications: Collection<Publication>
    ) {
        if (!BuildProperties.avoidDuplicatePublications)
            return
        println("########### ${project.name}: Host OS is: $osName ##################")
        println("### Having publications: ")
        targetPublications.forEach { targetPublication ->
            val isDeploy = (targetPublication.name in multiPlatformPublicationsToAllow)
            println("### * \"${targetPublication.name}\" - publish: $isDeploy")
            avoidDuplicatePublication(project, targetPublication, isDeploy)
        }
    }

    /** When [BuildProperties.avoidDuplicatePublications] is true: For projects that are build from
     *  multiple platforms, this function can be used to restrict the publication to one platform
     *  (default Linux) */
    fun avoidDuplicatePublications(
        project: Project,
        targetPublications: Collection<Publication>,
        isDeploy: Boolean = isLinux
    ) {
        if (!BuildProperties.avoidDuplicatePublications)
            return
        targetPublications.forEach {
            avoidDuplicatePublication(project, it, isDeploy)
        }
    }


    /** When [BuildProperties.avoidDuplicatePublications] is true: For projects that are build from
     *  multiple platforms, this function can be used to restrict the publication to one platform
     *  (default Linux) */
    fun avoidDuplicatePublication(
        project: Project,
        targetPublication: Publication,
        isDeploy: Boolean = isLinux
    ) {
        if (!BuildProperties.avoidDuplicatePublications)
            return
        project.afterEvaluate {
            tasks.withType(AbstractPublishToMaven::class.java)
                .matching {
                    it.publication == targetPublication
                }
                .configureEach {
                    onlyIf {
                        println("###> ${project.name}: Publication \"${targetPublication.name}\" - publish: $isDeploy")
                        isDeploy
                    }
                }
        }
    }

    /** When [BuildProperties.avoidDuplicatePublications] is true, this list can be used to restrict
     * for multi platform projects, which target should be published from which OS
     * to avoid duplicate publications of modules that can be built on several platforms
     *
     * What can be build on with platform:
     * https://patrickjackson.dev/publishing-multiplatform-kotlin-libraries/
     *
     *  |                 | Linux  | Windows | Macos |
     *  |-----------------|--------|--------|--------|
     *  | androidNative32 |   ✅   |    ✅   |   ✅   |
     *  | androidNative64 |   ✅   |    ✅   |   ✅   |
     *  | jvm             |   ✅   |    ✅   |   ✅   |
     *  | js              |   ✅   |    ✅   |   ✅   |
     *  | iosArm32        |   ❌   |    ❌   |   ✅   |
     *  | iosArm64        |   ❌   |    ❌   |   ✅   |
     *  | iosX64          |   ❌   |    ❌   |   ✅   |
     *  | macosX64        |   ❌   |    ❌   |   ✅   |
     *  | mingwx64        |   ❌   |    ✅   |   ❌   |
     *  | mingwx86        |   ❌   |    ✅   |   ❌   |
     *  | wasm32          |   ✅   |    ❌   |   ✅   |
     *  | linuxArm32Hfp   |   ✅   |    ✅   |   ✅   |
     *  | linuxArm64      |   ✅   |    ✅   |   ✅   |
     *  | linuxMips32     |   ✅   |    ❌   |   ❌   |
     *  | linuxMipsel32   |   ✅   |    ❌   |   ❌   |
     *  | linuxX64        |   ✅   |    ✅   |   ✅   |
     */
    val multiPlatformPublicationsToAllow: List<String>
        get() = when {
            isWindows -> listOf(
                "mingwX64",
                "mingwx86"
            )
            isLinux -> listOf(
                "kotlinMultiplatform",
                "metadata",
                "jvm",
                "js",
                "linuxArm32Hfp",
                "linuxArm64",
                "linuxX64",
                "linuxMips32",
                "linuxMipsel32",
                "androidNativeArm32",
                "androidNativeArm64",
                "wasm32",
            )
            isMacOS -> listOf(
                "macosX64",
                "macosArm64",
                "iosX64",
                "iosArm32",
                "iosArm64",
                "iosSimulatorArm64",
                "watchosArm32",
                "watchosArm64",
                "watchosX86",
                "watchosX64",
                "watchosSimulatorArm64",
                "tvosArm64",
                "tvosX64",
                "tvosSimulatorArm64",
            )
            else -> emptyList()
        }

    /** Nome of the current operating system */
    val osName: String?
        get() = System.getProperty("os.name")

    /** Is the current operating system Windows  */
    val isWindows: Boolean
        get() = osName?.startsWith("Windows") ?: false

    /** Is the current operating system Linux  */
    val isLinux: Boolean
        get() = osName?.startsWith("Linux") ?: false

    /** Is the current operating system MacOS  */
    val isMacOS: Boolean
        get() = osName?.startsWith("Mac OS") ?: false
}