package com.example.gnammy.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.example.gnammy.ui.composables.RecipeCardBig
import kotlinx.coroutines.launch

@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier) {
    val state = rememberSwipeableCardState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
                .swipableCard(
                    state = state,
                    onSwiped = { direction ->
                        println("The card was swiped to $direction")
                    },
                    onSwipeCancel = {
                        println("The swiping was cancelled")
                    }
                )
        ) {
            RecipeCardBig(modifier = Modifier.padding(16.dp))
        }

        Row (
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth()
        ){
            Button(
                onClick = {
                    scope.launch {
                        state.swipe(Direction.Right)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(1f/2f)
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
                    .padding(16.dp, 0.dp, 16.dp, 16.dp)
            ) {
                Text("Like")
            }
            Button(
                onClick = {
                    scope.launch {
                        state.swipe(Direction.Left)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxSize()
                    .padding(16.dp, 0.dp, 16.dp, 16.dp)
            ) {
                Text("Dislike")
            }
        }
    }

    LaunchedEffect(state.swipedDirection){
        if (state.swipedDirection!=null) {
            println("The card was swiped to ${state.swipedDirection!!}")
        }
    }
}