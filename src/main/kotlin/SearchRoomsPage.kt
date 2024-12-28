import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import org.jetbrains.exposed.sql.and

class SearchRoomsPage {
    private var buildings = listOf<String>()
    private var computerTypes = listOf<String>()

    init {
        Database.connect(
            "jdbc:sqlite:4kitsw10_COM534_2_database.db",
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
        Column {
            var selectedBuilding by remember { mutableStateOf("") }

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

            var selectedType by remember { mutableStateOf("") }

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

            val number = mutableListOf("Room Number")
            val building = mutableListOf("Building")
            val computerType = mutableListOf("Computer Type")
            val nComputers = mutableListOf("Number of Computers")

            transaction {
                RoomsTable.selectAll().where {
                    RoomsTable.building.eq(selectedBuilding).and {
                        RoomsTable.computerType.eq(selectedType)
                    }
                }.forEach {
                    number += it[RoomsTable.number].toString()
                    building += it[RoomsTable.building]
                    computerType += it[RoomsTable.computerType]
                    nComputers += it[RoomsTable.nComputers].toString()
                }
            }

            Row {
                LazyColumn {
                    items(number) { curItem -> Text(curItem) }
                }
                LazyColumn {
                    items(building) { curItem -> Text(curItem) }
                }
                LazyColumn {
                    items(computerType) { curItem -> Text(curItem) }
                }
                LazyColumn {
                    items(nComputers) { curItem -> Text(curItem) }
                }
            }

            Button (onClick = {}) {
                Text("Add Room")
            }
        }
    }
}
