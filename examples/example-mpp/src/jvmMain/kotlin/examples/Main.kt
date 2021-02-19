package examples

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.provider.MessagesProviderViaResource
import de.comahe.i18n4k.toTag
import net.miginfocom.swing.MigLayout
import x.y.MyMessages
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.WindowConstants
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

fun main(args: Array<String>) {
    val i18n4kConfig = I18n4kConfigDefault()
    i18n4k = i18n4kConfig

    val myFrame = JFrame("Example JVM")
    myFrame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    myFrame.setSize(500, 500);
    myFrame.layout = MigLayout("wrap 1, fillx", "[grow]")

    // load "fr" and "nl" at runtime
    MyMessages.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/MyMessages_fr.i18n4k.txt"))
    MyMessages.registerTranslation(MessagesProviderViaResource(pathToResource = "/x/y/MyMessages_nl.i18n4k.txt"))


    val label1 = JLabel("")
    val label2 = JLabel("");
    val textField = JTextField()
    val buttons = mutableMapOf<Locale, JButton>()

    val updateUI = {
        label1.text = MyMessages.title()
        label2.text = MyMessages.sayHello(textField.text)
        buttons.forEach {
            it.value.isSelected = it.key == i18n4kConfig.locale
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
    textField.document.addDocumentListener(object : DocumentListener {
        override fun insertUpdate(e: DocumentEvent?) {
            updateUI()
        }

        override fun removeUpdate(e: DocumentEvent?) {
            updateUI()
        }

        override fun changedUpdate(e: DocumentEvent?) {
            updateUI()
        }

    })
    updateUI()

// add components
    myFrame.add(label1, "growx")
    myFrame.add(label2, "growx")
    myFrame.add(textField, "growx")

    buttons.values.forEachIndexed { index, button ->
        myFrame.add(button, if (index == 0) "split ${buttons.size}" else "")
    }


    myFrame.setVisible(true)
}