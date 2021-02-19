package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.strings.LocalizedStringNumber
import de.comahe.i18n4k.strings.LocalizedStringNumber.Companion.getFormattedArea
import de.comahe.i18n4k.strings.LocalizedStringNumber.Companion.getFormattedLength
import de.comahe.i18n4k.strings.LocalizedStringNumber.Companion.getFormattedTimeSpan
import de.comahe.i18n4k.strings.LocalizedStringNumber.Companion.getFormattedWeight
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizedStringNumberTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
        i18n4kConfig.locale = Locale("en")
    }


    @Test
    fun formatEnglishFullTest() {

        assertEquals("1", LocalizedStringNumber(1).toString())
        assertEquals("1", LocalizedStringNumber(1, 2).toString())
        assertEquals("1.00", LocalizedStringNumber(1, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(1, -2).toString())
        assertEquals("1", LocalizedStringNumber(1, 0).toString())

        assertEquals("-1", LocalizedStringNumber(-1).toString())
        assertEquals("-1", LocalizedStringNumber(-1, 2).toString())
        assertEquals("-1.00", LocalizedStringNumber(-1, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(-1, -2).toString())
        assertEquals("-1", LocalizedStringNumber(-1, 0).toString())

        assertEquals("0.1", LocalizedStringNumber(0.1).toString())
        assertEquals("0.1", LocalizedStringNumber(0.1, 2).toString())
        assertEquals("0.10", LocalizedStringNumber(0.1, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(0.1, -2).toString())
        assertEquals("0", LocalizedStringNumber(0.1, 0).toString())

        assertEquals("-0.1", LocalizedStringNumber(-0.1).toString())
        assertEquals("-0.1", LocalizedStringNumber(-0.1, 2).toString())
        assertEquals("-0.10", LocalizedStringNumber(-0.1, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(-0.1, -2).toString())
        assertEquals("0", LocalizedStringNumber(-0.1, 0).toString())


        assertEquals("123", LocalizedStringNumber(123).toString())
        assertEquals("123", LocalizedStringNumber(123, 2).toString())
        assertEquals("123.00", LocalizedStringNumber(123, 2, true).toString())
        assertEquals("100", LocalizedStringNumber(123, -2).toString())
        assertEquals("123", LocalizedStringNumber(123, 0).toString())

        assertEquals("-123", LocalizedStringNumber(-123).toString())
        assertEquals("-123", LocalizedStringNumber(-123, 2).toString())
        assertEquals("-123.00", LocalizedStringNumber(-123, 2, true).toString())
        assertEquals("-100", LocalizedStringNumber(-123, -2).toString())
        assertEquals("-123", LocalizedStringNumber(-123, 0).toString())

        assertEquals("0.123", LocalizedStringNumber(0.123).toString())
        assertEquals("0.12", LocalizedStringNumber(0.123, 2).toString())
        assertEquals("0.12", LocalizedStringNumber(0.123, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(0.123, -2).toString())
        assertEquals("0", LocalizedStringNumber(0.123, 0).toString())

        assertEquals("-0.123", LocalizedStringNumber(-0.123).toString())
        assertEquals("-0.12", LocalizedStringNumber(-0.123, 2).toString())
        assertEquals("-0.12", LocalizedStringNumber(-0.123, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(-0.123, -2).toString())
        assertEquals("0", LocalizedStringNumber(-0.123, 0).toString())

        assertEquals("0.0123", LocalizedStringNumber(0.0123).toString())
        assertEquals("0.01", LocalizedStringNumber(0.0123, 2).toString())
        assertEquals("0.01", LocalizedStringNumber(0.0123, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(0.0123, -2).toString())
        assertEquals("0", LocalizedStringNumber(0.0123, 0).toString())

        assertEquals("-0.0123", LocalizedStringNumber(-0.0123).toString())
        assertEquals("-0.01", LocalizedStringNumber(-0.0123, 2).toString())
        assertEquals("-0.01", LocalizedStringNumber(-0.0123, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(-0.0123, -2).toString())
        assertEquals("0", LocalizedStringNumber(-0.0123, 0).toString())

        assertEquals("0.10203", LocalizedStringNumber(0.10203).toString())
        assertEquals("0.1", LocalizedStringNumber(0.10203, 2).toString())
        assertEquals("0.10", LocalizedStringNumber(0.10203, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(0.10203, -2).toString())
        assertEquals("0", LocalizedStringNumber(0.10203, 0).toString())

        assertEquals("-0.10203", LocalizedStringNumber(-0.10203).toString())
        assertEquals("-0.1", LocalizedStringNumber(-0.10203, 2).toString())
        assertEquals("-0.10", LocalizedStringNumber(-0.10203, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(-0.10203, -2).toString())
        assertEquals("0", LocalizedStringNumber(-0.10203, 0).toString())

        assertEquals("0.000123", LocalizedStringNumber(0.000123).toString())
        assertEquals("0", LocalizedStringNumber(0.000123, 2).toString())
        assertEquals("0.00", LocalizedStringNumber(0.000123, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(0.000123, -2).toString())
        assertEquals("0", LocalizedStringNumber(0.000123, 0).toString())


        assertEquals("-0.000123", LocalizedStringNumber(-0.000123).toString())
        assertEquals("0", LocalizedStringNumber(-0.000123, 2).toString())
        assertEquals("0.00", LocalizedStringNumber(-0.000123, 2, true).toString())
        assertEquals("0", LocalizedStringNumber(-0.000123, -2).toString())
        assertEquals("0", LocalizedStringNumber(-0.000123, 0).toString())

        assertEquals("123.456", LocalizedStringNumber(123.456).toString())
        assertEquals("123.46", LocalizedStringNumber(123.456, 2).toString())
        assertEquals("123.46", LocalizedStringNumber(123.456, 2, true).toString())
        assertEquals("100", LocalizedStringNumber(123.456, -2).toString())
        assertEquals("123", LocalizedStringNumber(123.456, 0).toString())

        assertEquals("-123.456", LocalizedStringNumber(-123.456).toString())
        assertEquals("-123.46", LocalizedStringNumber(-123.456, 2).toString())
        assertEquals("-123.46", LocalizedStringNumber(-123.456, 2, true).toString())
        assertEquals("-100", LocalizedStringNumber(-123.456, -2).toString())
        assertEquals("-123", LocalizedStringNumber(-123.456, 0).toString())

        assertEquals("123,456.789", LocalizedStringNumber(123_456.789).toString())
        assertEquals("123,456.79", LocalizedStringNumber(123_456.789, 2).toString())
        assertEquals("123,456.79", LocalizedStringNumber(123_456.789, 2, true).toString())
        assertEquals("123,500", LocalizedStringNumber(123_456.789, -2).toString())
        assertEquals("123,457", LocalizedStringNumber(123_456.789, 0).toString())

        assertEquals("-123,456.789", LocalizedStringNumber(-123_456.789).toString())
        assertEquals("-123,456.79", LocalizedStringNumber(-123_456.789, 2).toString())
        assertEquals("-123,456.79", LocalizedStringNumber(-123_456.789, 2, true).toString())
        assertEquals("-123,500", LocalizedStringNumber(-123_456.789, -2).toString())
        assertEquals("-123,457", LocalizedStringNumber(-123_456.789, 0).toString())

        assertEquals("9,123,456.789", LocalizedStringNumber(9_123_456.789).toString())
        assertEquals("9,123,456.79", LocalizedStringNumber(9_123_456.789, 2).toString())
        assertEquals("9,123,456.79", LocalizedStringNumber(9_123_456.789, 2, true).toString())
        assertEquals("9,123,500", LocalizedStringNumber(9_123_456.789, -2).toString())
        assertEquals("9,123,457", LocalizedStringNumber(9_123_456.789, 0).toString())

        assertEquals("-9,123,456.789", LocalizedStringNumber(-9_123_456.789).toString())
        assertEquals("-9,123,456.79", LocalizedStringNumber(-9_123_456.789, 2).toString())
        assertEquals("-9,123,456.79", LocalizedStringNumber(-9_123_456.789, 2, true).toString())
        assertEquals("-9,123,500", LocalizedStringNumber(-9_123_456.789, -2).toString())
        assertEquals("-9,123,457", LocalizedStringNumber(-9_123_456.789, 0).toString())

        // max precision = 16
        assertEquals("123,456,789,123.456", LocalizedStringNumber(123_456_789_123.456).toString())
        assertEquals("123,456,789,123,456", LocalizedStringNumber(123_456_789_123_456).toString())
        assertEquals("123.456789123456", LocalizedStringNumber(123.456_789_123_456).toString())
        assertEquals("0.123456789123456", LocalizedStringNumber(0.123_456_789_123_456).toString())

        assertEquals("-123,456,789,123.456", LocalizedStringNumber(-123_456_789_123.456).toString())
        assertEquals("-123,456,789,123,456", LocalizedStringNumber(-123_456_789_123_456).toString())
        assertEquals("-123.456789123456", LocalizedStringNumber(-123.456_789_123_456).toString())
        assertEquals("-0.123456789123456", LocalizedStringNumber(-0.123_456_789_123_456).toString())

        // Exponent notation (1.2e15) currently not supported
        assertEquals("1.23456789e23", LocalizedStringNumber(1234.56789e20).toString())
        assertEquals("1.23456789e23", LocalizedStringNumber(1234.56789e20, 2).toString())
        assertEquals("1.23456789e23", LocalizedStringNumber(1234.56789e20, -2).toString())
        assertEquals("1.23456789e23", LocalizedStringNumber(1234.56789e20, 0).toString())
    }

    @Test
    fun formatGermanTest() {
        i18n4kConfig.locale = Locale("de")

        assertEquals("123.456,789", LocalizedStringNumber(123_456.789).toString())
    }

    @Test
    fun getFormattedLengthTest() {
        assertEquals("100 mm", getFormattedLength(0.1).toString())
        assertEquals("1 mm", getFormattedLength(0.001).toString())
        assertEquals("0.1 mm", getFormattedLength(0.0001).toString())

        assertEquals("-100 mm", getFormattedLength(-0.1).toString())
        assertEquals("-1 mm", getFormattedLength(-0.001).toString())
        assertEquals("-0.1 mm", getFormattedLength(-0.0001).toString())

        assertEquals("1 m", getFormattedLength(1.0).toString())
        assertEquals("100 m", getFormattedLength(100.0).toString())
        assertEquals("10.1 m", getFormattedLength(10.1).toString())

        assertEquals("-1 m", getFormattedLength(-1.0).toString())
        assertEquals("-100 m", getFormattedLength(-100.0).toString())
        assertEquals("-10.1 m", getFormattedLength(-10.1).toString())

        assertEquals("1 km", getFormattedLength(1_000.0).toString())
        assertEquals("1.1 km", getFormattedLength(1_100.0).toString())
        assertEquals("101.01 km", getFormattedLength(101_010.0).toString())

        assertEquals("-1 km", getFormattedLength(-1_000.0).toString())
        assertEquals("-1.1 km", getFormattedLength(-1_100.0).toString())
        assertEquals("-101.01 km", getFormattedLength(-101_010.0).toString())

    }

    @Test
    fun getFormattedAreaTest() {
        assertEquals("1 mm²", getFormattedArea(0.001 * 0.001).toString())
        assertEquals("10.1 mm²", getFormattedArea(0.010_1 * 0.001).toString())
        assertEquals("0.1 mm²", getFormattedArea(0.000_1 * 0.001).toString())

        assertEquals("-1 mm²", getFormattedArea(-0.001 * 0.001).toString())
        assertEquals("-10.1 mm²", getFormattedArea(-0.010_1 * 0.001).toString())
        assertEquals("-0.1 mm²", getFormattedArea(-0.000_1 * 0.001).toString())

        assertEquals("1 m²", getFormattedArea(1.0 * 1.0).toString())
        assertEquals("10.1 m²", getFormattedArea(10.1 * 1.0).toString())
        assertEquals("0.1 m²", getFormattedArea(0.1 * 1.0).toString())

        assertEquals("-1 m²", getFormattedArea(-1.0 * 1.0).toString())
        assertEquals("-10.1 m²", getFormattedArea(-10.1 * 1.0).toString())
        assertEquals("-0.1 m²", getFormattedArea(-0.1 * 1.0).toString())

        assertEquals("1 km²", getFormattedArea(1000.0 * 1000.0).toString())
        assertEquals("10.1 km²", getFormattedArea(10_100.0 * 1000.0).toString())
        assertEquals("0.1 km²", getFormattedArea(100.0 * 1000.0).toString())

        assertEquals("-1 km²", getFormattedArea(-1000.0 * 1000.0).toString())
        assertEquals("-10.1 km²", getFormattedArea(-10_100.0 * 1000.0).toString())
        assertEquals("-0.1 km²", getFormattedArea(-100.0 * 1000.0).toString())
    }

    @Test
    fun getFormattedWeightTest() {
        assertEquals("0.1 mg", getFormattedWeight(0.0001).toString())
        assertEquals("100 mg", getFormattedWeight(0.1).toString())
        assertEquals("1.1 mg", getFormattedWeight(0.0011).toString())

        assertEquals("-0.1 mg", getFormattedWeight(-0.0001).toString())
        assertEquals("-100 mg", getFormattedWeight(-0.1).toString())
        assertEquals("-1.1 mg", getFormattedWeight(-0.0011).toString())

        assertEquals("100 g", getFormattedWeight(100.0).toString())
        assertEquals("1.1 g", getFormattedWeight(1.1).toString())

        assertEquals("-100 g", getFormattedWeight(-100.0).toString())
        assertEquals("-1.1 g", getFormattedWeight(-1.1).toString())

        assertEquals("100 kg", getFormattedWeight(100_000.0).toString())
        assertEquals("1.1 kg", getFormattedWeight(1_100.0).toString())

        assertEquals("-100 kg", getFormattedWeight(-100_000.0).toString())
        assertEquals("-1.1 kg", getFormattedWeight(-1_100.0).toString())

        assertEquals("100 t", getFormattedWeight(100_000_000.0).toString())
        assertEquals("1.1 t", getFormattedWeight(1_100_000.0).toString())

        assertEquals("-100 t", getFormattedWeight(-100_000_000.0).toString())
        assertEquals("-1.1 t", getFormattedWeight(-1_100_000.0).toString())
    }


    @Test
    fun getFormattedTimeSpanTest() {
        assertEquals("0.1 s", getFormattedTimeSpan(0.1).toString())
        assertEquals("1.1 s", getFormattedTimeSpan(1.1).toString())

        assertEquals("1 min", getFormattedTimeSpan(60.0).toString())
        assertEquals("1:01 min", getFormattedTimeSpan(61.1).toString())
        assertEquals("1:59 min", getFormattedTimeSpan(60.0 + 59.0).toString())

        assertEquals("1 h", getFormattedTimeSpan(60.0 * 60.0).toString())
        assertEquals("1:01 h", getFormattedTimeSpan(60.0 * 60.0 + 60.0).toString())
        assertEquals("1:01:01 h", getFormattedTimeSpan(60.0 * 60.0 + 61.1).toString())
        assertEquals("1:01:59 h", getFormattedTimeSpan(60.0 * 60.0 + 60.0 + 59.0).toString())
        assertEquals("1:59 h", getFormattedTimeSpan(60.0 * 60.0 + 59.0 * 60.0).toString())
        assertEquals("1:59:01 h", getFormattedTimeSpan(60.0 * 60.0 + 59.0 * 60.0 + 1.0).toString())
        assertEquals("1:59:59 h", getFormattedTimeSpan(60.0 * 60.0 + 59.0 * 60.0 + 59.0).toString())
    }
}