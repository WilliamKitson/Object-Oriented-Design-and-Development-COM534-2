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
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun App() {
    LoginPage().render()
    searchRoomsWidget()
}

@Composable
fun searchRoomsWidget() {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableStateOf(0)
    }

    val buildings = listOf("The Spark", "Library")

    Column {
        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(text = buildings[itemPosition.value])
            }
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            }) {

            buildings.forEachIndexed { index, building ->
                DropdownMenuItem(
                    onClick = {
                        isDropDownExpanded.value = false
                        itemPosition.value = index
                    }, content = {
                        Text(text = building)
                    }
                )
            }
        }
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
