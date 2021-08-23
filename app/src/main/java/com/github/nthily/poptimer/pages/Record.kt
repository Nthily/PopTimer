package com.github.nthily.poptimer.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.components.SecondaryText
import com.github.nthily.poptimer.database.Puzzle
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.utils.Puzzles
import com.github.nthily.poptimer.utils.Utils
import com.github.nthily.poptimer.viewModel.RecordPageViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordPage(
    recordPageViewModel: RecordPageViewModel
) {

    val listState = rememberLazyListState()
    val dataRepository: DataRepository = get()
    val all = dataRepository.all.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp),
    ) {
        RecordPageTopBar()
        Spacer(Modifier.padding(vertical = 5.dp))
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 120.dp),
            state = listState,
        ) {
            all.value?.let {
                itemsIndexed(it.asReversed()) { index, item ->
                    RecordCard(
                        puzzle = item,
                        score = Utils.solveTimeFormat(item.solveTime!!),
                        onClick = {
                            recordPageViewModel.detailIndex = it.size - index - 1
                            recordPageViewModel.displayDetail = true
                        }
                    )
                }
            }
        }
    }
    all.value?.let {
        if(it.isNotEmpty()) RecordDetailsDialog(
            puzzle = it[recordPageViewModel.detailIndex],
            recordPageViewModel = recordPageViewModel
        )
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordCard(
    puzzle: Puzzle,
    score: String,
    onClick: () -> Unit
) {
    val timestamp: ZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(puzzle.timestamp), ZoneId.systemDefault())
    val monthDay = Utils.monthDayFormat(timestamp, FormatStyle.MEDIUM)
    Surface(
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        modifier = Modifier
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    onClick = onClick
                )
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
                                text = monthDay,
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
                                id = Utils.getTypeImg(puzzle.type)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDetailsDialog(
    puzzle: Puzzle,
    recordPageViewModel: RecordPageViewModel
) {
    val timestamp = ZonedDateTime.ofInstant(Instant.ofEpochSecond(puzzle.timestamp), ZoneId.systemDefault())
    val date = timestamp.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

    val time = Utils.timeFormat(timestamp, LocalContext.current)

    if(recordPageViewModel.displayDetail) {
        Dialog(
            onDismissRequest = { recordPageViewModel.displayDetail = false },
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = Utils.solveTimeFormat(puzzle.solveTime!!),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(
                                onClick = {

                                }
                            ) {
                                Icon(Icons.Filled.Share, null)
                            }
                        }
                    }
                    Divider(
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(id = Utils.getTypeImg(puzzle.type)),
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 15.dp))
                        Text(
                            text = puzzle.scramble,
                            fontSize = 14.sp,
                        )
                    }
                    /*
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(painterResource(id = R.drawable.date), null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                Column {
                                    Text(
                                        text = date,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = time,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                     */
                }
            }
        }
    }
}
