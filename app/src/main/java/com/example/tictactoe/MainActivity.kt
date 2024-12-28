package com.example.tictactoe

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val board = Array(3) { arrayOfNulls<Button>(3) }
    private var currentPlayer = "X"
    private var moves = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the board buttons
        for (i in 0..2) {
            for (j in 0..2) {
                val buttonId = "button${i * 3 + j}"
                val resId = resources.getIdentifier(buttonId, "id", packageName)
                board[i][j] = findViewById(resId)
                board[i][j]?.setOnClickListener { onCellClick(it as Button, i, j) }
            }
        }

        findViewById<Button>(R.id.resetButton).setOnClickListener { resetGame() }
    }

    private fun onCellClick(button: Button, row: Int, col: Int) {
        if (button.text.isNotEmpty()) {
            Toast.makeText(this, "Cell already taken!", Toast.LENGTH_SHORT).show()
            return
        }

        // Make the move
        button.text = currentPlayer
        moves++

        // Check for a winner
        if (checkWinner(row, col)) {
            Toast.makeText(this, "Player $currentPlayer Wins!", Toast.LENGTH_SHORT).show()
            disableBoard()
            return
        }

        // Check for a draw
        if (moves == 9) {
            Toast.makeText(this, "It's a Draw!", Toast.LENGTH_SHORT).show()
            return
        }

        // Switch player
        currentPlayer = if (currentPlayer == "X") "O" else "X"
    }

    private fun checkWinner(row: Int, col: Int): Boolean {
        // Check row, column, and diagonals
        return (board[row].all { it?.text == currentPlayer } ||
                board.all { it[col]?.text == currentPlayer } ||
                (row == col && (0..2).all { board[it][it]?.text == currentPlayer }) ||
                (row + col == 2 && (0..2).all { board[it][2 - it]?.text == currentPlayer }))
    }

    private fun disableBoard() {
        board.flatten().forEach { it?.isEnabled = false }
    }

    private fun resetGame() {
        board.flatten().forEach {
            it?.text = ""
            it?.isEnabled = true
        }
        currentPlayer = "X"
        moves = 0
    }
}