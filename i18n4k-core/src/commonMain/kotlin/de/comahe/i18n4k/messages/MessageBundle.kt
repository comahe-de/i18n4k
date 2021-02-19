@file:Suppress("unused")

package de.comahe.i18n4k.messages

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.providers.MessagesProvider
import de.comahe.i18n4k.messages.providers.MessagesProviderFactory
import de.comahe.i18n4k.strings.AbstractLocalizedString
import de.comahe.i18n4k.strings.LocalizedString
import de.comahe.i18n4k.strings.LocalizedStringFactory1
import de.comahe.i18n4k.strings.LocalizedStringFactory2
import de.comahe.i18n4k.strings.LocalizedStringFactory3
import de.comahe.i18n4k.strings.LocalizedStringFactory4
import de.comahe.i18n4k.strings.LocalizedStringFactory5
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf

/**
 * Base class for bundles of messages.
 *
 * Messages are referenced by indexes. Sub-classes should defined constants with the indexes to
 * access the messages
 *
 * Also manages a map of locales to translations ([MessagesProvider])
 */
@Suppress("MemberVisibilityCanBePrivate", "SameParameterValue")
open class MessageBundle {
    /** Map the locale to the set [MessagesProvider] */
    private val localeToStringsRef: AtomicRef<PersistentMap<Locale, MessagesProvider>> =
        atomic(persistentMapOf())

    /** Add or replaces a translation in this message bundle */
    fun registerTranslation(messagesProvider: MessagesProvider) {
        localeToStringsRef.update { localeToStrings ->
            localeToStrings.put(messagesProvider.locale, messagesProvider)
        }
    }

    /** Add or replaces a translation in this message bundle. Loaded asynchronously by the factory.  */
    fun registerTranslationFactory(messagesProviderFactory: MessagesProviderFactory) {
        messagesProviderFactory.loadMessagesProvider(this::registerTranslation)
    }

    /** Remove all registered translations (see [registerTranslation]) */
    fun unregisterAllTranslations() {
        localeToStringsRef.update { persistentMapOf() }
    }

    /** Locales registered to this bundle
     *
     * See [registerTranslation]
     */
    val locales: Collection<Locale>
        get() = localeToStringsRef.value.keys

    /** Get string with index from the [MessagesProvider] fitting to the locale. Use default locale
     * if no [MessagesProvider] or string at index is `null` */
    protected fun getString0(index: Int, locale: Locale?): String {
        if (index < 0)
            throw IllegalArgumentException("Index must be greater or equal to 0")
        var messages = localeToStringsRef.value[locale ?: i18n4k.locale]
        var string: String? = null
        if (messages !== null && index < messages.size)
            string = messages[index]
        if (i18n4k.treadBlankStringAsNull && string?.isBlank() == true)
            string = null
        if (string === null)
            messages = localeToStringsRef.value[i18n4k.defaultLocale]
        if (messages !== null && index < messages.size)
            string = messages[index]
        if (i18n4k.treadBlankStringAsNull && string?.isBlank() == true)
            string = null
        if (string === null)
            return "?$index?"
        return string
    }

    // Maybe add support for ICU message format?
    // https://unicode-org.github.io/icu-docs/apidoc/released/icu4j/com/ibm/icu/text/MessageFormat.html
    // https://docs.transifex.com/formats/java-properties#plural-support
    // http://www.unicode.org/cldr/charts/27/supplemental/language_plural_rules.html

    /**
     * Similar to [getString0] but parameters are evaluated via the set [MessageFormatter] in [i18n4k] ([de.comahe.i18n4k.config.I18n4kConfig.messageFormatter])
     */
    protected fun getStringN(index: Int, parameters: List<Any>, locale: Locale?) =
        i18n4k.messageFormatter.format(
            getString0(index, locale),
            parameters,
            locale ?: i18n4k.locale
        )


    /** Create a [LocalizedString] for the given index.
     *
     * The result should be stored in an constant to be used several times
     */
    protected fun getLocalizedString0(index: Int) =
        LocalizedString0(this, index)

    /** Create a [LocalizedString] with 1 parameter. */
    protected fun getLocalizedString1(index: Int, p0: Any) =
        LocalizedStringN(this, index, listOf(p0))

    /** See [getLocalizedString1], but with 2 parameters. */
    protected fun getLocalizedString2(index: Int, p0: Any, p1: Any) =
        LocalizedStringN(this, index, listOf(p0, p1))

    /** See [getLocalizedString1], but with 3 parameters. */
    protected fun getLocalizedString3(index: Int, p0: Any, p1: Any, p2: Any) =
        LocalizedStringN(this, index, listOf(p0, p1, p2))

    /** See [getLocalizedString1], but with 4 parameters. */
    protected fun getLocalizedString4(index: Int, p0: Any, p1: Any, p2: Any, p3: Any) =
        LocalizedStringN(this, index, listOf(p0, p1, p2, p3))

