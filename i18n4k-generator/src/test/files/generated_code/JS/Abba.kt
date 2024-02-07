import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.MessageBundle
import de.comahe.i18n4k.messages.MessageBundleLocalizedString
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory1
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory10
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory2
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory3
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory4
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory5
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory6
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory7
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory8
import de.comahe.i18n4k.messages.MessageBundleLocalizedStringFactory9
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.Array
import kotlin.Int
import kotlin.String

/**
 * Massage constants for bundle 'Abba'. Generated by i18n4k.
 */
public object Abba : MessageBundle("Abba", "") {
  /**
   * Number
   */
  public val _1_number_key: MessageBundleLocalizedString = getLocalizedString0("1-number-key", 0)

  /**
   * Hello, World
   */
  public val Hello_World_: MessageBundleLocalizedString = getLocalizedString0("Hello World!", 1)

  /**
   * I ^
   * ^have two
   * circumflexes
   * in the text
   */
  public val Multi_Line_Value: MessageBundleLocalizedString =
      getLocalizedString0("Multi-Line-Value", 2)

  /**
   * {0} has forgotten {1, select, female {her} other {his} } {3, select, one {bag} other {{2}
   * bags}}.
   */
  public val forgotten_bags: MessageBundleLocalizedStringFactory4 =
      getLocalizedStringFactory4("forgotten-bags", 3)

  /**
   * {0, capitalize} is the best!
   */
  public val format_pattern: MessageBundleLocalizedStringFactory1 =
      getLocalizedStringFactory1("format-pattern", 4)

  /**
   * Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus faucibus molestie nisl.
   * Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit
   * mauris vel metus. Pellentesque ipsum. Integer malesuada. Fusce wisi. Nullam dapibus fermentum
   * ipsum. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
   * commodo consequat. Integer rutrum, orci vestibulum ullamcorper ultricies, lacus quam ultricies
   * odio, vitae placerat pede sem sit amet enim. Quisque tincidunt scelerisque libero. Etiam posuere
   * lacus quis dolor. In laoreet, magna id viverra tincidunt, sem odio bibendum justo, vel imperdiet
   * sapien wisi sed libero. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet,
   * consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore
   * magnam aliquam quaerat voluptatem. Pellentesque sapien.
   */
  public val longTextExample: MessageBundleLocalizedString = getLocalizedString0("longTextExample",
      5)

  /**
   * 3rd value: {2}
   */
  public val max_param_3: MessageBundleLocalizedStringFactory3 =
      getLocalizedStringFactory3("max_param-3", 6)

  /**
   * En: {0, select, a: {1, select, x: {{4}} other {{5}} } | b: {2, select, y {{6}} other {{7}} } |
   * {3, select, z {{8}} other {{9}} }!
   */
  public val nested_parameter: MessageBundleLocalizedStringFactory10 =
      getLocalizedStringFactory10("nested-parameter", 7)

  /**
   * Not all have translations
   */
  public val no_all_have_translations: MessageBundleLocalizedString =
      getLocalizedString0("no-all-have-translations", 8)

  /**
   * En - Parameter 1: {0}
   */
  public val param_1: MessageBundleLocalizedStringFactory1 = getLocalizedStringFactory1("param_1",
      9)

  /**
   * En - Parameter 10: {0},{1},{2},{3},{4},{5},{6},{7},{8},{9}
   */
  public val param_10: MessageBundleLocalizedStringFactory10 =
      getLocalizedStringFactory10("param_10", 10)

  /**
   * En - Parameter 2: {0},{1}
   */
  public val param_2: MessageBundleLocalizedStringFactory2 = getLocalizedStringFactory2("param_2",
      11)

  /**
   * En - Parameter 3: {0},{1},{2}
   */
  public val param_3: MessageBundleLocalizedStringFactory3 = getLocalizedStringFactory3("param_3",
      12)

