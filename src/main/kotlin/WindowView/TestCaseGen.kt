package WindowView

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import code.scripting.ScriptingSetup
import colors
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import testcaseManagement.*
import java.io.File

val sm = ScriptingSetup()
val gn = GenerationUtils(sm)
val dp = Dumper()

var pc = mutableStateOf(false)

@Composable
@Preview
fun GeneratorWindow(popupText: MutableState<String>, popup: MutableState<Boolean>, code: MutableState<String>, args: MutableList<generationArg>, amount: MutableState<Int>, showEditor: MutableState<Boolean>, casesName: MutableState<String>) {
    MaterialTheme(colors = colors) {
        Column {
            Row(modifier = Modifier.padding(10F.dp)) {
                Button(modifier = Modifier.padding(Dp(5F)), onClick = {
                    showEditor.value = !showEditor.value

                }) {
                    Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Editor")
                    Text("Generation Function Editor", color = Color.White, modifier = Modifier.padding(1F.dp))
                }

                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow), modifier = Modifier.padding(Dp(5F)), onClick = {
                    var result = gn.test(args = args, code = code)
                    popupText.value = result
                    popup.value = true
                }) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "Test", tint = Color.Gray)
                    Text("Test", color = Color.Gray, modifier = Modifier.padding(1F.dp))
                }
            }

            Row(modifier = Modifier.padding(10F.dp)) {
                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green), modifier = Modifier.padding(Dp(5F)), onClick = {
                    var cases = gn.gen(args = args, amount = amount, code = code)
                    dp.dumpCases(cases, casesName.value)
                }) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Generate", tint = Color.Gray)
                    Text("Generate", color = Color.Gray, modifier = Modifier.padding(1F.dp))
                }

                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green), modifier = Modifier.padding(Dp(5F)), onClick = {
                    dp.dumpGenFunc(code.value, casesName.value)
                }) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Dump Func", tint = Color.Gray)
                    Text("Dump Func", color = Color.Gray, modifier = Modifier.padding(1F.dp))
                }

                AmountPicker(modifier = Modifier.padding(Dp(5F)), amount = amount)
            }

            Row(modifier = Modifier.padding(10F.dp)) {
                TextField(value = casesName.value, onValueChange = {casesName.value = it}, label = { Text("Dump Name")}, modifier = Modifier.padding(Dp(5F)))

                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray), modifier = Modifier.padding(Dp(5F)), onClick = {
                    pc.value = true
                }) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = "Load Func", tint = Color.White)
                    Text(" Load Func", color = Color.White, modifier = Modifier.padding(1F.dp))
                }

                FilePicker(fileExtension = "func.dump", show = pc.value, onFileSelected = {
                    pc.value = false
                    loadFunc(code, it)
                })
            }
        }
    }
}

@Composable
@Preview
fun AmountPicker(modifier: Modifier, amount: MutableState<Int>) {
    Row(modifier = modifier) {
        Button(onClick = {
            if (amount.value > 0) amount.value-=10
        }, modifier = modifier.height(Dp(40F))) {
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Down")
        }

        Button(onClick = {}, modifier = modifier.height(Dp(40F)).width(Dp(200F))) {
            Text("Amount: ${amount.value}")
        }

        Button(onClick = {
            amount.value+=10
        }, modifier = modifier.height(Dp(40F))) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Up")
        }
    }
}

fun loadFunc(code: MutableState<String>, value: String?) {
    if (value != null) {
        var lines: MutableList<String> = File(value).readLines().toMutableList()
        var output = ""
        for (s in lines) {
            output += "$s\n"
        }
        code.value = output
    }
}