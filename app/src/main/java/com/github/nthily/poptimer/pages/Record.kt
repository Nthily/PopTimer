package com.github.nthily.poptimer.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.components.SecondaryText
import com.github.nthily.poptimer.utils.Puzzles
import com.github.nthily.poptimer.utils.Utils
import com.github.nthily.poptimer.viewModel.AppViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordPage() {

    val listState = rememberLazyListState()
    val appViewModel = hiltViewModel<AppViewModel>()
    val all = appViewModel.all.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp),
    ) {
        RecordPageTopBar()
        Spacer(Modifier.padding(vertical = 5.dp))
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 120.dp),
            state = listState,
            modifier = Modifier
                .padding(bottom = appViewModel.bottomPadding),

        ) {
            all.value?.let {
                itemsIndexed(it.asReversed()) { _, item ->
                    RecordCard(
                        score = Utils.format(item.solveTime!!),
                        type = item.type
                    )
                }
            }
        }
    }
}

@Composable
fun RecordPageTopBar() {
    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .height(48.dp),
            elevation = 6.dp
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.history),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RecordCard(
    score: String,
    type: Puzzles
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        modifier = Modifier
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable {

                }
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.TopStart
                    ) {
                        SecondaryText {
                            Text(
                                text = "08/09",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    letterSpacing = 0.sp
                                )
                            )
                        }
                    }
                    Box(

                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Image(
                            painterResource(
                                id = when(type) {
                                    Puzzles.TWO -> R.drawable.ic_2x2
                                    Puzzles.THREE -> R.drawable.ic_3x3
                                    Puzzles.FOUR -> R.drawable.ic_4x4
                                    Puzzles.FIVE -> R.drawable.ic_5x5
                                    Puzzles.SIX -> R.drawable.ic_6x6
                                    Puzzles.SEVEN -> R.drawable.ic_7x7
                                    Puzzles.PYRA -> R.drawable.ic_pyra
                                    Puzzles.SQ1 -> R.drawable.ic_sq1
                                    Puzzles.MEGA -> R.drawable.ic_mega
                                    Puzzles.CLOCK -> R.drawable.ic_clock
                                    Puzzles.SKEWB -> R.drawable.ic_skewb
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = score,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700)
                    )
                }
            }
        }
    }
}
