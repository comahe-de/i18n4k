package example.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import de.comahe.i18n4k.config.I18n4kConfigDelegate
import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.i18n4kInitCldrPluralRules
import example.compose.resources.Res
import example.compose.resources.compose_multiplatform
import example.compose.resources.logo
import org.jetbrains.compose.resources.painterResource
import x.y.Actions
import x.y.MyMessages
import x.y.Things

/** The current i18n4k configuration, observed by Compose so locale changes recompose. */
var i18n4kConfig = mutableStateOf(I18n4kConfigImmutable())

// Initialises i18n4k once. All translations are inlined into the generated
// Kotlin code, so nothing has to be loaded from language resource files.
@Suppress("unused")
private val i18n4kInitialized = true.also {
    i18n4k = I18n4kConfigDelegate { i18n4kConfig.value }
    // Initialise the optional "i18n4k-cldr-plural-rules" library.
    i18n4kInitCldrPluralRules()
}

@Composable
fun App() {
    key(i18n4kConfig) {
        MaterialTheme {
            Column(
                Modifier
                    .safeContentPadding()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Header()
                LocaleSelection()
                AppContent()
            }
        }
    }
}

@Composable
private fun Header() {
    Row(modifier = Modifier.height(30.dp)) {
        Text("Compose: ${Greeting().greet()}")
        Image(painterResource(Res.drawable.compose_multiplatform), null)
        Image(painterResource(Res.drawable.logo), null)
    }
}

@Composable
private fun LocaleSelection() {
    Row {
        for (locale in MyMessages.locales) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    if (locale == i18n4k.locale) Color.Red else Color.Yellow
                ),
                onClick = {
                    i18n4kConfig.value = i18n4kConfig.value.withLocale(locale)
                },
            ) {
                Text(locale.getDisplayNameInLocale())
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppContent() {
    Column {
        var textHello by remember { mutableStateOf("") }
        var selectedThing by remember { mutableStateOf(Things.sun) }
        var countOfShapes by remember { mutableStateOf("") }
        var ordinalOfShape by remember { mutableStateOf("") }

        Text(MyMessages.title())
        Text(MyMessages.sayHello(textHello))
        TextField(textHello, onValueChange = { textHello = it })

        HorizontalDivider(thickness = 10.dp)

        Text(Actions.select_thing())

        var thingMenuExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = thingMenuExpanded,
            onExpandedChange = { thingMenuExpanded = it },
        ) {
            TextField(
                // The `menuAnchor` modifier must be passed to the text field to
                // handle expanding/collapsing the menu on click.
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                value = selectedThing.toString(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = thingMenuExpanded)
                },
            )
            ExposedDropdownMenu(
                expanded = thingMenuExpanded,
                onDismissRequest = { thingMenuExpanded = false },
            ) {
                arrayOf(Things.sun, Things.moon, Things.water, Things.boy, Things.girl)
                    .forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                selectedThing = item
                                thingMenuExpanded = false
                            },
                            text = { Text(item.toString()) },
                        )
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
        Text(Actions.shape_count(countOfShapes.toIntOrNull() ?: 0, selectedThing))
        Text(Actions.shape_order(ordinalOfShape.toIntOrNull() ?: 0, selectedThing))
        Text(Actions.attr_value(selectedThing))
        Text(Actions.attr_select(selectedThing))
    }
}
