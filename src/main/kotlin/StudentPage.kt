import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class StudentPage(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "studentPage") {
            composable(route = "studentPage") {
                Column {
                    Text("welcome ${bookingSystem.currentUser?.toString()}")

                    Button (onClick = {
                        navController.navigate(route = "bookComputer")
                    }) {
                        Text("Book Computer")
                    }

                    Button (onClick = {
                        navController.navigate(route = "login")
                    }) {
                        Text("Logout")
                    }
                }
            }
            composable(route = "bookComputer") {
                BookComputerPage(connection, bookingSystem).render()
            }
            composable(route = "login") {
                LoginPage(connection).render()
            }
        }
    }
}
