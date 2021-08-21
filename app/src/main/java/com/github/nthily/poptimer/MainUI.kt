package com.github.nthily.poptimer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.nthily.poptimer.components.BottomBar
import com.github.nthily.poptimer.pages.RecordPage
import com.github.nthily.poptimer.pages.SettingPage
import com.github.nthily.poptimer.pages.TimerPage
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.viewModel.RecordPageViewModel
import com.github.nthily.poptimer.viewModel.TimerPageViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun PopTimer() {
    val navController = rememberNavController()
    val dataRepository: DataRepository = get()


    val recordPageViewModel = getViewModel<RecordPageViewModel>()
    val timerPageViewModel = getViewModel<TimerPageViewModel>()

    LaunchedEffect(true) {
        timerPageViewModel.init()
    }

    Scaffold(
        bottomBar = { BottomBar(navController) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            NavHost(
                navController = navController,
                startDestination = dataRepository.bottomNavigationItem.value!!
            ) {
                composable("TimerPage") {
                    TimerPage(timerPageViewModel)
                }
                composable("RecordPage") {
                    RecordPage(recordPageViewModel)
                }
                composable("SettingPage") {
                    SettingPage()
                }
            }
        }
    }
}
