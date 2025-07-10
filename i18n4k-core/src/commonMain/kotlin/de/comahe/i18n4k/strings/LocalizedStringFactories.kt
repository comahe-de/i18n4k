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

/** A factory for [LocalizedString] with 1 typed parameter. */
interface LocalizedStringFactory1Typed<T0 : Any> : LocalizedStringFactoryX {
    /**
     * Create a string with the given parameter
     *
     * @param locale The locale to be used. If `null` the current setting in [i18n4k]
     *    ([I18n4kConfig.locale]) will be used
     */
    fun createString(p0: T0, locale: Locale? = null): String

    /** Create [LocalizedString] with the given parameter */
    fun createLocalizedString(p0: T0): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(p0: T0, locale: Locale? = null): String =
        createString(p0, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0): LocalizedString =
        createLocalizedString(p0)


}

/** A factory for [LocalizedString] with 1 parameter. */
interface LocalizedStringFactory1 : LocalizedStringFactory1Typed<Any>

/** See [LocalizedStringFactory1Typed], but with 2 typed parameters. */
interface LocalizedStringFactory2Typed<T0 : Any, T1 : Any> : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(p0: T0, p1: T1, locale: Locale? = null): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(p0: T0, p1: T1): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(p0: T0, p1: T1, locale: Locale? = null): String =
        createString(p0, p1, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1): LocalizedString =
        createLocalizedString(p0, p1)
}

/** A factory for [LocalizedString] with 2 parameters. */
interface LocalizedStringFactory2 : LocalizedStringFactory2Typed<Any, Any>

/** See [LocalizedStringFactory1Typed], but with 3 typed parameters */
interface LocalizedStringFactory3Typed<T0 : Any, T1 : Any, T2 : Any> : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(p0: T0, p1: T1, p2: T2, locale: Locale? = null): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(p0: T0, p1: T1, p2: T2): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(p0: T0, p1: T1, p2: T2, locale: Locale? = null): String =
        createString(p0, p1, p2, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1, p2: T2): LocalizedString =
        createLocalizedString(p0, p1, p2)
}

/** A factory for [LocalizedString] with 3 parameters. */
interface LocalizedStringFactory3 : LocalizedStringFactory3Typed<Any, Any, Any>

/** See [LocalizedStringFactory1Typed], but with 4 typed parameters */
interface LocalizedStringFactory4Typed<T0 : Any, T1 : Any, T2 : Any, T3 : Any> : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(p0: T0, p1: T1, p2: T2, p3: T3, locale: Locale? = null): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(p0: T0, p1: T1, p2: T2, p3: T3, locale: Locale? = null): String =
        createString(p0, p1, p2, p3, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1, p2: T2, p3: T3): LocalizedString =
        createLocalizedString(p0, p1, p2, p3)
}

/** A factory for [LocalizedString] with 4 parameters. */
interface LocalizedStringFactory4 : LocalizedStringFactory4Typed<Any, Any, Any, Any>

/** See [LocalizedStringFactory1Typed], but with 5 typed parameters */
interface LocalizedStringFactory5Typed<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any> : LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, locale: Locale? = null): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: T0, p1: T1, p2: T2, p3: T3, p4: T4,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4)
}

/** A factory for [LocalizedString] with 5 parameters. */
interface LocalizedStringFactory5 : LocalizedStringFactory5Typed<Any, Any, Any, Any, Any>

/** See [LocalizedStringFactory1Typed], but with 6 typed parameters */
interface LocalizedStringFactory6Typed<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any> :
    LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, locale: Locale? = null): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5)
}

/** A factory for [LocalizedString] with 6 parameters. */
interface LocalizedStringFactory6 : LocalizedStringFactory6Typed<Any, Any, Any, Any, Any, Any>

/** See [LocalizedStringFactory1Typed], but with 7 typed parameters */
interface LocalizedStringFactory7Typed<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any> :
    LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, locale: Locale? = null): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, p6, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5, p6)
}

/** A factory for [LocalizedString] with 7 parameters. */
interface LocalizedStringFactory7 : LocalizedStringFactory7Typed<Any, Any, Any, Any, Any, Any, Any>

/** See [LocalizedStringFactory1Typed], but with 8 typed parameters */
interface LocalizedStringFactory8Typed<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any> :
    LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, locale: Locale? = null): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, p6, p7, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5, p6, p7)
}

/** A factory for [LocalizedString] with 8 parameters. */
interface LocalizedStringFactory8 : LocalizedStringFactory8Typed<Any, Any, Any, Any, Any, Any, Any, Any>


/** See [LocalizedStringFactory1Typed], but with 9 typed parameters */
interface LocalizedStringFactory9Typed<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any> :
    LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(
        p0: T0,
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4,
        p5: T5,
        p6: T6,
        p7: T7,
        p8: T8,
        locale: Locale? = null
    ): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, p6, p7, p8, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5, p6, p7, p8)
}

/** A factory for [LocalizedString] with 9 parameters. */
interface LocalizedStringFactory9 : LocalizedStringFactory9Typed<Any, Any, Any, Any, Any, Any, Any, Any, Any>


/** See [LocalizedStringFactory1Typed], but with 10 typed parameters */
interface LocalizedStringFactory10Typed<T0 : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any, T9 : Any> :
    LocalizedStringFactoryX {

    /**
     * Create a string with the given parameter.
     *
     * @see [LocalizedStringFactory1Typed.createString]]
     */
    fun createString(
        p0: T0,
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4,
        p5: T5,
        p6: T6,
        p7: T7,
        p8: T8,
        p9: T9,
        locale: Locale? = null
    ): String

    /**
     * Create [LocalizedString] with the given parameter
     *
     * @see [LocalizedStringFactory1Typed.createLocalizedString]]
     */
    fun createLocalizedString(
        p0: T0,
        p1: T1,
        p2: T2,
        p3: T3,
        p4: T4,
        p5: T5,
        p6: T6,
        p7: T7,
        p8: T8,
        p9: T9
    ): LocalizedString

    /** Shortcut for [createString] */
    operator fun invoke(
        p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8, p9: T9,
        locale: Locale? = null
    ): String =
        createString(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, locale)

    /** Shortcut for [createLocalizedString] */
    operator fun get(p0: T0, p1: T1, p2: T2, p3: T3, p4: T4, p5: T5, p6: T6, p7: T7, p8: T8, p9: T9): LocalizedString =
        createLocalizedString(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
}


/** A factory for [LocalizedString] with 10 parameters. */
interface LocalizedStringFactory10 : LocalizedStringFactory10Typed<Any, Any, Any, Any, Any, Any, Any, Any, Any, Any>