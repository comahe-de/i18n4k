package da.comahe.i18n4k

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.parsing.MessageParseException
import de.comahe.i18n4k.messages.formatter.parsing.MessageParser
import de.comahe.i18n4k.messages.formatter.parsing.MessagePart
import de.comahe.i18n4k.messages.formatter.parsing.MessagePartList
import de.comahe.i18n4k.messages.formatter.parsing.MessagePartParam
import de.comahe.i18n4k.messages.formatter.parsing.MessagePartText
import de.comahe.i18n4k.messages.formatter.parsing.StylePartArgument
import de.comahe.i18n4k.messages.formatter.parsing.StylePartList
import de.comahe.i18n4k.messages.formatter.parsing.StylePartMessage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MessageParserTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
        i18n4kConfig.ignoreMessageParseErrors = false
    }


    @Test
    fun testSimple() {
        assertEquals(
            MessagePartText("Hallo, Welt!"),
            parse("Hallo, Welt!"),
        )
        assertEquals(
            MessagePartText("Hallo, {-.-}!"),
            parse("Hallo, '{-.-}'!"),
        )
    }

    @Test
    fun testEmpty() {
        assertEquals(MessagePartText(""), parse(""))
    }

    @Test
    fun testParameterSimple() {
        assertEquals(
            MessagePartParam("0", null, null),
            parse("{0}"),
        )

        assertEquals(
            MessagePartParam("1", "foo", null),
            parse("{1, foo }"),
        )

        assertEquals(
            MessagePartParam("2", "foo", StylePartArgument("bar")),
            parse("{2, foo , bar }"),
        )

        assertEquals(
            MessagePartParam(
                "~",
                "foobar",
                StylePartList(StylePartArgument("bar"), StylePartArgument("foo"))
            ),
            parse("{~, foobar , bar foo }"),
        )
    }

    @Test
    fun testParameterNameWhitespaces() {
        assertEquals(
            MessagePartParam("a b c", null, null),
            parse("{ a b c }"),
        )
        assertEquals(
            MessagePartParam("a b c", "e d f", null),
            parse("{ a b c , e d f }"),
        )
    }

    @Test
    fun testParameterNameQotes() {
        assertEquals(
            MessagePartParam("a' b' c", "d' e' f", null),
            parse("{ a'' b'' c ,  d'' e'' f }"),
        )
        assertEquals(
            MessagePartParam("a{} b c", "e d{} f", null),
            parse("{ a'{} b' c , e 'd{} 'f }"),
        )
    }

    @Test
    fun testParameterQuotes() {

        assertEquals(
            MessagePartParam("2", "foo", StylePartArgument("bar")),
            parse("{2, foo , 'bar' }"),
        )

        assertEquals(
            MessagePartParam(
                "~",
                "foobar",
                StylePartList(StylePartArgument("bar"), StylePartArgument("foo"))
            ),
            parse("{~, foobar , 'bar' 'foo' }"),
        )

        assertEquals(
            MessagePartParam(
                "~",
                "foobar",
                StylePartList(StylePartArgument(" bar "), StylePartArgument(" foo "))
            ),
            parse("{~, foobar , ' bar ' ' foo ' }"),
        )

        assertEquals(
            MessagePartParam(
                "~",
                "foobar",
                StylePartArgument("bar foo")
            ),
            parse("{~, foobar , 'bar foo' }"),
        )

        assertEquals(
            MessagePartParam(
                "~",
                "foobar",
                StylePartList(
                    StylePartArgument(" "),
                    StylePartArgument("  "),
                    StylePartArgument("{}"),
                )
            ),
            parse("{~, foobar , ' ' '  ' '{}' }"),
        )

        assertEquals(
            MessagePartParam(
                "~",
                "foobar",
                StylePartList(
                    StylePartArgument(" "),
                    StylePartMessage(MessagePartText(" ")),
                    StylePartArgument("  "),
                    StylePartMessage(MessagePartText("")),
                    StylePartArgument("{}"),
                )
            ),
            parse("{~, foobar , ' ' { } '  ' {} '{}' }"),
        )

        assertEquals(
            MessagePartParam(
                "~",
                "foobar",
                StylePartList(
                    StylePartArgument("'"),
                    StylePartArgument("'"),
                    StylePartMessage(MessagePartText("")),
                    StylePartMessage(MessagePartText("'")),
                )
            ),
            parse("{~, foobar , '' '' {} {''} }"),
        )
    }


    @Test
    fun testParameterLists() {
        assertEquals(
            MessagePartList(
                MessagePartParam("0", null, null),
                MessagePartParam("1", "foo", null),
                MessagePartParam("2", "foo", StylePartArgument("bar")),
                MessagePartParam(
                    "~",
                    "foobar",
                    StylePartList(StylePartArgument("bar"), StylePartArgument("foo"))
                ),
            ),
            parse("{0}{1, foo }{2, foo , bar }{~, foobar , bar foo }"),
        )
    }

    @Test
    fun testParameterListsInStyle() {
        assertEquals(
            MessagePartParam(
                "0", "xyz",
                StylePartMessage(
                    MessagePartList(
                        MessagePartParam("1", "foo", null),
                        MessagePartParam("2", "foo", StylePartArgument("bar")),
                        MessagePartParam(
                            "~",
                            "foobar",
                            StylePartList(StylePartArgument("bar"), StylePartArgument("foo"))
                        )
                    ),
                )
            ),
            parse("{0, xyz, {{1, foo }{2, foo , bar }{~, foobar , bar foo }} }")
        )
    }

    @Test
    fun testMixed() {
        assertEquals(
            MessagePartList(
                MessagePartText("A "),
                MessagePartParam("0", null, null),
                MessagePartText(" b "),
                MessagePartParam("1", "foo", null),
                MessagePartText(" c "),
                MessagePartParam("2", "foo", StylePartArgument("bar")),
                MessagePartText(" "),
                MessagePartParam(
                    "~",
                    "foobar",
                    StylePartList(StylePartArgument("bar"), StylePartArgument("foo"))
                ),
                MessagePartText(" d!"),
            ),
            parse("A {0} b {1, foo } c {2, foo , bar } {~, foobar , bar foo } d!"),
        )
    }

    @Test
    fun testMixedInStyle() {
        assertEquals(
            MessagePartList(
                MessagePartText("A "),
                MessagePartParam(
                    "0", "xyz",
                    StylePartList(
                        StylePartArgument("b"),
                        StylePartMessage(MessagePartText(" foo ")),
                        StylePartArgument("c"),
                        StylePartMessage(
                            MessagePartList(
                                MessagePartParam("2", "foo", StylePartArgument("bar")),
                                MessagePartText(" "),
                                MessagePartParam(
                                    "~", "foobar", StylePartList(
                                        StylePartArgument("bar"),
                                        StylePartArgument("foo"),
                                    )
                                ),
                            )
                        ),
                        StylePartArgument("d!"),
                    ),
                ),
                MessagePartText(" ff? "),
            ),
            parse("A {0, xyz, b{ foo } c {{2, foo , bar } {~, foobar , bar foo }}d! } ff? "),
        )
    }

    // TODO: test quotes in style list

    @Test
    fun testStyleNested() {
        assertEquals(
            MessagePartParam(
                "0", "foo",
                StylePartMessage(
                    MessagePartParam(
                        "1", "bar",
                        StylePartMessage(
                            MessagePartParam(
                                "2", "foobar",
                                StylePartArgument("hi!"),
                            )
                        ),
                    )
                ),
            ),
            parse("{0,foo, {{1, bar, {{2, foobar, hi! }}}} }"),
        )
    }

    @Test()
    fun testParameterName() {
        assertEquals(
            setOf<CharSequence>("a b", "c", "e", "f", "'", "g", "{", "}"),
            getParameterNames("{ a b } { c , d } { e' '} { ' 'f} {''} {' g '} {'{'} {'}'}")
        )
    }

    @Test()
    fun testParameterNameComplex() {
        assertEquals(
            setOf<CharSequence>("0", "1", "2", "3", "4", "5", "6"),
            getParameterNames("{0,foo, op1 {{1, bar, {{2, foobar, {{3, zu , {{4}} }} }}  }} op2 {{5, bar3} - {6, foobar3, ' III ' }} }")
        )
    }

    @Test()
    fun testInvalidName() {
        assertFailsWith(MessageParseException::class) { parse("{}") }
        assertFailsWith(MessageParseException::class) { parse("{ }") }
        assertFailsWith(MessageParseException::class) { parse("{' '}") }
    }

    @Test()
    fun testInvalidBraces() {
        assertFailsWith(MessageParseException::class) { parse("{") }
        assertFailsWith(MessageParseException::class) { parse("{ ") }
        assertFailsWith(MessageParseException::class) { parse(" {") }
        assertFailsWith(MessageParseException::class) { parse(" { ") }
        assertFailsWith(MessageParseException::class) { parse("{{}") }
        assertFailsWith(MessageParseException::class) { parse(" { { }") }
        assertFailsWith(MessageParseException::class) { parse(" { } {") }
        assertFailsWith(MessageParseException::class) { parse(" { } { ") }
        assertFailsWith(MessageParseException::class) { parse(" { 0, { , ") }
        assertFailsWith(MessageParseException::class) { parse(" { 0, } , ") }
        assertFailsWith(MessageParseException::class) { parse(" { 0, foo , ") }
        assertFailsWith(MessageParseException::class) { parse(" { 0, foo , {") }
        assertFailsWith(MessageParseException::class) { parse(" { 0, foo , { }") }
        assertFailsWith(MessageParseException::class) { parse(" { 0, foo , { {1}") }
        assertFailsWith(MessageParseException::class) { parse(" { 0, foo , { {1} }") }
    }

    @Test()
    fun testInvalidBraces_ignoreErrors() {
        i18n4kConfig.ignoreMessageParseErrors = true

        assertEquals(MessagePartText(""), parse("{"))
        assertEquals(MessagePartText(""), parse("{ "))
        assertEquals(MessagePartText(" "), parse(" {"))
        assertEquals(MessagePartText(" "), parse(" { "))
        assertEquals(MessagePartText(""), parse("{{}"))
        assertEquals(MessagePartText(" "), parse(" { { }"))
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam("0"),
                MessagePartText(" "),
            ),
            parse(" {0} {")
        )
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam("0"),
                MessagePartText(" "),
            ), parse(" {0} { ")
        )
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", "foo",
                    StylePartArgument(""),
                )
            ),
            parse(" { 0, foo , ")
        )
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", "foo",
                    StylePartMessage(MessagePartText("")),
                )
            ),
            parse(" { 0, foo , {")
        )
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", "foo",
                    StylePartMessage(MessagePartText(" ")),
                )
            ),
            parse(" { 0, foo , { }")
        )
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", "foo",
                    StylePartMessage(MessagePartParam("1")),
                )
            ),
            parse(" { 0, foo , {{1}")
        )

        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", "foo",
                    StylePartMessage(MessagePartParam("1")),
                )
            ),
            parse(" { 0, foo , {{1}}")
        )
    }

    private fun getParameterNames(message: String): Set<CharSequence> {
        val names = mutableSetOf<CharSequence>()
        parse(message).fillInParameterNames(names)
        return names
    }

    private fun parse(message: String): MessagePart {
        return MessageParser(message).parseMessage()
    }
}