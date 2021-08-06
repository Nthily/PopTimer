package com.github.nthily.poptimer.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.viewModel.AppViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

@ExperimentalComposeUiApi
@Composable
fun TimerPage() {
    val appViewModel = hiltViewModel<AppViewModel>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if (!appViewModel.isTiming) {
                    appViewModel.startTimer()
                } else {
                    appViewModel.stopTimer()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            String.format(
                when (appViewModel.time) {
                    in 0 until 60 * 1000 -> "%1\$tS.%1\$tL"
                    in 60 * 1000 until 60 * 60 * 1000 -> "%1\$tM:%1\$tS.%1\$tL"
                    else -> "%2\$d:%1\$tM:%1\$tS.%1\$tL"
                },
                appViewModel.time, TimeUnit.MILLISECONDS.toHours(appViewModel.time)
            ).replace(Regex("(^0)|(\\d$)"), ""), // removing numbers at the beginning and end
            fontWeight = FontWeight.W700,
            fontSize = 56.sp
        )
    }
}