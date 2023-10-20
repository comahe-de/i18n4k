import de.comahe.i18n4k.config.I18n4kConfigDelegateObject
import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.messages.provider.MessagesProviderFactoryViaFetch
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.input
import react.useState
import x.y.MyMessages

external interface WelcomeProps : Props {
    var name: String
    var i18n4k: I18n4kConfigImmutable
    // needed to change the state when new locales are loaded to trigger a refresh
    var countLocales: Int
    var init: Boolean
}

val i18n4kConfig = I18n4kConfigDelegateObject(I18n4kConfigImmutable())

val Welcome = FC<WelcomeProps> { props ->

    var name: String by useState(props.name)

    var i18n4kProp: I18n4kConfigImmutable by useState(props.i18n4k)
    i18n4kConfig.configProvider = i18n4kProp
    println("# Current locale: " + i18n4kProp.locale)


    h1 {
        +MyMessages.title()
    }
    div {
        +MyMessages.sayHello(name)
    }
    input {
        type = InputType.text
        value = name
        onChange = { event ->
            name = event.target.value
        }
    }

    div {
        MyMessages.locales.forEach { locale ->
            button {
                +locale.getDisplayNameInLocale()
                +(if (i18n4kProp.locale.language == locale.language) " # " else "")
                onClick = { _ ->
                    i18n4kProp = i18n4kProp.withLocale(locale)
                }
            }
        }
    }

    var init: Boolean by useState(props.init)
    var countLocales by useState(props.countLocales)
    if (init) {
        // load "fr", "nl" and "en_US_texas" at runtime...
        MyMessages.registerTranslationFactory(
            MessagesProviderFactoryViaFetch(
                pathToResource = "x/y/MyMessages_fr.i18n4k.txt",
                onLoaded = { countLocales = MyMessages.locales.size }
            )
        )
        MyMessages.registerTranslationFactory(
            MessagesProviderFactoryViaFetch(
                pathToResource = "x/y/MyMessages_nl.i18n4k.txt",
                onLoaded = { countLocales = MyMessages.locales.size }
            )
        )
        MyMessages.registerTranslationFactory(
            MessagesProviderFactoryViaFetch(
                pathToResource = "x/y/MyMessages_en_US_texas.i18n4k.txt",
                onLoaded = { countLocales = MyMessages.locales.size }
            )
        )
        init = false
    }
}

