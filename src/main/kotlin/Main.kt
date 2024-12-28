import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@Composable
fun App() {
    LoginPage().render()
    SearchRoomsPage().render()
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

        RoomsTable.deleteAll()

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
