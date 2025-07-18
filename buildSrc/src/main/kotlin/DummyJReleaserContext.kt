import org.jreleaser.logging.JReleaserLogger
import org.jreleaser.logging.SimpleJReleaserLoggerAdapter
import org.jreleaser.model.api.JReleaserCommand
import org.jreleaser.model.api.JReleaserContext
import org.jreleaser.model.api.JReleaserModel
import org.jreleaser.model.api.announce.Announce
import org.jreleaser.model.api.assemble.Assemble
import org.jreleaser.model.api.catalog.Catalog
import org.jreleaser.model.api.checksum.Checksum
import org.jreleaser.model.api.common.Matrix
import org.jreleaser.model.api.deploy.Deploy
import org.jreleaser.model.api.distributions.Distribution
import org.jreleaser.model.api.download.Download
import org.jreleaser.model.api.environment.Environment
import org.jreleaser.model.api.extensions.Extension
import org.jreleaser.model.api.files.Files
import org.jreleaser.model.api.hooks.Hooks
import org.jreleaser.model.api.packagers.Packagers
import org.jreleaser.model.api.platform.Platform
import org.jreleaser.model.api.project.Project
import org.jreleaser.model.api.release.Release
import org.jreleaser.model.api.signing.Keyring
import org.jreleaser.model.api.signing.Signing
import org.jreleaser.model.api.signing.SigningException
import org.jreleaser.model.api.upload.Upload
import org.jreleaser.mustache.TemplateContext
import java.nio.file.Path
import java.time.ZonedDateTime
import java.util.Properties

/** Dummy implementation of [JReleaserContext] as JReleaser is used for maven-central upload. */
object DummyJReleaserContext : JReleaserContext {
    private var logger: SimpleJReleaserLoggerAdapter = SimpleJReleaserLoggerAdapter()

    private var model = object : JReleaserModel {

        private var environment = object : Environment {
            private val properties = Properties()

            override fun asMap(full: Boolean): MutableMap<String, Any> {
                return mutableMapOf()
            }

            override fun getVars(): Properties {
                return properties
            }

            override fun getVariables(): String {
                TODO("Not yet implemented")
            }

            override fun getProperties(): MutableMap<String, Any> {
                return mutableMapOf()
            }
        }

        override fun asMap(full: Boolean): MutableMap<String, Any> {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getNow(): ZonedDateTime {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getTimestamp(): String {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getCommit(): JReleaserModel.Commit {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getEnvironment(): Environment {
            return environment
        }

        override fun getHooks(): Hooks {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getPlatform(): Platform {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getProject(): Project {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getRelease(): Release {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getPackagers(): Packagers {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getAnnounce(): Announce {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getAssemble(): Assemble {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getDownload(): Download {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getDeploy(): Deploy {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getUpload(): Upload {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getChecksum(): Checksum {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getSigning(): Signing {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getFiles(): Files {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getCatalog(): Catalog {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getDistributions(): MutableMap<String, out Distribution> {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getExtensions(): MutableMap<String, out Extension> {
            throw UnsupportedOperationException("Not supported yet.")
        }

        override fun getMatrix(): Matrix?{
            throw UnsupportedOperationException("Not supported yet.")
        }
    }


    private fun readResolve(): Any = DummyJReleaserContext

    override fun relativize(path: Path, path1: Path): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun relativizeToBasedir(path: Path): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getLogger(): JReleaserLogger {
        return logger
    }

    override fun getMode(): JReleaserContext.Mode {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getModel(): JReleaserModel {
        return model
    }

    override fun getBasedir(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getOutputDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getChecksumsDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getCatalogsDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getSignaturesDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getPrepareDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getPackageDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getAssembleDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getDownloadDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getArtifactsDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getDeployDirectory(): Path {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun isDryrun(): Boolean {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun isGitRootSearch(): Boolean {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun isStrict(): Boolean {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedAnnouncers(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedAssemblers(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedDistributions(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedPackagers(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedDownloaderTypes(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedDownloaderNames(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedDeployerTypes(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedDeployerNames(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedUploaderTypes(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getIncludedUploaderNames(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedAnnouncers(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedAssemblers(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedDistributions(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedPackagers(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedDownloaderTypes(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedDownloaderNames(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedDeployerTypes(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedDeployerNames(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedUploaderTypes(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getExcludedUploaderNames(): List<String> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getCommand(): JReleaserCommand {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun props(): TemplateContext {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun fullProps(): TemplateContext {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun nag(s: String, s1: String) {
        throw UnsupportedOperationException("Not supported yet.")
    }

    @Throws(SigningException::class)
    override fun createKeyring(): Keyring {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getChangelog(): JReleaserContext.Changelog {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun getAdditionalProperties(): Map<String, Any> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun isYolo(): Boolean {
        throw UnsupportedOperationException("Not supported yet.")
    }
}
