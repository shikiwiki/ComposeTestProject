package com.example.composeornot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.composeornot.components.HomeScreen
import com.example.composeornot.ui.theme.ComposeOrNotTheme

class MainActivity : ComponentActivity() {

    lateinit var navController : NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ComposeOrNotTheme {
                navController = rememberNavController()
                SetupNavaGraph(navController = navController)

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyPreview() {
    ComposeOrNotTheme {
        HomeScreen(navController = rememberNavController())
    }
}