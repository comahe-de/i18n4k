package de.comahe.i18n4k.cldr.plurals

/** CLDR Plural rules. This is an automatically generated file. */
object PluralRules {
    /** The CLDR version from which this was generated. */
    const val CLDR_VERSION = "44.1.0"
    private val _C_RULE_0 =
        { op: PluralOperand ->
            if (op.i == 0L || op.i == 1L) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_1 =
        { op: PluralOperand ->
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_2 =
        { op: PluralOperand ->
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0) {
                PluralCategory.TWO
            } else if (contains(op.n, 3.0, 6.0)) {
                PluralCategory.FEW
            } else if (contains(op.n, 7.0, 10.0)) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_3 =
        { op: PluralOperand ->
            if (op.n == 0.0 || op.n == 1.0 || op.i == 0L && op.f == 1) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_4 =
        { op: PluralOperand ->
            val mod_i1000000: Long = op.i % 1000000
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.e == 0 && op.i != 0L && mod_i1000000 == 0L && op.v == 0 || !contains(
                    op.e,
                    0,
                    5
                )
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_5 =
        { op: PluralOperand ->
            if (op.n == 1.0 || op.t != 0 && (op.i == 0L || op.i == 1L)) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_6 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            if (op.n == 0.0) {
                PluralCategory.ZERO
            } else if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0) {
                PluralCategory.TWO
            } else if (contains(mod_n100, 3.0, 10.0)) {
                PluralCategory.FEW
            } else if (contains(mod_n100, 11.0, 99.0)) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_7 =
        { op: PluralOperand ->
            val mod_i10: Long = op.i % 10
            val mod_f10: Int = op.f % 10
            if (op.v == 0 && (op.i == 1L || op.i == 2L || op.i == 3L) || op.v == 0 && !(mod_i10 == 4L || mod_i10 == 6L || mod_i10 == 9L) || op.v != 0 && !(mod_f10 == 4 || mod_f10 == 6 || mod_f10 == 9)) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_8 =
        { op: PluralOperand ->
            if (op.n == 0.0) {
                PluralCategory.ZERO
            } else if ((op.i == 0L || op.i == 1L) && op.n != 0.0) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_9 =
        { op: PluralOperand ->
            if (op.n == 1.0 || op.n == 11.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0 || op.n == 12.0) {
                PluralCategory.TWO
            } else if (contains(op.n, 3.0, 10.0) || contains(op.n, 13.0, 19.0)) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_10 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0) {
                PluralCategory.TWO
            } else if (op.n == 0.0 || contains(mod_n100, 3.0, 10.0)) {
                PluralCategory.FEW
            } else if (contains(mod_n100, 11.0, 19.0)) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_11 =
        { op: PluralOperand ->
            val mod_i1000000: Long = op.i % 1000000
            if (contains(op.i, 0, 1)) {
                PluralCategory.ONE
            } else if (op.e == 0 && op.i != 0L && mod_i1000000 == 0L && op.v == 0 || !contains(
                    op.e,
                    0,
                    5
                )
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_12 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            if (op.i == 1L && op.v == 0) {
                PluralCategory.ONE
            } else if (op.v != 0 || op.n == 0.0 || op.n != 1.0 && contains(mod_n100, 1.0, 19.0)) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_13 =
        { op: PluralOperand ->
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0) {
                PluralCategory.TWO
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_14 =
        { op: PluralOperand -> PluralCategory.OTHER }
    private val _C_RULE_15 =
        { op: PluralOperand ->
            if (contains(op.n, 0.0, 1.0) || contains(op.n, 11.0, 99.0)) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_16 =
        { op: PluralOperand ->
            if (op.i == 0L || op.n == 1.0) {
                PluralCategory.ONE
            } else if (contains(op.n, 2.0, 10.0)) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_17 =
        { op: PluralOperand ->
            if (op.i == 0L || op.n == 1.0) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_18 =
        { op: PluralOperand ->
            val mod_i10: Long = op.i % 10
            val mod_i100: Long = op.i % 100
            if (op.i == 1L && op.v == 0) {
                PluralCategory.ONE
            } else if (op.v == 0 && contains(mod_i10, 2, 4) && !contains(mod_i100, 12, 14)) {
                PluralCategory.FEW
            } else if (op.v == 0 && op.i != 1L && contains(
                    mod_i10,
                    0,
                    1
                ) || op.v == 0 && contains(mod_i10, 5, 9) || op.v == 0 && contains(
                    mod_i100,
                    12,
                    14
                )
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_19 =
        { op: PluralOperand ->
            if (op.n == 0.0) {
                PluralCategory.ZERO
            } else if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0) {
                PluralCategory.TWO
            } else if (op.n == 3.0) {
                PluralCategory.FEW
            } else if (op.n == 6.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_20 =
        { op: PluralOperand ->
            val mod_i100: Long = op.i % 100
            if (op.v == 0 && mod_i100 == 1L) {
                PluralCategory.ONE
            } else if (op.v == 0 && mod_i100 == 2L) {
                PluralCategory.TWO
            } else if (op.v == 0 && contains(mod_i100, 3, 4) || op.v != 0) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_21 =
        { op: PluralOperand ->
            if (op.n == 0.0) {
                PluralCategory.ZERO
            } else if (op.n == 1.0) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_22 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n10: Double = op.n % 10
            if (mod_n10 == 1.0 && !contains(mod_n100, 11.0, 19.0)) {
                PluralCategory.ONE
            } else if (contains(mod_n10, 2.0, 9.0) && !contains(mod_n100, 11.0, 19.0)) {
                PluralCategory.FEW
            } else if (op.f != 0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_23 =
        { op: PluralOperand ->
            val mod_i10: Long = op.i % 10
            val mod_i100: Long = op.i % 100
            if (op.v == 0 && mod_i10 == 1L && mod_i100 != 11L) {
                PluralCategory.ONE
            } else if (op.v == 0 && contains(mod_i10, 2, 4) && !contains(mod_i100, 12, 14)) {
                PluralCategory.FEW
            } else if (op.v == 0 && mod_i10 == 0L || op.v == 0 && contains(
                    mod_i10,
                    5,
                    9
                ) || op.v == 0 && contains(mod_i100, 11, 14)
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_24 =
        { op: PluralOperand ->
            val mod_i10: Long = op.i % 10
            val mod_i100: Long = op.i % 100
            val mod_f10: Int = op.f % 10
            val mod_f100: Int = op.f % 100
            if (op.v == 0 && mod_i10 == 1L && mod_i100 != 11L || mod_f10 == 1 && mod_f100 != 11) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_25 =
        { op: PluralOperand ->
            val mod_t100: Int = op.t % 100
            val mod_i10: Long = op.i % 10
            val mod_i100: Long = op.i % 100
            val mod_t10: Int = op.t % 10
            if (op.t == 0 && mod_i10 == 1L && mod_i100 != 11L || mod_t10 == 1 && mod_t100 != 11) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_26 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n1000000: Double = op.n % 1000000
            val mod_n10: Double = op.n % 10
            if (mod_n10 == 1.0 && !(mod_n100 == 11.0 || mod_n100 == 71.0 || mod_n100 == 91.0)) {
                PluralCategory.ONE
            } else if (mod_n10 == 2.0 && !(mod_n100 == 12.0 || mod_n100 == 72.0 || mod_n100 == 92.0)) {
                PluralCategory.TWO
            } else if ((contains(mod_n10, 3.0, 4.0) || mod_n10 == 9.0) && !(contains(
                    mod_n100,
                    10.0,
                    19.0
                ) || contains(mod_n100, 70.0, 79.0) || contains(mod_n100, 90.0, 99.0))
            ) {
                PluralCategory.FEW
            } else if (op.n != 0.0 && mod_n1000000 == 0.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_27 =
        { op: PluralOperand ->
            if (op.i == 1L && op.v == 0) {
                PluralCategory.ONE
            } else if (contains(op.i, 2, 4) && op.v == 0) {
                PluralCategory.FEW
            } else if (op.v != 0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_28 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_f10: Int = op.f % 10
            val mod_f100: Int = op.f % 100
            val mod_n10: Double = op.n % 10
            if (mod_n10 == 0.0 || contains(mod_n100, 11.0, 19.0) || op.v == 2 && contains(
                    mod_f100,
                    11,
                    19
                )
            ) {
                PluralCategory.ZERO
            } else if (mod_n10 == 1.0 && mod_n100 != 11.0 || op.v == 2 && mod_f10 == 1 && mod_f100 != 11 || op.v != 2 && mod_f10 == 1) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_29 =
        { op: PluralOperand ->
            val mod_i10: Long = op.i % 10
            val mod_i100: Long = op.i % 100
            if (op.v == 0 && mod_i10 == 1L) {
                PluralCategory.ONE
            } else if (op.v == 0 && mod_i10 == 2L) {
                PluralCategory.TWO
            } else if (op.v == 0 && (mod_i100 == 0L || mod_i100 == 20L || mod_i100 == 40L || mod_i100 == 60L || mod_i100 == 80L)) {
                PluralCategory.FEW
            } else if (op.v != 0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_30 =
        { op: PluralOperand ->
            val mod_i100: Long = op.i % 100
            val mod_f100: Int = op.f % 100
            if (op.v == 0 && mod_i100 == 1L || mod_f100 == 1) {
                PluralCategory.ONE
            } else if (op.v == 0 && mod_i100 == 2L || mod_f100 == 2) {
                PluralCategory.TWO
            } else if (op.v == 0 && contains(mod_i100, 3, 4) || contains(mod_f100, 3, 4)) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_31 =
        { op: PluralOperand ->
            if (op.i == 1L && op.v == 0) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_32 =
        { op: PluralOperand ->
            if (op.i == 1L && op.v == 0 || op.i == 0L && op.v != 0) {
                PluralCategory.ONE
            } else if (op.i == 2L && op.v == 0) {
                PluralCategory.TWO
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_33 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n10: Double = op.n % 10
            if (mod_n10 == 1.0 && mod_n100 != 11.0) {
                PluralCategory.ONE
            } else if (contains(mod_n10, 2.0, 4.0) && !contains(mod_n100, 12.0, 14.0)) {
                PluralCategory.FEW
            } else if (mod_n10 == 0.0 || contains(mod_n10, 5.0, 9.0) || contains(
                    mod_n100,
                    11.0,
                    14.0
                )
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_34 =
        { op: PluralOperand ->
            val mod_i1000000: Long = op.i % 1000000
            if (op.i == 0L || op.i == 1L) {
                PluralCategory.ONE
            } else if (op.e == 0 && op.i != 0L && mod_i1000000 == 0L && op.v == 0 || !contains(
                    op.e,
                    0,
                    5
                )
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_35 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n1000: Double = op.n % 1000
            val mod_n1000000: Double = op.n % 1000000
            val mod_n100000: Double = op.n % 100000
            if (op.n == 0.0) {
                PluralCategory.ZERO
            } else if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (mod_n100 == 2.0 || mod_n100 == 22.0 || mod_n100 == 42.0 || mod_n100 == 62.0 || mod_n100 == 82.0 || mod_n1000 == 0.0 && (contains(
                    mod_n100000,
                    1000.0,
                    20000.0
                ) || mod_n100000 == 40000.0 || mod_n100000 == 60000.0 || mod_n100000 == 80000.0) || op.n != 0.0 && mod_n1000000 == 100000.0
            ) {
                PluralCategory.TWO
            } else if (mod_n100 == 3.0 || mod_n100 == 23.0 || mod_n100 == 43.0 || mod_n100 == 63.0 || mod_n100 == 83.0) {
                PluralCategory.FEW
            } else if (op.n != 1.0 && (mod_n100 == 1.0 || mod_n100 == 21.0 || mod_n100 == 41.0 || mod_n100 == 61.0 || mod_n100 == 81.0)) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_36 =
        { op: PluralOperand ->
            if (contains(op.n, 0.0, 1.0)) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_37 =
        { op: PluralOperand ->
            if (op.n == 0.0) {
                PluralCategory.ZERO
            } else if (op.n == 1.0) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_38 =
        { op: PluralOperand ->
            val mod_i10: Long = op.i % 10
            val mod_i100: Long = op.i % 100
            val mod_f10: Int = op.f % 10
            val mod_f100: Int = op.f % 100
            if (op.v == 0 && mod_i10 == 1L && mod_i100 != 11L || mod_f10 == 1 && mod_f100 != 11) {
                PluralCategory.ONE
            } else if (op.v == 0 && contains(mod_i10, 2, 4) && !contains(
                    mod_i100,
                    12,
                    14
                ) || contains(mod_f10, 2, 4) && !contains(mod_f100, 12, 14)
            ) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _C_RULE_39 =
        { op: PluralOperand ->
            val mod_i1000000: Long = op.i % 1000000
            if (op.i == 1L && op.v == 0) {
                PluralCategory.ONE
            } else if (op.e == 0 && op.i != 0L && mod_i1000000 == 0L && op.v == 0 || !contains(
                    op.e,
                    0,
                    5
                )
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_0 =
        { op: PluralOperand ->
            if (op.n == 1.0 || op.n == 11.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0 || op.n == 12.0) {
                PluralCategory.TWO
            } else if (op.n == 3.0 || op.n == 13.0) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_1 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n10: Double = op.n % 10
            if (mod_n10 == 1.0 && mod_n100 != 11.0) {
                PluralCategory.ONE
            } else if (mod_n10 == 2.0 && mod_n100 != 12.0) {
                PluralCategory.TWO
            } else if (mod_n10 == 3.0 && mod_n100 != 13.0) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_2 =
        { op: PluralOperand ->
            if (op.i == 0L) {
                PluralCategory.ZERO
            } else if (op.i == 1L) {
                PluralCategory.ONE
            } else if (op.i == 2L || op.i == 3L || op.i == 4L || op.i == 5L || op.i == 6L) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_3 =
        { op: PluralOperand ->
            val mod_i10: Long = op.i % 10
            val mod_i100: Long = op.i % 100
            if (mod_i10 == 1L && mod_i100 != 11L) {
                PluralCategory.ONE
            } else if (mod_i10 == 2L && mod_i100 != 12L) {
                PluralCategory.TWO
            } else if ((mod_i10 == 7L || mod_i10 == 8L) && !(mod_i100 == 17L || mod_i100 == 18L)) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_4 =
        { op: PluralOperand ->
            if (contains(op.n, 1.0, 4.0)) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_5 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n10: Double = op.n % 10
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (mod_n10 == 4.0 && mod_n100 != 14.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_6 =
        { op: PluralOperand ->
            if (op.n == 1.0 || op.n == 5.0 || contains(op.n, 7.0, 9.0)) {
                PluralCategory.ONE
            } else if (op.n == 2.0 || op.n == 3.0) {
                PluralCategory.TWO
            } else if (op.n == 4.0) {
                PluralCategory.FEW
            } else if (op.n == 6.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_7 =
        { op: PluralOperand ->
            if (op.n == 11.0 || op.n == 8.0 || op.n == 80.0 || op.n == 800.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_8 =
        { op: PluralOperand ->
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_9 =
        { op: PluralOperand ->
            if (op.n == 11.0 || op.n == 8.0 || contains(op.n, 80.0, 89.0) || contains(
                    op.n,
                    800.0,
                    899.0
                )
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_10 =
        { op: PluralOperand -> PluralCategory.OTHER }
    private val _O_RULE_11 =
        { op: PluralOperand ->
            if (op.n == 0.0 || op.n == 7.0 || op.n == 8.0 || op.n == 9.0) {
                PluralCategory.ZERO
            } else if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0) {
                PluralCategory.TWO
            } else if (op.n == 3.0 || op.n == 4.0) {
                PluralCategory.FEW
            } else if (op.n == 5.0 || op.n == 6.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_12 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            if (contains(op.n, 1.0, 4.0) || contains(mod_n100, 1.0, 4.0) || contains(
                    mod_n100,
                    21.0,
                    24.0
                ) || contains(mod_n100, 41.0, 44.0) || contains(mod_n100, 61.0, 64.0) || contains(
                    mod_n100,
                    81.0,
                    84.0
                )
            ) {
                PluralCategory.ONE
            } else if (op.n == 5.0 || mod_n100 == 5.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_13 =
        { op: PluralOperand ->
            if (op.n == 1.0 || op.n == 3.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0) {
                PluralCategory.TWO
            } else if (op.n == 4.0) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_14 =
        { op: PluralOperand ->
            val mod_n10: Double = op.n % 10
            if (mod_n10 == 6.0 || mod_n10 == 9.0 || mod_n10 == 0.0 && op.n != 0.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_15 =
        { op: PluralOperand ->
            val mod_i10: Long = op.i % 10
            val mod_i1000: Long = op.i % 1000
            val mod_i100: Long = op.i % 100
            if (mod_i10 == 1L || mod_i10 == 2L || mod_i10 == 5L || mod_i10 == 7L || mod_i10 == 8L || mod_i100 == 20L || mod_i100 == 50L || mod_i100 == 70L || mod_i100 == 80L) {
                PluralCategory.ONE
            } else if (mod_i10 == 3L || mod_i10 == 4L || mod_i1000 == 100L || mod_i1000 == 200L || mod_i1000 == 300L || mod_i1000 == 400L || mod_i1000 == 500L || mod_i1000 == 600L || mod_i1000 == 700L || mod_i1000 == 800L || mod_i1000 == 900L) {
                PluralCategory.FEW
            } else if (op.i == 0L || mod_i10 == 6L || mod_i100 == 40L || mod_i100 == 60L || mod_i100 == 90L) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_16 =
        { op: PluralOperand ->
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0 || op.n == 3.0) {
                PluralCategory.TWO
            } else if (op.n == 4.0) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_17 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n10: Double = op.n % 10
            if ((mod_n10 == 1.0 || mod_n10 == 2.0) && !(mod_n100 == 11.0 || mod_n100 == 12.0)) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_18 =
        { op: PluralOperand ->
            val mod_i100: Long = op.i % 100
            if (op.i == 1L) {
                PluralCategory.ONE
            } else if (op.i == 0L || contains(
                    mod_i100,
                    2,
                    20
                ) || mod_i100 == 40L || mod_i100 == 60L || mod_i100 == 80L
            ) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_19 =
        { op: PluralOperand ->
            if (op.n == 1.0 || op.n == 5.0 || op.n == 7.0 || op.n == 8.0 || op.n == 9.0 || op.n == 10.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0 || op.n == 3.0) {
                PluralCategory.TWO
            } else if (op.n == 4.0) {
                PluralCategory.FEW
            } else if (op.n == 6.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_20 =
        { op: PluralOperand ->
            if (op.n == 1.0) {
                PluralCategory.ONE
            } else if (op.n == 2.0 || op.n == 3.0) {
                PluralCategory.TWO
            } else if (op.n == 4.0) {
                PluralCategory.FEW
            } else if (op.n == 6.0) {
                PluralCategory.MANY
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_21 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n10: Double = op.n % 10
            if (mod_n10 == 3.0 && mod_n100 != 13.0) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_22 =
        { op: PluralOperand ->
            val mod_n100: Double = op.n % 100
            val mod_n10: Double = op.n % 10
            if ((mod_n10 == 2.0 || mod_n10 == 3.0) && !(mod_n100 == 12.0 || mod_n100 == 13.0)) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_23 =
        { op: PluralOperand ->
            if (op.n == 1.0 || op.n == 5.0) {
                PluralCategory.ONE
            } else {
                PluralCategory.OTHER
            }
        }
    private val _O_RULE_24 =
        { op: PluralOperand ->
            val mod_n10: Double = op.n % 10
            if (mod_n10 == 6.0 || mod_n10 == 9.0 || op.n == 10.0) {
                PluralCategory.FEW
            } else {
                PluralCategory.OTHER
            }
        }

    private fun contains(`val`: Double, min: Double, max: Double): Boolean {
        return `val` >= min && `val` <= max && `val` % 1 == 0.0
    }

    private fun contains(`val`: Int, min: Int, max: Int): Boolean {
        return `val` >= min && `val` <= max
    }

    private fun contains(`val`: Long, min: Long, max: Long): Boolean {
        return `val` >= min && `val` <= max
    }

    fun selectCardinal(
        language: String,
        region: String? = null
    ): ((PluralOperand) -> PluralCategory)? {
        return when (language) {
            "ff" -> _C_RULE_0
            "hy" -> _C_RULE_0
            "kab" -> _C_RULE_0
            "jmc" -> _C_RULE_1
            "nyn" -> _C_RULE_1
            "cgg" -> _C_RULE_1
            "fur" -> _C_RULE_1
            "ps" -> _C_RULE_1
            "gsw" -> _C_RULE_1
            "bem" -> _C_RULE_1
            "kkj" -> _C_RULE_1
            "bal" -> _C_RULE_1
            "hu" -> _C_RULE_1
            "kcg" -> _C_RULE_1
            "vun" -> _C_RULE_1
            "dv" -> _C_RULE_1
            "ug" -> _C_RULE_1
            "tig" -> _C_RULE_1
            "mgo" -> _C_RULE_1
            "nah" -> _C_RULE_1
            "bez" -> _C_RULE_1
            "ssy" -> _C_RULE_1
            "pap" -> _C_RULE_1
            "teo" -> _C_RULE_1
            "haw" -> _C_RULE_1
            "ml" -> _C_RULE_1
            "ee" -> _C_RULE_1
            "mn" -> _C_RULE_1
            "af" -> _C_RULE_1
            "uz" -> _C_RULE_1
            "mr" -> _C_RULE_1
            "el" -> _C_RULE_1
            "brx" -> _C_RULE_1
            "nnh" -> _C_RULE_1
            "eo" -> _C_RULE_1
            "chr" -> _C_RULE_1
            "an" -> _C_RULE_1
            "ve" -> _C_RULE_1
            "eu" -> _C_RULE_1
            "nb" -> _C_RULE_1
            "sdh" -> _C_RULE_1
            "nd" -> _C_RULE_1
            "ne" -> _C_RULE_1
            "az" -> _C_RULE_1
            "vo" -> _C_RULE_1
            "rm" -> _C_RULE_1
            "nn" -> _C_RULE_1
            "no" -> _C_RULE_1
            "nr" -> _C_RULE_1
            "bg" -> _C_RULE_1
            "rwk" -> _C_RULE_1
            "rof" -> _C_RULE_1
            "fo" -> _C_RULE_1
            "ny" -> _C_RULE_1
            "syr" -> _C_RULE_1
            "sd" -> _C_RULE_1
            "xog" -> _C_RULE_1
            "jgo" -> _C_RULE_1
            "kaj" -> _C_RULE_1
            "ka" -> _C_RULE_1
            "seh" -> _C_RULE_1
            "wae" -> _C_RULE_1
            "sn" -> _C_RULE_1
            "so" -> _C_RULE_1
            "sq" -> _C_RULE_1
            "mas" -> _C_RULE_1
            "om" -> _C_RULE_1
            "ss" -> _C_RULE_1
            "kk" -> _C_RULE_1
            "st" -> _C_RULE_1
            "kl" -> _C_RULE_1
            "ce" -> _C_RULE_1
            "saq" -> _C_RULE_1
            "or" -> _C_RULE_1
            "os" -> _C_RULE_1
            "ks" -> _C_RULE_1
            "ku" -> _C_RULE_1
            "ta" -> _C_RULE_1
            "asa" -> _C_RULE_1
            "ky" -> _C_RULE_1
            "xh" -> _C_RULE_1
            "te" -> _C_RULE_1
            "lb" -> _C_RULE_1
            "tk" -> _C_RULE_1
            "ha" -> _C_RULE_1
            "tn" -> _C_RULE_1
            "ckb" -> _C_RULE_1
            "lg" -> _C_RULE_1
            "ksb" -> _C_RULE_1
            "tr" -> _C_RULE_1
            "ts" -> _C_RULE_1
            "ga" -> _C_RULE_2
            "si" -> _C_RULE_3
            "es" -> _C_RULE_4
            "da" -> _C_RULE_5
            "ar" -> _C_RULE_6
            "ars" -> _C_RULE_6
            "ceb" -> _C_RULE_7
            "fil" -> _C_RULE_7
            "tl" -> _C_RULE_7
            "lag" -> _C_RULE_8
            "gd" -> _C_RULE_9
            "mt" -> _C_RULE_10
            "mo" -> _C_RULE_12
            "ro" -> _C_RULE_12
            "smj" -> _C_RULE_13
            "se" -> _C_RULE_13
            "smn" -> _C_RULE_13
            "sma" -> _C_RULE_13
            "sat" -> _C_RULE_13
            "sms" -> _C_RULE_13
            "iu" -> _C_RULE_13
            "smi" -> _C_RULE_13
            "naq" -> _C_RULE_13
            "" -> _C_RULE_14
            "lo" -> _C_RULE_14
            "kea" -> _C_RULE_14
            "yue" -> _C_RULE_14
            "bm" -> _C_RULE_14
            "jv" -> _C_RULE_14
            "bo" -> _C_RULE_14
            "nqo" -> _C_RULE_14
            "jw" -> _C_RULE_14
            "sg" -> _C_RULE_14
            "lkt" -> _C_RULE_14
            "dz" -> _C_RULE_14
            "yo" -> _C_RULE_14
            "root" -> _C_RULE_14
            "wo" -> _C_RULE_14
            // id==in, see https://stackoverflow.com/a/55965008/2611134
            "id" -> _C_RULE_14
            "in" -> _C_RULE_14
            "sah" -> _C_RULE_14
            "ig" -> _C_RULE_14
            "ses" -> _C_RULE_14
            "ii" -> _C_RULE_14
            "su" -> _C_RULE_14
            "km" -> _C_RULE_14
            "ko" -> _C_RULE_14
            "hnj" -> _C_RULE_14
            "ms" -> _C_RULE_14
            "und" -> _C_RULE_14
            "tpi" -> _C_RULE_14
            "my" -> _C_RULE_14
            "kde" -> _C_RULE_14
            "zh" -> _C_RULE_14
            "vi" -> _C_RULE_14
            "th" -> _C_RULE_14
            "jbo" -> _C_RULE_14
            "ja" -> _C_RULE_14
            "to" -> _C_RULE_14
            "osa" -> _C_RULE_14
            "tzm" -> _C_RULE_15
            "shi" -> _C_RULE_16
            "hi" -> _C_RULE_17
            "as" -> _C_RULE_17
            "kn" -> _C_RULE_17
            "fa" -> _C_RULE_17
            "pcm" -> _C_RULE_17
            "zu" -> _C_RULE_17
            "am" -> _C_RULE_17
            "bn" -> _C_RULE_17
            "gu" -> _C_RULE_17
            "doi" -> _C_RULE_17
            "pl" -> _C_RULE_18
            "cy" -> _C_RULE_19
            "sl" -> _C_RULE_20
            "blo" -> _C_RULE_21
            "lt" -> _C_RULE_22
            "ru" -> _C_RULE_23
            "uk" -> _C_RULE_23
            "mk" -> _C_RULE_24
            "is" -> _C_RULE_25
            "br" -> _C_RULE_26
            "cs" -> _C_RULE_27
            "sk" -> _C_RULE_27
            "prg" -> _C_RULE_28
            "lv" -> _C_RULE_28
            "gv" -> _C_RULE_29
            "dsb" -> _C_RULE_30
            "hsb" -> _C_RULE_30
            "de" -> _C_RULE_31
            "sv" -> _C_RULE_31
            "ast" -> _C_RULE_31
            "fi" -> _C_RULE_31
            "sw" -> _C_RULE_31
            "gl" -> _C_RULE_31
            "io" -> _C_RULE_31
            "en" -> _C_RULE_31
            "lij" -> _C_RULE_31
            "et" -> _C_RULE_31
            "sc" -> _C_RULE_31
            // yi==ji, see https://stackoverflow.com/a/55965008/2611134
            "yi" -> _C_RULE_31
            "ji" -> _C_RULE_31
            "fy" -> _C_RULE_31
            "ia" -> _C_RULE_31
            "ur" -> _C_RULE_31
            "nl" -> _C_RULE_31
            "scn" -> _C_RULE_31
            // he==iw, see https://stackoverflow.com/a/55965008/2611134
            "he" -> _C_RULE_32
            "iw" -> _C_RULE_32
            "be" -> _C_RULE_33
            "fr" -> _C_RULE_34
            "kw" -> _C_RULE_35
            "pa" -> _C_RULE_36
            "ln" -> _C_RULE_36
            "ti" -> _C_RULE_36
            "bho" -> _C_RULE_36
            "guw" -> _C_RULE_36
            "ak" -> _C_RULE_36
            "wa" -> _C_RULE_36
            "mg" -> _C_RULE_36
            "nso" -> _C_RULE_36
            "ksh" -> _C_RULE_37
            "bs" -> _C_RULE_38
            "sh" -> _C_RULE_38
            "hr" -> _C_RULE_38
            "sr" -> _C_RULE_38
            "vec" -> _C_RULE_39
            "it" -> _C_RULE_39
            "ca" -> _C_RULE_39
            "pt" -> {
                if ("PT".equals(region)) {
                    _C_RULE_39
                } else {
                    _C_RULE_11
                }
            }

            else -> null
        }
    }

    fun selectOrdinal(
        language: String,
        region: String? = null
    ): ((PluralOperand) -> PluralCategory)? {
        return when (language) {
            "gd" -> _O_RULE_0
            "en" -> _O_RULE_1
            "blo" -> _O_RULE_2
            "mk" -> _O_RULE_3
            "ne" -> _O_RULE_4
            "sq" -> _O_RULE_5
            "or" -> _O_RULE_6
            "sc" -> _O_RULE_7
            "vec" -> _O_RULE_7
            "it" -> _O_RULE_7
            "scn" -> _O_RULE_7
            "hy" -> _O_RULE_8
            "mo" -> _O_RULE_8
            "lo" -> _O_RULE_8
            "vi" -> _O_RULE_8
            "ms" -> _O_RULE_8
            "fil" -> _O_RULE_8
            "tl" -> _O_RULE_8
            "ga" -> _O_RULE_8
            "bal" -> _O_RULE_8
            "fr" -> _O_RULE_8
            "ro" -> _O_RULE_8
            "lij" -> _O_RULE_9
            "" -> _O_RULE_10
            "de" -> _O_RULE_10
            "ps" -> _O_RULE_10
            "ast" -> _O_RULE_10
            "pt" -> _O_RULE_10
            "lt" -> _O_RULE_10
            "gsw" -> _O_RULE_10
            "hr" -> _O_RULE_10
            "lv" -> _O_RULE_10
            "ia" -> _O_RULE_10
            // id==in, see https://stackoverflow.com/a/55965008/2611134
            "id" -> _O_RULE_10
            "in" -> _O_RULE_10
            "ur" -> _O_RULE_10
            "ml" -> _O_RULE_10
            "mn" -> _O_RULE_10
            "prg" -> _O_RULE_10
            "af" -> _O_RULE_10
            "uz" -> _O_RULE_10
            "el" -> _O_RULE_10
            "is" -> _O_RULE_10
            "am" -> _O_RULE_10
            "my" -> _O_RULE_10
            "an" -> _O_RULE_10
            "zh" -> _O_RULE_10
            "es" -> _O_RULE_10
            "et" -> _O_RULE_10
            "eu" -> _O_RULE_10
            "ar" -> _O_RULE_10
            "dsb" -> _O_RULE_10
            "nb" -> _O_RULE_10
            "hsb" -> _O_RULE_10
            "ja" -> _O_RULE_10
            "zu" -> _O_RULE_10
            "fa" -> _O_RULE_10
            "nl" -> _O_RULE_10
            "no" -> _O_RULE_10
            "ru" -> _O_RULE_10
            "fi" -> _O_RULE_10
            "bg" -> _O_RULE_10
            "yue" -> _O_RULE_10
            "bs" -> _O_RULE_10
            "sd" -> _O_RULE_10
            "fy" -> _O_RULE_10
            "sh" -> _O_RULE_10
            "si" -> _O_RULE_10
            "root" -> _O_RULE_10
            "sk" -> _O_RULE_10
            "sl" -> _O_RULE_10
            "sr" -> _O_RULE_10
            "ce" -> _O_RULE_10
            "km" -> _O_RULE_10
            "kn" -> _O_RULE_10
            "ko" -> _O_RULE_10
            "sw" -> _O_RULE_10
            "gl" -> _O_RULE_10
            "und" -> _O_RULE_10
            "tpi" -> _O_RULE_10
            "ta" -> _O_RULE_10
            "ky" -> _O_RULE_10
            "cs" -> _O_RULE_10
            "te" -> _O_RULE_10
            "pa" -> _O_RULE_10
            "th" -> _O_RULE_10
            "pl" -> _O_RULE_10
            "da" -> _O_RULE_10
            // he==iw, see https://stackoverflow.com/a/55965008/2611134
            "he" -> _O_RULE_10
            "iw" -> _O_RULE_10
            "tr" -> _O_RULE_10
            "cy" -> _O_RULE_11
            "kw" -> _O_RULE_12
            "ca" -> _O_RULE_13
            "kk" -> _O_RULE_14
            "az" -> _O_RULE_15
            "mr" -> _O_RULE_16
            "sv" -> _O_RULE_17
            "ka" -> _O_RULE_18
            "as" -> _O_RULE_19
            "bn" -> _O_RULE_19
            "hi" -> _O_RULE_20
            "gu" -> _O_RULE_20
            "uk" -> _O_RULE_21
            "be" -> _O_RULE_22
            "hu" -> _O_RULE_23
            "tk" -> _O_RULE_24
            else -> null
        }
    }
}
