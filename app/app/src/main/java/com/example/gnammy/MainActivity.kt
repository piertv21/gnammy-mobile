package com.example.gnammy

import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    //private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*locationService = get<LocationService>()

        setContent {
            GnammyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            TravelDiaryRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: TravelDiaryRoute.Home
                        }
                    }

                    Scaffold(
                        topBar = { AppBar(navController, currentRoute) }
                    ) { contentPadding ->
                        TravelDiaryNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }*/
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
