package com.dibe.chess

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dibe.chess.Presentation.ChessEvent
import com.dibe.chess.Presentation.ChessState
import com.dibe.chess.Screens.HomeScreen
import com.dibe.chess.Screens.TwoPlayerScreen

@Composable
fun Nav(state: ChessState, onEvent: (ChessEvent) -> Unit) {
    val navController = rememberNavController()
    NavHost(navController =  navController, startDestination = Screen.HOME.name) {
        composable(route = Screen.HOME.name){
            HomeScreen(navController = navController)
        }

        composable(route = Screen.TWOPLAYERS.name){
            TwoPlayerScreen(navController = navController, state, onEvent)
        }
    }
}