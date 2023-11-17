@file:Suppress("unused")

package de.comahe.i18n4k.messages

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.lessSpecificLocale
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.providers.MessagesProvider
import de.comahe.i18n4k.messages.providers.MessagesProviderFactory
import de.comahe.i18n4k.strings.AbstractLocalizedString
import de.comahe.i18n4k.strings.LocalizedString
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap

/**
 * Base class for bundles of messages.
 *
 * Messages are referenced by indexes. Sub-classes should define constants
 * with the indexes to access the messages
 *
 * Also manages a map of locales to translations ([MessagesProvider])
 */
@Suppress("MemberVisibilityCanBePrivate", "SameParameterValue")
open class MessageBundle(
    /** Name of the bundle. Empty string for none. */
    val messageBundleName: String = "",
    /** Package of the bundle. Empty string for none. */
    val messageBundlePackage: String = "",
) {
    /** Map the locale to the set [MessagesProvider] */
    private val localeToStringsRef: AtomicRef<PersistentMap<Locale, MessagesProvider>> =
        atomic(persistentMapOf())

    /** Names of the keys mapped to the corresponding objects. */
    private val keyObjectsByName: AtomicRef<PersistentMap<String, MessageBundleEntry>> =
        atomic(persistentMapOf())

    private val keyObjectsByIndex: AtomicRef<PersistentList<MessageBundleEntry?>> = atomic(
        persistentListOf()
    )

    /** Add or replaces a translation in this message bundle */
    fun registerTranslation(messagesProvider: MessagesProvider) {
        localeToStringsRef.update { localeToStrings ->
            localeToStrings.put(messagesProvider.locale, messagesProvider)
        }
    }

    /** Register the keyObjects for this message bungle. */
    protected fun registerMessageBundleEntries(vararg entries: MessageBundleEntry) {
        if (entries.isEmpty())
            return
        val map = mutableMapOf<String, MessageBundleEntry>()
        val array =
            Array<MessageBundleEntry?>(
                size = entries.reduce { e1, e2 -> if (e1.messageIndex > e2.messageIndex) e1 else e2 }.messageIndex + 1,
                init = { _ -> null }
            )
        for (entry in entries) {
            require(entry.messageBundle === this) { "The entries must a created with this bundle!" }
            require(!map.containsKey(entry.messageKey)) { "The messageKey ${entry.messageKey} is duplicate!" }
            require(array[entry.messageIndex] == null) { "The messageIndex ${entry.messageIndex} is duplicate!" }
            map[entry.messageKey] = entry
            array[entry.messageIndex] = entry
        }

        keyObjectsByName.update { map.toPersistentMap() }
        keyObjectsByIndex.update { array.toList().toPersistentList() }
    }

    /**
     * Add or replaces a translation in this message bundle. Loaded
     * asynchronously by the factory.
     */
    fun registerTranslationFactory(messagesProviderFactory: MessagesProviderFactory) {
        messagesProviderFactory.loadMessagesProvider(this::registerTranslation)
    }

    /** Remove all registered translations (see [registerTranslation]) */
    fun unregisterAllTranslations() {
        localeToStringsRef.update { persistentMapOf() }
    }

    /**
     * Locales registered to this bundle
     *
     * See [registerTranslation]
     */
    val locales: Collection<Locale>
        get() = localeToStringsRef.value.keys

    /**
     * Returns the entry registered for the given key.
     *
     * See [MessageBundleEntry.messageKey]
     */
    fun getEntryByKey(key: String): MessageBundleEntry? {
        return keyObjectsByName.value[key]
    }

    fun getEntryByIndex(index: Int): MessageBundleEntry? {
        val list = keyObjectsByIndex.value;
        if (index < 0 || index >= list.size)
            return null
        return list[index]
    }

    /**
     * Get string with index from the [MessagesProvider] fitting to the locale.
     * Use default locale if no [MessagesProvider] or string at index is `null`
     */
    protected fun getString0(index: Int, locale: Locale?): String {
        if (index < 0)
            throw IllegalArgumentException("Index must be greater or equal to 0")
        val localeToUse = locale ?: i18n4k.locale
        val messages = localeToStringsRef.value[localeToUse]
        var string: String? = null
        if (messages !== null && index < messages.size)
            string = messages[index]
        if (i18n4k.treadBlankStringAsNull && string?.isBlank() == true)
            string = null
        // try less specific locale
        if (string === null) {
            val lessSpecificLocale = localeToUse.lessSpecificLocale
            if (lessSpecificLocale != null)
                string = getString0(index, lessSpecificLocale)
        }
        // try default locale
        if (string === null && locale != i18n4k.defaultLocale)
            string = getString0(index, i18n4k.defaultLocale)

        if (string === null)
            return "?${getEntryByIndex(index)?.messageKey ?: index}?"
        return string
    }

    // Maybe add support for ICU message format?
    // https://unicode-org.github.io/icu-docs/apidoc/released/icu4j/com/ibm/icu/text/MessageFormat.html
    // https://docs.transifex.com/formats/java-properties#plural-support
    // http://www.unicode.org/cldr/charts/27/supplemental/language_plural_rules.html

    /**
     * Similar to [getString0] but parameters are evaluated
     * via the set [MessageFormatter] in [i18n4k]
     * ([de.comahe.i18n4k.config.I18n4kConfig.messageFormatter])
     */
    protected fun getStringN(index: Int, parameters: List<Any>, locale: Locale?) =
        i18n4k.messageFormatter.format(
            getString0(index, locale),
            parameters,
            locale ?: i18n4k.locale
        )


    /**
     * Create a [LocalizedString] for the given index.
     *
     * The result should be stored in a constant to be used several times
     */
    protected fun getLocalizedString0(key: String, index: Int)
        : MessageBundleLocalizedString = LocalizedString0(this, key, index)

    /** Create a [LocalizedString] with 1 parameter. */
    protected fun getLocalizedString1(key: String, index: Int, p0: Any)
        : MessageBundleLocalizedString = LocalizedStringN(this, key, index, listOf(p0))

    /** See [getLocalizedString1], but with 2 parameters. */
    protected fun getLocalizedString2(key: String, index: Int, p0: Any, p1: Any)
        : MessageBundleLocalizedString = LocalizedStringN(this, key, index, listOf(p0, p1))

    /** See [getLocalizedString1], but with 3 parameters. */
    protected fun getLocalizedString3(key: String, index: Int, p0: Any, p1: Any, p2: Any)
        : MessageBundleLocalizedString = LocalizedStringN(this, key, index, listOf(p0, p1, p2))

    /** See [getLocalizedString1], but with 4 parameters. */
    protected fun getLocalizedString4(key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any)
        : MessageBundleLocalizedString = LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3))

    /** See [getLocalizedString1], but with 5 parameters. */
    protected fun getLocalizedString5(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any
    ): MessageBundleLocalizedString = LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4))

    /** See [getLocalizedString1], but with 6 parameters. */
    protected fun getLocalizedString6(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any,
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5))

    /** See [getLocalizedString1], but with 7 parameters. */
    protected fun getLocalizedString7(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any,
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5, p6))

    /** See [getLocalizedString1], but with 8 parameters. */
    protected fun getLocalizedString8(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any,
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5, p6, p7))

    /** See [getLocalizedString1], but with 9 parameters. */
    protected fun getLocalizedString9(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any,
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8))

    /** See [getLocalizedString1], but with 10 parameters. */
    protected fun getLocalizedString10(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any, p9: Any,
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9))


    /**
     * Create a factory for [LocalizedString] for the given index.
     *
     * The [LocalizedString] must have 1 parameter
     *
     * The result should be stored in a constant to be used several times
     */
    protected fun getLocalizedStringFactory1(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory1 =
        LocalizedStringFactory1Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 2 parameters. */
    protected fun getLocalizedStringFactory2(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory2 =
        LocalizedStringFactory2Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 3 parameters. */
    protected fun getLocalizedStringFactory3(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory3 =
        LocalizedStringFactory3Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 4 parameters. */
    protected fun getLocalizedStringFactory4(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory4 =
        LocalizedStringFactory4Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 5 parameters. */
    protected fun getLocalizedStringFactory5(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory5 =
        LocalizedStringFactory5Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 6 parameters. */
    protected fun getLocalizedStringFactory6(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory6 =
        LocalizedStringFactory6Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 7 parameters. */
    protected fun getLocalizedStringFactory7(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory7 =
        LocalizedStringFactory7Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 8 parameters. */
    protected fun getLocalizedStringFactory8(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory8 =
        LocalizedStringFactory8Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 9 parameters. */
    protected fun getLocalizedStringFactory9(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory9 =
        LocalizedStringFactory9Bundled(this, key, index)

    /** See [getLocalizedStringFactory1], but with 10 parameters. */
    protected fun getLocalizedStringFactory10(
        key: String,
        index: Int
    ): MessageBundleLocalizedStringFactory10 =
        LocalizedStringFactory10Bundled(this, key, index)


    ////////////////////////////////////////////////////////////
    // LocalizedString
    ////////////////////////////////////////////////////////////

    /**
     * [LocalizedString] for a given index in a message bundle with 0
     * parameters
     */
    private class LocalizedString0(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : AbstractLocalizedString(), MessageBundleLocalizedString {

        override fun toString() =
            toString(null)

        override fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)


        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is LocalizedString0) return false

            if (messageBundle != other.messageBundle) return false
            if (messageIndex != other.messageIndex) return false

            return true
        }

        override fun hashCode(): Int {
            var result = messageBundle.hashCode()
            result = 31 * result + messageIndex
            return result
        }
    }


    /**
     * [LocalizedString] for a given index in a message bundle with N
     * parameters
     */
    private class LocalizedStringN(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int,
        private val parameters: List<Any>
    ) : AbstractLocalizedString(), MessageBundleLocalizedString {

        override fun toString() =
            toString(null)

        override fun toString(locale: Locale?) =
            messageBundle.getStringN(messageIndex, parameters, locale)


        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is LocalizedStringN) return false

            if (messageBundle != other.messageBundle) return false
            if (messageIndex != other.messageIndex) return false
            if (parameters != other.parameters) return false

            return true
        }

        override fun hashCode(): Int {
            var result = messageBundle.hashCode()
            result = 31 * result + messageIndex
            result = 31 * result + parameters.hashCode()
            return result
        }
    }


    ////////////////////////////////////////////////////////////
    // LocalizedString-Factories
    ////////////////////////////////////////////////////////////

    private data class LocalizedStringFactory1Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory1 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0), locale)

        override fun createLocalizedString(p0: Any) =
            messageBundle.getLocalizedString1(messageKey, messageIndex, p0)
    }

    private data class LocalizedStringFactory2Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory2 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1), locale)

        override fun createLocalizedString(p0: Any, p1: Any) =
            messageBundle.getLocalizedString2(messageKey, messageIndex, p0, p1)
    }

    private data class LocalizedStringFactory3Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory3 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1, p2), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any) =
            messageBundle.getLocalizedString3(messageKey, messageIndex, p0, p1, p2)

    }

    private data class LocalizedStringFactory4Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory4 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1, p2, p3), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any) =
            messageBundle.getLocalizedString4(messageKey, messageIndex, p0, p1, p2, p3)

    }

    private data class LocalizedStringFactory5Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory5 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1, p2, p3, p4), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any) =
            messageBundle.getLocalizedString5(messageKey, messageIndex, p0, p1, p2, p3, p4)

    }

    private data class LocalizedStringFactory6Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory6 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1, p2, p3, p4, p5), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any) =
            messageBundle.getLocalizedString6(messageKey, messageIndex, p0, p1, p2, p3, p4, p5)
    }

    private data class LocalizedStringFactory7Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory7 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1, p2, p3, p4, p5, p6), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any) =
            messageBundle.getLocalizedString7(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, p6)
    }

    private data class LocalizedStringFactory8Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory8 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1, p2, p3, p4, p5, p6, p7), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any) =
            messageBundle.getLocalizedString8(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, p6, p7)
    }

    private data class LocalizedStringFactory9Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory9 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any) =
            messageBundle.getLocalizedString9(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, p6, p7, p8)
    }

    private data class LocalizedStringFactory10Bundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : MessageBundleLocalizedStringFactory10 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any, p9: Any, locale: Locale?) =
            messageBundle.getStringN(messageIndex, listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any, p9: Any) =
            messageBundle.getLocalizedString10(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
    }
}