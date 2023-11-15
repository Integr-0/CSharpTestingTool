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
import testcaseManagement.RandomIntValueFromRange
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

var genCode = mutableStateOf("")
var genAmount = mutableStateOf(0)
var genArgs: MutableList<generationArg> = mutableListOf(RandomIntValueFromRange(1, 50), RandomIntValueFromRange(50, 70))

fun main() = application {

    Window(onCloseRequest = ::exitApplication, title = "Testcase Generator", icon = painterResource("Icon.ico")) {
        GeneratorWindow(genCode, genArgs, genAmount, showEditor)
    }

    if (showEditor.value) {
        Window(onCloseRequest = {showEditor.value = false}, title = "Editor", icon = painterResource("Icon.ico")) {
            CodeEditor(code = genCode)
        }
    }

}