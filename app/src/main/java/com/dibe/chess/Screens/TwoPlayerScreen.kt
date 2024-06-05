package com.dibe.chess.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dibe.chess.Presentation.ChessEvent
import com.dibe.chess.Presentation.ChessState
import com.dibe.chess.Presentation.ChessViewModel
import com.dibe.chess.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoPlayerScreen(navController: NavController, state: ChessState, onEvent: (ChessEvent) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text(text = "2 Player") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEvent(ChessEvent.Restart) }) {
                            Icon(Icons.Default.Refresh, contentDescription = "refresh")
                        }
                    }
                         )
            }
                 ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                  ) {
                CollectedPiecesRow(pieces = state.collectedPiecesWhite, backgroundColor = Color(0xFF476A6F))

                ChessBoard(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                          )

                CollectedPiecesRow(pieces = state.collectedPiecesBlack, backgroundColor = Color(0xFF476A6F))
            }

            if (state.checkMate) {
                AlertDialog(
                    icon = { Icon(Icons.Default.Info, contentDescription = "info") },
                    dismissButton = {
                        Button(onClick = { navController.popBackStack() }) {
                            Text(text = "Home")
                        }
                    },
                    confirmButton = {
                        Button(onClick = { onEvent(ChessEvent.Restart) }) {
                            Text(text = "Restart")
                        }
                    },
                    title = {
                        Text(text = "Game over ${state.chessBoard[state.kingInCheck]?.piece?.color?.name?.lowercase()} won")
                    },
                    text = {
                        Text(text = "Do you wish to restart?")
                    },
                    onDismissRequest = { }
                           )
            }
        }
    }
}

@Composable
fun CollectedPiecesRow(pieces: List<String>, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
       ) {
        pieces.forEach {
            Text(text = it, fontSize = 25.sp)
        }
    }
}

@Composable
fun ChessBoard(state: ChessState, onEvent: (ChessEvent) -> Unit, modifier: Modifier = Modifier) {
    val color1 = Color(0xFF476A6F)
    val color2 = Color(0xFF519E8A)
    val squareSize = 40.dp
    val columns = 'a'..'h'
    val rows = 1..8

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        Spacer(modifier = Modifier.padding(7.dp))

        Column {
            Box(modifier = Modifier.height((squareSize.value / 2).dp))
            rows.forEach { row ->
                Box(modifier = Modifier.height(squareSize)) {
                    Text(text = row.toString(), fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }
        }

        Column {
            Row {
                columns.forEach { column ->
                    Box(modifier = Modifier.weight(1f).width(squareSize)) {
                        Text(text = column.toString(), fontSize = 12.sp, textAlign = TextAlign.End)
                    }
                }
            }

            rows.forEach { row ->
                Row {
                    columns.forEach { column ->
                        val cellColor = if ((column - 'a' + row) % 2 == 0) color1 else color2
                        val cell = state.chessBoard["$column$row"]
                        Box(
                            modifier = Modifier
                                .border(width = 0.2.dp, color = Color.Black)
                                .size(squareSize)
                                .background(
                                    color = when {
                                        cell?.text == state.kingInCheck && state.check -> Color(0xFFBC0102)
                                        cell?.highlighted == true -> Color(0xFF328CBA)
                                        cell?.danger == true -> Color(0xFFFA6262)
                                        cell?.wasMovedFrom == true -> Color.LightGray
                                        cell?.wasMovedTo == true -> Color.DarkGray
                                        else -> cellColor
                                    }
                                           )
                                .clickable {
                                    if (state.isActivated) {
                                        onEvent(ChessEvent.Play(cellClicked = "$column$row", cellFrom = state.clickedPiece.orEmpty()))
                                    } else {
                                        onEvent(ChessEvent.Activate(cellClicked = "$column$row"))
                                    }
                                },
                            contentAlignment = Alignment.Center
                           ) {
                            Text(
                                text = cell?.text.orEmpty(),
                                fontSize = 30.sp,
                                color = if (cell?.piece?.color == com.dibe.chess.Presentation.Color.WHITE) Color.White else Color.Black
                                )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TwoPlayerScreenUIPreview() {
    val navController = rememberNavController()
    val viewModel: ChessViewModel = viewModel()
    val state = viewModel.state.collectAsState().value
    val onEvent = viewModel::onEvent
    TwoPlayerScreen(navController = navController, state = state, onEvent = onEvent)
}
