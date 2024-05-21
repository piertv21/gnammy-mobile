package com.example.gnammy.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gnammy.ui.composables.RecipeCardSmall
import java.time.LocalDate

@Composable
fun SearchScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var numberInput by remember { mutableStateOf("") }

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
            modifier = Modifier.fillMaxWidth()
        )

        DatePicker(selectedDate) { date ->
            selectedDate = date
        }

        OutlinedTextField(
            value = numberInput,
            onValueChange = { value ->
                if (value.all { it.isDigit() }) {
                    numberInput = value
                }
            },
            label = { Text("Numero di like") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
            modifier = Modifier.fillMaxSize()
                .padding(top = 8.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(10) {
                RecipeCardSmall(modifier = Modifier.padding(5.dp))
            }
        }
    }
}

@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedDate.toString(),
            onValueChange = {},
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DatePickerDialog(onDateSelected = {
                onDateChange(it)
                expanded = false
            })
        }
    }
}

@Composable
fun DatePickerDialog(onDateSelected: (LocalDate) -> Unit) {
    // TODO Implementare date picker dialog
    val date = LocalDate.now() // Dummy date
    Button(onClick = { onDateSelected(date) }) {
        Text("Select Date: $date")
    }
}