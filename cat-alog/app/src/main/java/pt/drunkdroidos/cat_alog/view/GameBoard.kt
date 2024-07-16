package pt.drunkdroidos.cat_alog.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GameBoard(score: Int, highlighted: Int, modifier: Modifier = Modifier) {
    Row(modifier) {
        Column(modifier) {
            SequenceButton(
                label = "1",
                modifier = modifier
            )
        }

        Column(modifier) {
            SequenceButton(
                label = "2",
                modifier = modifier
            )

            Text("Score: $score")

            SequenceButton(
                label = "3",
                modifier = modifier
            )
        }
        Column(modifier) {
            SequenceButton(
                label = "4",
                modifier = modifier
            )
        }
    }
}
