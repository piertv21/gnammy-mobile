package com.example.gnammy

import NavigationBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gnammy.ui.GnammyNavGraph
import com.example.gnammy.ui.GnammyRoute
import com.example.gnammy.ui.composables.TopBar
import com.example.gnammy.ui.theme.GnammyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            //test colori in altri temi:  GnammyTheme (darkTheme = false) {
            GnammyTheme(dynamicColor = false, darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            GnammyRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: GnammyRoute.Home
                        }
                    }

                    Scaffold(
                        bottomBar = {
                            if (currentRoute !in setOf(GnammyRoute.Login, GnammyRoute.Register)) {
                                NavigationBar(navController, currentRoute)
                            }
                        },
                        topBar = {
                            TopBar(navController, currentRoute)
                        }
                    ) { contentPadding ->
                        GnammyNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        //locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        //locationService.resumeLocationRequest()
    }
}
