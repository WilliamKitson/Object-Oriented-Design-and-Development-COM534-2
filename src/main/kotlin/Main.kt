import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.Text

@Composable
fun App() {
    val connection = "jdbc:sqlite:4kitsw10_COM534_2_database.db"
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable(route = "login") {
            LoginPage(connection).render()
        }
        composable(route = "searchRooms") {
            SearchRoomsPage(connection).render()
        }
        composable(route = "addRooms") {
            AddRoomPage(connection).render()
        }
    }

    Column {
        Button(onClick = {
            navController.navigate("login")
        }){
            Text("Login")
        }
        Button(onClick = {
            navController.navigate("searchRooms")
        }){
            Text("searchRooms")
        }
        Button(onClick = {
            navController.navigate("addRooms")
        }){
            Text("addRoom")
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "4kitsw10 COM534_2") {
        App()
    }
}
