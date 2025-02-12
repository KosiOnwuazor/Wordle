package com.example.wordle

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var guessInput: EditText
    private lateinit var submitButton: Button
    private lateinit var resetButton: Button
    private lateinit var feedbackText: TextView
    private lateinit var answerText: TextView

    private var targetWord = FourLetterWordList.getRandomFourLetterWord()
    private var attempts = 0
    private val maxAttempts = 3

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        guessInput = findViewById(R.id.guessInput)
        submitButton = findViewById(R.id.submitButton)
        resetButton = findViewById(R.id.resetButton)
        feedbackText = findViewById(R.id.feedbackText)
        answerText = findViewById(R.id.answerText)

        submitButton.setOnClickListener { handleGuess() }
        resetButton.setOnClickListener { resetGame() }
    }

    private fun handleGuess() {
        val userGuess = guessInput.text.toString().uppercase()

        if (userGuess.length != 4) {
            Toast.makeText(this, "Please enter a 4-letter word!", Toast.LENGTH_SHORT).show()
            return
        }

        val feedback = checkGuess(userGuess, targetWord)
        feedbackText.text = "Feedback: $feedback"
        attempts++

        if (feedback == "OOOO" || attempts >= maxAttempts) {
            endGame()
        }

        guessInput.text.clear()
    }

    private fun endGame() {
        submitButton.isEnabled = false
        answerText.text = "The correct word was: $targetWord"
        answerText.visibility = TextView.VISIBLE
        resetButton.visibility = Button.VISIBLE
    }

    private fun resetGame() {
        targetWord = FourLetterWordList.getRandomFourLetterWord()
        attempts = 0
        feedbackText.text = ""
        answerText.visibility = TextView.GONE
        resetButton.visibility = Button.GONE
        submitButton.isEnabled = true
        guessInput.text.clear()
    }
}

// Function to check the guess and return feedback
fun checkGuess(guess: String, target: String): String {
    val result = StringBuilder("XXXX")
    val targetChars = target.toCharArray()

    for (i in guess.indices) {
        if (guess[i] == targetChars[i]) {
            result.setCharAt(i, 'O') // Correct letter & position
        } else if (guess[i] in targetChars) {
            result.setCharAt(i, '+') // Correct letter, wrong position
        }
    }
    return result.toString()
}

// Mock function to get a random 4-letter word
object FourLetterWordList {
    fun getRandomFourLetterWord(): String {
        val words = listOf("STAR", "FISH", "MOON", "JUMP", "TREE")
        return words.random()
    }
}
