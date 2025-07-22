package ru.normno.paginationcmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyPaginationCMP",
    ) {
        App()
    }
}