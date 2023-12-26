package de.comahe.i18n4k.messages.formatter.provider

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.strings.LocalizedString

interface GenderProvider {
        fun getGenderOf(value: Any?, locale: Locale?): String?
    }

    object GenderProviderDefault : GenderProvider {
        private const val GENDER_ATTRIBUTE = "gender"
        override fun getGenderOf(value: Any?, locale: Locale?): String? {
            return (value as? LocalizedString)?.getAttribute(GENDER_ATTRIBUTE, locale)
        }
    }