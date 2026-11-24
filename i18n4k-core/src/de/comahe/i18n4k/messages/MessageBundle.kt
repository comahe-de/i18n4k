@file:Suppress("unused")

package de.comahe.i18n4k.messages

import de.comahe.i18n4k.*
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.formatter.MessageParameters
import de.comahe.i18n4k.messages.formatter.MessageParametersEmpty
import de.comahe.i18n4k.messages.formatter.MessageParametersList
import de.comahe.i18n4k.messages.providers.MessagesProvider
import de.comahe.i18n4k.messages.providers.MessagesProviderFactory
import de.comahe.i18n4k.strings.AbstractLocalizedString
import de.comahe.i18n4k.strings.LocalizedString
import de.comahe.i18n4k.strings.LocalizedStringFactoryX
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlin.jvm.JvmOverloads

/**
 * Base class for bundles of messages.
 *
 * Messages are referenced by indexes. Sub-classes should define constants with the indexes to
 * access the messages
 *
 * Also manages a map of locales to translations ([MessagesProvider])
 */
@Suppress("MemberVisibilityCanBePrivate", "SameParameterValue")
open class MessageBundle @JvmOverloads constructor(
    /** Name of the bundle. Empty string for none. */
    val messageBundleName: String = "",
    /** Package of the bundle. Empty string for none. */
    val messageBundlePackage: String = "",
) {
    /** Map the locale to the set [MessagesProvider] */
    private val localeToStringsRef: AtomicRef<PersistentMap<Locale, MessagesProvider>> =
        atomic(persistentMapOf())

    /** Handles the locales with the "attr"-extension */
    private val attribToLocaleToStringsRef: AtomicRef<PersistentMap<CharSequence, PersistentMap<Locale, MessagesProvider>>> =
        atomic(persistentMapOf())

    /** Names of the keys mapped to the corresponding objects. */
    private val keyObjectsByName: AtomicRef<PersistentMap<String, MessageBundleEntry>> =
        atomic(persistentMapOf())

    private val keyObjectsByIndex: AtomicRef<PersistentList<MessageBundleEntry?>> = atomic(
        persistentListOf()
    )

    /** Add or replaces a translation in this message bundle */
    fun registerTranslation(messagesProvider: MessagesProvider) {
        val extension = messagesProvider.locale.getExtension(EXTENSION_KEY_PRIVATE)
        if (extension?.startsWith(EXTENSION_VALUE_ATTRIB_PREFIX) == true) {
            val attrib = extension.substring(EXTENSION_VALUE_ATTRIB_PREFIX.length)
            // store as attribute provider
            attribToLocaleToStringsRef.update { attribToLocaleToStrings ->
                val localeToStrings =
                    (attribToLocaleToStrings[attrib] ?: persistentMapOf()).put(
                        messagesProvider.locale.removeExtensions(),
                        messagesProvider
                    )
                attribToLocaleToStrings.put(attrib, localeToStrings)
            }

        } else {
            localeToStringsRef.update { localeToStrings ->
                localeToStrings.put(messagesProvider.locale.removeExtensions(), messagesProvider)
            }
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
     * Add or replaces a translation in this message bundle. Loaded asynchronously by the factory.
     */
    fun registerTranslationFactory(messagesProviderFactory: MessagesProviderFactory) {
        messagesProviderFactory.loadMessagesProvider(this::registerTranslation)
    }

    /** Remove all registered translations (see [registerTranslation]) */
    fun unregisterAllTranslations() {
        localeToStringsRef.update { persistentMapOf() }
        attribToLocaleToStringsRef.update { persistentMapOf() }
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
        val list = keyObjectsByIndex.value
        if (index < 0 || index >= list.size)
            return null
        return list[index]
    }

    /**
     * Get string with index from the [MessagesProvider] fitting to the locale. Use default locale
     * if no [MessagesProvider] or string at index is `null`
     */
    protected fun getString0(index: Int, locale: Locale?): String {
        return getMapString0(localeToStringsRef.value, index, locale)
            ?: "?${getEntryByIndex(index)?.messageKey ?: index}?"
    }

    protected fun getAttribString0(attrib: CharSequence, index: Int, locale: Locale?): String? {
        val map = attribToLocaleToStringsRef.value[attrib] ?: return null
        return getMapString0(map, index, locale)
    }

    protected fun getMapString0(
        map: PersistentMap<Locale, MessagesProvider>,
        index: Int,
        locale: Locale?,
    ): String? {
        if (index < 0)
            throw IllegalArgumentException("Index must be greater or equal to 0")
        return applyLocales(locale) { localeToUse ->
            val messages = map[localeToUse]
            var string: String? = null
            if (messages !== null && index < messages.size)
                string = messages[index]
            if (i18n4k.treadBlankStringAsNull && string?.isBlank() == true)
                string = null
            return@applyLocales string
        }
    }

    /**
     * Similar to [getString0] but for strings with not parameters the escape
     * sequences are evaluated via the set [MessageFormatter] in [i18n4k]
     * ([de.comahe.i18n4k.config.I18n4kConfig.messageFormatter])
     */
    protected fun getStringS(index: Int, locale: Locale?) =
        i18n4k.messageFormatter.format(
            getString0(index, locale),
            MessageParametersEmpty,
            locale ?: i18n4k.locale
        )

    /**
     * Similar to [getMapString0] but parameters are evaluated via the set [MessageFormatter] in
     * [i18n4k] ([de.comahe.i18n4k.config.I18n4kConfig.messageFormatter])
     */
    protected fun getStringN(index: Int, parameters: MessageParameters, locale: Locale?) =
        i18n4k.messageFormatter.format(
            getString0(index, locale),
            parameters,
            locale ?: i18n4k.locale
        )

    // @formatter:off

    /**
     * Create a [LocalizedString] for the given index.
     *
     * The result should be stored in a constant to be used several times
     */
    protected fun getLocalizedString0(key: String, index: Int): MessageBundleLocalizedString =
        LocalizedString0(this, key, index)

    /** Create a [LocalizedString] with N parameter. */
    protected fun getLocalizedStringN(key: String, index: Int, parameters: MessageParameters) : MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, parameters)

    /** Create a [LocalizedString] with 1 parameter. */
    protected fun getLocalizedString1(key: String, index: Int, p0: Any,
        nameMapper: NameToIndexMapper) : MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0), nameMapper)

    /** See [getLocalizedString1], but with 2 parameters. */
    protected fun getLocalizedString2(key: String, index: Int, p0: Any, p1: Any,
        nameMapper: NameToIndexMapper) : MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1), nameMapper)

    /** See [getLocalizedString1], but with 3 parameters. */
    protected fun getLocalizedString3(key: String, index: Int, p0: Any, p1: Any, p2: Any,
        nameMapper: NameToIndexMapper) : MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2), nameMapper)

    /** See [getLocalizedString1], but with 4 parameters. */
    protected fun getLocalizedString4(key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any,
        nameMapper: NameToIndexMapper) : MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3), nameMapper)

    /** See [getLocalizedString1], but with 5 parameters. */
    protected fun getLocalizedString5(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,
        nameMapper: NameToIndexMapper): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4), nameMapper)

    /** See [getLocalizedString1], but with 6 parameters. */
    protected fun getLocalizedString6(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any,
        nameMapper: NameToIndexMapper
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5), nameMapper)

    /** See [getLocalizedString1], but with 7 parameters. */
    protected fun getLocalizedString7(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any,
        nameMapper: NameToIndexMapper
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5, p6), nameMapper)

    /** See [getLocalizedString1], but with 8 parameters. */
    protected fun getLocalizedString8(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any,
        nameMapper: NameToIndexMapper
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5, p6, p7), nameMapper)

    /** See [getLocalizedString1], but with 9 parameters. */
    protected fun getLocalizedString9(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any,
        nameMapper: NameToIndexMapper
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8), nameMapper)

    /** See [getLocalizedString1], but with 10 parameters. */
    protected fun getLocalizedString10(
        key: String, index: Int, p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any, p9: Any,
        nameMapper: NameToIndexMapper
    ): MessageBundleLocalizedString =
        LocalizedStringN(this, key, index, listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9), nameMapper)

    // @formatter:on

    protected fun getLocalizedStringFactoryN(
        key: String,
        index: Int,
    ): MessageBundleLocalizedStringFactoryN =
        LocalizedStringFactoryNBundled(this, key, index)

    /**
     * Create a factory for [LocalizedString] for the given index.
     *
     * The [LocalizedString] must have 1 parameter
     *
     * The result should be stored in a constant to be used several times
     */
    protected fun getLocalizedStringFactory1(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory1 =
        LocalizedStringFactory1Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any> getLocalizedStringFactory1Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory1Typed<T0> =
        LocalizedStringFactory1TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory1(
        key: String,
        index: Int,
        name0: CharSequence,
    ): MessageBundleLocalizedStringFactory1 =
        getLocalizedStringFactory1(key, index, NameToIndexMapperList(name0))


    protected fun <T0 : Any> getLocalizedStringFactory1Typed(
        key: String,
        index: Int,
        name0: CharSequence,
    ): MessageBundleLocalizedStringFactory1Typed<T0> =
        getLocalizedStringFactory1Typed(key, index, NameToIndexMapperList(name0))

    /** See [getLocalizedStringFactory1], but with 2 parameters. */
    protected fun getLocalizedStringFactory2(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory2 =
        LocalizedStringFactory2Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any> getLocalizedStringFactory2Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory2Typed<T0, T1> =
        LocalizedStringFactory2TypedBundled(this, key, index, nameMapper)


    protected fun getLocalizedStringFactory2(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
    ): MessageBundleLocalizedStringFactory2 =
        getLocalizedStringFactory2(key, index, NameToIndexMapperList(name0, name1))


    protected fun <T0 : Any, T1 : Any> getLocalizedStringFactory2Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
    ): MessageBundleLocalizedStringFactory2Typed<T0, T1> =
        getLocalizedStringFactory2Typed(key, index, NameToIndexMapperList(name0, name1))

    /** See [getLocalizedStringFactory1], but with 3 parameters. */
    protected fun getLocalizedStringFactory3(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory3 =
        LocalizedStringFactory3Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any, T2 : Any> getLocalizedStringFactory3Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory3Typed<T0, T1, T2> =
        LocalizedStringFactory3TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory3(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
    ): MessageBundleLocalizedStringFactory3 =
        getLocalizedStringFactory3(key, index, NameToIndexMapperList(name0, name1, name2))

    protected fun <T0 : Any, T1 : Any, T2 : Any> getLocalizedStringFactory3Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
    ): MessageBundleLocalizedStringFactory3Typed<T0, T1, T2> =
        getLocalizedStringFactory3Typed(key, index, NameToIndexMapperList(name0, name1, name2))

    /** See [getLocalizedStringFactory1], but with 4 parameters. */
    protected fun getLocalizedStringFactory4(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory4 =
        LocalizedStringFactory4Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any> getLocalizedStringFactory4Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory4Typed<T0, T1, T2, T3> =
        LocalizedStringFactory4TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory4(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
    ): MessageBundleLocalizedStringFactory4 =
        getLocalizedStringFactory4(key, index, NameToIndexMapperList(name0, name1, name2, name3))

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any> getLocalizedStringFactory4Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
    ): MessageBundleLocalizedStringFactory4Typed<T0, T1, T2, T3> =
        getLocalizedStringFactory4Typed(key, index, NameToIndexMapperList(name0, name1, name2, name3))

    /** See [getLocalizedStringFactory1], but with 5 parameters. */
    protected fun getLocalizedStringFactory5(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory5 =
        LocalizedStringFactory5Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any> getLocalizedStringFactory5Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory5Typed<T0, T1, T2, T3, T4> =
        LocalizedStringFactory5TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory5(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
    ): MessageBundleLocalizedStringFactory5 =
        getLocalizedStringFactory5(key, index, NameToIndexMapperList(name0, name1, name2, name3, name4))

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any> getLocalizedStringFactory5Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
    ): MessageBundleLocalizedStringFactory5Typed<T0, T1, T2, T3, T4> =
        getLocalizedStringFactory5Typed(key, index, NameToIndexMapperList(name0, name1, name2, name3, name4))

    /** See [getLocalizedStringFactory1], but with 6 parameters. */
    protected fun getLocalizedStringFactory6(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory6 =
        LocalizedStringFactory6Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any> getLocalizedStringFactory6Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory6Typed<T0, T1, T2, T3, T4, T5> =
        LocalizedStringFactory6TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory6(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
    ): MessageBundleLocalizedStringFactory6 =
        getLocalizedStringFactory6(key, index, NameToIndexMapperList(name0, name1, name2, name3, name4, name5))

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any> getLocalizedStringFactory6Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
    ): MessageBundleLocalizedStringFactory6Typed<T0, T1, T2, T3, T4, T5> =
        getLocalizedStringFactory6Typed(key, index, NameToIndexMapperList(name0, name1, name2, name3, name4, name5))

    /** See [getLocalizedStringFactory1], but with 7 parameters. */
    protected fun getLocalizedStringFactory7(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory7 =
        LocalizedStringFactory7Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any> getLocalizedStringFactory7Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory7Typed<T0, T1, T2, T3, T4, T5, T6> =
        LocalizedStringFactory7TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory7(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
        name6: CharSequence,
    ): MessageBundleLocalizedStringFactory7 =
        getLocalizedStringFactory7(
            key, index, NameToIndexMapperList(
                name0, name1, name2, name3, name4, name5, name6
            )
        )
    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any> getLocalizedStringFactory7Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
        name6: CharSequence,
    ): MessageBundleLocalizedStringFactory7Typed<T0, T1, T2, T3, T4, T5, T6> =
        getLocalizedStringFactory7Typed(
            key, index, NameToIndexMapperList(
                name0, name1, name2, name3, name4, name5, name6
            )
        )

    /** See [getLocalizedStringFactory1], but with 8 parameters. */
    protected fun getLocalizedStringFactory8(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory8 =
        LocalizedStringFactory8Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any> getLocalizedStringFactory8Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory8Typed<T0, T1, T2, T3, T4, T5, T6, T7> =
        LocalizedStringFactory8TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory8(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
        name6: CharSequence,
        name7: CharSequence,
    ): MessageBundleLocalizedStringFactory8 =
        getLocalizedStringFactory8(
            key, index, NameToIndexMapperList(
                name0, name1, name2, name3, name4, name5, name6, name7
            )
        )

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any> getLocalizedStringFactory8Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
        name6: CharSequence,
        name7: CharSequence,
    ): MessageBundleLocalizedStringFactory8Typed<T0, T1, T2, T3, T4, T5, T6, T7> =
        getLocalizedStringFactory8Typed(
            key, index, NameToIndexMapperList(
                name0, name1, name2, name3, name4, name5, name6, name7
            )
        )

    /** See [getLocalizedStringFactory1], but with 9 parameters. */
    protected fun getLocalizedStringFactory9(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory9 =
        LocalizedStringFactory9Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any> getLocalizedStringFactory9Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory9Typed<T0, T1, T2, T3, T4, T5, T6, T7, T8> =
        LocalizedStringFactory9TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory9(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
        name6: CharSequence,
        name7: CharSequence,
        name8: CharSequence,
    ): MessageBundleLocalizedStringFactory9 =
        getLocalizedStringFactory9(
            key, index, NameToIndexMapperList(
                name0, name1, name2, name3, name4, name5, name6, name7, name8
            )
        )

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any> getLocalizedStringFactory9Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
        name6: CharSequence,
        name7: CharSequence,
        name8: CharSequence,
    ): MessageBundleLocalizedStringFactory9Typed<T0, T1, T2, T3, T4, T5, T6, T7, T8> =
        getLocalizedStringFactory9Typed(
            key, index, NameToIndexMapperList(
                name0, name1, name2, name3, name4, name5, name6, name7, name8
            )
        )

    /** See [getLocalizedStringFactory1], but with 10 parameters. */
    protected fun getLocalizedStringFactory10(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory10 =
        LocalizedStringFactory10Bundled(this, key, index, nameMapper)

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any, T9 : Any> getLocalizedStringFactory10Typed(
        key: String,
        index: Int,
        nameMapper: NameToIndexMapper = NameToIndexMapperNumbersFrom0,
    ): MessageBundleLocalizedStringFactory10Typed<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> =
        LocalizedStringFactory10TypedBundled(this, key, index, nameMapper)

    protected fun getLocalizedStringFactory10(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
        name6: CharSequence,
        name7: CharSequence,
        name8: CharSequence,
        name9: CharSequence,
    ): MessageBundleLocalizedStringFactory10 =
        getLocalizedStringFactory10(
            key, index, NameToIndexMapperList(
                name0, name1, name2, name3, name4, name5, name6, name7, name8, name9
            )
        )

    protected fun <T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any, T9 : Any> getLocalizedStringFactory10Typed(
        key: String,
        index: Int,
        name0: CharSequence,
        name1: CharSequence,
        name2: CharSequence,
        name3: CharSequence,
        name4: CharSequence,
        name5: CharSequence,
        name6: CharSequence,
        name7: CharSequence,
        name8: CharSequence,
        name9: CharSequence,
    ): MessageBundleLocalizedStringFactory10Typed<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> =
        getLocalizedStringFactory10Typed(
            key, index, NameToIndexMapperList(
                name0, name1, name2, name3, name4, name5, name6, name7, name8, name9
            )
        )

    ////////////////////////////////////////////////////////////
    // companion
    ////////////////////////////////////////////////////////////

    companion object {
        private const val EXTENSION_KEY_PRIVATE = 'x'
        private const val EXTENSION_VALUE_ATTRIB_PREFIX = "attr-"
    }

    ////////////////////////////////////////////////////////////
    // LocalizedString
    ////////////////////////////////////////////////////////////

    /** [LocalizedString] for a given index in a message bundle with 0 parameters */
    private class LocalizedString0(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int
    ) : AbstractLocalizedString(), MessageBundleLocalizedString {

        override fun toString() =
            toString(null)

        override fun toString(locale: Locale?) =
            messageBundle.getStringS(messageIndex, locale)

        override fun getAttribute(attributeName: CharSequence, locale: Locale?): String? {
            return messageBundle.getAttribString0(attributeName, messageIndex, locale)
        }

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

    /** [LocalizedString] for a given index in a message bundle with N parameters */

    /** [LocalizedString] for a given index in a message bundle with N parameters */
    private class LocalizedStringN(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int,
        private val parameters: MessageParameters,
    ) : AbstractLocalizedString(), MessageBundleLocalizedString {

        constructor(
            messageBundle: MessageBundle,
            messageKey: String,
            messageIndex: Int,
            parameters: List<Any>,
            nameMapper: NameToIndexMapper,
        ) : this(
            messageBundle, messageKey, messageIndex,
            MessageParametersList(parameters, nameMapper)
        )


        override fun toString() =
            toString(null)

        override fun toString(locale: Locale?) =
            messageBundle.getStringN(messageIndex, parameters, locale)

        override fun getAttribute(attributeName: CharSequence, locale: Locale?): String? {
            return messageBundle.getAttribString0(attributeName, messageIndex, locale)
        }

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
    private open class LocalizedStringFactoryNBundled(
        override val messageBundle: MessageBundle,
        override val messageKey: String,
        override val messageIndex: Int,
    ) : MessageBundleLocalizedStringFactoryN, LocalizedStringFactoryX {

        override fun toString() =
            toString(null)

        fun toString(locale: Locale?) =
            messageBundle.getString0(messageIndex, locale)

        // @formatter:off
        override fun createString(parameters: MessageParameters, locale: Locale?) =
            messageBundle.getStringN(messageIndex, parameters, locale)

        override fun createLocalizedString(parameters: MessageParameters) =
            messageBundle.getLocalizedStringN(messageKey, messageIndex, parameters)
        // @formatter:on

        override val asN
            get() = this

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as LocalizedStringFactoryNBundled

            if (messageBundle != other.messageBundle) return false
            if (messageKey != other.messageKey) return false
            if (messageIndex != other.messageIndex) return false

            return true
        }

        override fun hashCode(): Int {
            var result = messageBundle.hashCode()
            result = 31 * result + messageKey.hashCode()
            result = 31 * result + messageIndex
            return result
        }


    }

    private open class LocalizedStringFactory1TypedBundled<T0 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory1Typed<T0> {

        // @formatter:off
        override fun createString(p0: T0, locale: Locale?) =
            messageBundle.getStringN(messageIndex, MessageParametersList(listOf(p0), nameMapper), locale)

        override fun createLocalizedString(p0: T0) =
            messageBundle.getLocalizedString1(messageKey, messageIndex, p0, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory1Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory1TypedBundled<Any>(messageBundle, messageKey, messageIndex, nameMapper),
        MessageBundleLocalizedStringFactory1

    private open class LocalizedStringFactory2TypedBundled<T0 : Any, T1 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory2Typed<T0, T1> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, locale: Locale?) =
            messageBundle.getStringN(messageIndex,MessageParametersList( listOf(p0, p1), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1) =
            messageBundle.getLocalizedString2(messageKey, messageIndex, p0, p1, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory2Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory2TypedBundled<Any, Any>(messageBundle, messageKey, messageIndex, nameMapper),
        MessageBundleLocalizedStringFactory2

    private open class LocalizedStringFactory3TypedBundled<T0 : Any, T1 : Any, T2 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory3Typed<T0, T1, T2> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, p2: T2, locale: Locale?) =
            messageBundle.getStringN(messageIndex, MessageParametersList(listOf(p0, p1, p2), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1, p2: T2) =
            messageBundle.getLocalizedString3(messageKey, messageIndex, p0, p1, p2, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory3Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory3TypedBundled<Any, Any, Any>(messageBundle, messageKey, messageIndex, nameMapper),
        MessageBundleLocalizedStringFactory3

    private open class LocalizedStringFactory4TypedBundled<T0 : Any, T1 : Any, T2 : Any, T3 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory4Typed<T0, T1, T2, T3> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, p2: T2, p3: T3, locale: Locale?) =
            messageBundle.getStringN(messageIndex,MessageParametersList( listOf(p0, p1, p2, p3), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3) =
            messageBundle.getLocalizedString4(messageKey, messageIndex, p0, p1, p2, p3, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory4Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory4TypedBundled<Any, Any, Any, Any>(messageBundle, messageKey, messageIndex, nameMapper),
        MessageBundleLocalizedStringFactory4

    private open class LocalizedStringFactory5TypedBundled<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory5Typed<T0, T1, T2, T3, T4> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, locale: Locale?) =
            messageBundle.getStringN(messageIndex, MessageParametersList(listOf(p0, p1, p2, p3, p4), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4) =
            messageBundle.getLocalizedString5(messageKey, messageIndex, p0, p1, p2, p3, p4, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory5Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory5TypedBundled<Any, Any, Any, Any, Any>(
        messageBundle,
        messageKey,
        messageIndex,
        nameMapper
    ),
        MessageBundleLocalizedStringFactory5

    private open class LocalizedStringFactory6TypedBundled<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory6Typed<T0, T1, T2, T3, T4, T5> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, locale: Locale?) =
            messageBundle.getStringN(messageIndex, MessageParametersList(listOf(p0, p1, p2, p3, p4, p5), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5) =
            messageBundle.getLocalizedString6(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory6Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory6TypedBundled<Any, Any, Any, Any, Any, Any>(
        messageBundle,
        messageKey,
        messageIndex,
        nameMapper
    ),
        MessageBundleLocalizedStringFactory6

    private open class LocalizedStringFactory7TypedBundled<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory7Typed<T0, T1, T2, T3, T4, T5, T6> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, locale: Locale?) =
            messageBundle.getStringN(messageIndex, MessageParametersList(listOf(p0, p1, p2, p3, p4, p5, p6), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6) =
            messageBundle.getLocalizedString7(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, p6, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory7Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory7TypedBundled<Any, Any, Any, Any, Any, Any, Any>(
        messageBundle,
        messageKey,
        messageIndex,
        nameMapper
    ),
        MessageBundleLocalizedStringFactory7

    private open class LocalizedStringFactory8TypedBundled<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory8Typed<T0, T1, T2, T3, T4, T5, T6, T7> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, locale: Locale?) =
            messageBundle.getStringN(messageIndex, MessageParametersList(listOf(p0, p1, p2, p3, p4, p5, p6, p7), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7) =
            messageBundle.getLocalizedString8(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, p6, p7, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory8Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory8TypedBundled<Any, Any, Any, Any, Any, Any, Any, Any>(
        messageBundle,
        messageKey,
        messageIndex,
        nameMapper
    ),
        MessageBundleLocalizedStringFactory8


    private open class LocalizedStringFactory9TypedBundled<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory9Typed<T0, T1, T2, T3, T4, T5, T6, T7, T8> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8, locale: Locale?) =
            messageBundle.getStringN(messageIndex,MessageParametersList( listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8) =
            messageBundle.getLocalizedString9(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, p6, p7, p8, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory9Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory9TypedBundled<Any, Any, Any, Any, Any, Any, Any, Any, Any>(
        messageBundle,
        messageKey,
        messageIndex,
        nameMapper
    ),
        MessageBundleLocalizedStringFactory9

    private open class LocalizedStringFactory10TypedBundled<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any, T9 : Any>(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        val nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactoryNBundled(messageBundle, messageKey, messageIndex),
        MessageBundleLocalizedStringFactory10Typed<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> {

        // @formatter:off
        override fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8, p9: T9, locale: Locale?) =
            messageBundle.getStringN(messageIndex, MessageParametersList(listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9), nameMapper), locale)

        override fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8, p9: T9) =
            messageBundle.getLocalizedString10(messageKey, messageIndex, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, nameMapper)
        // @formatter:on
    }

    private open class LocalizedStringFactory10Bundled(
        messageBundle: MessageBundle,
        messageKey: String,
        messageIndex: Int,
        nameMapper: NameToIndexMapper,
    ) : LocalizedStringFactory10TypedBundled<Any, Any, Any, Any, Any, Any, Any, Any, Any, Any>(
        messageBundle,
        messageKey,
        messageIndex,
        nameMapper
    ),
        MessageBundleLocalizedStringFactory10
}