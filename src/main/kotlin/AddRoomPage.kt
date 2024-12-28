import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*

class AddRoomPage {
    @Composable
    fun render() {
        Column {
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
        }
    }
}