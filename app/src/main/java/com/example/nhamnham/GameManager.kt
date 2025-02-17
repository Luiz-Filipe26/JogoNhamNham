package com.example.nhamnham

import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.example.nhamnham.databinding.ActivityGameBinding

class GameManager(
    private val context: Context,
    private val binding: ActivityGameBinding,
    private val bluePlayerName: String,
    private val orangePlayerName: String,
    orangeStarts: Boolean
) {

    var orangePieces = mutableListOf<MutableList<Piece>>() // 3x3 grid of Pieces
    var bluePieces = mutableListOf<MutableList<Piece>>()  // 3x3 grid of Pieces
    private lateinit var board: Board
    var colorOfTurn: PieceColor = if (orangeStarts) PieceColor.ORANGE else PieceColor.BLUE
    private var playerOfTurn: String = if (orangeStarts) orangePlayerName else bluePlayerName
    private var gameFinished: Boolean = false

    fun setupGame() {
        board = Board(3, 3, this)
        binding.main.post {
            board.mapBoardGridCoordinates(binding.boardImg)
        }
        binding.gameInfoTxt.text = context.resources.getString(R.string.current_turn, playerOfTurn)
        addPiecesDynamically()
    }

    fun canDragPiece(piece: Piece): Boolean {
        return colorOfTurn == piece.color && !gameFinished
    }

    fun handleInBoardPositioned(piece: Piece) {
        colorOfTurn = piece.color.getTheOtherColor()
        playerOfTurn = if (colorOfTurn == PieceColor.ORANGE) orangePlayerName else bluePlayerName

        binding.gameInfoTxt.text = context.resources.getString(R.string.current_turn, playerOfTurn)

        if (board.determineDraw()) {
            binding.gameInfoTxt.text = context.resources.getString(R.string.draw_game)
            gameFinished = true
            return
        }


        val colorOfWinner: PieceColor? = board.determineWinner()


        if (colorOfWinner != null) {
            val winnerName =
                if (colorOfWinner == PieceColor.ORANGE) orangePlayerName else bluePlayerName
            binding.gameInfoTxt.text =
                context.resources.getString(R.string.player_won, winnerName)
            gameFinished = true
        }
    }

    private fun addPiecesDynamically() {
        createPieces(PieceColor.ORANGE, orangePieces)
        createPieces(PieceColor.BLUE, bluePieces)

        binding.main.post {
            setPiecesToInitialPos(binding.topPiecesHouseImg, orangePieces)
            setPiecesToInitialPos(binding.bottomPiecesHouseImg, bluePieces)
        }
    }

    private fun createPieces(
        color: PieceColor,
        targetList: MutableList<MutableList<Piece>>
    ) {
        for (row in 0 until 3) {
            val rowList = mutableListOf<Piece>()
            for (col in 0 until 3) {
                val piece = Piece(
                    context = this.context,
                    color = color,
                    pieceSize = when (row) {
                        0 -> PieceSize.BIG
                        1 -> PieceSize.MEDIUM
                        2 -> PieceSize.SMALL
                        else -> PieceSize.MEDIUM
                    },
                    board = this.board,
                    gameManager = this
                )
                piece.createPiece(binding.main)
                rowList.add(piece)
            }
            targetList.add(rowList)
        }
    }

    private fun setPiecesToInitialPos(
        referenceView: View,
        targetList: MutableList<MutableList<Piece>>
    ) {
        for ((rowIndex, row) in targetList.withIndex()) {
            for ((colIndex, piece) in row.withIndex()) {
                piece.positionPiece(
                    referenceView = referenceView,
                    numOfPiecesInLine = 3,
                    inLinePosition = colIndex + 1,
                    numOfPiecesInColumn = 3,
                    inColumnPosition = rowIndex + 1
                )
            }
        }
    }

    companion object {
        fun makeDraggableObject(
            guiObject: View,
            actionUpHandler: (View) -> Unit,
            canDrag: () -> Boolean
        ) {
            @Suppress("ClickableViewAccessibility")
            guiObject.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (canDrag()) {
                            guiObject.x = event.rawX - guiObject.width / 2
                            guiObject.y = event.rawY - guiObject.height / 2
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (canDrag()) {
                            guiObject.x = event.rawX - guiObject.width / 2
                            guiObject.y = event.rawY - guiObject.height / 2
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        actionUpHandler(guiObject)
                    }
                }
                true
            }
        }

        fun removeDraggability(guiObject: View) {
            guiObject.setOnTouchListener(null)
        }
    }
}