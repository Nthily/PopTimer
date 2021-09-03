package com.github.nthily.poptimer.pages.record

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.nthily.poptimer.components.Screen
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.utils.Utils
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import org.koin.androidx.compose.get

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDetails(
    primaryKey: Int,
    navController: NavHostController,
) {
    val dataRepository: DataRepository = get()
    val puzzle = dataRepository.query(primaryKey).collectAsState(initial = null).value
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            },
            elevation = 0.dp,
            backgroundColor = Color.Transparent
        )
        puzzle?.let {
            val timestamp = ZonedDateTime.ofInstant(Instant.ofEpochSecond(it.timestamp), ZoneId.systemDefault())
            val date = timestamp.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            val time = Utils.timeFormat(timestamp, LocalContext.current)
        }
    }
}
