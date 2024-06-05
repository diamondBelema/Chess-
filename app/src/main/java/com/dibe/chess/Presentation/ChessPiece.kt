package com.dibe.chess.Presentation

import android.util.Log

sealed class ChessPiece(val color: Color, val text: String) {
    abstract fun evaluateNextMove(cell: String, board: Map<String, CellState>): List<String>
    abstract fun evaluateNextKill(cell: String, board: Map<String, CellState>): List<String>

    class King(color: Color) : ChessPiece(color, text = "king") {
        override fun evaluateNextMove(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getKingMoves(cell, board))
        }

        override fun evaluateNextKill(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getKingKills(cell, board))
        }

        fun isInCheck(cell: String, board: Map<String, CellState>): Boolean {
            val opponentColor = if (color == Color.WHITE) Color.BLACK else Color.WHITE
            return board.any { (otherCell, cellState) ->
                if (cellState.text.orEmpty() == "♚" || cellState.text.orEmpty() == "♔"){
                    cellState.piece?.color == opponentColor && getKingKills(otherCell , board).contains(cell)
                }else if (cellState.text.orEmpty() == "♕" || cellState.text.orEmpty() == "♛"){
                    cellState.piece?.color == opponentColor && getQueenKills(otherCell , board).contains(cell)
                }else if (cellState.text.orEmpty() == "♗" || cellState.text.orEmpty() == "♝"){
                    cellState.piece?.color == opponentColor && getBishopKills(otherCell , board).contains(cell)
                }else if (cellState.text.orEmpty() == "♘" || cellState.text.orEmpty() == "♞"){
                    cellState.piece?.color == opponentColor && getKnightKills(otherCell , board).contains(cell)
                }else if (cellState.text.orEmpty() == "♖" || cellState.text.orEmpty() == "♜"){
                    cellState.piece?.color == opponentColor && getRookKills(otherCell , board).contains(cell)
                }else if (cellState.text.orEmpty() == "♙" || cellState.text.orEmpty() == "♟"){
                    cellState.piece?.color == opponentColor && getPawnKills(otherCell , board, color).contains(cell)
                }else {
                    false
                }
            }
        }

        fun isCheckMate(color: Color, board: Map<String, CellState>): Boolean {
            return board.filter { it.value.piece?.color == color }
                .all { (cell, cellState) ->
                    val moves = cellState.piece?.evaluateNextMove(cell, board) ?: emptyList()
                    val kills = cellState.piece?.evaluateNextKill(cell, board) ?: emptyList()
                    moves.isEmpty() && kills.isEmpty()
                }
        }
    }

    class Queen(color: Color) : ChessPiece(color, text = "queen") {
        override fun evaluateNextMove(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getQueenMoves(cell, board))
        }

        override fun evaluateNextKill(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getQueenKills(cell, board))
        }
    }

    class Rook(color: Color) : ChessPiece(color, text = "rook") {
        override fun evaluateNextMove(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getRookMoves(cell, board))
        }

        override fun evaluateNextKill(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getRookKills(cell, board))
        }
    }

    class Bishop(color: Color) : ChessPiece(color, text = "bishop") {
        override fun evaluateNextMove(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getBishopMoves(cell, board))
        }

        override fun evaluateNextKill(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getBishopKills(cell, board))
        }
    }

    class Knight(color: Color) : ChessPiece(color, text = "knight") {
        override fun evaluateNextMove(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getKnightMoves(cell, board))
        }

        override fun evaluateNextKill(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getKnightKills(cell, board))
        }
    }

    class Pawn(color: Color) : ChessPiece(color, text = "pawn") {
        override fun evaluateNextMove(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getPawnMoves(cell, board, color))
        }
        override fun evaluateNextKill(cell: String, board: Map<String, CellState>): List<String> {
            return filterSafeMoves(cell, board, getPawnKills(cell, board, color))
        }

        fun evaluateEnPassant(cell: String, board: Map<String, CellState>, enPassantTarget: String?): List<String> {
            return filterSafeMoves(cell, board, getEnPassantKills(cell, board, color, enPassantTarget))
        }
    }

    // New helper function to filter moves that protect the King
    protected fun filterSafeMoves(cell: String, board: Map<String, CellState>, moves: List<String>): List<String> {
        return moves.filter { move ->
            val simulatedBoard = simulateMove(cell , move , board)
            ! King(color).isInCheck(findKingPosition(color , simulatedBoard) , simulatedBoard)
        }
    }

    // Helper function to simulate a move on the board
    fun simulateMove(from: String, to: String, board: Map<String, CellState>): Map<String, CellState> {
        val simulatedBoard = board.toMutableMap()
        simulatedBoard[to] = simulatedBoard[from]!!.copy(piece = simulatedBoard[from]!!.piece)
        simulatedBoard[from] = simulatedBoard[from]!!.copy(piece = null, empty = true)
        return simulatedBoard
    }

    // Helper function to find the King's position
    fun findKingPosition(color: Color, board: Map<String, CellState>): String {
        return board.entries.find { it.value.piece is King && (it.value.piece as King).color == color }?.key.orEmpty()
    }
}

