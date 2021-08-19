package com.github.nthily.poptimer.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.viewModel.TimerPageViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun BottomBar(
) {
    val appViewModel = getViewModel<TimerPageViewModel>()
    val dataRepository: DataRepository = get()
    val bottomNavigationItem by dataRepository.bottomNavigationItem.observeAsState()

    Crossfade(targetState = appViewModel.isTiming) {
        when(it) {
            false -> {
                BottomNavigation(
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .border(1.dp, Color(0xFFF2F2F2)),
                ) {
                    for(index in 1..3) {
                        BottomNavigationItem(
                            icon = {
                                when(index){
                                    1 -> Icon(painterResource(id = R.drawable.timer), contentDescription = null)
                                    2 -> Icon(painterResource(id = R.drawable.analytics), contentDescription = null)
                                    else -> Icon(Icons.Filled.Settings, contentDescription = null)
                                }
                            },
                            selected = bottomNavigationItem == index,
                            onClick = {
                                dataRepository.bottomNavigationItem.value = index
                            }
                        )
                    }
                }
            }
        }
    }
}

fun getPageName(index: Int): String {
    return when(index) {
        1 -> "TimerPage"
        2 -> "RecordPage"
        else -> ""
    }
}
