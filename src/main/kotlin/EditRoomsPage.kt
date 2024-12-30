import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class EditRoomsPage(private val connection: String, private val bookingSystem: BookingSystem) {
    private var rooms = listOf<Room>()
    private var editedRoom = ""

    init {
        Database.connect(
            connection,
            "org.sqlite.JDBC"
        )

        transaction {
            RoomsTable.selectAll().forEach {
                rooms += Room(
                    it[RoomsTable.number].toString(),
                    it[RoomsTable.building],
                    it[RoomsTable.computerType],
                    it[RoomsTable.nComputers].toInt()
                )
            }
        }
    }

    @Composable
    fun render() {
        val navController = rememberNavController()
        var isDialogOpen by remember { mutableStateOf(false) }

        NavHost(navController, startDestination = "editRooms") {
            composable("editRooms") {
                Column {
                    val columnWeight = .3f

                    LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
                        item {
                            Row(Modifier.background(Color.Gray)) {
                                TableCell(text = "Room Number", weight = columnWeight)
                                TableCell(text = "Building", weight = columnWeight)
                                TableCell(text = "Computer OS", weight = columnWeight)
                                TableCell(text = "Number of Computers", weight = columnWeight)
                            }
                        }
                        items(rooms) {
                            Row(Modifier.fillMaxWidth()) {
                                TableCell(text = it.number, weight = columnWeight)
                                TableCell(text = it.building, weight = columnWeight)
                                TableButton(text = it.compType, weight = columnWeight, onClick = {
                                    editedRoom = it.toString()
                                    isDialogOpen = true
                                })
                                TableCell(text = it.computers.size.toString(), weight = columnWeight)
                            }
                        }
                    }
                    Button(onClick = {
                        navController.navigate("adminPage")
                    }) {
                        Text("Back")
                    }
                }

                if (isDialogOpen) {
                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            Button(onClick = {
                                isDialogOpen = false
                            }) {
                                Text("Save")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { isDialogOpen = false }) {
                                Text("Back")
                            }
                        },
                        title = { Text("Edit $editedRoom") },
                        text = {
                            Button(onClick = {
                            isDialogOpen = false
                        }) {
                            Text("Save")
                        }
                               },
                    )
                }
            }
            composable("adminPage") {
                AdminPage(connection, bookingSystem).render()
            }
        }
    }

    @Composable
    fun RowScope.TableCell(
        text: String,
        weight: Float
    ) {
        Text(
            text = text,
            Modifier
                .border(1.dp, Color.Black)
                .weight(weight)
                .padding(8.dp)
        )
    }

    @Composable
    fun RowScope.TableButton(
        text: String,
        weight: Float,
        onClick: () -> Unit
    ) {
        Button(
            modifier = Modifier
                .border(1.dp, Color.Black)
                .weight(weight)
                .padding(8.dp),
            onClick = {
                onClick()
        }) {
            Text(text)
        }
    }
}
