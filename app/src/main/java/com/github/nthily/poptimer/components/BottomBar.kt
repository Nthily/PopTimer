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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.viewModel.TimerPageViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

object Destinations {
    const val TIMERPAGE = "TimerPage"
    const val RECORDPAGE = "RecordPage"
    const val SETTINGPAGE = "SettingPage"
    val list = listOf(TIMERPAGE, RECORDPAGE, SETTINGPAGE)
}

@Composable
fun BottomBar(
    navController: NavController
) {
    val timerPageViewModel = getViewModel<TimerPageViewModel>()
    val dataRepository: DataRepository = get()
    val bottomNavigationItem by dataRepository.bottomNavigationItem.observeAsState()

    Crossfade(targetState = timerPageViewModel.isTiming) {
        when(it) {
            false -> {
                BottomNavigation(
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .border(1.dp, Color(0xFFF2F2F2)),
                ) {
                    for(index in Destinations.list) {
                        BottomNavigationItem(
                            icon = {
                                when(index){
                                    Destinations.TIMERPAGE -> Icon(painterResource(id = R.drawable.timer), contentDescription = null)
                                    Destinations.RECORDPAGE -> Icon(painterResource(id = R.drawable.analytics), contentDescription = null)
                                    else -> Icon(Icons.Filled.Settings, contentDescription = null)
                                }
                            },
                            selected = bottomNavigationItem == index,
                            onClick = {
                                if(bottomNavigationItem != index) {
                                    navController.navigate(index) {
                                        popUpTo(dataRepository.bottomNavigationItem.value!!) {
                                            inclusive = true
                                        }
                                    }
                                    dataRepository.bottomNavigationItem.value = index
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

