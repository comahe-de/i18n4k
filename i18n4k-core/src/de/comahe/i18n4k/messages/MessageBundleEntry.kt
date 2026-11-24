package de.comahe.i18n4k.messages

/**
 * Entry in a [MessageBundle].
 *
 * Implementing classes are [de.comahe.i18n4k.strings.LocalizedString],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory1],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory2],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory3],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory4],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory5],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory6],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory7],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory8],
 * [de.comahe.i18n4k.strings.LocalizedStringFactory9],
 */
interface MessageBundleEntry {
    /** The message bundle where the entry belongs to */
    val messageBundle: MessageBundle

    /** The key of the entry of the message bundle. */
    val messageKey: String

    /** the index of the entry in the bundle */
    val messageIndex: Int

}