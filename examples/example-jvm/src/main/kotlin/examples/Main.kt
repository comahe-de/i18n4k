package examples

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.i18n4kInitCldrPluralRules
import de.comahe.i18n4k.messages.provider.MessagesProviderViaResource
import de.comahe.i18n4k.strings.LocalizedString
import net.miginfocom.swing.MigLayout
import x.y.Actions
import x.y.MyMessages
import x.y.Things
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JSeparator
import javax.swing.JTextField
import javax.swing.WindowConstants
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

fun main(args: Array<String>) {
    val i18n4kConfig = I18n4kConfigDefault()
    i18n4k = i18n4kConfig

    // initialise the lib "i18n4k-cldr-plural-rules"
    i18n4kInitCldrPluralRules()

    val myFrame = JFrame("Example JVM")
    myFrame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    myFrame.setSize(1000, 800);
    myFrame.layout = MigLayout("wrap 1, fillx", "[grow]")

    // load "fr", "nl" and "en_US_texas" at runtime
    MyMessages.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/MyMessages_fr.i18n4k.txt"))
    MyMessages.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/MyMessages_nl.i18n4k.txt"))
    MyMessages.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/MyMessages_en_US_texas.i18n4k.txt"))
    Actions.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/Actions_fr.i18n4k.txt"))
    Actions.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/Actions_nl.i18n4k.txt"))
    Things.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/Things_fr.i18n4k.txt"))
    Things.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/Things_fr_x_attr-gender.i18n4k.txt"))
    Things.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/Things_nl.i18n4k.txt"))
    Things.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/Things_nl_x_attr-gender.i18n4k.txt"))

    val labelTitle = JLabel("")
    val labelHello = JLabel("")
    val textHello = JTextField()

    val labelSelectThing = JLabel("")
    val labelCountOfShapes = JLabel("")
    val labelOrdinalOfShape = JLabel("")
    val labelThingChangedShape = JLabel("")
    val labelThingShapeCount = JLabel("")
    val labelThingShapeOrder = JLabel("")
    val labelAttrValue = JLabel("")
    val labelAttrSelect = JLabel("")

    val comboThings = JComboBox<LocalizedString>()
    val textCountOfShapes = JTextField()
    val textOrdinalOfShape = JTextField()

    val buttons = mutableMapOf<Locale, JButton>()

    comboThings.addItem(Things.sun)
    comboThings.addItem(Things.moon)
    comboThings.addItem(Things.water)
    comboThings.addItem(Things.boy)
    comboThings.addItem(Things.girl)

    val updateUI = {
        buttons.forEach {
            it.value.isSelected = it.key == i18n4kConfig.locale
        }

        labelTitle.text = MyMessages.title()
        labelHello.text = MyMessages.sayHello(textHello.text)

        labelSelectThing.text = Actions.select_thing()
        labelCountOfShapes.text = Actions.count_of_shapes()
        labelOrdinalOfShape.text = Actions.ordinal_of_shape()

        val selectedThing = comboThings.selectedItem
        val count = textCountOfShapes.text
        val ordinal = textOrdinalOfShape.text

        if (selectedThing == null) {
            labelThingChangedShape.text = ""
            labelThingShapeCount.text = ""
            labelThingShapeOrder.text = ""
            labelAttrValue.text = ""
            labelAttrSelect.text = ""
        } else {
            labelThingChangedShape.text = Actions.change_shape(selectedThing)
            labelThingShapeCount.text = Actions.shape_count(count, selectedThing)
            labelThingShapeOrder.text = Actions.shape_order(ordinal, selectedThing)

            labelAttrValue.text = Actions.attr_value(selectedThing)
            labelAttrSelect.text = Actions.attr_select(selectedThing)
        }

        comboThings.invalidate()
        comboThings.doLayout()

    }
    val documentListener = object : DocumentListener {
        override fun insertUpdate(e: DocumentEvent?) {
            updateUI()
        }

        override fun removeUpdate(e: DocumentEvent?) {
            updateUI()
        }

        override fun changedUpdate(e: DocumentEvent?) {
            updateUI()
        }
    }

    for (locale in MyMessages.locales) {
        val button = JButton(locale.getDisplayNameInLocale())
        button.addActionListener {
            i18n4kConfig.locale = locale
            updateUI()
        }
        buttons[locale] = button
    }

    // simple text field

    textHello.document.addDocumentListener(documentListener)
    textCountOfShapes.document.addDocumentListener(documentListener)
    textOrdinalOfShape.document.addDocumentListener(documentListener)
    comboThings.addActionListener { updateUI() }

    updateUI()

    // plural and gender example

    // add components
    buttons.values.forEachIndexed { index, button ->
        myFrame.add(button, if (index == 0) "split ${buttons.size}" else "")
    }


    myFrame.add(JSeparator(), "growx")

    myFrame.add(labelTitle, "growx")
    myFrame.add(labelHello, "growx")
    myFrame.add(textHello, "growx")

    myFrame.add(JSeparator(), "growx")


    myFrame.add(labelSelectThing, "growx")
    myFrame.add(comboThings, "growx")

    myFrame.add(labelCountOfShapes, "split 2")
    myFrame.add(textCountOfShapes, "growx")

    myFrame.add(labelOrdinalOfShape, "split 2")
    myFrame.add(textOrdinalOfShape, "growx")

    myFrame.add(labelThingChangedShape, "growx")
    myFrame.add(labelThingShapeCount, "growx")
    myFrame.add(labelThingShapeOrder, "growx")


    myFrame.add(labelAttrValue, "growx")
    myFrame.add(labelAttrSelect, "growx")

    myFrame.setVisible(true)
}