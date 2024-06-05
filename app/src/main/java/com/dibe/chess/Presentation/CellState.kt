package com.dibe.chess.Presentation

data class CellState (
        var piece: ChessPiece? = null,
        var text: String? = null,
        var empty: Boolean = true,
        var highlighted: Boolean = false,
        var danger: Boolean = false,
        var wasMovedFrom: Boolean = false,
        var wasMovedTo: Boolean = false,
        var isCheckMate: Boolean = false,
                     )
