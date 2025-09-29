import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.publish.Publication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.extra
import org.jreleaser.model.Http;
import org.jreleaser.sdk.mavencentral.MavenCentral
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.system.measureTimeMillis


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

    val multiPlatformPublicationsToAllow: Map<String, OperationSystem> =
        mapOf(
            // Windows
            "mingwX64" to OperationSystem.WINDOWS,
            "mingwx86" to OperationSystem.WINDOWS,
            // Linux
            "androidRelease" to OperationSystem.LINUX,
            "kotlinMultiplatform" to OperationSystem.LINUX,
            "metadata" to OperationSystem.LINUX,
            "jvm" to OperationSystem.LINUX,
            "js" to OperationSystem.LINUX,
            "linuxArm32Hfp" to OperationSystem.LINUX,
            "linuxArm64" to OperationSystem.LINUX,
            "linuxX64" to OperationSystem.LINUX,
            "linuxMips32" to OperationSystem.LINUX,
            "linuxMipsel32" to OperationSystem.LINUX,
            "androidNativeX86" to OperationSystem.LINUX,
            "androidNativeX64" to OperationSystem.LINUX,
            "androidNativeArm32" to OperationSystem.LINUX,
            "androidNativeArm64" to OperationSystem.LINUX,
            "wasm32" to OperationSystem.LINUX,
            "wasmJs" to OperationSystem.LINUX,
            "wasmWasi" to OperationSystem.LINUX,
            // MacOS
            "macosX64" to OperationSystem.MACOS,
            "macosArm64" to OperationSystem.MACOS,
            "iosX64" to OperationSystem.MACOS,
            "iosArm32" to OperationSystem.MACOS,
            "iosArm64" to OperationSystem.MACOS,
            "iosSimulatorArm64" to OperationSystem.MACOS,
            "watchosArm32" to OperationSystem.MACOS,
            "watchosArm64" to OperationSystem.MACOS,
            "watchosX86" to OperationSystem.MACOS,
            "watchosX64" to OperationSystem.MACOS,
            "watchosSimulatorArm64" to OperationSystem.MACOS,
            "tvosArm64" to OperationSystem.MACOS,
            "tvosX64" to OperationSystem.MACOS,
            "tvosSimulatorArm64" to OperationSystem.MACOS,
        )


    /** Nome of the current operating system */
    val osName: String?
        get() = System.getProperty("os.name")

    val os: OperationSystem
        get() {
            if (isWindows) return OperationSystem.WINDOWS
            if (isLinux) return OperationSystem.LINUX
            if (isMacOS) return OperationSystem.MACOS
            return OperationSystem.UNKNOWN
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

    enum class OperationSystem {
        WINDOWS,
        LINUX,
        MACOS,
        UNKNOWN
    }

    fun uploadStagingRepositoryToMavenCentral() {
        val stagingDir = mainProject.layout.buildDirectory.dir("staging-deploy").get().asFile
        val stagingZip = File(mainProject.layout.buildDirectory.get().asFile, "staging-deploy.zip")

        if (!stagingDir.isDirectory || stagingDir.list()?.isNotEmpty() != true)
            throw GradleException("Staging repository not existing or empty: $stagingDir")

        val sonatypeUsername = mainProject.extra["sonatype.username"].toString()
        val sonatypePassword = mainProject.extra["sonatype.password"].toString()

        println("Zipping $stagingDir to $stagingZip")

        zipDir(stagingDir, stagingZip)


        println("Deploying to Maven Central ...")
        val mavenCentral = MavenCentral(
            DummyJReleaserContext,
            "https://central.sonatype.com/api/v1/publisher/",
            Http.Authorization.BASIC,
            sonatypeUsername,
            sonatypePassword,
            10,
            60,
            false,
            10,
            100,
            "i18n4k-"+Date()
        )

        val deploymentId: String
        val timeTaken = measureTimeMillis {
            deploymentId = mavenCentral.upload(stagingZip.toPath())
        }

        println("Deployed to Maven Central in $timeTaken ms; deploymentId = $deploymentId")
    }

    private fun zipDir(dirToZip: File, zipFile: File) {
        if (!dirToZip.isDirectory) {
            throw GradleException("$dirToZip is not a directory")
        }
        val buffer = ByteArray(1024)

        zipFile.outputStream().use { fos ->
            ZipOutputStream(fos).use { zipOut ->
                dirToZip.listFiles()?.forEach { file ->
                    zipFileIntoZip(file, file.name, zipOut, buffer)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun zipFileIntoZip(fileToZip: File, fileName: String, zipOut: ZipOutputStream, buffer: ByteArray) {
        if (fileToZip.isHidden()) {
            return
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(ZipEntry(fileName))
                zipOut.closeEntry()
            } else {
                zipOut.putNextEntry(ZipEntry("$fileName/"))
                zipOut.closeEntry()
            }
            val children: Array<out File>? = fileToZip.listFiles()
            children?.forEach { childFile ->
                zipFileIntoZip(childFile, fileName + "/" + childFile.getName(), zipOut, buffer)
            }
            return
        }
        fileToZip.inputStream().use { fis ->
            val zipEntry = ZipEntry(fileName)
            zipOut.putNextEntry(zipEntry)

            var length: Int
            while ((fis.read(buffer).also { length = it }) >= 0) {
                zipOut.write(buffer, 0, length)
            }
        }
    }

}