import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class RoomDayBookings(private val connection: String, private val bookingSystem: BookingSystem) {
    @Composable
    fun render() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "roomDayBookings") {
            composable(route = "roomDayBookings") {
                Column {
                    val rooms = bookingSystem.getUniqueRoomNumbers()
                    var selectedRoom by remember { mutableStateOf(rooms.first()) }

                    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
                    var selectedDay by remember { mutableStateOf(days.first()) }

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
                            }) {
                                Text(text = rooms[itemPosition.value])
                            }
                            DropdownMenu(
                                expanded = isDropDownExpanded.value,
                                onDismissRequest = {
                                    isDropDownExpanded.value = false
                                }) {
                                rooms.forEachIndexed { index, item ->
                                    DropdownMenuItem(
                                        onClick = {
                                            isDropDownExpanded.value = false
                                            itemPosition.value = index
                                            selectedRoom = item
                                        }, content = {
                                            Text(text = item)
                                        }
                                    )
                                }
                            }
                        }

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
                                }) {
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
                        }
                    }

                    val bookings = mutableListOf<Booking>()

                    for (i in bookingSystem.getAllBookingsForRoomAndDay(selectedRoom, selectedDay)!!) {
                        bookings += i
                    }

                    val columnWeight = .3f

                    LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
                        item {
                            Row(Modifier.background(Color.Gray)) {
                                TableCell(text = "Booking", weight = columnWeight)
                            }
                        }
                        items(bookings) {
                            Row(Modifier.fillMaxWidth()) {
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