package de.comahe.i18n4k.config

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatter
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import de.comahe.i18n4k.systemLocale
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

/** Mutabel implementation of the configuration for all the I18N parameters */
@Suppress("MemberVisibilityCanBePrivate")
class I18n4kConfigDefault : I18n4kConfig {

    /////////////////////////////////////////
    // Provider to change the behaviour of the getter below

    /** atomic ref for [defaultLocale] */
    private val defaultLocaleRef: AtomicRef<Locale> =
        atomic(Locale("en"))

    /** atomic ref for [locale] */
    private val localeRef: AtomicRef<Locale> =
        atomic(systemLocale)

    /** atomic ref for [messageFormatter] */
    private val messageFormatterRef: AtomicRef<MessageFormatter> =
        atomic(MessageFormatterDefault)

    /** atomic ref for [treadBlankStringAsNull] */
    private val treadBlankStringAsNullRef: AtomicBoolean = atomic(true)

    /** Restores the default settings. */
    fun restoreDefaultSettings() {
        defaultLocale = Locale("en")
        locale = systemLocale
        messageFormatter = MessageFormatterDefault
        treadBlankStringAsNull = true
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
     *  For null strings the default locale is used.
     */
    override var treadBlankStringAsNull: Boolean
        get() = treadBlankStringAsNullRef.value
        set(value) {
            treadBlankStringAsNullRef.value = value
        }

}