  /**
   * En - Parameter 4: {0},{1},{2},{3}
   */
  public val param_4: MessageBundleLocalizedStringFactory4 = getLocalizedStringFactory4("param_4",
      13)

  /**
   * En - Parameter 5: {0},{1},{2},{3},{4}
   */
  public val param_5: MessageBundleLocalizedStringFactory5 = getLocalizedStringFactory5("param_5",
      14)

  /**
   * En - Parameter 6: {0},{1},{2},{3},{4},{5}
   */
  public val param_6: MessageBundleLocalizedStringFactory6 = getLocalizedStringFactory6("param_6",
      15)

  /**
   * En - Parameter 7: {0},{1},{2},{3},{4},{5},{6}
   */
  public val param_7: MessageBundleLocalizedStringFactory7 = getLocalizedStringFactory7("param_7",
      16)

  /**
   * En - Parameter 8: {0},{1},{2},{3},{4},{5},{6},{7}
   */
  public val param_8: MessageBundleLocalizedStringFactory8 = getLocalizedStringFactory8("param_8",
      17)

  /**
   * En - Parameter 9: {0},{1},{2},{3},{4},{5},{6},{7},{8}
   */
  public val param_9: MessageBundleLocalizedStringFactory9 = getLocalizedStringFactory9("param_9",
      18)

  /**
   * This '{0}' is not a parameter because of the '' characters around it.
   */
  public val single_quotes: MessageBundleLocalizedString = getLocalizedString0("single.quotes", 19)

  /**
   * %S%P%T%M%N%L - '{0}' '' '{''}'
   */
  public val special_value: MessageBundleLocalizedString = getLocalizedString0("special-value", 20)

  /**
   * Extraterrestrial 👽
   */
  public val utf_8_value: MessageBundleLocalizedString = getLocalizedString0("utf-8-value", 21)

  /**
   * Special key
   */
  public val _Special_Key_: MessageBundleLocalizedString = getLocalizedString0("~Special#Key~", 22)

  /**
   * Special key 2
   */
  public val _Special__S_P_T_M_N_L_Key2_: MessageBundleLocalizedString =
      getLocalizedString0("~Special-%S%P%T%M%N%L-Key2~", 23)

  init {
    registerTranslation(Abba_en)
    registerTranslation(Abba_en_US)
    registerTranslation(Abba_en_US_texas)
    registerTranslation(Abba_de)
  }
  init {
    registerMessageBundleEntries(_1_number_key, Hello_World_, Multi_Line_Value, forgotten_bags,
        format_pattern, longTextExample, max_param_3, nested_parameter, no_all_have_translations,
        param_1, param_10, param_2, param_3, param_4, param_5, param_6, param_7, param_8, param_9,
        single_quotes, special_value, utf_8_value, _Special_Key_, _Special__S_P_T_M_N_L_Key2_)
  }
}

/**
 * Translation of message bundle 'Abba' for locale 'en'. Generated by i18n4k.
 */
private object Abba_en : MessagesProvider {
  private val _data: Array<String?> = arrayOf(
      "Number",
      "Hello, World",
      """
      |I ^
      |^have two
      |circumflexes
      |in the text
      """.trimMargin(),
      "{0} has forgotten {1, select, female {her} other {his} } {3, select, one {bag} other {{2} bags}}.",
      "{0, capitalize} is the best!",
      "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus faucibus molestie nisl. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Pellentesque ipsum. Integer malesuada. Fusce wisi. Nullam dapibus fermentum ipsum. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Integer rutrum, orci vestibulum ullamcorper ultricies, lacus quam ultricies odio, vitae placerat pede sem sit amet enim. Quisque tincidunt scelerisque libero. Etiam posuere lacus quis dolor. In laoreet, magna id viverra tincidunt, sem odio bibendum justo, vel imperdiet sapien wisi sed libero. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Pellentesque sapien.",
      "3rd value: {2}",
      "En: {0, select, a: {1, select, x: {{4}} other {{5}} } | b: {2, select, y {{6}} other {{7}} } | {3, select, z {{8}} other {{9}} }!",
      "Not all have translations",
      "En - Parameter 1: {0}",
      "En - Parameter 10: {0},{1},{2},{3},{4},{5},{6},{7},{8},{9}",
      "En - Parameter 2: {0},{1}",
      "En - Parameter 3: {0},{1},{2}",
      "En - Parameter 4: {0},{1},{2},{3}",
      "En - Parameter 5: {0},{1},{2},{3},{4}",
      "En - Parameter 6: {0},{1},{2},{3},{4},{5}",
      "En - Parameter 7: {0},{1},{2},{3},{4},{5},{6}",
      "En - Parameter 8: {0},{1},{2},{3},{4},{5},{6},{7}",
      "En - Parameter 9: {0},{1},{2},{3},{4},{5},{6},{7},{8}",
      "This '{0}' is not a parameter because of the '' characters around it.",
      "%S%P%T%M%N%L - '{0}' '' '{''}'",
      "Extraterrestrial 👽",
      "Special key",
      "Special key 2")

