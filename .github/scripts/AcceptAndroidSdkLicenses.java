import com.android.repository.api.License;
import com.android.repository.api.LocalPackage;
import com.android.repository.api.NullProgressIndicator;
import com.android.sdklib.repository.AndroidSdkHandler;
import org.jetbrains.amper.android.sdk.provisioning.AndroidSdkDetector;

import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Accepts the licenses of the Android SDK packages that are already installed in the SDK.
 * <p>
 * Some packages of the Windows runner image are only understood by sdklib's <em>legacy</em>
 * repository loader: the license id is derived from the package's {@code source.properties} and
 * the license text is not available in {@code package.xml}. Recomputing the hash that sdklib
 * expects from those files is fragile, so this tool asks the very same sdklib (the one bundled
 * with the Kotlin Toolchain) for {@link License#getLicenseHash()} and stores it in
 * {@code <sdk>/licenses/<id>}.
 * <p>
 * Run it with the Kotlin Toolchain's {@code lib} directory on the class path. It is a no-op when
 * every installed license is already accepted.
 */
public final class AcceptAndroidSdkLicenses {
    /** Package kinds that are needed by the Android builds of this repository. */
    private static final String[] PACKAGE_KINDS = {"platforms", "build-tools"};

    public static void main(String[] args) throws Exception {
        Path sdkRoot = AndroidSdkDetector.INSTANCE.detectSdkPath();
        System.out.println("Android SDK root: " + sdkRoot);
        if (sdkRoot == null || !Files.isDirectory(sdkRoot)) {
            System.out.println("WARNING: Android SDK root does not exist, skipping license acceptance");
            return;
        }

        Path licenses = sdkRoot.resolve("licenses");
        Files.createDirectories(licenses);

        AndroidSdkHandler handler = new AndroidSdkHandler(
                sdkRoot, Paths.get(System.getProperty("user.home"), ".android"));
        NullProgressIndicator progress = NullProgressIndicator.INSTANCE;

        int accepted = 0;
        int alreadyAccepted = 0;
        List<String> failures = new ArrayList<>();

        for (String kind : PACKAGE_KINDS) {
            for (String name : listDirectories(sdkRoot.resolve(kind))) {
                String packagePath = kind + ";" + name;
                LocalPackage pkg;
                try {
                    pkg = handler.getLocalPackage(packagePath, progress);
                } catch (Throwable t) {
                    System.out.println("SKIP  " + packagePath + ": " + t);
                    continue;
                }
                if (pkg == null) {
                    System.out.println("SKIP  " + packagePath + ": not a package understood by sdklib");
                    continue;
                }
                License license = pkg.getLicense();
                if (license == null) {
                    continue;
                }
                String id = license.getId();
                if (id == null || id.isEmpty()) {
                    System.out.println("SKIP  " + packagePath + ": license has no id");
                    continue;
                }
                try {
                    if (license.checkAccepted(sdkRoot)) {
                        alreadyAccepted++;
                        System.out.println("OK    " + packagePath + " (" + id + ")");
                        continue;
                    }
                    String hash = license.getLicenseHash();
                    Path file = licenses.resolve(id);
                    writeHash(file, hash);
                    if (license.checkAccepted(sdkRoot)) {
                        accepted++;
                        System.out.println("ACCEPT " + packagePath + " (" + id + "): " + hash);
                    } else {
                        failures.add(packagePath + " (" + id + ")");
                        System.out.println("FAIL  " + packagePath + " (" + id + "): wrote " + hash
                                + " to " + file + ", but sdklib still rejects the license");
                    }
                } catch (Throwable t) {
                    failures.add(packagePath + " (" + id + ")");
                    System.out.println("ERROR " + packagePath + " (" + id + "): " + t);
                }
            }
        }

        System.out.println("Android SDK licenses: " + alreadyAccepted + " already accepted, "
                + accepted + " newly accepted, " + failures.size() + " unresolved");
        if (!failures.isEmpty()) {
            System.out.println("WARNING: unresolved licenses: " + String.join(", ", failures));
        }
    }

    private static List<String> listDirectories(Path root) throws Exception {
        if (!Files.isDirectory(root)) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    result.add(path.getFileName().toString());
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    /** Appends {@code hash} to {@code file} unless it is already present. */
    private static void writeHash(Path file, String hash) throws Exception {
        Set<String> lines = new LinkedHashSet<>();
        if (Files.isRegularFile(file)) {
            lines.addAll(Files.readAllLines(file, StandardCharsets.UTF_8));
        }
        lines.add(hash);
        Files.write(file, lines, StandardCharsets.UTF_8);
    }
}
