package com.dibe.chess.Presentation

sealed class ChessEvent {
    data class Play(var cellClicked : String , var cellFrom : String) : ChessEvent()
    data class Activate(var cellClicked : String) : ChessEvent()
    object Restart: ChessEvent()
}
