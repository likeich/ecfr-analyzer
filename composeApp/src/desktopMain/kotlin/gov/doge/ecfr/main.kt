package gov.doge.ecfr

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import gov.doge.ecfr.core.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "eCFRAnalyzer",
    ) {
        App()
    }
}