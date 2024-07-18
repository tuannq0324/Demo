package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.MainViewModel
import com.example.myapplication.ui.common.NavigationScreen.FourthScreen
import com.example.myapplication.ui.common.NavigationScreen.FirstScreen
import com.example.myapplication.ui.common.NavigationScreen.SecondScreen
import com.example.myapplication.ui.common.NavigationScreen.ThirdScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    val viewModel = viewModel<MainViewModel>()

                    var navigationSelectedItem by remember {
                        mutableIntStateOf(0)
                    }
                    val navController = rememberNavController()

                    //bottom bar
                    Scaffold(modifier = Modifier, bottomBar = {
                        NavigationBar {
                            //getting the list of bottom navigation items for our data class
                            BottomNavigationItem().bottomNavigationItems()
                                .forEachIndexed { index, navigationItem ->

                                    //iterating all items with their respective indexes
                                    NavigationBarItem(selected = index == navigationSelectedItem,
                                        label = {
                                            Text(navigationItem.label)
                                        },
                                        icon = {
                                            Icon(
                                                navigationItem.icon,
                                                contentDescription = navigationItem.label
                                            )
                                        },
                                        onClick = {
                                            navigationSelectedItem = index
                                            navController.navigate(navigationItem.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        })
                                }
                        }
                    }) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Screens.First.route,
                            modifier = Modifier.padding(paddingValues = paddingValues)
                        ) {
                            composable(Screens.First.route) {
                                //call our composable screens here
                                FirstScreen(viewModel)
                            }
                            composable(Screens.Second.route) {
                                //call our composable screens here
                                SecondScreen(viewModel)
                            }
                            composable(Screens.Third.route) {
                                //call our composable screens here
                                ThirdScreen(viewModel)
                            }
                            composable(Screens.Fourth.route) {
                                //call our composable screens here
                                FourthScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

//initializing the data class with default parameters
data class BottomNavigationItem(
    val label: String = "", val icon: ImageVector = Icons.Filled.Home, val route: String = ""
) {

    //function to get the list of bottomNavigationItems
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "First", route = Screens.First.route
            ),
            BottomNavigationItem(
                label = "Second", route = Screens.Second.route
            ),
            BottomNavigationItem(
                label = "Third", route = Screens.Third.route
            ),
            BottomNavigationItem(
                label = "Fourth", route = Screens.Fourth.route
            ),
        )
    }
}

sealed class Screens(val route: String) {
    data object First : Screens("first_route")
    data object Second : Screens("second_route")
    data object Third : Screens("third_route")
    data object Fourth : Screens("fourth_route")
}