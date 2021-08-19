package com.github.nthily.poptimer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.github.nthily.poptimer.components.BottomBar
import com.github.nthily.poptimer.pages.RecordPage
import com.github.nthily.poptimer.pages.TimerPage
import com.github.nthily.poptimer.repository.DataRepository
import org.koin.androidx.compose.get

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun PopTimer(
) {
    val dataRepository: DataRepository = get()
    Scaffold(
        bottomBar = { BottomBar() },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            when(dataRepository.bottomNavigationItem.observeAsState().value) {
                1 -> TimerPage()
                2 -> RecordPage()
            }
        }
    }
}
