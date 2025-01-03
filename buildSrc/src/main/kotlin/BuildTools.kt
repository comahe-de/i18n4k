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
            val deployOs = multiPlatformPublicationsToAllow[targetPublication.name]
            if (deployOs == null)
                throw IllegalStateException("OS to deploy publication of name '${targetPublication.name}' not specified!")
            val isDeploy = deployOs == os
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

    /**
     * When [BuildProperties.avoidDuplicatePublications] is true, this
     * list can be used to restrict for multi platform projects, which
     * target should be published from which OS to avoid duplicate
     * publications of modules that can be built on several platforms
     *
     * What can be build on with platform:
     * https://patrickjackson.dev/publishing-multiplatform-kotlin-libraries/
     *
     * ```
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
     *  ```
     */

    val multiPlatformPublicationsToAllow: Map<String, OperationSysten> =
        mapOf(
            // Windows
            "mingwX64" to OperationSysten.WINDOWS,
            "mingwx86" to OperationSysten.WINDOWS,
            // Linux
            "androidRelease" to OperationSysten.LINUX,
            "kotlinMultiplatform" to OperationSysten.LINUX,
            "metadata" to OperationSysten.LINUX,
            "jvm" to OperationSysten.LINUX,
            "js" to OperationSysten.LINUX,
            "linuxArm32Hfp" to OperationSysten.LINUX,
            "linuxArm64" to OperationSysten.LINUX,
            "linuxX64" to OperationSysten.LINUX,
            "linuxMips32" to OperationSysten.LINUX,
            "linuxMipsel32" to OperationSysten.LINUX,
            "androidNativeArm32" to OperationSysten.LINUX,
            "androidNativeArm64" to OperationSysten.LINUX,
            "wasm32" to OperationSysten.LINUX,
            "wasmJs" to OperationSysten.LINUX,
            "wasmWasi" to OperationSysten.LINUX,
            // MacOS
            "macosX64" to OperationSysten.MACOS,
            "macosArm64" to OperationSysten.MACOS,
            "iosX64" to OperationSysten.MACOS,
            "iosArm32" to OperationSysten.MACOS,
            "iosArm64" to OperationSysten.MACOS,
            "iosSimulatorArm64" to OperationSysten.MACOS,
            "watchosArm32" to OperationSysten.MACOS,
            "watchosArm64" to OperationSysten.MACOS,
            "watchosX86" to OperationSysten.MACOS,
            "watchosX64" to OperationSysten.MACOS,
            "watchosSimulatorArm64" to OperationSysten.MACOS,
            "tvosArm64" to OperationSysten.MACOS,
            "tvosX64" to OperationSysten.MACOS,
            "tvosSimulatorArm64" to OperationSysten.MACOS,
        )


    /** Nome of the current operating system */
    val osName: String?
        get() = System.getProperty("os.name")

    val os: OperationSysten
        get() {
            if (isWindows) return OperationSysten.WINDOWS
            if (isLinux) return OperationSysten.LINUX
            if (isMacOS) return OperationSysten.MACOS
            return OperationSysten.UNKNOWN
        }

    /** Is the current operating system Windows */
    val isWindows: Boolean
        get() = osName?.startsWith("Windows") ?: false

    /** Is the current operating system Linux */
    val isLinux: Boolean
        get() = osName?.startsWith("Linux") ?: false

    /** Is the current operating system MacOS */
    val isMacOS: Boolean
        get() = osName?.startsWith("Mac OS") ?: false

    enum class OperationSysten {
        WINDOWS,
        LINUX,
        MACOS,
        UNKNOWN
    }
}