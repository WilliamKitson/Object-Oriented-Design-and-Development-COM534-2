import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class EditRoomsPage(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "editRooms") {
            composable("editRooms") {
                Column {
                    Button(onClick = {
                        navController.navigate("adminPage")
                    }) {
                        Text("Back")
                    }
                }
            }
            composable("adminPage") {
                AdminPage(connection, bookingSystem).render()
            }
        }
    }
}