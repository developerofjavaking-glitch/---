package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.QuranViewModel
import com.example.ui.screens.SurahDetailScreen
import com.example.ui.screens.SurahListScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuranApp()
                }
            }
        }
    }
}

@Composable
fun QuranApp(viewModel: QuranViewModel = viewModel()) {
    val selectedSurah by viewModel.selectedSurah.collectAsState()

    BackHandler(enabled = selectedSurah != null) {
        viewModel.selectSurah(null)
    }

    AnimatedContent(
        targetState = selectedSurah,
        transitionSpec = {
            if (targetState != null) {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width / 3 } + fadeOut()
                )
            } else {
                (slideInHorizontally { width -> -width / 3 } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> width } + fadeOut()
                )
            }
        },
        label = "quran_screen_transition"
    ) { surah ->
        if (surah != null) {
            SurahDetailScreen(
                surah = surah,
                viewModel = viewModel,
                onBackClick = {
                    viewModel.selectSurah(null)
                }
            )
        } else {
            SurahListScreen(
                viewModel = viewModel,
                onSurahSelected = { chosenSurah ->
                    viewModel.selectSurah(chosenSurah)
                }
            )
        }
    }
}

/**
 * Kept for test compatibility
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
