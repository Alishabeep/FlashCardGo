package com.example.flashcardgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardgo.ui.screen.StudyScreen
import com.example.flashcardgo.ui.theme.FlashCardGoTheme
import com.example.flashcardgo.ui.viewmodel.FlashcardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashCardGoTheme {
                // Memanggil ViewModel dengan AndroidViewModel(application)
                val flashcardViewModel: FlashcardViewModel = viewModel()
                StudyScreen(viewModel = flashcardViewModel)
            }
        }
    }
}