    /** See [getLocalizedString1], but with 5 parameters. */
    protected fun getLocalizedString5(index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any) =
        LocalizedStringN(this, index, listOf(p0, p1, p2, p3, p4))

    /** Create a factory for [LocalizedString] for the given index.
     *
     * The [LocalizedString] must have 1 parameter
     *
     * The result should be stored in an constant to be used several times */
    protected fun getLocalizedStringFactory1(index: Int): LocalizedStringFactory1 =
        LocalizedStringFactory1Bundled(this, index)

    /** See [getLocalizedStringFactory1], but with 2 parameters. */
    protected fun getLocalizedStringFactory2(index: Int): LocalizedStringFactory2 =
        LocalizedStringFactory2Bundled(this, index)

    /** See [getLocalizedStringFactory1], but with 3 parameters. */
    protected fun getLocalizedStringFactory3(index: Int): LocalizedStringFactory3 =
        LocalizedStringFactory3Bundled(this, index)

    /** See [getLocalizedStringFactory1], but with 4 parameters. */
    protected fun getLocalizedStringFactory4(index: Int): LocalizedStringFactory4 =
        LocalizedStringFactory4Bundled(this, index)

    /** See [getLocalizedStringFactory1], but with 5 parameters. */
    protected fun getLocalizedStringFactory5(index: Int): LocalizedStringFactory5 =
        LocalizedStringFactory5Bundled(this, index)


    ////////////////////////////////////////////////////////////
    // LocalizedString
    ////////////////////////////////////////////////////////////

    /** [LocalizedString] for a given index in a message bundle with 0 parameters */
    class LocalizedString0(
        private val messages: MessageBundle,
        private val key: Int
    ) : AbstractLocalizedString() {

        override fun toString() =
            toString(null)

        override fun toString(locale: Locale?) =
            messages.getString0(key, locale)


        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is LocalizedString0) return false

            if (messages != other.messages) return false
            if (key != other.key) return false

            return true
        }

        override fun hashCode(): Int {
            var result = messages.hashCode()
            result = 31 * result + key
            return result
        }
    }


    /** [LocalizedString] for a given index in a message bundle with N parameters */
    class LocalizedStringN(
        private val messages: MessageBundle,
        private val key: Int,
        private val parameters: List<Any>
    ) : AbstractLocalizedString() {

        override fun toString() =
            toString(null)

        override fun toString(locale: Locale?) =
            messages.getStringN(key, parameters, locale)


        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is LocalizedStringN) return false

            if (messages != other.messages) return false
            if (key != other.key) return false
            if (parameters != other.parameters) return false

            return true
        }

        override fun hashCode(): Int {
            var result = messages.hashCode()
            result = 31 * result + key
            result = 31 * result + parameters.hashCode()
            return result
        }
    }


    ////////////////////////////////////////////////////////////
    // LocalizedString-Factories
    ////////////////////////////////////////////////////////////

    private data class LocalizedStringFactory1Bundled(
        private val messages: MessageBundle,
        private val index: Int
    ) : LocalizedStringFactory1 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messages.getString0(index, locale)

        override fun createString(p0: Any, locale: Locale?) =
            messages.getStringN(index, listOf(p0), locale)

        override fun createLocalizedString(p0: Any) =
            messages.getLocalizedString1(index, p0)
    }

    data class LocalizedStringFactory2Bundled(
        private val messages: MessageBundle,
        private val index: Int
    ) : LocalizedStringFactory2 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messages.getString0(index, locale)

        override fun createString(p0: Any, p1: Any, locale: Locale?) =
            messages.getStringN(index, listOf(p0, p1), locale)

        override fun createLocalizedString(p0: Any, p1: Any) =
            messages.getLocalizedString2(index, p0, p1)
    }

    data class LocalizedStringFactory3Bundled(
        private val messages: MessageBundle,
        private val index: Int
    ) : LocalizedStringFactory3 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messages.getString0(index, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, locale: Locale?) =
            messages.getStringN(index, listOf(p0, p1, p2), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any) =
            messages.getLocalizedString3(index, p0, p1, p2)

    }

    data class LocalizedStringFactory4Bundled(
        private val messages: MessageBundle,
        private val index: Int
    ) : LocalizedStringFactory4 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messages.getString0(index, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, locale: Locale?) =
            messages.getStringN(index, listOf(p0, p1, p2, p3), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any) =
            messages.getLocalizedString4(index, p0, p1, p2, p3)

    }

    data class LocalizedStringFactory5Bundled(
        private val messages: MessageBundle,
        private val index: Int
    ) : LocalizedStringFactory5 {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messages.getString0(index, locale)

        override fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, locale: Locale?) =
            messages.getStringN(index, listOf(p0, p1, p2, p3, p4), locale)

        override fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any) =
            messages.getLocalizedString5(index, p0, p1, p2, p3, p4)

    }
}