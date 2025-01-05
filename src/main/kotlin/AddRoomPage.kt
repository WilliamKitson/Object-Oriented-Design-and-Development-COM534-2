import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class AddRoomPage(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "addRoom") {
            composable("addRoom") {
                Column {
                    var number by remember { mutableStateOf("") }
                    var errors by remember { mutableStateOf("") }

                    TextField(
                        value = number,
                        onValueChange = { number = it },
                        label = { Text("Room Number") }
                    )

                    var building by remember { mutableStateOf("") }

                    TextField(
                        value = building,
                        onValueChange = { building = it },
                        label = { Text("Building") }
                    )

                    var computerType by remember { mutableStateOf("") }

                    TextField(
                        value = computerType,
                        onValueChange = { computerType = it },
                        label = { Text("Computer Type") }
                    )

                    var nComputers by remember { mutableStateOf("") }

                    TextField(
                        value = nComputers,
                        onValueChange = { nComputers = it },
                        label = { Text("Number of Computers") }
                    )

                    Button (onClick = {
                        var computers = 0

                        val nComputersFiltered = nComputers.replace(
                            "[^0-9]".toRegex(),
                            ""
                        )

                        if (nComputersFiltered.isNotEmpty()) {
                            computers = nComputersFiltered.toInt()
                        }

                        bookingSystem.addRoom(Room(
                            number,
                            building,
                            computerType,
                            computers,
                        ))

                        errors = bookingSystem.getLastError()

                        if (errors == "") {
                            navController.navigate("adminPage")
                        }
                    }) {
                        Text("Save Room")
                    }

                    Button (onClick = {
                        navController.navigate("adminPage")
                    }) {
                        Text("Back")
                    }

                    Text(errors, color = Color.Red)
                }
            }
            composable("adminPage") {
                AdminPage(connection, bookingSystem).render()
            }
        }
    }
}
