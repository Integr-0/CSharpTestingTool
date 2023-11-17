import androidx.compose.material.Colors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import code.CodeEditor
import WindowView.GeneratorWindow
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.rememberWindowState
import testcaseManagement.RandomIntValueFromRange
import testcaseManagement.StringValueFromSet
import testcaseManagement.generationArg

var colors = Colors(
    isLight = false,

    primary = Color.Gray,
    primaryVariant = Color.Gray,
    onPrimary = Color.White,

    secondary = Color.Gray,
    secondaryVariant = Color.Gray,
    onSecondary = Color.White,

    error = Color.Red,
    onError = Color.White,

    background = Color.White,
    onBackground = Color.Gray,

    surface = Color.White,
    onSurface = Color.Gray
)

var showEditor = mutableStateOf(false)

var showPopup = mutableStateOf(false)
var popupText = mutableStateOf("")

var genCode = mutableStateOf("var inputs: List<Any> = listOf()\n" +
        "/* Inputs will be parsed here in order */\n" +
        "/* Example: input 1 is a string: use * inputs[0] * to reference it */\n" +
        "/* Write your Evaluation code here */\n" +
        "/* use Kotlin code */\n" +
        "/* Output needs to be in a String Variable inside the printOutput function at the end of the document */\n" +
        "putOutput(\"Output\")\n")
var genCasesDumpName = mutableStateOf("Dump_${System.currentTimeMillis().toInt()}")
var genAmount = mutableStateOf(0)
var genArgs: MutableList<generationArg> = mutableListOf(StringValueFromSet(listOf("r", "ö")), RandomIntValueFromRange(1, 2))

fun main() = application {

    Window(onCloseRequest = ::exitApplication, title = "Testcase Generator", icon = painterResource("Icon.ico")) {
        GeneratorWindow(popupText, showPopup, genCode, genArgs, genAmount, showEditor, genCasesDumpName)
    }

    if (showEditor.value) {
        Window(onCloseRequest = {showEditor.value = false}, title = "Editor", icon = painterResource("Icon.ico")) {
            CodeEditor(code = genCode)
        }
    }

    var state = rememberWindowState(size = DpSize(500F.dp, 300F.dp))
    if (showPopup.value) {
        Window(state = state, onCloseRequest = {showPopup.value = false}, title = "Info", icon = painterResource("Icon.ico"), resizable = false) {
            MaterialTheme(colors = colors) {
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(popupText.value, textAlign = TextAlign.Center, fontSize = TextUnit(1F, TextUnitType.Em))
                    Button(onClick = {
                        showPopup.value = false
                    }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}
