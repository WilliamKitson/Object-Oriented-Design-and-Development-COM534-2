import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import java.awt.Component

class LoginPage(private val connection: String) {
    @Composable
    fun render() {
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
                label = { Text("Password") }
            )

            var errors by remember { mutableStateOf("") }

            Row {
                val bookingSystem = BookingSystem(connection)

                Button (onClick = {
                    bookingSystem.login(username, password)
                    errors = bookingSystem.getLastError()
                }) {
                    Text("Login")
                }

                Button (onClick = {
                    bookingSystem.signup(username, password)
                    errors = bookingSystem.getLastError()
                }) {
                    Text("register")
                }
            }

            Text(errors, color = Color.Red)
        }
    }
}
