package com.dibe.chess.Presentation

fun initializeChessBoard(): MutableMap<String, CellState> {
    val board = mutableMapOf<String , CellState>()
    val columns = 'a'..'h'
    val rows = 1..8

    // Initialize all cells as empty
    for (column in columns) {
        for (row in rows) {
            board["$column$row"] = CellState()
        }
    }

    // Place pawns
    for (column in columns) {
        board["${column}2"] = CellState(piece = ChessPiece.Pawn(Color.WHITE), empty = false, text = "♟")
        board["${column}7"] = CellState(piece = ChessPiece.Pawn(Color.BLACK), empty = false, text = "♙")
    }

    // Place rooks
    board["a1"] = CellState(piece = ChessPiece.Rook(Color.WHITE), empty = false, text = "♜")
    board["h1"] = CellState(piece = ChessPiece.Rook(Color.WHITE), empty = false, text = "♜")
    board["a8"] = CellState(piece = ChessPiece.Rook(Color.BLACK), empty = false, text = "♖")
    board["h8"] = CellState(piece = ChessPiece.Rook(Color.BLACK), empty = false, text = "♖")

    // Place knights
    board["b1"] = CellState(piece = ChessPiece.Knight(Color.WHITE), empty = false, text = "♞")
    board["g1"] = CellState(piece = ChessPiece.Knight(Color.WHITE), empty = false, text = "♞")
    board["b8"] = CellState(piece = ChessPiece.Knight(Color.BLACK), empty = false, text = "♘")
    board["g8"] = CellState(piece = ChessPiece.Knight(Color.BLACK), empty = false, text = "♘")

    // Place bishops
    board["c1"] = CellState(piece = ChessPiece.Bishop(Color.WHITE), empty = false, text = "♝")
    board["f1"] = CellState(piece = ChessPiece.Bishop(Color.WHITE), empty = false, text = "♝")
    board["c8"] = CellState(piece = ChessPiece.Bishop(Color.BLACK), empty = false, text = "♗")
    board["f8"] = CellState(piece = ChessPiece.Bishop(Color.BLACK), empty = false, text = "♗")

    // Place queens
    board["d1"] = CellState(piece = ChessPiece.Queen(Color.WHITE), empty = false, text = "♛")
    board["d8"] = CellState(piece = ChessPiece.Queen(Color.BLACK), empty = false, text = "♕")

    // Place kings
    board["e1"] = CellState(piece = ChessPiece.King(Color.WHITE), empty = false, text = "♚")
    board["e8"] = CellState(piece = ChessPiece.King(Color.BLACK), empty = false, text = "♔")
    return board
}


