import de.comahe.i18n4k.config.I18n4kConfigDelegate
import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.provider.MessagesProviderFactoryViaFetch
import de.comahe.i18n4k.toTag
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.h1
import react.dom.input
import x.y.MyMessages

external interface WelcomeProps : RProps {
    var name: String
}

data class WelcomeState(
    val name: String,
    val i18n4k: I18n4kConfigImmutable
) : RState

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {

    init {
        state = WelcomeState(props.name, I18n4kConfigImmutable())
        i18n4k = I18n4kConfigDelegate { state.i18n4k }

        // load "fr" and "nl" at runtime...
        MyMessages.registerTranslationFactory(
            MessagesProviderFactoryViaFetch(
                pathToResource = "x/y/MyMessages_fr.i18n4k.txt",
                onLoaded = this::refresh
            )
        )
        MyMessages.registerTranslationFactory(
            MessagesProviderFactoryViaFetch(
                pathToResource = "x/y/MyMessages_nl.i18n4k.txt",
                onLoaded = this::refresh
            )
        )
    }

    private fun refresh() {
        setState(
            WelcomeState(
                name = state.name,
                i18n4k = state.i18n4k
            )
        )
    }

    override fun RBuilder.render() {
        h1 {
            +MyMessages.title()
        }
        div {
            +MyMessages.sayHello(state.name)
        }
        input {
            attrs {
                type = InputType.text
                value = state.name
                onChangeFunction = { event ->
                    setState(
                        WelcomeState(
                            name = (event.target as HTMLInputElement).value,
                            i18n4k = state.i18n4k
                        )
                    )
                }
            }
        }
        div {
            MyMessages.locales.forEach { locale ->
                button {
                    +locale.getDisplayNameInLocale()
                    +(if (state.i18n4k.locale == locale) " # " else "")
                    attrs {
                        onClickFunction = { _ ->
                            setState(
                                WelcomeState(
                                    name = state.name,
                                    i18n4k = state.i18n4k.withLocale(locale)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
