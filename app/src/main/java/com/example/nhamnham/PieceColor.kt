package com.example.nhamnham

enum class PieceColor {
    ORANGE, BLUE;

    fun getTheOtherColor(): PieceColor {
        return if (this == ORANGE) BLUE else ORANGE
    }
}
