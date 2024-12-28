import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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
            Row {
                renderDropdown(buildings)
                renderDropdown(computerTypes)
            }

            Button (onClick = {}) {
                Text("Add Room")
            }
        }
    }

    @Composable
    private fun renderDropdown(dropdownItems: List<String>) {
        val isDropDownExpanded = remember {
            mutableStateOf(false)
        }

        val itemPosition = remember {
            mutableStateOf(0)
        }

        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(text = dropdownItems[itemPosition.value])
            }
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            }) {
            dropdownItems.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        isDropDownExpanded.value = false
                        itemPosition.value = index
                    }, content = {
                        Text(text = item)
                    }
                )
            }
        }
    }
}