  override val locale: Locale = Locale("en")

  override val size: Int
    get() = _data.size

  override fun `get`(index: Int): String? = _data[index]
}

/**
 * Translation of message bundle 'Abba' for locale 'en_US'. Generated by i18n4k.
 */
private object Abba_en_US : MessagesProvider {
  private val _data: Array<String?> = arrayOf(
      null,
      "Hi, World",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null)

  override val locale: Locale = Locale("en",  "US")

  override val size: Int
    get() = _data.size

  override fun `get`(index: Int): String? = _data[index]
}

/**
 * Translation of message bundle 'Abba' for locale 'en_US_texas'. Generated by i18n4k.
 */
private object Abba_en_US_texas : MessagesProvider {
  private val _data: Array<String?> = arrayOf(
      null,
      "Howdy, World",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null)

  override val locale: Locale = Locale("en",  "US",  "texas")

  override val size: Int
    get() = _data.size

  override fun `get`(index: Int): String? = _data[index]
}

/**
 * Translation of message bundle 'Abba' for locale 'de'. Generated by i18n4k.
 */
private object Abba_de : MessagesProvider {
  private val _data: Array<String?> = arrayOf(
      "Nummer",
      "Hallo, Welt!",
      """
      |Ich ^
      |^ habe zwei
      | Zirkumflex
      |im Text
      """.trimMargin(),
      "{0} hat {1, select, female {ihre} other {seine} } {3, select, one {Tasche} other {{2} Taschen}} vergessen.",
      "{0, capitalize} ist das Beste!",
      null,
      "3. Wert: {2}",
      "De: {0, select, a: {1, select, x: {{4}} other {{5}} } | b: {2, select, y {{6}} other {{7}} } | {3, select, z {{8}} other {{9}} }!",
      "",
      "De - Parameter 1: {0}",
      "De - Parameter 10: {0},{1},{2},{3},{4},{5},{6},{7},{8},{9}",
      "De - Parameter 2: {0},{1}",
      "De - Parameter 3: {0},{1},{2}",
      "De - Parameter 4: {0},{1},{2},{3}",
      "De - Parameter 5: {0},{1},{2},{3},{4}",
      "De - Parameter 6: {0},{1},{2},{3},{4},{5}",
      "De - Parameter 7: {0},{1},{2},{3},{4},{5},{6}",
      "De - Parameter 8: {0},{1},{2},{3},{4},{5},{6},{7}",
      "De - Parameter 9: {0},{1},{2},{3},{4},{5},{6},{7},{8}",
      "Dieses '{0}' ist kein Parameter, wegen der ''-Zeichen drumherum.",
      "%S%P%T%M%N%L - '{0}' '' '{''}'",
      "Außerirdischer 👽",
      "Besonderer Schlüssel",
      "Besonderer Schlüssel 2")

  override val locale: Locale = Locale("de")

  override val size: Int
    get() = _data.size

  override fun `get`(index: Int): String? = _data[index]
}
