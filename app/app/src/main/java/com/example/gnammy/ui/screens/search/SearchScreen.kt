package com.example.gnammy.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.gnammy.ui.composables.Picker
import com.example.gnammy.ui.composables.RecipeCardSmall
import com.example.gnammy.ui.composables.rememberPickerState

@OptIn(ExperimentalMaterial3Api::class, )
@Composable
fun SearchScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }

    val openDialog = remember { mutableStateOf(false) }
    var likesInput by remember { mutableStateOf("") }

    val fromSelectedDate = remember { mutableStateOf("") }
    val toSelectedDate = remember { mutableStateOf("") }
    val fromDatePickerState = rememberDatePickerState()
    val toDatePickerState = rememberDatePickerState()
    val fromDateChipEnabled = remember { mutableStateOf(false) }
    val toDateChipEnabled = remember { mutableStateOf(false) }
    val openFromDatePicker = remember { mutableStateOf(false) }
    val openToDatePicker = remember { mutableStateOf(false) }

    val chipEnabled = remember { mutableStateOf(false) }
    val openNumberPicker = remember { mutableStateOf(false) }
    val selectedValue = remember { mutableStateOf("") }
    val valuesPickerState = rememberPickerState()


    LaunchedEffect(fromDatePickerState.selectedDateMillis) {
        fromDatePickerState.selectedDateMillis?.let {
            fromSelectedDate.value = java.text.SimpleDateFormat("yyyy-MM-dd").format(it)
        }
    }

    LaunchedEffect(toDatePickerState.selectedDateMillis) {
        toDatePickerState.selectedDateMillis?.let {
            toSelectedDate.value = java.text.SimpleDateFormat("yyyy-MM-dd").format(it)
        }
    }

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
                IconButton(
                    onClick = {
                        openDialog.value = true
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Tune, contentDescription = null)
                }
            }
        )

        val scrollstate = rememberScrollState()
        val width = 120.dp
        Row (modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollstate),
        ) {
            // TODO: controllo che le date scelte siano compatibili
            DateFilter(
                fromDateChipEnabled,
                openFromDatePicker,
                fromSelectedDate,
                fromDatePickerState,
                "Pubblicato dopo il",
                modifier = Modifier.defaultMinSize(minWidth = width).padding(end = 8.dp)
            )
            DateFilter(
                toDateChipEnabled,
                openToDatePicker,
                toSelectedDate,
                toDatePickerState,
                "Pubblicato prima il",
                modifier = Modifier.defaultMinSize(minWidth = width).padding(end = 8.dp)

            )
            NumberFilter(
                chipEnabled,
                openNumberPicker,
                selectedValue,
                listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),
                "Likes",
                modifier = Modifier.defaultMinSize(minWidth = width)
            )
        }


        if (openFromDatePicker.value) {
            DatePicker(
                state = fromDatePickerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                title = null,
                headline = null,
                showModeToggle = false
            )
            if (fromSelectedDate.value.isNotEmpty()) {
                openFromDatePicker.value = false
            }
        }

        if (openToDatePicker.value) {
            DatePicker(
                state = toDatePickerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                title = null,
                headline = null,
                showModeToggle = false
            )
            if (toSelectedDate.value.isNotEmpty()) {
                openToDatePicker.value = false
            }
        }

        if (openNumberPicker.value) {
            Row (modifier = Modifier.fillMaxWidth()) {
                Picker (
                    items = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),
                    state = valuesPickerState,
                    modifier = Modifier.fillMaxWidth(),
                    startIndex = 0,
                    visibleItemsCount = 3,
                    textModifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(16.dp),
                    textStyle = MaterialTheme.typography.titleLarge,
                )
            }
            Button(
                onClick = { openNumberPicker.value = false},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("DONE")
            }
            LaunchedEffect(valuesPickerState.selectedItem) {
                selectedValue.value = valuesPickerState.selectedItem
            }
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

    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            },
            properties = DialogProperties(dismissOnBackPress = true),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {
                    // TODO: controllo che le date scelte siano compatibili
                    DateFilter(
                        fromDateChipEnabled,
                        openFromDatePicker,
                        fromSelectedDate,
                        fromDatePickerState,
                        "Dal"
                    )
                    DateFilter(
                        toDateChipEnabled,
                        openToDatePicker,
                        toSelectedDate,
                        toDatePickerState,
                        "Al"
                    )
                    NumberFilter(
                        chipEnabled,
                        openNumberPicker,
                        selectedValue,
                        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),
                        "Likes")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilter(
    inputChipEnabled: MutableState<Boolean>,
    openDatePicker: MutableState<Boolean>,
    selectedDate: MutableState<String>,
    datePickerState: DatePickerState,
    description: String,
    modifier: Modifier = Modifier
) {
        InputChip(
            onClick = {
                if (!inputChipEnabled.value) {
                    openDatePicker.value = true
                    inputChipEnabled.value = true
                }
            },
            label = { Text("$description ${selectedDate.value}") },
            selected = inputChipEnabled.value,
            avatar = {
                Icon(
                    Icons.Filled.CalendarToday,
                    contentDescription = "",
                    Modifier.size(InputChipDefaults.AvatarSize)
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Localized description",
                    Modifier
                        .size(InputChipDefaults.AvatarSize)
                        .clickable(onClick = {
                            inputChipEnabled.value = false
                            selectedDate.value = ""
                            openDatePicker.value = false
                        })
                )
            },
            modifier = modifier
        )

}

@Composable
fun NumberFilter(
    chipEnabled: MutableState<Boolean>,
    openNumberPicker: MutableState<Boolean>,
    selectedValue: MutableState<String>,
    values: List<String>,
    description: String,
    modifier: Modifier = Modifier
) {
        InputChip(
            onClick = {
                if(!chipEnabled.value) {
                    openNumberPicker.value = true
                    chipEnabled.value = true
                }
            },
            label = { Text("$description ${selectedValue.value}") },
            selected = chipEnabled.value,
            avatar = {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "",
                    Modifier.size(InputChipDefaults.AvatarSize)
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Localized description",
                    Modifier
                        .size(InputChipDefaults.AvatarSize)
                        .clickable(onClick = {
                            chipEnabled.value = false
                            selectedValue.value = ""
                            openNumberPicker.value = false
                        })
                )
            },
            modifier = modifier
        )
}
