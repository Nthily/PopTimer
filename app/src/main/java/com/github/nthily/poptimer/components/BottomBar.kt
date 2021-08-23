package com.github.nthily.poptimer.components

import androidx.annotation.StringRes
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.utils.Utils
import com.github.nthily.poptimer.viewModel.TimerPageViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

sealed class Screen(val route: String) {
    object Timer : Screen("TimerPage")
    object Record : Screen("RecordPage")
    object Setting : Screen("SettingPage")
    object About : Screen("AboutPage")
}

@Composable
fun BottomBar(
    navController: NavController
) {

    val timerPageViewModel = getViewModel<TimerPageViewModel>()
    val items = listOf(Screen.Timer, Screen.Record, Screen.Setting)

    Crossfade(targetState = timerPageViewModel.isTiming) {
        when(it) {
            false -> {
                BottomNavigation(
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .border(1.dp, Color(0xFFF2F2F2)),
                ) {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                when(screen){
                                    Screen.Timer -> Icon(painterResource(id = R.drawable.timer), contentDescription = null)
                                    Screen.Record -> Icon(painterResource(id = R.drawable.analytics), contentDescription = null)
                                    else -> Icon(Icons.Filled.Settings, contentDescription = null)
                                }
                            },
                            selected = currentDestination?.hierarchy?.any { it -> it.route == screen.route } == true,
                            onClick = {
                                if(currentDestination!!.route != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(currentDestination.route!!) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
