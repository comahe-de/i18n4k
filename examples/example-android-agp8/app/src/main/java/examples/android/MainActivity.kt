package examples.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import com.example.myapplication2.ui.theme.MyApplication2Theme
import de.comahe.i18n4k.config.I18n4kConfigDelegate
import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.i18n4kInitCldrPluralRules
import de.comahe.i18n4k.messages.providers.MessagesProviderViaText
import x.y.MyMessages

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initI18n4k(this)

        setContent {
            var text by remember { mutableStateOf("i18n4k") }
            var count by remember { mutableStateOf(1) }

            MyApplication2Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {

                        Row {
                            Text(
                                fontWeight = FontWeight.Bold,
                                text = MyMessages.title(),
                            )
                        }
                        Row {
                            Text(
                                text = MyMessages.sayHello(text)
                            )
                        }
                        Row {
                            TextField(
                                value = text,
                                onValueChange = { text = it }
                            )

                        }
                        Row {
                            Text(
                                text = MyMessages.bottles(count)
                            )
                        }
                        Row {
                            TextField(
                                value = count.toString(),
                                onValueChange = { count = it.toIntOrNull() ?: 0 }
                            )

                        }
                        for (locale in MyMessages.locales) {
                            Row (verticalAlignment = Alignment.CenterVertically){
                                RadioButton(
                                    selected = i18n4kConfig.value.locale == locale,
                                    onClick = { i18n4kConfig.value = i18n4kConfig.value.withLocale(locale) })
                                Text(
                                    text = buildAnnotatedString {
                                        withLink(
                                            link = LinkAnnotation.Clickable(
                                                tag = "language",
                                                styles = TextLinkStyles(style = SpanStyle()),
                                                linkInteractionListener = {
                                                    i18n4kConfig.value =
                                                        i18n4kConfig.value.withLocale(locale)
                                                }
                                            )
                                        ) {
                                            append(locale.getDisplayNameInLocale())
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }

                    }
                }
            }
        }
    }

    companion object {
        private var inited = false

        var i18n4kConfig = mutableStateOf(I18n4kConfigImmutable())

        fun initI18n4k(context: Context) {
            if (inited) return

            i18n4k = I18n4kConfigDelegate { i18n4kConfig.value }

            // initialise the lib "i18n4k-cldr-plural-rules"
            i18n4kInitCldrPluralRules()

            // load "fr", "nl" and "en_US_texas" at runtime
            MyMessages.registerTranslation(
                MessagesProviderViaText(
                    text = String(
                        context.resources.openRawResource(
                            R.raw.x_y_my_messages_fr_i18n4k
                        ).readBytes()
                    )
                )
            )
            MyMessages.registerTranslation(
                MessagesProviderViaText(
                    text = String(
                        context.resources.openRawResource(
                            R.raw.x_y_my_messages_nl_i18n4k
                        ).readBytes()
                    )
                )
            )
            MyMessages.registerTranslation(
                MessagesProviderViaText(
                    text = String(
                        context.resources.openRawResource(
                            R.raw.x_y_my_messages_en_us_texas_i18n4k
                        ).readBytes()
                    )
                )
            )
            inited = true
        }
    }
}

