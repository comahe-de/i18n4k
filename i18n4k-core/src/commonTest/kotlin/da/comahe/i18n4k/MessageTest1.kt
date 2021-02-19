package da.comahe.i18n4k

import de.comahe.i18n4k.messages.MessageBundle
import kotlin.jvm.JvmField

object MessageTest1 : MessageBundle() {
    private var idx = 0;

    /* yes */
    @JvmField
    val YES = getLocalizedString0(idx++)

    /* no */
    @JvmField
    val NO = getLocalizedString0(idx++)

    /* Hello {0} */
    @JvmField
    val HELLO_X1 = getLocalizedStringFactory1(idx++)

    /* Hello {0} & {0} */
    @JvmField
    val HELLO_X1_2 = getLocalizedStringFactory1(idx++)

    /* Hello {0} and {1} */
    @JvmField
    val HELLO_X2 = getLocalizedStringFactory2(idx++)

    /* Hello {0}, {1} and {2} */
    @JvmField
    val HELLO_X3 = getLocalizedStringFactory3(idx++)

    /* Hello {0}, {1}, {2} and {3} */
    @JvmField
    val HELLO_X4 = getLocalizedStringFactory4(idx++)

    /* Hello {0}, {1}, {2}, {3} and {4} */
    @JvmField
    val HELLO_X5 = getLocalizedStringFactory5(idx++)

    /* Hello {4}, {0}, {1}, {3} and {2} */
    @JvmField
    val HELLO_X5_2 = getLocalizedStringFactory5(idx++)

    /* Hello {1}, {1}, {2}, {2} and {0}! */
    @JvmField
    val HELLO_X5_3 = getLocalizedStringFactory5(idx++)

}
