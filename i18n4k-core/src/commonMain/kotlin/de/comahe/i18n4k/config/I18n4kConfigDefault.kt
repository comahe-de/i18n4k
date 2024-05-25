package de.comahe.i18n4k.config

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.createLocale
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProvider
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProviderDefault
import de.comahe.i18n4k.messages.formatter.provider.GenderProvider
import de.comahe.i18n4k.messages.formatter.provider.GenderProviderDefault
import de.comahe.i18n4k.systemLocale
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

/** Mutabel implementation of the configuration for all the I18N parameters */
@Suppress("MemberVisibilityCanBePrivate")
class I18n4kConfigDefault : I18n4kConfig {

    /////////////////////////////////////////
    // Provider to change the behaviour of the getter below

    /** atomic ref for [defaultLocale] */
    private val defaultLocaleRef: AtomicRef<Locale> =
        atomic(createLocale("en"))

    /** atomic ref for [locale] */
    private val localeRef: AtomicRef<Locale> =
        atomic(systemLocale)

    /** atomic ref for [messageFormatter] */
    private val messageFormatterRef: AtomicRef<MessageFormatter> =
        atomic(MessageFormatterDefault)

    /** atomic ref for [treadBlankStringAsNull] */
    private val treadBlankStringAsNullRef: AtomicBoolean = atomic(true)


    /** atomic ref for [ignoreMessageParseErrors] */
    private val ignoreMessageParseErrorsRef: AtomicBoolean = atomic(super.ignoreMessageParseErrors)

    /** atomic ref for [genderProvider] */
    private val genderProviderRef = atomic<GenderProvider>(GenderProviderDefault)

    /** atomic ref for [declensionProvider] */
    private val declensionProviderRef = atomic<DeclensionProvider>(DeclensionProviderDefault)

    /** Restores the default settings. */
    fun restoreDefaultSettings() {
        defaultLocale = createLocale("en")
        locale = systemLocale
        messageFormatter = MessageFormatterDefault
        treadBlankStringAsNull = true
        ignoreMessageParseErrors = super.ignoreMessageParseErrors
        genderProvider = GenderProviderDefault
        declensionProvider = DeclensionProviderDefault
    }

    /////////////////////////////////////////

    /** Fallback locale when a string in the currently set [locale] are not found */
    override var defaultLocale: Locale
        get() = defaultLocaleRef.value
        set(value) {
            defaultLocaleRef.value = value
        }

    /** currently set locale */
    override var locale: Locale
        get() = localeRef.value
        set(value) {
            localeRef.value = value
        }

    /** the format of the parameters */
    override var messageFormatter: MessageFormatter
        get() = messageFormatterRef.value
        set(value) {
            messageFormatterRef.value = value
        }

    /**
     * if a string of a localisation is null, the default locale is used. If this flag is true,
     * blank strings (empty of only whitespace) are also threaded as null.
     *
     * For null strings the default locale is used.
     */
    override var treadBlankStringAsNull: Boolean
        get() = treadBlankStringAsNullRef.value
        set(value) {
            treadBlankStringAsNullRef.value = value
        }

    override var ignoreMessageParseErrors: Boolean
        get() = ignoreMessageParseErrorsRef.value
        set(value) {
            ignoreMessageParseErrorsRef.value = value
        }

    override var genderProvider: GenderProvider
        get() = genderProviderRef.value
        set(value) = genderProviderRef.update { value }

    override var declensionProvider: DeclensionProvider
        get() = declensionProviderRef.value
        set(value) = declensionProviderRef.update { value }
}