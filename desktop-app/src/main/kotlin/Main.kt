import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.smilestudio.ui.MoleculeEditor

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "SmilesStudio") {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                var smilesText by remember { mutableStateOf("") }
                MoleculeEditor(
                    smilesText = smilesText,
                    onSmilesTextChange = { smilesText = it },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
