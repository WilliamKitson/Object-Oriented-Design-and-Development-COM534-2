import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*

class AddRoomPage {
    @Composable
    fun render() {
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
                println("Add new room")
            }) {
                Text("Save Room")
            }
        }
    }
}