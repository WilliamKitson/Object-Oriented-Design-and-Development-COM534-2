import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class LoginPage(private val connection: String) {
    private val bookingSystem = BookingSystem(connection)

    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "login") {
            composable(route = "login") {
                Column {
                    Text("Hello, welcome to my COM534 2 project!")
                    Text("Please sign up for an account or login below.")
                    Text("Remember, this project is linked to a database so registered accounts are persistent.")

                    var username by remember { mutableStateOf("") }

                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") }
                    )

                    var password by remember { mutableStateOf("") }

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    var errors by remember { mutableStateOf("") }

                    Row {
                        Button (onClick = {
                            bookingSystem.login(username, password)
                            errors = bookingSystem.getLastError()

                            if (bookingSystem.currentUser != null) {
                                if (bookingSystem.currentUser?.isAdmin == true) {
                                    navController.navigate("adminPage")
                                }

                                if (bookingSystem.currentUser?.isAdmin == false) {
                                    navController.navigate("studentPage")
                                }
                            }
                        }) {
                            Text("Login")
                        }

                        Button (onClick = {
                            bookingSystem.signupStudent(username, password)
                            errors = bookingSystem.getLastError()

                            bookingSystem.login(username, password)
                            navController.navigate("studentPage")
                        }) {
                            Text("register student")
                        }

                        Button (onClick = {
                            bookingSystem.signupAdministrator(username, password)
                            errors = bookingSystem.getLastError()

                            bookingSystem.login(username, password)
                            navController.navigate("adminPage")
                        }) {
                            Text("register admin")
                        }
                    }

                    Text(errors, color = Color.Red)
                }
            }
            composable(route = "studentPage") {
                StudentPage(connection, bookingSystem).render()
            }

            composable(route = "adminPage") {
                AdminPage(connection, bookingSystem).render()
            }
        }
    }
}
