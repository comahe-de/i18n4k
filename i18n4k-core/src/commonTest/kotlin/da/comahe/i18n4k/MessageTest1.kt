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

    /* Hello {0}, {1}, {2}, {3}, {4} and {5} */
    @JvmField
    val HELLO_X6 = getLocalizedStringFactory6("HELLO_X6", 10)

    /* Hello {0}, {1}, {2}, {3}, {4}, {5} and {6} */
    @JvmField
    val HELLO_X7 = getLocalizedStringFactory7("HELLO_X7", 11)

    /* Hello {0}, {1}, {2}, {3}, {4}, {5}, {6} and {7} */
    @JvmField
    val HELLO_X8 = getLocalizedStringFactory8("HELLO_X8", 12)

    /* Hello {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7} and {8} */
    @JvmField
    val HELLO_X9 = getLocalizedStringFactory9("HELLO_X9", 13)

    /* Hello {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8} and {9} */
    @JvmField
    val HELLO_X10 = getLocalizedStringFactory10("HELLO_X10", 14)

    @JvmField
    val SOME_ARE_NULL_1 = getLocalizedString0("SOME_ARE_NULL_1", 15)

    @JvmField
    val SOME_ARE_NULL_2 = getLocalizedString0("SOME_ARE_NULL_2", 16)

    /**
     * forgotten-bags={0} has forgotten {1, select, female: her | his } {3,
     * select, one: bag | {2} bags}.
     */
    val SELECT_PATTERN = getLocalizedStringFactory4("SELECT_PATTERN", 17)

    /**
     * It's {0, number, .02}!
     */
    val NUMBER_PATTERN = getLocalizedStringFactory1("NUMBER_PATTERN", 18)


    /**
     * It's a qute! Look: '
     */
    val SINGLE_QUOTES = getLocalizedString0("SINGLE_QUOTES", 19)

    // @formatter:off

    /* Hello {pa} */
    @JvmField
    val HELLO_X1_NAMED = getLocalizedStringFactory1("HELLO_X1_NAMED", 20, "pa")

    /* Hello {pa} & {pa} */
    @JvmField
    val HELLO_X1_2_NAMED = getLocalizedStringFactory1("HELLO_X1_2_NAMED", 21, "pa")

    /* Hello {pa} and {pb} */
    @JvmField
    val HELLO_X2_NAMED = getLocalizedStringFactory2("HELLO_X2_NAMED", 22,"pa", "pb")

    /* Hello {pa}, {pb} and {pc} */
    @JvmField
    val HELLO_X3_NAMED = getLocalizedStringFactory3("HELLO_X3_NAMED", 23,"pa", "pb", "pc")
    /* Hello {pa}, {pb}, {pc} and {pd} */
    @JvmField
    val HELLO_X4_NAMED = getLocalizedStringFactory4("HELLO_X4_NAMED", 24,"pa", "pb", "pc", "pd")

    /* Hello {pa}, {pb}, {pc}, {pd} and {pe} */
    @JvmField
    val HELLO_X5_NAMED = getLocalizedStringFactory5("HELLO_X5_NAMED", 25,"pa", "pb", "pc", "pd", "pe")

    /* Hello {pe}, {pa}, {pb}, {pd} and {pc} */
    @JvmField
    val HELLO_X5_2_NAMED = getLocalizedStringFactory5("HELLO_X5_NAMED", 26,"pa", "pb", "pc", "pd", "pe")

    /* Hello {pb}, {pb}, {pc}, {pc} and {pa}! */
    @JvmField
    val HELLO_X5_3_NAMED = getLocalizedStringFactory5("HELLO_X5_NAMED", 27,"pa", "pb", "pc", "pd", "pe")

    /* Hello {pa}, {pb}, {pc}, {pd}, {pe} and {pf} */
    @JvmField
    val HELLO_X6_NAMED = getLocalizedStringFactory6("HELLO_X6_NAMED", 28,"pa", "pb", "pc", "pd", "pe", "pf")

    /* Hello {pa}, {pb}, {pc}, {pd}, {pe}, {pf} and {pg} */
    @JvmField
    val HELLO_X7_NAMED = getLocalizedStringFactory7("HELLO_X7_NAMED", 29,"pa", "pb", "pc", "pd", "pe", "pf", "pg")

    /* Hello {pa}, {pb}, {pc}, {pd}, {pe}, {pf}, {pg} and {ph} */
    @JvmField
    val HELLO_X8_NAMED = getLocalizedStringFactory8("HELLO_X8_NAMED", 30,"pa", "pb", "pc", "pd", "pe", "pf", "pg", "ph")

    /* Hello {pa}, {pb}, {pc}, {pd}, {pe}, {pf}, {pg}, {ph} and {pi} */
    @JvmField
    val HELLO_X9_NAMED = getLocalizedStringFactory9("HELLO_X9_NAMED", 31,"pa", "pb", "pc", "pd", "pe", "pf", "pg", "ph", "pi")

    /* Hello {pa}, {pb}, {pc}, {pd}, {pe}, {pf}, {pg}, {ph}, {pi} and {pj} */
    @JvmField
    val HELLO_X10_NAMED = getLocalizedStringFactory10("HELLO_X10_NAMED", 32,"pa", "pb", "pc", "pd", "pe", "pf", "pg", "ph", "pi", "pj")

    // @formatter:on

    /** To large index and no translations anywhere */
    @JvmField
    val NO_TEXT = getLocalizedString0("NO_TEXT", 33)

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
            HELLO_X6,
            HELLO_X7,
            HELLO_X8,
            HELLO_X9,
            HELLO_X10,
            SOME_ARE_NULL_1,
            SOME_ARE_NULL_2,
            NO_TEXT
        )
    }

}
