package da.comahe.i18n4k

import de.comahe.i18n4k.messages.MessageBundle
import kotlin.jvm.JvmField

object MessageTest1 : MessageBundle() {

    /* yes */
    @JvmField
    val YES = getLocalizedString0("YES", 0)

    /* no */
    @JvmField
    val NO = getLocalizedString0("NO", 1)

    /* Hello {0} */
    @JvmField
    val HELLO_X1 = getLocalizedStringFactory1("HELLO_X1", 2)

    /* Hello {0} & {0} */
    @JvmField
    val HELLO_X1_2 = getLocalizedStringFactory1("HELLO_X1_2", 3)

    /* Hello {0} and {1} */
    @JvmField
    val HELLO_X2 = getLocalizedStringFactory2("HELLO_X2", 4)

    /* Hello {0}, {1} and {2} */
    @JvmField
    val HELLO_X3 = getLocalizedStringFactory3("", 5)

    /* Hello {0}, {1}, {2} and {3} */
    @JvmField
    val HELLO_X4 = getLocalizedStringFactory4("HELLO_X4", 6)

    /* Hello {0}, {1}, {2}, {3} and {4} */
    @JvmField
    val HELLO_X5 = getLocalizedStringFactory5("HELLO_X5", 7)

    /* Hello {4}, {0}, {1}, {3} and {2} */
    @JvmField
    val HELLO_X5_2 = getLocalizedStringFactory5("HELLO_X5_2", 8)

    /* Hello {1}, {1}, {2}, {2} and {0}! */
    @JvmField
    val HELLO_X5_3 = getLocalizedStringFactory5("HELLO_X5_3", 9)


    @JvmField
    val SOME_ARE_NULL_1 = getLocalizedString0("SOME_ARE_NULL_1", 10)

    @JvmField
    val SOME_ARE_NULL_2 = getLocalizedString0("SOME_ARE_NULL_2", 11)

    /** To large index and no translations anywhere */
    @JvmField
    val NO_TEXT = getLocalizedString0("NO_TEXT", 20)

    init {
        registerMessageBundleEntries(
            YES,
            NO,
            HELLO_X1,
            HELLO_X1_2,
            HELLO_X2,
            HELLO_X3,
            HELLO_X4,
            HELLO_X5,
            HELLO_X5_2,
            HELLO_X5_3,
            SOME_ARE_NULL_1,
            SOME_ARE_NULL_2,
            NO_TEXT
        )
    }

}
