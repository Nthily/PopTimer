package com.github.nthily.poptimer

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import com.github.nthily.poptimer.database.PuzzleDatabase
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.ui.theme.PopTimerTheme
import com.github.nthily.poptimer.utils.Utils
import com.github.nthily.poptimer.viewModel.TimerPageViewModel
import com.github.nthily.poptimer.viewModel.RecordPageViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class PopTimerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PopTimerApp)
            modules(
                module {
                    factory(named("IO")) { CoroutineScope(Dispatchers.IO) }
                    single { PuzzleDatabase.getDatabase(get(), get(named("IO"))) }
                    single { DataRepository(get<PuzzleDatabase>().getPuzzleDao()) }
                    viewModel { TimerPageViewModel(get(), get()) }
                    viewModel { RecordPageViewModel(get()) }
                }
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(this)
        setContent {
            PopTimerTheme {

                val systemUiController = rememberSystemUiController()
                val timerPageViewModel = getViewModel<TimerPageViewModel>()

                LaunchedEffect(true) {
                    Utils.log("puzzle vm $timerPageViewModel")
                    timerPageViewModel.init()
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
