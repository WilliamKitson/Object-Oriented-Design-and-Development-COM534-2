import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }

    val bookingSystem = BookingSystem()

    bookingSystem.addRoom(Room(
        "1",
        "Spark",
        "Mac OS",
        15
    ))

    bookingSystem.addRoom(Room(
        "2",
        "Spark",
        "Windows PC",
        25
    ))

    print("rooms: ${bookingSystem.rooms}\n")

    bookingSystem.addUser(User(
        "BEELZEBLUBBER",
        "20CharactersOrMore20",
        true
    ))

    bookingSystem.addUser(User(
        "Hillsy",
        "21CharactersOrMore21",
        false
    ))

    print("users: ${bookingSystem.users}\n")

    bookingSystem.login("Hillsy", "21CharactersOrMore21")

    bookingSystem.bookRoom(
        "1",
        "Monday",
        9
    )

    bookingSystem.logout()
    bookingSystem.login("BEELZEBLUBBER", "20CharactersOrMore20")

    bookingSystem.bookRoom(
        "1",
        "Monday",
        9
    )

    print("bookings: ${bookingSystem.getAllBookingsForRoomAndDay("1", "Monday")}\n")
}
