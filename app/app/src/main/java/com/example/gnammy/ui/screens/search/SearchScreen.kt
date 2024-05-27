package com.example.gnammy.ui.screens.search

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gnammy.ui.composables.Picker
import com.example.gnammy.ui.composables.RecipeCardSmall
import com.example.gnammy.ui.composables.rememberPickerState
import java.util.Locale

enum class ExpandedChip {
    LIKES,
    DATE,
    NONE
}

enum class DatePickerType(val descriptor: String) {
    FROM("Dopo"),
    TO("Prima"),
    RANGE("Intervallo")
}

@Composable
fun SearchScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }

    val datePickerType = remember { mutableStateOf(DatePickerType.FROM) }

    val expandedChip = remember { mutableStateOf(ExpandedChip.NONE) }

    val likesChipEnabled = remember { mutableStateOf(false) }
    val dateChipEnabled = remember { mutableStateOf(false) }

    val likesContent = remember { mutableStateOf("") }
    val dateContent = remember { mutableStateOf("") }

    val chipsRowScrollState = rememberScrollState()
    val chipsModifier = Modifier.defaultMinSize(minWidth = 120.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Cerca") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
            }
        )

        Row (modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(chipsRowScrollState),
        ) {

            InputChip(
                onClick = {
                    if (!dateChipEnabled.value) {
                        dateChipEnabled.value = true
                        expandedChip.value = ExpandedChip.DATE
                    }
                },
                label = { Text(dateContent.value) },
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
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Localized description",
                        Modifier
                            .size(InputChipDefaults.AvatarSize)
                            .clickable(onClick = {
                                dateChipEnabled.value = false
                            })
                    )
                },
                modifier = chipsModifier
            )

            Spacer(modifier = Modifier.padding(4.dp))

            InputChip(
                onClick = {
                    if(!likesChipEnabled.value) {
                        likesChipEnabled.value = true
                        expandedChip.value = ExpandedChip.LIKES
                    }
                },
                label = { Text(likesContent.value) },
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
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Localized description",
                        Modifier
                            .size(InputChipDefaults.AvatarSize)
                            .clickable(onClick = {
                                likesChipEnabled.value = false
                            })
                    )
                },
                modifier = chipsModifier
            )

            Spacer(modifier = Modifier.padding(4.dp))
        }

        when (expandedChip.value) {
            ExpandedChip.LIKES -> {
                LikesPickerFilter(likesContent)
                Button(
                    onClick = { expandedChip.value = ExpandedChip.NONE},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DONE")
                }
            }
            ExpandedChip.DATE -> {
                DatePickerFilter(datePickerType, dateContent)
                Button(
                    onClick = { expandedChip.value = ExpandedChip.NONE},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DONE")
                }
            }
            ExpandedChip.NONE -> {}
        }


        Button(
            onClick = {
                // TODO Handle search action
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerca")
        }

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(10) {
                RecipeCardSmall(navController, Modifier.padding(5.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFilter(
    datePickerType: MutableState<DatePickerType>,
    dateContent: MutableState<String>
) {
    val dateRangePickerState = rememberDateRangePickerState()
    val datePickerState = rememberDatePickerState()
    val selectedDate = remember { mutableStateOf("") }
    val selectedDateStart = remember { mutableStateOf("") }
    val selectedDateEnd = remember { mutableStateOf("") }

    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
    ) {
        DatePickerType.entries.forEach { entry ->
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(0.3f)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .clickable(onClick = { datePickerType.value = entry })
                    .padding(vertical = 10.dp)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = datePickerType.value == entry,
                        onClick = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = entry.descriptor
                    )
                }
            }
        }
    }

    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)

    if (datePickerType.value == DatePickerType.RANGE) {
        DateRangePicker (
            state = dateRangePickerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            title = null,
            headline = null,
            showModeToggle = false
        )
    } else {
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

    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            selectedDate.value = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(it)
            when (datePickerType.value) {
                DatePickerType.FROM -> dateContent.value = "Publicato dopo il ${selectedDate.value}"
                DatePickerType.TO -> dateContent.value = "Publicato prima del ${selectedDate.value}"
                else -> {}
            }

        }
    }

    LaunchedEffect(dateRangePickerState.selectedEndDateMillis) {
        dateRangePickerState.selectedStartDateMillis?.let {
            selectedDateStart.value = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(it)
            dateContent.value = "${selectedDateStart.value} -> "
        }
        dateRangePickerState.selectedEndDateMillis?.let {
            selectedDateEnd.value = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(it)
            dateContent.value = "${selectedDateStart.value} ->  ${selectedDateEnd.value}"
        }
    }
}

@Composable
fun LikesPickerFilter(
    selectedValue: MutableState<String>
) {
    val likesPickerState = rememberPickerState()

    Row (modifier = Modifier.fillMaxWidth()) {
        Picker (
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

