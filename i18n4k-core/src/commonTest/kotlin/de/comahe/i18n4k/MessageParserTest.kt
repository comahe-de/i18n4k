package de.comahe.i18n4k

import de.comahe.i18n4k.config.I18n4kConfigDefault
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
            MessagePartParam("0", null, null, null),
            parse("{0}"),
        )

        assertEquals(
            MessagePartParam("1", null, "foo", null),
            parse("{1, foo }"),
        )

        assertEquals(
            MessagePartParam("2", null, "foo", StylePartArgument("bar")),
            parse("{2, foo , bar }"),
        )

        assertEquals(
            MessagePartParam(
                "~", null,
                "foobar",
                StylePartList(StylePartArgument("bar"), StylePartArgument("foo"))
            ),
            parse("{~, foobar , bar foo }"),
        )
    }

    @Test
    fun testParameterNameWhitespaces() {
        assertEquals(
            MessagePartParam("a b c", null, null, null),
            parse("{ a b c }"),
        )
        assertEquals(
            MessagePartParam("a b c", null, "e d f", null),
            parse("{ a b c , e d f }"),
        )
    }

    @Test
    fun testParameterNameQotes() {
        assertEquals(
            MessagePartParam("a' b' c", null, "d' e' f", null),
            parse("{ a'' b'' c ,  d'' e'' f }"),
        )
        assertEquals(
            MessagePartParam("a{} b c", null, "e d{} f", null),
            parse("{ a'{} b' c , e 'd{} 'f }"),
        )
    }

    @Test
    fun testParameterQuotes() {

        assertEquals(
            MessagePartParam("2", null, "foo", StylePartArgument("bar")),
            parse("{2, foo , 'bar' }"),
        )

        assertEquals(
            MessagePartParam(
                "~", null,
                "foobar",
                StylePartList(StylePartArgument("bar"), StylePartArgument("foo"))
            ),
            parse("{~, foobar , 'bar' 'foo' }"),
        )

        assertEquals(
            MessagePartParam(
                "~", null,
                "foobar",
                StylePartList(StylePartArgument(" bar "), StylePartArgument(" foo "))
            ),
            parse("{~, foobar , ' bar ' ' foo ' }"),
        )

        assertEquals(
            MessagePartParam(
                "~", null,
                "foobar",
                StylePartArgument("bar foo")
            ),
            parse("{~, foobar , 'bar foo' }"),
        )

        assertEquals(
            MessagePartParam(
                "~", null,
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
                "~", null,
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
                "~", null,
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
                MessagePartParam("0", null, null, null),
                MessagePartParam("1", null, "foo", null),
                MessagePartParam("2", null, "foo", StylePartArgument("bar")),
                MessagePartParam(
                    "~", null,
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
                "0", null, "xyz",
                StylePartMessage(
                    MessagePartList(
                        MessagePartParam("1", null, "foo", null),
                        MessagePartParam("2", null, "foo", StylePartArgument("bar")),
                        MessagePartParam(
                            "~", null,
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
                MessagePartParam("0", null, null, null),
                MessagePartText(" b "),
                MessagePartParam("1", null, "foo", null),
                MessagePartText(" c "),
                MessagePartParam("2", null, "foo", StylePartArgument("bar")),
                MessagePartText(" "),
                MessagePartParam(
                    "~", null,
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
                    "0", null, "xyz",
                    StylePartList(
                        StylePartArgument("b"),
                        StylePartMessage(MessagePartText(" foo ")),
                        StylePartArgument("c"),
                        StylePartMessage(
                            MessagePartList(
                                MessagePartParam("2", null, "foo", StylePartArgument("bar")),
                                MessagePartText(" "),
                                MessagePartParam(
                                    "~", null, "foobar", StylePartList(
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
                "0", null, "foo",
                StylePartMessage(
                    MessagePartParam(
                        "1", null, "bar",
                        StylePartMessage(
                            MessagePartParam(
                                "2", null, "foobar",
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
            mapOf<CharSequence, CharSequence?>(
                "a b" to null,
                "c" to null,
                "e" to null,
                "f" to null,
                "'" to null,
                "g" to null,
                "{" to null,
                "}" to null
            ),
            getParameterNames("{ a b } { c , d } { e' '} { ' 'f} {''} {' g '} {'{'} {'}'}")
        )
    }

    @Test()
    fun testParameterNameComplex() {
        assertEquals(
            mapOf<CharSequence, CharSequence?>(
                "0" to null,
                "1" to null,
                "2" to null,
                "3" to null,
                "4" to null,
                "5" to null,
                "6" to null
            ),
            getParameterNames("{0,foo, op1 {{1, bar, {{2, foobar, {{3, zu , {{4}} }} }}  }} op2 {{5, bar3} - {6, foobar3, ' III ' }} }")
        )
    }

    @Test()
    fun testParameterNameWithValueType() {
        assertEquals(
            mapOf<CharSequence, CharSequence?>(
                "a b" to "A B",
                "c" to "C",
                "e" to "E",
                "f" to "F",
                "'" to "'",
                "g:G" to "G:g",
                "{" to "}",
                "}" to "{"
            ),
            getParameterNames("{ a b : A B} { c : C , d : D } { e' ': E' '} { ' 'f: ' 'F} {'':''} {' g:G ': ' G:g '} {'{':'}'} {'}':'{'}")
        )
    }

    @Test()
    fun testParameterNameComplexWithValueClass() {
        assertEquals(
            mapOf<CharSequence, CharSequence?>(
                "0" to "A",
                "1" to "B",
                "2" to "C",
                "3" to "D",
                "4" to "E",
                "5" to "F",
                "6" to "G"
            ),
            getParameterNames("{0 :A ,foo, op1 {{1:B, bar, {{2:C, foobar, {{3:D, zu , {{4:E}} }} }}  }} op2 {{5:F, bar3} - {6:G, foobar3, ' III ' }} }")
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
                    "0", null, "foo",
                    StylePartArgument(""),
                )
            ),
            parse(" { 0, foo , ")
        )
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", null, "foo",
                    StylePartMessage(MessagePartText("")),
                )
            ),
            parse(" { 0, foo , {")
        )
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", null, "foo",
                    StylePartMessage(MessagePartText(" ")),
                )
            ),
            parse(" { 0, foo , { }")
        )
        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", null, "foo",
                    StylePartMessage(MessagePartParam("1")),
                )
            ),
            parse(" { 0, foo , {{1}")
        )

        assertEquals(
            MessagePartList(
                MessagePartText(" "),
                MessagePartParam(
                    "0", null, "foo",
                    StylePartMessage(MessagePartParam("1")),
                )
            ),
            parse(" { 0, foo , {{1}}")
        )
    }

    @Test()
    fun testInvalidSingleQuotes() {
        assertFailsWith(MessageParseException::class) { parse("'") }
        assertFailsWith(MessageParseException::class) { parse("'''") }
        assertFailsWith(MessageParseException::class) { parse("'Hi") }
        assertFailsWith(MessageParseException::class) { parse("H'i") }
        assertFailsWith(MessageParseException::class) { parse("Hi'") }

        assertFailsWith(MessageParseException::class) { parse("{0, foo, '}") }
    }

    @Test()
    fun testInvalidSingleQuotes_ignoreErrors() {
        i18n4kConfig.ignoreMessageParseErrors = true

        assertEquals(MessagePartText(""), parse("'"))
        assertEquals(MessagePartText("'"), parse("'''"))
        assertEquals(MessagePartText("Hi"), parse("'Hi"))
        assertEquals(MessagePartText("Hi"), parse("H'i"))
        assertEquals(MessagePartText("Hi"), parse("Hi'"))

        assertEquals(
            MessagePartParam(
                "0", null, "foo",
                StylePartArgument("}"),
            ),
            parse("{0, foo, '}")
        )
    }

    @Test
    fun testValueTypesSimple() {
        assertEquals(
            MessagePartParam("0", "Int", null, null),
            parse("{0:Int}"),
        )

        assertEquals(
            MessagePartParam("0", null, null, null),
            parse("{0: }"),
        )


        assertEquals(
            MessagePartParam("1", "Bool", "foo", null),
            parse("{1 : Bool, foo }"),
        )

        assertEquals(
            MessagePartParam("2", "Short", "foo", StylePartArgument("bar")),
            parse("{ 2: Short, foo , bar }"),
        )

        assertEquals(
            MessagePartParam(
                "~", "String",
                "foobar",
                StylePartList(StylePartArgument("bar"), StylePartArgument("foo"))
            ),
            parse("{~ : String , foobar , bar foo }"),
        )
    }

    @Test
    fun testStyleNestedWithTypesClass() {
        assertEquals(
            MessagePartParam(
                "0", "Int", "foo",
                StylePartMessage(
                    MessagePartParam(
                        "1", "Bool", "bar",
                        StylePartMessage(
                            MessagePartParam(
                                "2", "Short", "foobar",
                                StylePartArgument("hi!"),
                            )
                        ),
                    )
                ),
            ),
            parse("{0:Int,foo, {{1: Bool , bar, {{2 : Short, foobar, hi! }}}} }"),
        )
    }

    private fun getParameterNames(message: String): Map<CharSequence, CharSequence?> {
        val names = mutableListOf<Pair<CharSequence, CharSequence?>>()
        parse(message).fillInParameterNames(names)
        return names.associateBy({ it.first }, { it.second })
    }

    private fun parse(message: String): MessagePart {
        return MessageParser(message).parseMessage()
    }
}