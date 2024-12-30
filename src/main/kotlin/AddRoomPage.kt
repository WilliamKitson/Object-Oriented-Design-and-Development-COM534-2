import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class AddRoomPage(private val connection: String) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "addRoom") {
            composable("addRoom") {
                Column {
                    var number by remember { mutableStateOf("") }

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
                        BookingSystem(connection).addRoom(Room(
                            number,
                            building,
                            computerType,
                            nComputers.toInt(),
                        ))

                        navController.navigate("searchRooms")
                    }) {
                        Text("Save Room")
                    }
                }
            }
            composable("searchRooms") {
                //SearchRoomsPage(connection).render()
            }
        }
    }
}
