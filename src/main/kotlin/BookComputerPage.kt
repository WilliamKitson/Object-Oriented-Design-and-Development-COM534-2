import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class BookComputerPage(private val connection: String, private val bookingSystem: BookingSystem) {
    private var buildings = listOf<String>()
    private var computerTypes = listOf<String>()
    private var currentRoom: Room? = null

    init {
        Database.connect(
            connection,
            "org.sqlite.JDBC"
        )

        transaction {
            RoomsTable.selectAll().forEach {
                buildings += it[RoomsTable.building]
                computerTypes += it[RoomsTable.computerType]
            }
        }

        buildings = buildings.distinct()
        computerTypes = computerTypes.distinct()
    }

    @Composable
    fun render() {
        val navController = rememberNavController()
        var isDialogOpen by remember { mutableStateOf(false) }

        NavHost(navController, startDestination = "bookComputer") {
            composable(route = "bookComputer") {
                Column {
                    if (renderTable()) {
                        var selectedBuilding by remember { mutableStateOf(buildings.first()) }
                        var selectedType by remember { mutableStateOf(computerTypes.first()) }

                        Row {
                            Row {
                                val isDropDownExpanded = remember {
                                    mutableStateOf(false)
                                }

                                val itemPosition = remember {
                                    mutableStateOf(0)
                                }

                                Button(onClick = {
                                    isDropDownExpanded.value = true
                                }){
                                    Text(text = buildings[itemPosition.value])
                                }
                                DropdownMenu(
                                    expanded = isDropDownExpanded.value,
                                    onDismissRequest = {
                                        isDropDownExpanded.value = false
                                    }) {
                                    buildings.forEachIndexed { index, item ->
                                        DropdownMenuItem(
                                            onClick = {
                                                isDropDownExpanded.value = false
                                                itemPosition.value = index
                                                selectedBuilding = item
                                            }, content = {
                                                Text(text = item)
                                            }
                                        )
                                    }
                                }
                            }
                            Row {
                                val isDropDownExpanded = remember {
                                    mutableStateOf(false)
                                }

                                val itemPosition = remember {
                                    mutableStateOf(0)
                                }

                                Button(onClick = {
                                    isDropDownExpanded.value = true
                                }){
                                    Text(text = computerTypes[itemPosition.value])
                                }
                                DropdownMenu(
                                    expanded = isDropDownExpanded.value,
                                    onDismissRequest = {
                                        isDropDownExpanded.value = false
                                    }) {
                                    computerTypes.forEachIndexed { index, item ->
                                        DropdownMenuItem(
                                            onClick = {
                                                isDropDownExpanded.value = false
                                                itemPosition.value = index
                                                selectedType = item
                                            }, content = {
                                                Text(text = item)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        val rooms = mutableListOf<Room>()

                        transaction {
                            RoomsTable.selectAll().where {
                                RoomsTable.building.eq(selectedBuilding).and {
                                    RoomsTable.computerType.eq(selectedType)
                                }
                            }.forEach {
                                rooms += Room(
                                    it[RoomsTable.number].toString(),
                                    it[RoomsTable.building],
                                    it[RoomsTable.computerType],
                                    it[RoomsTable.nComputers].toInt()
                                )
                            }
                        }

                        val columnWeight = .3f

                        LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
                            item {
                                Row(Modifier.background(Color.Gray)) {
                                    TableCell(text = "Room Number", weight = columnWeight)
                                    TableCell(text = "Building", weight = columnWeight)
                                    TableCell(text = "Computer OS", weight = columnWeight)
                                    TableCell(text = "Number of Computers", weight = columnWeight)
                                    TableCell(text = "Book Computer", weight = columnWeight)
                                }
                            }
                            items(rooms) {
                                Row(Modifier.fillMaxWidth()) {
                                    TableCell(text = it.number, weight = columnWeight)
                                    TableCell(text = it.building, weight = columnWeight)
                                    TableCell(text = it.compType, weight = columnWeight)
                                    TableCell(text = it.computers.size.toString(), weight = columnWeight)
                                    TableButton(text = "Book", weight = columnWeight, onClick = {
                                        isDialogOpen = true
                                        currentRoom = it
                                    })
                                }
                            }
                        }
                    }
                    Button(onClick = {
                        navController.navigate("studentPage")
                    }) {
                        Text("Back")
                    }
                }
                if (isDialogOpen) {
                    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
                    var selectedDay by remember { mutableStateOf(days.first()) }

                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            Button(onClick = {
                                isDialogOpen = false

                                bookingSystem.bookRoom(
                                    currentRoom?.number.toString(),
                                    selectedDay,
                                    9
                                )

                                navController.navigate("studentPage")
                            }) {
                                Text("Book")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                isDialogOpen = false
                            }) {
                                Text("Back")
                            }
                        },
                        title = { Text("Book $currentRoom") },
                        text = {
                            Column {
                                Text("TEMP")

                                val isDropDownExpanded = remember {
                                    mutableStateOf(false)
                                }

                                val itemPosition = remember {
                                    mutableStateOf(0)
                                }

                                Button(onClick = {
                                    isDropDownExpanded.value = true
                                }){
                                    Text(text = days[itemPosition.value])
                                }
                                DropdownMenu(
                                    expanded = isDropDownExpanded.value,
                                    onDismissRequest = {
                                        isDropDownExpanded.value = false
                                    }) {
                                    days.forEachIndexed { index, item ->
                                        DropdownMenuItem(
                                            onClick = {
                                                isDropDownExpanded.value = false
                                                itemPosition.value = index
                                                selectedDay = item
                                            }, content = {
                                                Text(text = item)
                                            }
                                        )
                                    }
                                }
                            }
                        },
                    )
                }
            }
            composable(route = "studentPage") {
                StudentPage(connection, bookingSystem).render()
            }
        }
    }

    private fun renderTable(): Boolean {
        if (buildings.isEmpty()) {
            return false
        }

        if (computerTypes.isEmpty()) {
            return false
        }

        return true
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
