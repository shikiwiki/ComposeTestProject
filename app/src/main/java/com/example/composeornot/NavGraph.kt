package com.example.composeornot

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composeornot.components.DetailScreen
import com.example.composeornot.components.HomeScreen


@Composable
fun SetupNavaGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.Detail.route) {
            DetailScreen(navController)
        }
    }
}