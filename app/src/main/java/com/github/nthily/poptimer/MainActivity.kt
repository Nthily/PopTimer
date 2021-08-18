package com.github.nthily.poptimer

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.ui.theme.PopTimerTheme
import com.github.nthily.poptimer.viewModel.AppViewModel
import com.github.nthily.poptimer.viewModel.PuzzleViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tencent.mmkv.MMKV
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PopTimerApp: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(this)
        setContent {
            PopTimerTheme {

                val systemUiController = rememberSystemUiController()
                val puzzleViewModel = hiltViewModel<PuzzleViewModel>()

                LaunchedEffect(true) {
                    puzzleViewModel.init()
                }

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.White
                    )
                }

                PopTimer()
            }
        }
    }
}

/*
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewTopAppBar() {
    PopTimerTheme {
        BottomBar()
    }
}
 */