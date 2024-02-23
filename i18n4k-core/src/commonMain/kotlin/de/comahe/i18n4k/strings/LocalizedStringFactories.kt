package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageParameters
import de.comahe.i18n4k.messages.formatter.MessageParametersMap

/** A factory for [LocalizedString] with arbitrary number of parameters. */
interface LocalizedStringFactoryN {
    /**
     * Create a string with the given parameter
     *
     * @param locale The locale to be used. If `null` the current setting in [i18n4k]
     *     ([I18n4kConfig.locale]) will be used
     */
    fun createString(parameters: MessageParameters, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter */
    fun createLocalizedString(parameters: MessageParameters): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(parameters: MessageParameters, locale: Locale? = null): String =
        createString(parameters, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(parameters: MessageParameters): LocalizedString =
        createLocalizedString(parameters)

    /** applies the parameter pairs and creates the string */
    operator fun invoke(vararg parameters: Pair<String, Any>, locale: Locale? = null): String =
        this(MessageParametersMap(*parameters), locale)

    /** applies the parameter pairs and creates the [LocalizedString] instance */
    operator fun get(vararg parameters: Pair<String, Any>): LocalizedString =
        this[MessageParametersMap(*parameters)]
}

/** Base interface for all factories with direct parameters */
interface LocalizedStringFactoryX {
    /**
     * A view of this instance with the [LocalizedStringFactoryN] interface, so that
     * [MessageParameters] can be used.
     *
     * [LocalizedStringFactoryN] cannot be the parent interface because the methods would be
     * ambiguous with the ones in [LocalizedStringFactory1] and [LocalizedStringFactory2]
     */
    val asN: LocalizedStringFactoryN
}

/** A factory for [LocalizedString] with 1 parameter. */
interface LocalizedStringFactory1 : LocalizedStringFactoryX {
    /**
     * Create a string with the given parameter
     *
     * @param locale The locale to be used. If `null` the current setting in [i18n4k]
     *     ([I18n4kConfig.locale]) will be used
     */
    fun createString(p0: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter */
    fun createLocalizedString(p0: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(p0: Any, locale: Locale? = null): String =
        createString(p0, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any): LocalizedString =
        createLocalizedString(p0)


}

/** See [LocalizedStringFactory1], but with 2 parameters. */
interface LocalizedStringFactory2 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(p0: Any, p1: Any, locale: Locale? = null): String =
        createString(p0, p1, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any): LocalizedString =
        createLocalizedString(p0, p1)
}

/** See [LocalizedStringFactory1], but with 3 parameters. */
interface LocalizedStringFactory3 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, p2: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any, p2: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(p0: Any, p1: Any, p2: Any, locale: Locale? = null): String =
        createString(p0, p1, p2, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any, p2: Any): LocalizedString =
        createLocalizedString(p0, p1, p2)
}


/** See [LocalizedStringFactory1], but with 4 parameters. */
interface LocalizedStringFactory4 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, p2: Any, p3: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(p0: Any, p1: Any, p2: Any, p3: Any, locale: Locale? = null): String =
        createString(p0, p1, p2, p3, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any, p2: Any, p3: Any): LocalizedString =
        createLocalizedString(p0, p1, p2, p3)
}

/** See [LocalizedStringFactory1], but with 5 parameters. */
interface LocalizedStringFactory5 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4)
}

/** See [LocalizedStringFactory1], but with 6 parameters. */
interface LocalizedStringFactory6 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5)
}

/** See [LocalizedStringFactory1], but with 7 parameters. */
interface LocalizedStringFactory7 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, p6: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, p6: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, p6, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5, p6)
}

/** See [LocalizedStringFactory1], but with 8 parameters. */
interface LocalizedStringFactory8 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, p6: Any, p7: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, p6: Any, p7: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, p6, p7, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5, p6, p7)
}

/** See [LocalizedStringFactory1], but with 9 parameters. */
interface LocalizedStringFactory9 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, p6: Any, p7: Any, p8: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, p6: Any, p7: Any, p8: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, p6, p7, p8, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5, p6, p7, p8)
}

/** See [LocalizedStringFactory1], but with 10 parameters. */
interface LocalizedStringFactory10 : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1.createString]]
     */
    fun createString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, p6: Any, p7: Any, p8: Any, p9: Any, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1.createLocalizedString]]
     */
    fun createLocalizedString(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any,  p5: Any, p6: Any, p7: Any, p8: Any, p9: Any): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any, p9: Any,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: Any, p1: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any, p9: Any): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
}