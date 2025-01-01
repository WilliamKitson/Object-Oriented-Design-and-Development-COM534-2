import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class ViewBookingsPage(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "viewBookings") {
            composable(route = "viewBookings") {
                Button (onClick = {
                    navController.navigate(route = "studentPage")
                }) {
                    Text("Back")
                }
            }
            composable(route = "studentPage") {
                StudentPage(connection, bookingSystem).render()
            }
        }
    }
}
