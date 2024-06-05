package com.dibe.chess.Presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChessViewModel : ViewModel() {
    private val _state = MutableStateFlow(ChessState())
    val state: StateFlow<ChessState> = _state

    fun onEvent(event: ChessEvent) = when (event) {
        is ChessEvent.Activate -> handleActivation(event.cellClicked)
        is ChessEvent.Play -> handlePlay(event.cellClicked, event.cellFrom)
        ChessEvent.Restart -> {
            _state.value = ChessState()
        }
    }

    private fun handleActivation(cellClicked: String) {
        if (_state.value.turn == _state.value.chessBoard[cellClicked]?.piece?.color) {
            val board = _state.value.chessBoard.toMutableMap().apply {
                clearCellStates()
                updateActivationStates(cellClicked)
            }
            _state.value = _state.value.copy(
                isActivated = true ,
                chessBoard = board ,
                clickedPiece = cellClicked
                                            )
            Log.i("ChessViewModel" , "Activation handled for cell: $cellClicked")
        }else {
            val board = _state.value.chessBoard.toMutableMap().apply {
                clearCellStates()
            }
            _state.value = _state.value.copy(
                chessBoard = board
                                            )
        }
    }

    private fun handlePlay(cellClicked: String, cellFrom: String?) {
        if (cellFrom?.let { _state.value.chessBoard[cellFrom]?.piece?.evaluateNextMove(board = _state.value.chessBoard , cell = it) }
                ?.contains(cellClicked) == true || cellFrom?.let { _state.value.chessBoard[cellFrom]?.piece?.evaluateNextKill(board = _state.value.chessBoard , cell = it) }
            ?.contains(cellClicked) == true) {

            if (cellFrom == cellClicked) return

            val opponentColor = if (_state.value.chessBoard[cellFrom]?.piece?.color == Color.WHITE) Color.BLACK else Color.WHITE

            val collectedPiecesWhite  = _state.value.collectedPiecesWhite.toMutableList()
            val collectedPiecesBlack  = _state.value.collectedPiecesBlack.toMutableList()

            if (! _state.value.chessBoard[cellClicked]?.empty !!){
                if (_state.value.chessBoard[cellClicked]?.piece?.color == Color.WHITE){
                    _state.value.chessBoard[cellClicked]?.text?.let { collectedPiecesWhite.add(it) }
                }else {
                    _state.value.chessBoard[cellClicked]?.text?.let { collectedPiecesBlack.add(it) }
                }
            }

            val board = _state.value.chessBoard.toMutableMap().apply {
                clearCellStates()
                updatePlayStates(cellClicked , cellFrom)
            }

            var isInCheck = false
            var kingInCheck: String? = ""
            var checkMate = false

            _state.value.chessBoard.forEach{(cell, cellState) ->
                if (cellState.piece?.text == "king" && cellState.piece?.color == opponentColor){
                    isInCheck = (cellState.piece as ChessPiece.King).isInCheck(cell, board)
                    kingInCheck = cell
                    _state.value = _state.value.copy(kingInCheck = kingInCheck)
                    checkMate = (cellState.piece as ChessPiece.King).isCheckMate(getNextTurn(board, cellClicked) , board)
                }
            }

            val archive = _state.value.archive.toMutableList()
            archive.add("$cellFrom to $cellClicked")

            _state.value = _state.value.copy(
                archive = archive,
                isActivated = false ,
                chessBoard = board ,
                check = isInCheck,
                kingInCheck = kingInCheck,
                checkMate = checkMate,
                turn = getNextTurn(board , cellClicked),
                collectedPiecesWhite = collectedPiecesWhite,
                collectedPiecesBlack = collectedPiecesBlack
                                            )
            Log.i("ChessViewModel" , "Play handled from $cellFrom to $cellClicked")
        }else {
            val board = _state.value.chessBoard.toMutableMap().apply {
                clearCellStates()
            }
            _state.value = _state.value.copy(
                isActivated = false ,
                chessBoard = board ,
                                            )
        }
    }

    private fun MutableMap<String, CellState>.clearCellStates() {
        values.forEach { cellState ->
            cellState.clearState()
        }
    }

    private fun CellState.clearState() {
        wasMovedTo = false
        wasMovedFrom = false
        highlighted = false
        danger = false
    }

    private fun MutableMap<String, CellState>.updateActivationStates(cellClicked: String) {
        val possibleMoves = this[cellClicked]?.piece?.evaluateNextMove(cellClicked, this)
        possibleMoves?.forEach { cell ->
            this[cell]?.highlighted = true
        }
        val possibleKills = this[cellClicked]?.piece?.evaluateNextKill(cellClicked, this)
        possibleKills?.forEach { cell ->
            this[cell]?.highlighted = false
            this[cell]?.danger = true
        }
    }

    private fun MutableMap<String, CellState>.updatePlayStates(cellClicked: String, cellFrom: String) {
        this[cellClicked]?.apply {
            empty = false
            piece = this@updatePlayStates[cellFrom]?.piece
            text = this@updatePlayStates[cellFrom]?.text
            wasMovedTo = true
            highlighted = false
            danger = false
        }
        this[cellFrom]?.apply {
            empty = true
            piece = null
            text = null
            wasMovedFrom = true
        }
    }

    private fun getNextTurn(board: Map<String, CellState>, cellClicked: String): Color {
        return if (board[cellClicked]?.piece?.color == Color.WHITE) Color.BLACK else Color.WHITE
    }
}
