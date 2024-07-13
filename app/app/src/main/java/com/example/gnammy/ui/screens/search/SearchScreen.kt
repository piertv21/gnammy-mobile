package com.example.gnammy.ui.screens.search

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.gnammy.R
import com.example.gnammy.ui.composables.Picker
import com.example.gnammy.ui.composables.RecipeCardSmall
import com.example.gnammy.ui.composables.rememberPickerState
import com.example.gnammy.ui.viewmodels.GnamViewModel
import com.example.gnammy.utils.DateFormats
import com.example.gnammy.utils.millisToDateString
import java.util.Locale

enum class ExpandedChip {
    LIKES,
    DATE,
    NONE
}

enum class DatePickerType(val descriptorResId: Int) {
    FROM(R.string.date_after),
    TO(R.string.date_before),
    RANGE(R.string.date_range),
    NOT_SELECTED(0)
}

fun capitalize(string: String): String {
    return string.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}

@Composable
fun SearchScreen(
    navController: NavHostController,
    gnamViewModel: GnamViewModel,
    loggedUserId: String
) {
    val searchResult by gnamViewModel.searchResultsState.collectAsStateWithLifecycle()
    val isSearchingState by remember { gnamViewModel.isSearchingState }
    var searchText by remember { mutableStateOf("") }
    val datePickerType = remember { mutableStateOf(DatePickerType.NOT_SELECTED) }
    val expandedChip = remember { mutableStateOf(ExpandedChip.NONE) }
    val likesChipEnabled = remember { mutableStateOf(false) }
    val dateChipEnabled = remember { mutableStateOf(false) }
    val defaultLikesContent = stringResource(R.string.search_likes)
    val likesContent = remember { mutableStateOf(capitalize(defaultLikesContent)) }
    val dateFrom: MutableState<Long> = remember { mutableLongStateOf(0) }
    val dateTo: MutableState<Long> = remember { mutableLongStateOf(0) }
    val chipsModifier = Modifier.defaultMinSize(minWidth = 120.dp)
    val keyboardController = LocalSoftwareKeyboardController.current
    val loading = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(stringResource(R.string.search_search) + "...") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
            },
            shape = RoundedCornerShape(30.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            InputChip(
                onClick = {
                    if (!dateChipEnabled.value) {
                        dateChipEnabled.value = true
                        expandedChip.value = ExpandedChip.DATE
                    } else {
                        dateChipEnabled.value = false
                        expandedChip.value = ExpandedChip.NONE
                        dateTo.value = 0
                        dateFrom.value = 0
                        datePickerType.value = DatePickerType.NOT_SELECTED
                    }
                },
                label = {
                    when (datePickerType.value) {
                        DatePickerType.FROM -> {
                            Text(
                                capitalize(stringResource(R.string.date_from)) + " " + SimpleDateFormat(
                                    "dd/MM/yy", Locale.getDefault()
                                ).format(dateFrom.value),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 20.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        DatePickerType.TO -> {
                            Text(
                                capitalize(stringResource(R.string.date_to)) + " " + SimpleDateFormat(
                                    "dd/MM/yy", Locale.getDefault()
                                ).format(dateTo.value),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 20.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        DatePickerType.RANGE -> {
                            Text(
                                capitalize(stringResource(R.string.date_from)) + " " + SimpleDateFormat(
                                    "dd/MM/yy", Locale.getDefault()
                                ).format(dateFrom.value) + "\n" + capitalize(stringResource(R.string.date_to)) + " " +
                                        SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(
                                            dateTo.value
                                        ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 20.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        else -> {
                            Text(
                                capitalize(stringResource(R.string.date_not_selected)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 20.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                },
                selected = dateChipEnabled.value,
                avatar = {
                    Icon(
                        Icons.Filled.CalendarToday,
                        contentDescription = "",
                        Modifier
                            .size(InputChipDefaults.AvatarSize)
                            .padding(2.dp)
                    )
                },
                modifier = chipsModifier.fillMaxWidth(1 / 2f),
                shape = RoundedCornerShape(20.dp)
            )

            InputChip(
                onClick = {
                    if (!likesChipEnabled.value) {
                        likesChipEnabled.value = true
                        expandedChip.value = ExpandedChip.LIKES
                    } else {
                        likesContent.value = capitalize(defaultLikesContent)
                        likesChipEnabled.value = false
                        expandedChip.value = ExpandedChip.NONE
                    }
                },
                label = {
                    Text(
                        likesContent.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        textAlign = TextAlign.Center
                    )
                },
                selected = likesChipEnabled.value,
                avatar = {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "",
                        Modifier
                            .size(InputChipDefaults.AvatarSize)
                            .padding(2.dp)
                    )
                },
                modifier = chipsModifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            )
        }

        when (expandedChip.value) {
            ExpandedChip.LIKES -> {
                LikesPickerFilter(likesContent)
                Button(
                    onClick = {
                        expandedChip.value = ExpandedChip.NONE
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.input_chip_confirm))
                }
            }

            ExpandedChip.DATE -> {
                DatePickerFilter(datePickerType, dateFrom, dateTo)
                Button(
                    onClick = {
                        expandedChip.value = ExpandedChip.NONE
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.input_chip_confirm))
                }
            }

            ExpandedChip.NONE -> {}
        }
        if (expandedChip.value == ExpandedChip.NONE) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    loading.value = true

                    val dateFromValue = if (dateFrom.value == 0L) "" else millisToDateString(
                        dateFrom.value,
                        DateFormats.DB_FORMAT
                    )
                    val dateToValue = if (dateTo.value == 0L) "" else millisToDateString(
                        dateTo.value,
                        DateFormats.DB_FORMAT
                    )
                    gnamViewModel.fetchSearchResults(
                        loggedUserId,
                        searchText,
                        dateToValue,
                        dateFromValue,
                        likesContent.value.toIntOrNull()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.search_search))
            }
        }

        if (isSearchingState) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            LaunchedEffect(searchResult) {
                loading.value = false
            }
        } else if (searchResult.gnams.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("Nessuno gnam trovato.", modifier = Modifier.align(Alignment.Center))
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(searchResult.gnams, key = { it.id }) { gnam ->
                    RecipeCardSmall(
                        gnam = gnam,
                        navController,
                        Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFilter(
    datePickerType: MutableState<DatePickerType>,
    dateFrom: MutableState<Long>,
    dateTo: MutableState<Long>
) {
    val dateRangePickerState = rememberDateRangePickerState()
    val datePickerState = rememberDatePickerState()

    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
    ) {
        val options = DatePickerType.entries.filter { it != DatePickerType.NOT_SELECTED }.toList()
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, type ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = {
                        datePickerType.value = type
                    },
                    selected = datePickerType.value == type
                ) {
                    Text(capitalize(stringResource(type.descriptorResId)))
                }
            }
        }
    }
    if (datePickerType.value == DatePickerType.RANGE) {
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            title = null,
            headline = null,
            showModeToggle = false
        )
    } else if (datePickerType.value != DatePickerType.NOT_SELECTED) {
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)
        DatePicker(
            state = datePickerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / 1f),
            title = null,
            headline = null,
            showModeToggle = false
        )
    }

    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            when (datePickerType.value) {
                DatePickerType.FROM -> dateFrom.value = it
                DatePickerType.TO -> dateTo.value = it
                else -> {}
            }

        }
    }

    LaunchedEffect(dateRangePickerState.selectedEndDateMillis) {
        dateRangePickerState.selectedStartDateMillis?.let {
            dateFrom.value = it
        }
        dateRangePickerState.selectedEndDateMillis?.let {
            dateTo.value = it
        }
    }
}

@Composable
fun LikesPickerFilter(
    selectedValue: MutableState<String>
) {
    val likesPickerState = rememberPickerState()

    Row(modifier = Modifier.fillMaxWidth()) {
        Picker(
            items = listOf("10", "25", "50", "100"),
            state = likesPickerState,
            modifier = Modifier.fillMaxWidth(),
            startIndex = 0,
            visibleItemsCount = 3,
            textModifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(16.dp),
            textStyle = MaterialTheme.typography.titleLarge,
        )
    }

    LaunchedEffect(likesPickerState.selectedItem) {
        selectedValue.value = likesPickerState.selectedItem
    }
}

