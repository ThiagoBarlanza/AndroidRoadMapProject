package pt.drunkdroidos.cat_alog.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SequenceButton(label: String, modifier: Modifier = Modifier) {
    Button(onClick = {

    }) {
        Text(label)
    }
}