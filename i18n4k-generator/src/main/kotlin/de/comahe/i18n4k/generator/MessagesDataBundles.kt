@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package de.comahe.i18n4k.generator

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties
import java.util.regex.Matcher
import java.util.regex.Pattern

/** Collection of multiple messages bundles */
class MessagesDataBundles(
    private val messageFormatter: MessageFormatter
) {
    val bundles: MutableMap<BundleName, MessagesDataBundle> = mutableMapOf()

    /**
     * Searches recursively for supported language files and adds the to the bundles.
     *
     * @param inputDirectory
     *      the directory to search for
     * @param packageName
     *      Package name where the generated classes will be stored.
     *      If null (default) the path relative to the [inputDirectory]  will be used.
     */
    fun findLanguageBundles(
        inputDirectory: File,
        packageName: String?
    ) {
        findLanguageBundlesRecursive(inputDirectory, "", packageName)
    }

    /**
     * Searches recursively for "i18n4k" config files
     * @param dir
     *      the directory to search for
     * @param currentPackageName
     *      name of the package relative to the start directory.
     * @param fixedPackageName
     *      If not null, this package name will be used instead of the [currentPackageName]
     */
    private fun findLanguageBundlesRecursive(
        dir: File,
        currentPackageName: String,
        fixedPackageName: String?
    ) {
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                findLanguageBundlesRecursive(
                    file,
                    currentPackageName + (if (currentPackageName.isEmpty()) "" else ".") + file.name,
                    fixedPackageName
                )
            } else {
                addLanguageFile(fixedPackageName ?: currentPackageName, file)
            }
        }
    }

    /**
     * Tries to add the given file to the message bundles.
     *
     * Several formats of language file may be supported and used according to the file name.
     *
     * @return
     *      if the file was added (supported format); false if the format of the file was not supported
     * */
    fun addLanguageFile(packageName: String, file: File): Boolean {
        val m: Matcher = BUNDLE_FILE_PATTERN.matcher(file.name)
        if (m.find()) {
            // its a properties file
            addToBundles(
                BundleName(
                    packageName = packageName,
                    name = m.group(1) ?: return false
                ),
                MessagesData(
                    locale = Locale(
                        m.group(2) ?: return false,
                        m.group(3) ?: "",
                        m.group(4) ?: ""),
                    messages = loadProperties(file)
                )
            )
            return true
        }
        return false // file not supported
    }

    /**
     *  adds the message bundle
     */
    private fun addToBundles(bundleName: BundleName, messagesData: MessagesData) {
        val bundle: MessagesDataBundle =
            bundles.computeIfAbsent(bundleName) { n -> MessagesDataBundle(n, messageFormatter) }
        bundle.addMessagesData(messagesData)
    }

    /** loads a properties file and return the key-value map */
    private fun loadProperties(file: File): Map<String, String> {
        FileInputStream(file).use { inputStream ->
            val p = Properties()
            p.load(InputStreamReader(inputStream, StandardCharsets.UTF_8))
            val r = HashMap<String, String>(p.size)
            p.forEach {
                r[it.key.toString()] = it.value.toString()
            }
            return r
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        bundles.values.forEach { data ->
            sb.append(data).append("\n")
        }
        return sb.toString()
    }


    /**
     * Contains several patterns for language file names. Group 1 must be the name. Group 2 the language ID
     */
    companion object {

        /** Patern to identify "*.properties"-files */
        val BUNDLE_FILE_PATTERN = Pattern
            .compile("^([^_]+)_([a-z]{2})(?:_([A-Z]{2}))?(?:_([a-zA-Z]+))?.properties\$")!!
    }
}