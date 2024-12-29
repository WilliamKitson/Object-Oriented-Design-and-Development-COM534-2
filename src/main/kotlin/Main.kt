import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable(route = "login") {
            LoginPage("jdbc:sqlite:4kitsw10_COM534_2_database.db").render()
        }
        composable(route = "searchRooms") {
            SearchRoomsPage("jdbc:sqlite:4kitsw10_COM534_2_database.db").render()
        }
        composable(route = "addRooms") {
            AddRoomPage("jdbc:sqlite:4kitsw10_COM534_2_database.db").render()
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "4kitsw10 COM534_2") {
        App()
    }
}
