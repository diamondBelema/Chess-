package com.dibe.chess.Presentation

data class ChessState(
        var archive: List<String> = emptyList(),
        var collectedPiecesWhite : List<String> = emptyList(),
        var collectedPiecesBlack : List<String> = emptyList(),
        var checkMate: Boolean = false,
        val isActivated: Boolean = false,
        val clickedPiece: String? = null,
        val possibleMoves: List<String> = emptyList(),
        val check: Boolean = false,
        val kingInCheck : String? = null,
        var chessBoard : MutableMap<String, CellState> = initializeChessBoard(),
        var turn : Color = Color.WHITE
                     )