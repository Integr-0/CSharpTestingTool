package WindowView

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import code.scripting.ScriptingSetup
import colors
import testcaseManagement.*

val sm = ScriptingSetup()
val gn = GenerationUtils(sm)
val dp = Dumper()

@Composable
@Preview
fun GeneratorWindow(code: MutableState<String>, args: MutableList<generationArg>, amount: MutableState<Int>, showEditor: MutableState<Boolean>, casesName: MutableState<String>) {
    MaterialTheme(colors = colors) {
        Column {
            Row(modifier = Modifier.padding(10F.dp)) {
                Button(modifier = Modifier.padding(Dp(5F)), onClick = {
                    showEditor.value = !showEditor.value
                    code.value = "var inputs: List<Any> = listOf()\n" +
                            "/* Inputs will be parsed here in order */\n" +
                            "/* Example: input 1 is a string: use * inputs[0] * to reference it */\n" +
                            "/* Write your Evaluation code here */\n" +
                            "/* use Kotlin code */\n" +
                            "/* Output needs to be in a String Variable inside the printOutput function at the end of the document */\n" +
                            "putOutput(\"Output\")\n"
                }) {
                    Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Editor")
                    Text("Generation Function Editor", color = Color.White, modifier = Modifier.padding(1F.dp))
                }

                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow), modifier = Modifier.padding(Dp(5F)), onClick = {
                    gn.test(args = args, code = code)
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

            Row {
                TextField(value = casesName.value, onValueChange = {casesName.value = it}, label = { Text("Dump Name")}, modifier = Modifier.padding(Dp(5F)))
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