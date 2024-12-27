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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.material.TextField

@Composable
fun App() {
    Column {
        Text("Hello, welcome to my COM534 2 project!", color = Color.Red, fontStyle=FontStyle.Italic, fontFamily=FontFamily.Serif)
        Text("Please sign up for an account or login below.")
        Text("Remember, this project is linked to a database so registered accounts are persistent.")
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "4kitsw10 COM534_2"
    ) {
        App()
    }
}
