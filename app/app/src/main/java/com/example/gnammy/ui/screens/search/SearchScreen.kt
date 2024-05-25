package com.example.gnammy.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.gnammy.ui.composables.RecipeCardSmall

@OptIn(ExperimentalMaterial3Api::class, )
@Composable
fun SearchScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }

    val openDialog = remember { mutableStateOf(false) }
    var likesInput by remember { mutableStateOf("") }

    var fromSelectedDate by remember { mutableStateOf("") }
    val fromDatePickerState = rememberDatePickerState()
    val toDatePickerState = rememberDatePickerState()
    val fromDateChipEnabled = remember { mutableStateOf(false) }
    val toDateChipEnabled = remember { mutableStateOf(false) }
    val openFromDatePicker = remember { mutableStateOf(false) }
    val openToDatePicker = remember { mutableStateOf(false) }

    LaunchedEffect(fromDatePickerState.selectedDateMillis) {
        fromDatePickerState.selectedDateMillis?.let {
            fromSelectedDate = java.text.SimpleDateFormat("yyyy-MM-dd").format(it)
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
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    InputChip(
                        onClick = {
                            if(!fromDateChipEnabled.value) {
                                openFromDatePicker.value = true
                                fromDateChipEnabled.value = true
                            }
                        },
                        label = { Text("Publicato dopo il $fromSelectedDate") },
                        selected = fromDateChipEnabled.value,
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
                                Modifier.size(InputChipDefaults.AvatarSize).clickable( onClick =  {
                                    fromDateChipEnabled.value = false
                                    fromSelectedDate = ""
                                    openFromDatePicker.value = false
                                })
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

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
                        if(fromSelectedDate.isNotEmpty()) {
                            openFromDatePicker.value = false
                        }
                    }
                }
            }
        }
    }
}
