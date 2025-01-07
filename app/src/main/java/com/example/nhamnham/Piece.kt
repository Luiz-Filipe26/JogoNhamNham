package com.example.nhamnham

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.view.View
import android.widget.ImageView
import android.widget.FrameLayout

class Piece(
    val context: Context,
    val color: PieceColor,
    val pieceSize: PieceSize,
    private val board: Board,
    private val gameManager: GameManager
) {
    private var initialX: Float = 0.0f
    private var initialY: Float = 0.0f
    var isInBoard = false


    private val imageView: ImageView = ImageView(context).apply {
        setImageResource(
            when (color) {
                PieceColor.ORANGE -> R.drawable.nham_nham_orange_piece
                PieceColor.BLUE -> R.drawable.nham_nham_blue_piece
            }
        )
    }

    private val pieceWidth = when (pieceSize) {
        PieceSize.SMALL -> 100
        PieceSize.MEDIUM -> 120
        PieceSize.BIG -> 140
    }
    private val pieceHeight = calculateHeight(pieceWidth)

    private fun calculateHeight(pieceWidth: Int): Int {
        val drawable = imageView.drawable
        val aspectRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()
        return (pieceWidth / aspectRatio).toInt()
    }

    fun getCenter(): PointF {
        val targetX = imageView.x + (imageView.width / 2)
        val targetY = imageView.y + (imageView.height / 2)

        return PointF(targetX, targetY)
    }

    fun createPiece(gameAreaLayout: FrameLayout) {
        val params = FrameLayout.LayoutParams(pieceWidth, pieceHeight)
        imageView.layoutParams = params
        gameAreaLayout.addView(imageView)
        GameManager.makeDraggableObject(imageView, this::adjustPiecePosition, this::canDrag)
    }

    fun canDrag() : Boolean {
        return gameManager.canDragPiece(this)
    }

    private fun adjustPiecePosition(piece: View) {
        val inGridPoint: Point? = board.determineInBoardPosition(this)
        if (inGridPoint == null || !board.canPositionPiece(this, inGridPoint)) {
            piece.x = initialX
            piece.y = initialY
            return
        }

        isInBoard = true
        val targetCoordinate = board.positionPieceAndGetCoordinate(this, inGridPoint)
        piece.x = targetCoordinate.x - (piece.width / 2)
        piece.y = targetCoordinate.y - (piece.height / 2)

        piece.bringToFront()
        GameManager.removeDraggability(piece)

        gameManager.handleInBoardPositioned(this)
    }

    fun positionPiece(
        referenceView: View,
        numOfPiecesInLine: Int,
        inLinePosition: Int,
        numOfPiecesInColumn: Int,
        inColumnPosition: Int
    ) {

        val horizontalFraction = referenceView.width / (numOfPiecesInLine + 1)
        val verticalFraction = referenceView.height / (numOfPiecesInColumn + 1)

        initialX = referenceView.x + (horizontalFraction * inLinePosition) - (pieceWidth / 2)
        initialY = referenceView.y + (verticalFraction * inColumnPosition) - (pieceHeight / 2)

        imageView.x = initialX
        imageView.y = initialY

    }
}