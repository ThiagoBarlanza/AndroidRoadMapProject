package pt.drunkdroidos.cat_alog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import pt.drunkdroidos.cat_alog.ui.theme.CatalogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        score = "Score",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GameScreen(score: String, modifier: Modifier = Modifier) {

    fun getRandomCatImageUrl(): String {
        return "https://api.thecatapi.com/v1/images/search"
    }
    val randomCatImageUrl = getRandomCatImageUrl()


    Box (modifier = Modifier.fillMaxSize()){
    Row {
        Text(
            text = "$score!",
            modifier = modifier
                .weight(1f)
        )
        Image(
                painter = painterResource(id = R.drawable.catbread),
        contentDescription = "Cat Description",
        modifier
            .weight(1f)
        )
    }
    AsyncImage(
        model = "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_square.jpg",
        contentDescription = null,
    )
}


}

@Composable
fun AsyncImage(model: String, contentDescription: Nothing?) {
   model = "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_square.jpg",
    contentDescription = null

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CatalogTheme {
        GameScreen("Score")
    }
}