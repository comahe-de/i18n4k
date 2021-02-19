package de.comahe.i18n4k.generator

import java.util.Locale

/** Data of the message bundle for a given locale/language. Maps key-strings to translations-strings. */
class MessagesData(
    /**
     * Locale
     * */
    val locale: Locale,
    /** Key: access key; value: message */
    val messages: Map<String, String>


)