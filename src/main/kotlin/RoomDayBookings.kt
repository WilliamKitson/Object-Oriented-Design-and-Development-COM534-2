import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.awt.print.Book

class RoomDayBookings(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "roomDayBookings") {
            composable(route = "roomDayBookings") {
                Column {
                    val bookings = mutableListOf<Booking>()

                    for (i in bookingSystem.getAllBookingsForRoomAndDay("JS001", "Monday")!!) {
                        bookings += i
                    }

                    val columnWeight = .3f

                    LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
                        item {
                            Row(Modifier.background(Color.Gray)) {
                                TableCell(text = "Room", weight = columnWeight)
                                TableCell(text = "Day", weight = columnWeight)
                                TableCell(text = "Booking", weight = columnWeight)
                            }
                        }
                        items(bookings) {
                            Row(Modifier.fillMaxWidth()) {
                                TableCell(text = "TEMP", weight = columnWeight)
                                TableCell(text = "TEMP", weight = columnWeight)
                                TableCell(text = it.toString(), weight = columnWeight)
                            }
                        }
                    }
                    Button(onClick = {
                        navController.navigate("adminPage")
                    }) {
                        Text("Back")
                    }
                }
            }
            composable(route = "adminPage") {
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