import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "4kitsw10 COM534_2") {
        LoginPage("jdbc:sqlite:4kitsw10_COM534_2_database.db").render()
    }
}
