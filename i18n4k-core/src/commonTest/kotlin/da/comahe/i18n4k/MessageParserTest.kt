package da.comahe.i18n4k

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.parsing.MessageParseException
import de.comahe.i18n4k.messages.formatter.parsing.MessageParser
import de.comahe.i18n4k.messages.formatter.parsing.MessagePart
import de.comahe.i18n4k.messages.formatter.parsing.MessagePartList
import de.comahe.i18n4k.messages.formatter.parsing.MessagePartParam
import de.comahe.i18n4k.messages.formatter.parsing.MessagePartText
import de.comahe.i18n4k.messages.formatter.parsing.StylePartList
import de.comahe.i18n4k.messages.formatter.parsing.StylePartNamed
import de.comahe.i18n4k.messages.formatter.parsing.StylePartSimple
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
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
            parse("Hallo, Welt!"),
            MessagePartText("Hallo, Welt!")
        )
        assertEquals(
            parse("Hallo, '{-.-}'!"),
            MessagePartText("Hallo, {-.-}!")
        )
    }

    @Test
    fun testParameterSimple() {
        assertEquals(
            MessagePartParam(0, null, null),
            parse("{0}"),
        )

        assertEquals(
            MessagePartParam(1, "foo", null),
            parse("{1, foo }"),
        )

        assertEquals(
            MessagePartParam(2, "foo", StylePartSimple(MessagePartText("bar"))),
            parse("{2, foo , bar }"),
        )

        assertEquals(
            MessagePartParam(null, "foobar", StylePartSimple(MessagePartText("bar foo"))),
            parse("{~, foobar , bar foo }"),
        )
    }

    @Test
    fun testParameterLists() {
        assertEquals(
            MessagePartList(
                list = persistentListOf(
                    MessagePartParam(0, null, null),
                    MessagePartParam(1, "foo", null),
                    MessagePartParam(2, "foo", StylePartSimple(MessagePartText("bar"))),
                    MessagePartParam(null, "foobar", StylePartSimple(MessagePartText("bar foo"))),
                )
            ),
            parse("{0}{1, foo }{2, foo , bar }{~, foobar , bar foo }"),
        )
    }

    @Test
    fun testParameterListsInStyle() {
        assertEquals(
            MessagePartParam(
                0, "xyz", StylePartSimple(
                    MessagePartList(
                        list = persistentListOf(
                            MessagePartParam(1, "foo", null),
                            MessagePartParam(2, "foo", StylePartSimple(MessagePartText("bar"))),
                            MessagePartParam(
                                null,
                                "foobar",
                                StylePartSimple(MessagePartText("bar foo"))
                            ),
                        )
                    )
                )
            ),
            parse("{0, xyz, {1, foo }{2, foo , bar }{~, foobar , bar foo }"),
        )
    }

    @Test
    fun testMixed() {
        assertEquals(
            MessagePartList(
                list = persistentListOf(
                    MessagePartText("A "),
                    MessagePartParam(0, null, null),
                    MessagePartText(" b "),
                    MessagePartParam(1, "foo", null),
                    MessagePartText(" c "),
                    MessagePartParam(2, "foo", StylePartSimple(MessagePartText("bar"))),
                    MessagePartText(" "),
                    MessagePartParam(null, "foobar", StylePartSimple(MessagePartText("bar foo"))),
                    MessagePartText(" d!"),
                )
            ),
            parse("A {0} b {1, foo } c {2, foo , bar } {~, foobar , bar foo } d!"),
        )
    }

    @Test
    fun testMixedInStyle() {
        assertEquals(
            MessagePartList(
                persistentListOf(
                    MessagePartText("A "),
                    MessagePartParam(
                        0, "xyz",
                        StylePartSimple(
                            MessagePartList(
                                persistentListOf(
                                    MessagePartText("b "),
                                    MessagePartParam(1, "foo", null),
                                    MessagePartText(" c "),
                                    MessagePartParam(
                                        2,
                                        "foo",
                                        StylePartSimple(MessagePartText("bar"))
                                    ),
                                    MessagePartText(" "),
                                    MessagePartParam(
                                        null,
                                        "foobar",
                                        StylePartSimple(MessagePartText("bar foo"))
                                    ),
                                    MessagePartText(" d!"),
                                )
                            )
                        ),
                    ),
                    MessagePartText(" ff? "),
                )
            ),
            parse("A {0, xyz, b {1, foo } c {2, foo , bar } {~, foobar , bar foo } d! } ff? "),
        )
    }

    @Test
    fun testStyleList() {
        assertEquals(
            MessagePartParam(
                0, "foo",
                StylePartList(
                    persistentListOf(
                        StylePartSimple(MessagePartText("bar1")),
                        StylePartNamed(persistentSetOf("foo2"), MessagePartText("bar2")),
                        StylePartNamed(persistentSetOf("foo3", "foo3a"), MessagePartText("bar3")),
                    )
                ),
            ),
            parse("{0,foo, bar1 | foo2: bar2 | foo3 / foo3a: bar3}"),
        )
    }

    @Test
    fun testStyleListWithQuotes() {
        assertEquals(
            MessagePartParam(
                0, "foo,b'ar'",
                StylePartList(
                    persistentListOf(
                        StylePartSimple(MessagePartText("{0}bar1:|")),
                        StylePartNamed(persistentSetOf("foo'2,"), MessagePartText("bar'2|")),
                        StylePartNamed(
                            persistentSetOf("foo3/''|{", "bar3:"),
                            MessagePartText(":bar3'-T")
                        ),
                    )
                ),
            ),
            parse("{0,foo','b''ar'' , '{0}'bar1':|'| foo''2',': bar''2'|'|'foo3/''''|{' / ' bar3:' :':'bar3''-T }"),
        )
    }

    @Test
    fun testStyleNested() {
        assertEquals(
            MessagePartParam(
                0, "foo",
                StylePartSimple(
                    MessagePartParam(
                        1, "bar",
                        StylePartSimple(
                            MessagePartParam(
                                2, "foobar",
                                StylePartSimple(MessagePartText("hi!")),
                            )
                        ),
                    )
                ),
            ),
            parse("{0,foo, {1, bar, {2, foobar, hi! } }"),
        )
    }

    @Test
    fun testStyleNested2() {
        assertEquals(
            MessagePartParam(
                0, "foo",
                StylePartList(
                    persistentListOf(
                        StylePartNamed(
                            persistentSetOf("op1"),
                            MessagePartParam(
                                1, "bar",
                                StylePartSimple(
                                    MessagePartParam(
                                        2, "foobar",
                                        StylePartSimple(MessagePartText("hi!")),
                                    )
                                ),
                            )
                        ),
                        StylePartNamed(
                            persistentSetOf("op2"),
                            MessagePartList(
                                persistentListOf(
                                    MessagePartParam(
                                        3, "bar3", null,
                                    ),
                                    MessagePartText(" - "),
                                    MessagePartParam(
                                        4, "foobar3",
                                        StylePartSimple(MessagePartText(" III ")),
                                    )
                                )
                            )
                        ),
                    )
                ),
            ),
            parse("{0,foo, op1: {1, bar, {2, foobar, hi! }  } | op2:  {3, bar3} - {4, foobar3, ' III ' }   }"),
        )
    }


    @Test()
    fun testMaxParameterIndex() {
        assertEquals(
            6,
            parse("{0,foo, op1: {1, bar, {2, foobar, {3, zu , {4} } }  } | op2:  {5, bar3} - {6, foobar3, ' III ' }   }").maxParameterIndex
        )
        assertEquals(
            6,
            parse("{5,foo, op1: {6, bar, {2, foobar, {3, zu , {4} } }  } | op2:  {0, bar3} - {1, foobar3, ' III ' }   }").maxParameterIndex
        )
        assertEquals(
            6,
            parse("{3,foo, op1: {4, bar, {1, foobar, {6, zu , {0} } }  } | op2:  {2, bar3} - {1, foobar3, ' III ' }   }").maxParameterIndex
        )
        assertEquals(
            6,
            parse("{6,foo, op1: {0, bar, {5, foobar, {4, zu , {3} } }  } | op2:  {1, bar3} - {2, foobar3, ' III ' }   }").maxParameterIndex
        )
    }

    @Test()
    fun testInvalidIndex() {
        assertFailsWith(MessageParseException::class) { parse("{}") }
        assertFailsWith(MessageParseException::class) { parse("{a}") }
        assertFailsWith(MessageParseException::class) { parse("{.}") }
        assertFailsWith(MessageParseException::class) { parse("{1a}") }
        assertFailsWith(MessageParseException::class) { parse("{-1}") }
    }

    private fun parse(message: String): MessagePart {
        return MessageParser(message).parseMessage()
    }
}