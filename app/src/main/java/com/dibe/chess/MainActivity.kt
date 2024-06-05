package com.dibe.chess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dibe.chess.Presentation.ChessViewModel
import com.dibe.chess.ui.theme.ChessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessTheme (darkTheme = true){
                val viewModel: ChessViewModel = viewModel()
                val state = viewModel.state.collectAsState().value
                val onEvent = viewModel::onEvent
                Nav(state, onEvent)
            }
        }
    }
}
