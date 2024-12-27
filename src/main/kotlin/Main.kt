import AccountsTable.accountId
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.material.TextField
import androidx.compose.material.Button
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

@Composable
fun App() {
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
            val bookingSystem = BookingSystem()

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

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "4kitsw10 COM534_2") {
        App()
    }

    Database.connect(
        "jdbc:sqlite:4kitsw10_COM534_2_database.db",
        "org.sqlite.JDBC"
    )

    transaction {
        SchemaUtils.create(RoomsTable)

        RoomsTable.insert {
            it[number] = 1
            it[building] = "The Spark"
            it[computerType] = "MAC OS"
            it[nComputers] = 15
        }[RoomsTable.roomId]

        RoomsTable.selectAll().forEach {
            println(it)
        }
    }
}
