package de.comahe.i18n4k




// TODO how to get the system locale in Linux-Native?
actual val systemLocale: Locale
    get() {
        return Locale("en")
    }