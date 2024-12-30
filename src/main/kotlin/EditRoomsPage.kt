import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class EditRoomsPage(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "editRooms") {
            composable("editRooms") {




                val tableData = (1..100).mapIndexed { index, item ->
                    index to "Item $index"
                }
                // Each cell of a column must have the same weight.
                val column1Weight = .3f // 30%
                val column2Weight = .7f // 70%
                // The LazyColumn will be our table. Notice the use of the weights below
                LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                    // Here is the header
                    item {
                        Row(Modifier.background(Color.Gray)) {
                            TableCell(text = "Column 1", weight = column1Weight)
                            TableCell(text = "Column 2", weight = column2Weight)
                        }
                    }
                    // Here are all the lines of your table.
                    items(tableData) {
                        val (id, text) = it
                        Row(Modifier.fillMaxWidth()) {
                            TableCell(text = id.toString(), weight = column1Weight)
                            TableCell(text = text, weight = column2Weight)
                        }
                    }
                }



                Column {
                    Button(onClick = {
                        navController.navigate("adminPage")
                    }) {
                        Text("Back")
                    }
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
}