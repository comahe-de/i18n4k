@file:Suppress("FunctionName")

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import example_compose.composeapp.generated.resources.Res
import example_compose.composeapp.generated.resources.compose_multiplatform
import example_compose.composeapp.generated.resources.logo
import de.comahe.i18n4k.config.I18n4kConfigDelegate
import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.i18n4kInitCldrPluralRules
import de.comahe.i18n4k.messages.providers.MessagesProviderViaBytes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import x.y.Actions
import x.y.MyMessages
import x.y.Things

var i18n4kConfig = mutableStateOf(I18n4kConfigImmutable())

// Hack to initialize [i18n4k]
@OptIn(ExperimentalResourceApi::class)
@Suppress("unused")
private val i18n4kInitialized = true.also {
    i18n4k = I18n4kConfigDelegate { i18n4kConfig.value }

    // load the other translations...
    GlobalScope.launch {
        MyMessages.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes("files/i18n/x/y/MyMessages_fr.i18n4k.txt")))
        MyMessages.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes( "files/i18n/x/y/MyMessages_nl.i18n4k.txt")))
        MyMessages.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes( "files/i18n/x/y/MyMessages_en_US_texas.i18n4k.txt")))
        Actions.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes( "files/i18n/x/y/Actions_fr.i18n4k.txt")))
        Actions.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes( "files/i18n/x/y/Actions_nl.i18n4k.txt")))
        Things.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes( "files/i18n/x/y/Things_fr.i18n4k.txt")))
        Things.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes( "files/i18n/x/y/Things_fr_x_attr-gender.i18n4k.txt")))
        Things.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes( "files/i18n/x/y/Things_nl.i18n4k.txt")))
        Things.registerTranslation(MessagesProviderViaBytes(textBytes = Res.readBytes( "files/i18n/x/y/Things_nl_x_attr-gender.i18n4k.txt")))
    }
    // initialise the lib "i18n4k-cldr-plural-rules"
    i18n4kInitCldrPluralRules()
}


@Composable
fun App() {

    println(i18n4k.locale)

    key(i18n4kConfig) {
        MaterialTheme {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Header()
                LocaleSelection()
                AppContent()
            }
        }
    }
}


@Composable
fun Header() {
    Row(modifier = Modifier.height(30.dp)) {
        Text("Compose: ${Greeting().greet()}")
        Image(
            painterResource(Res.drawable.compose_multiplatform), null
        )
        Image(
            painterResource(Res.drawable.logo), null
        )
    }
}

@Composable
fun LocaleSelection() {
    Row {
        for (locale in MyMessages.locales) {
            Button(colors = ButtonDefaults.buttonColors(if (locale == i18n4k.locale) Color.Red else Color.Yellow),
                onClick = {
                    i18n4kConfig.value = i18n4kConfig.value.withLocale(locale)
                }) {
                Text(locale.getDisplayNameInLocale())
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppContent() {
    Column {

        var textHello by remember { mutableStateOf("") }
        var selectedThing by remember { mutableStateOf(Things.sun) }
        var countOfShapes by remember { mutableStateOf("") }
        var ordinalOfShape by remember { mutableStateOf("") }

        Text(MyMessages.title())
        Text(MyMessages.sayHello(textHello))
        TextField(textHello, onValueChange = { textHello = it })

        Divider(thickness = 10.dp)

        Text(Actions.select_thing())


        var thingMenuExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = thingMenuExpanded,
            onExpandedChange = { thingMenuExpanded = !thingMenuExpanded }
        ) {
            TextField(
                value = selectedThing.toString(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = thingMenuExpanded) },
            )
            ExposedDropdownMenu(
                expanded = thingMenuExpanded,
                onDismissRequest = { thingMenuExpanded = false }
            ) {
                arrayOf(Things.sun, Things.moon, Things.water, Things.boy, Things.girl)
                    .forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                selectedThing = item
                                thingMenuExpanded = false
                            }
                        ) { Text(item.toString()) }
                    }
            }
        }
        Text(selectedThing.getAttribute("emoji").toString())

        Row {
            Text(Actions.count_of_shapes())
            TextField(countOfShapes, onValueChange = { countOfShapes = it })
        }
        Row {
            Text(Actions.ordinal_of_shape())
            TextField(ordinalOfShape, onValueChange = { ordinalOfShape = it })
        }

        Text(Actions.change_shape(selectedThing))
        Text(Actions.shape_count(countOfShapes, selectedThing))
        Text(Actions.shape_order(ordinalOfShape, selectedThing))
        Text(Actions.attr_value(selectedThing))
        Text(Actions.attr_select(selectedThing))


    }
}
