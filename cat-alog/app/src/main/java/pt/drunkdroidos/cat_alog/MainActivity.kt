package pt.drunkdroidos.cat_alog

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.drunkdroidos.cat_alog.ui.theme.CatalogTheme
import java.io.IOException
import kotlin.random.Random

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
    var sequence by rememberSaveable { mutableStateOf(mutableMapOf<Int, String>()) }
    var userSequence by rememberSaveable { mutableStateOf(listOf<Int>()) }
    var isShowingSequence by rememberSaveable { mutableStateOf(false) }
    var gameOver by rememberSaveable { mutableStateOf(false) }
    var score by rememberSaveable { mutableStateOf(0) }
    var currentImageIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var isGameStarted by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val startSoundPlayer = remember {
        MediaPlayer().apply {
            try {
                setDataSource("https://drive.google.com/uc?export=download&id=15wFVSkyDzCOp42ePqiaqSBCPXUZhjZqG")
                prepareAsync()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    val imageUrlBase = "https://cataas.com/cat"

    fun generateNewImageUrl(): String {
        val randomSeed = Random.nextInt(1000, 9999)
        return "$imageUrlBase?s=$randomSeed"
    }

    fun getRandomAvailablePosition(): Int? {
        val availablePositions = (0 until 16).filter { !sequence.containsKey(it) }
        return if (availablePositions.isNotEmpty()) availablePositions.random() else null
    }

    fun startNewRound() {
        val newImageUrl = generateNewImageUrl()
        val newPosition = getRandomAvailablePosition()
        if (newPosition != null) {
            sequence[newPosition] = newImageUrl
            userSequence = listOf()
            isShowingSequence = true
            coroutineScope.launch {
                for ((index, _) in sequence) {
                    currentImageIndex = index
                    delay(1000)
                }
                currentImageIndex = null
                isShowingSequence = false
            }
        } else {
            gameOver = true // No available positions left, game over
        }
    }

    fun checkUserSequence() {
        if (userSequence.map { sequence[it] } == sequence.values.toList()) {
            score++
            startNewRound()
        } else {
            gameOver = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (!isGameStarted) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cat-Alog",
                    modifier = Modifier.padding(20.dp),
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = 20.sp
                    )
                )
                Button(onClick = {
                    startSoundPlayer.seekTo(0)
                    startSoundPlayer.start()
                    isGameStarted = true
                    startNewRound()
                }) {
                    Text("Start")
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (gameOver) {
                    Text(
                        text = "Game Over! Score: $score",
                        modifier = Modifier.padding(20.dp),
                        style = TextStyle(
                            color = Color.Red,
                            fontSize = 20.sp
                        )
                    )
                    Button(
                        onClick = {
                            startSoundPlayer.seekTo(0)
                            startSoundPlayer.start()
                            sequence.clear()
                            userSequence = listOf()
                            gameOver = false
                            score = 0
                            startNewRound()
                        }
                    ) {
                        Text("Restart")
                    }
                } else {
                    Text(
                        text = "Cat-Alog",
                        modifier = Modifier.padding(20.dp),
                        style = TextStyle(
                            color = Color.Red,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "Score: $score",
                        modifier = Modifier.padding(8.dp),
                        style = TextStyle(
                            color = Color.Red,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp
                        )
                    )
                    if (isShowingSequence) {
                        currentImageIndex?.let { index ->
                            val imageUrl = sequence[index]
                            val painter = rememberAsyncImagePainter(imageUrl)
                            Image(
                                painter = painter,
                                contentDescription = "Current Image",
                                modifier = Modifier
                                    .size(140.dp)
                                    .padding(2.dp)
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            state = rememberLazyGridState(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items((0 until 16).toList()) { index ->
                                val imageUrl = sequence[index]
                                if (imageUrl != null) {
                                    val painter = rememberAsyncImagePainter(imageUrl)
                                    Image(
                                        painter = painter,
                                        contentDescription = "Selectable Image",
                                        modifier = Modifier
                                            .size(140.dp)
                                            .padding(2.dp)
                                            .clickable {
                                                userSequence = userSequence + index
                                                if (userSequence.size == sequence.size) {
                                                    checkUserSequence()
                                                }
                                            }
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(140.dp)
                                            .padding(2.dp)
                                            .background(Color.Gray)
                                    )
                                }
                            }
                        }
                    }
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
