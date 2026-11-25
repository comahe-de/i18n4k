package example.compose

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "i18n4k - example-compose (Kotlin Toolchain)",
    ) {
        App()
    }
}
