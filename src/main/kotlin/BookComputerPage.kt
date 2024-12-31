import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.Text

class BookComputerPage(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "bookComputer") {
            composable(route = "bookComputer") {
                Button(onClick = {
                    navController.navigate("studentPage")
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
