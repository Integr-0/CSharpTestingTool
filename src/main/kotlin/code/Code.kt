package code

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors

@Composable
@Preview
fun CodeEditor(code: MutableState<String>) {
    MaterialTheme(colors = colors) {
        CodeEditor(code = code, modifier = Modifier.fillMaxSize().padding(10.dp))
    }
}