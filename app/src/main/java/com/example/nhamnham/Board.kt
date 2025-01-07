package com.example.nhamnham

import android.graphics.Point
import android.graphics.PointF
import android.widget.ImageView
import kotlin.math.hypot

class Board(
    private val numOfRows: Int,
    private val numOfColumns: Int
) {
    private val boardGrid = MutableList(numOfRows) { MutableList(numOfColumns) { PointF() } }
    private val positionToPiece = Array(numOfRows) { arrayOfNulls<Piece>(numOfColumns) }
    private var minPieceToCellDistance = 0.0f

    private val victoryConditions = listOf(
        listOf(Point(0, 0), Point(0, 1), Point(0, 2)),
        listOf(Point(1, 0), Point(1, 1), Point(1, 2)),
        listOf(Point(2, 0), Point(2, 1), Point(2, 2)),
        listOf(Point(0, 0), Point(1, 0), Point(2, 0)),
        listOf(Point(0, 1), Point(1, 1), Point(2, 1)),
        listOf(Point(0, 2), Point(1, 2), Point(2, 2)),
        listOf(Point(0, 0), Point(1, 1), Point(2, 2)),
        listOf(Point(0, 2), Point(1, 1), Point(2, 0))
    )

    companion object {
        private const val MIN_PIECE_TO_CELL_DISTANCE_FRACTION = 0.5f
    }

    fun mapBoardGridCoordinates(boardImg: ImageView): List<List<PointF>> {
        val horizontalFraction = boardImg.width / numOfColumns.toFloat()
        val verticalFraction = boardImg.height / numOfRows.toFloat()

        minPieceToCellDistance = MIN_PIECE_TO_CELL_DISTANCE_FRACTION * PointF.length(
            horizontalFraction,
            verticalFraction
        )

        val xOffSet = horizontalFraction / 2
        val yOffSet = verticalFraction / 2

        for (row in 0 until numOfRows) {
            for (column in 0 until numOfColumns) {
                val x = boardImg.x + xOffSet + (horizontalFraction * column)
                val y = boardImg.y + yOffSet + (verticalFraction * row)

                boardGrid[row][column] = PointF(x, y)
            }
        }

        return boardGrid
    }

    fun determineInBoardPosition(piece: Piece): Point? {
        var shortestDistance = Float.MAX_VALUE
        var pointOfShortestDistance: Point? = null

        boardGrid.forEachIndexed { rowIndex, boardRow ->
            boardRow.forEachIndexed { colIndex, boardCell ->
                val distance = distanceBetween(piece.getCenter(), boardCell)
                if (distance <= minPieceToCellDistance && distance < shortestDistance) {
                    shortestDistance = distance
                    pointOfShortestDistance = Point(rowIndex, colIndex)
                }
            }
        }

        return pointOfShortestDistance
    }

    fun canPositionPiece(piece: Piece, gridPoint: Point): Boolean {
        val pieceInPosition: Piece? = positionToPiece[gridPoint.x][gridPoint.y]
        return pieceInPosition == null || (piece.color != pieceInPosition.color && piece.pieceSize.value > pieceInPosition.pieceSize.value)
    }

    fun positionPieceAndGetCoordinate(piece: Piece, gridPoint: Point): PointF {
        positionToPiece[gridPoint.x][gridPoint.y] = piece
        return boardGrid[gridPoint.x][gridPoint.y]
    }

    private fun distanceBetween(p1: PointF, p2: PointF): Float {
        return (hypot((p1.x - p2.x).toDouble(), (p1.y - p2.y).toDouble())).toFloat()
    }

    fun determineWinner(): PieceColor? {
        for (condition in victoryConditions) {
            val piece1 = positionToPiece[condition[0].x][condition[0].y]
            val piece2 = positionToPiece[condition[1].x][condition[1].y]
            val piece3 = positionToPiece[condition[2].x][condition[2].y]

            if (piece1 != null && piece2 != null && piece3 != null &&
                piece1.color == piece2.color && piece2.color == piece3.color) {
                return piece1.color
            }
        }
        return null
    }
}
