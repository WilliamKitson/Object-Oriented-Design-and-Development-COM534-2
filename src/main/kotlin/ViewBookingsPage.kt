import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class ViewBookingsPage(private val connection: String, private val bookingSystem: BookingSystem) {
    private var bookings = listOf<Booking>()

    init {
        for (i in bookingSystem.currentUser?.bookings!!) {
            bookings += i
        }
    }

    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "viewBookings") {
            composable(route = "viewBookings") {
                Column {

                    val columnWeight = .3f

                    LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
                        item {
                            Row(Modifier.background(Color.Gray)) {
                                TableCell(text = "Booking Info", weight = columnWeight)
                                TableCell(text = "Cancel", weight = columnWeight)
                            }
                        }
                        items(bookings) {
                            Row(Modifier.fillMaxWidth()) {
                                TableCell(text = it.toString(), weight = columnWeight)
                                TableButton(text = "Cancel", weight = columnWeight, onClick = {
                                    println(it.toString())
                                })
                            }
                        }
                    }

                    Button(onClick = {
                        navController.navigate(route = "studentPage")
                    }) {
                        Text("Back")
                    }
                }
                composable(route = "studentPage") {
                    StudentPage(connection, bookingSystem).render()
                }
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
