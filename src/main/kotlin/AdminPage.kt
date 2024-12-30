import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class AdminPage(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "adminPage") {
            composable(route = "adminPage") {
                Column {
                    Text("welcome ${bookingSystem.currentUser?.toString()}")

                    Button (onClick = {
                        navController.navigate("addRoom")
                    }) {
                        Text("Add Room")
                    }
                    Button (onClick = {
                        navController.navigate("login")
                    }) {
                        Text("Logout")
                    }
                }
            }
            composable(route = "addRoom") {
                AddRoomPage(connection, bookingSystem).render()
            }
            composable(route = "login") {
                LoginPage(connection).render()
            }
        }
    }
}