// Helper functions for other pieces...
fun getKingMoves(cell: String, board: Map<String, CellState>): List<String> {
    val moves = mutableListOf<String>()
    val directions = listOf(
        1 to 0, 1 to 1, 0 to 1, -1 to 1, -1 to 0, -1 to -1, 0 to -1, 1 to -1
                           )
    val (column, row) = cell[0] to cell[1].toString().toInt()
    for ((dc, dr) in directions) {
        val nextCell = "${(column + dc).toChar()}${row + dr}"
        if (nextCell in board && board[nextCell]?.empty == true) moves.add(nextCell)
    }
    return moves
}

fun getKingKills(cell: String, board: Map<String, CellState>): List<String> {
    val kills = mutableListOf<String>()
    val directions = listOf(
        1 to 0, 1 to 1, 0 to 1, -1 to 1, -1 to 0, -1 to -1, 0 to -1, 1 to -1
                           )
    val (column, row) = cell[0] to cell[1].toString().toInt()
    for ((dc, dr) in directions) {
        val nextCell = "${(column + dc).toChar()}${row + dr}"
        if (nextCell in board && !board[nextCell]!!.empty && board[nextCell]?.piece?.color != board[cell]?.piece?.color) {
            kills.add(nextCell)
        }
    }
    return kills
}

fun getQueenMoves(cell: String, board: Map<String, CellState>): List<String> {
    return getRookMoves(cell, board) + getBishopMoves(cell, board)
}

fun getQueenKills(cell: String, board: Map<String, CellState>): List<String> {
    return getRookKills(cell, board) + getBishopKills(cell, board)
}

fun getRookMoves(cell: String, board: Map<String, CellState>): List<String> {
    val moves = mutableListOf<String>()
    val directions = listOf(
        1 to 0, -1 to 0, 0 to 1, 0 to -1
                           )
    val (column, row) = cell[0] to cell[1].toString().toInt()
    for ((dc, dr) in directions) {
        for (i in 1..7) {
            val nextCell = "${(column + dc * i).toChar()}${row + dr * i}"
            if (nextCell !in board) break
            if (board[nextCell]?.empty == true) moves.add(nextCell)
            else break
        }
    }
    return moves
}

fun getRookKills(cell: String, board: Map<String, CellState>): List<String> {
    val kills = mutableListOf<String>()
    val directions = listOf(
        1 to 0, -1 to 0, 0 to 1, 0 to -1
                           )
    val (column, row) = cell[0] to cell[1].toString().toInt()
    for ((dc, dr) in directions) {
        for (i in 1..7) {
            val nextCell = "${(column + dc * i).toChar()}${row + dr * i}"
            if (nextCell !in board) break
            if (!board[nextCell]!!.empty && board[nextCell]?.piece?.color != board[cell]?.piece?.color) {
                kills.add(nextCell)
                break
            } else if (board[nextCell]?.piece?.color == board[cell]?.piece?.color) {
                break
            }
        }
    }
    return kills
}

fun getBishopMoves(cell: String, board: Map<String, CellState>): List<String> {
    val moves = mutableListOf<String>()
    val directions = listOf(
        1 to 1, -1 to 1, 1 to -1, -1 to -1
                           )
    val (column, row) = cell[0] to cell[1].toString().toInt()
    for ((dc, dr) in directions) {
        for (i in 1..7) {
            val nextCell = "${(column + dc * i).toChar()}${row + dr * i}"
            if (nextCell !in board) break
            if (board[nextCell]?.empty == true) moves.add(nextCell)
            else break
        }
    }
    return moves
}

