package pt.drunkdroidos.cat_alog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import pt.drunkdroidos.cat_alog.ui.theme.CatalogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GameScreen(modifier: Modifier = Modifier, initialClickCount: Int = 0) {
    // Click count state
    var clickCount by rememberSaveable { mutableStateOf(initialClickCount) }

    // Score save state
    var score by rememberSaveable { mutableStateOf("Score: $clickCount") }

    // URl's list state
    var imageUrls by rememberSaveable { mutableStateOf(listOf<String>()) }

    // Function to generate new seed each click
    fun generateNewSeed(): String {
        return "seed$clickCount"
    }

    val imageUrlBase = "https://picsum.photos/200/300?seed="

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = {
                        clickCount++
                        score = "Score: $clickCount"
                        val newImageUrl = "$imageUrlBase${generateNewSeed()}"
                        imageUrls = imageUrls + newImageUrl
                    }
                ) {
                    Text("New Cat")
                }
                Text(
                    text = score,
                    modifier = Modifier.padding(8.dp)
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(imageUrls) { imageUrl ->
                    val painter = rememberAsyncImagePainter(imageUrl)
                    Image(
                        painter = painter,
                        contentDescription = "Dynamic Image",
                        modifier = Modifier
                            .size(140.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    CatalogTheme {
        GameScreen(initialClickCount = 0)
    }
}
