package pt.drunkdroidos.cat_alog

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import pt.drunkdroidos.cat_alog.ui.theme.CatalogTheme
import java.io.IOException

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

    val gridState = rememberLazyGridState()

    // Coroutine scope for scrolling
    val coroutineScope = rememberCoroutineScope()

    // MediaPlayer for cat sound
    val mediaPlayer = remember {
        MediaPlayer().apply {
            try {
                setDataSource("https://drive.google.com/uc?export=download&id=15wFVSkyDzCOp42ePqiaqSBCPXUZhjZqG")
                prepareAsync()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Function to generate new seed each click
    fun generateNewSeed(): String {
        return "seed$clickCount"
    }

    // URL base to API cat images
    val imageUrlBase = "https://cataas.com/cat"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cat Game Show!",
                modifier = Modifier.padding(20.dp),
                style = TextStyle(
                    color = Color.Red,
                    fontSize = 20.sp
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.seekTo(0)
                        } else {
                            mediaPlayer.start()
                        }

                        clickCount++
                        score = "Score: $clickCount"
                        val newImageUrl = "$imageUrlBase?${generateNewSeed()}"
                        imageUrls = imageUrls + newImageUrl

                        // Scroll to the newly added item
                        coroutineScope.launch {
                            gridState.scrollToItem(imageUrls.size - 1)
                        }
                    }
                ) {
                    Text("New Cat")
                }
                Text(
                    text = score,
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                )
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 70.dp),
                state = gridState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(imageUrls) { imageUrl ->
                    val painter = rememberAsyncImagePainter(imageUrl)
                    Image(
                        painter = painter,
                        contentDescription = "Dynamic Image",
                        modifier = Modifier
                            .size(140.dp)
                            .padding(2.dp)
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