fun getBishopKills(cell: String, board: Map<String, CellState>): List<String> {
    val kills = mutableListOf<String>()
    val directions = listOf(
        1 to 1, -1 to 1, 1 to -1, -1 to -1
                           )
    val (column, row) = cell[0] to cell[1].toString().toInt()
    for ((dc, dr) in directions) {
        for (i in 1..7) {
            val nextCell = "${(column + dc * i).toChar()}${row + dr * i}"
            if (nextCell !in board) break
            if (!board[nextCell]!!.empty && board[nextCell]?.piece?.color != board[cell]?.piece?.color) {
                kills.add(nextCell)
                break
            } else if (board[nextCell]?.piece?.color == board[cell]?.piece?.color) {
                break
            }
        }
    }
    return kills
}

fun getKnightMoves(cell: String, board: Map<String, CellState>): List<String> {
    val moves = mutableListOf<String>()
    val directions = listOf(
        2 to 1, 2 to -1, -2 to 1, -2 to -1,
        1 to 2, 1 to -2, -1 to 2, -1 to -2
                           )
    val (column, row) = cell[0] to cell[1].toString().toInt()
    for ((dc, dr) in directions) {
        val nextCell = "${(column + dc).toChar()}${row + dr}"
        if (nextCell in board && board[nextCell]?.empty == true) moves.add(nextCell)
    }
    return moves
}

fun getKnightKills(cell: String, board: Map<String, CellState>): List<String> {
    val kills = mutableListOf<String>()
    val directions = listOf(
        2 to 1, 2 to -1, -2 to 1, -2 to -1,
        1 to 2, 1 to -2, -1 to 2, -1 to -2
                           )
    val (column, row) = cell[0] to cell[1].toString().toInt()
    for ((dc, dr) in directions) {
        val nextCell = "${(column + dc).toChar()}${row + dr}"
        if (nextCell in board && !board[nextCell]!!.empty && board[nextCell]?.piece?.color != board[cell]?.piece?.color) {
            kills.add(nextCell)
        }
    }
    return kills
}

fun getPawnMoves(cell: String, board: Map<String, CellState>, color: Color): List<String> {
    val moves = mutableListOf<String>()
    val (column, row) = cell[0] to cell[1].toString().toInt()
    if (color == Color.WHITE && row == 2) listOf(0 to 1, 0 to 2)
    val directions = if (color == Color.WHITE && row != 2) listOf(0 to 1)
    else if (color == Color.BLACK && row != 7) listOf(0 to -1)
    else if (color == Color.WHITE) listOf(0 to 1, 0 to 2)
    else  listOf(0 to -2, 0 to -1)

    for ((dc, dr) in directions) {
        val nextCell = "${(column + dc)}${row + dr}"
        if (nextCell in board && board[nextCell]?.empty == true) moves.add(nextCell)
    }

    return moves
}

fun getPawnKills(cell: String, board: Map<String, CellState>, color: Color): List<String> {
    val kills = mutableListOf<String>()
    val (column, row) = cell[0] to cell[1].toString().toInt()
    val directions = if (color == Color.WHITE) listOf(1 to 1, -1 to 1) else listOf(1 to -1, -1 to -1)

    for ((dc, dr) in directions) {
        val nextCell = "${(column + dc).toChar()}${row + dr}"
        if (nextCell in board && !board[nextCell]!!.empty && board[nextCell]?.piece?.color != board[cell]?.piece?.color) {
            kills.add(nextCell)
        }
    }

    return kills
}

fun getEnPassantKills(cell: String, board: Map<String, CellState>, color: Color, enPassantTarget: String?): List<String> {
    val kills = mutableListOf<String>()
    val (column, row) = cell[0] to cell[1].toString().toInt()
    val directions = if (color == Color.WHITE) listOf(1 to 1, -1 to 1) else listOf(1 to -1, -1 to -1)

    for ((dc, dr) in directions) {
        val nextCell = "${(column + dc).toChar()}${row + dr}"
        if (nextCell == enPassantTarget && enPassantTarget in board && !board[enPassantTarget]!!.empty && board[enPassantTarget]?.piece?.color != board[cell]?.piece?.color) {
            kills.add(nextCell)
        }
    }

    return kills
}
