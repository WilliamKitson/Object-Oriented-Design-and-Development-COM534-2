import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.exposed.sql.*


@Composable
fun App() {
    LoginPage("jdbc:sqlite:4kitsw10_COM534_2_database.db").render()
    SearchRoomsPage("jdbc:sqlite:4kitsw10_COM534_2_database.db").render()
    AddRoomPage("jdbc:sqlite:4kitsw10_COM534_2_database.db").render()
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "4kitsw10 COM534_2") {
        App()
    }
}
