package com.example.flashcardgo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardgo.data.local.AppDatabase
import com.example.flashcardgo.data.local.FlashcardEntity
import com.example.flashcardgo.data.repository.FlashcardRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FlashcardRepository

    private val _selectedSubject = MutableStateFlow("Semua")
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    private val _cards = MutableStateFlow<List<FlashcardEntity>>(emptyList())
    val cards: StateFlow<List<FlashcardEntity>> = _cards.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isFlipped = MutableStateFlow(false)
    val isFlipped: StateFlow<Boolean> = _isFlipped.asStateFlow()

    init {
        // Menginisialisasi Room Database menggunakan Application Context
        val dao = AppDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(dao)

        viewModelScope.launch {
            _selectedSubject.flatMapLatest { subject ->
                if (subject == "Semua") repository.allCards
                else repository.getCardsBySubject(subject)
            }.collect { list ->
                _cards.value = list
                _currentIndex.value = 0
                _isFlipped.value = false
            }
        }
    }

    fun selectSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun flipCard() {
        _isFlipped.value = !_isFlipped.value
    }

    fun nextCard() {
        if (_cards.value.isNotEmpty()) {
            _isFlipped.value = false
            _currentIndex.value = (_currentIndex.value + 1) % _cards.value.size
        }
    }

    fun shuffleCards() {
        if (_cards.value.size > 1) {
            _cards.value = _cards.value.shuffled()
            _currentIndex.value = 0
            _isFlipped.value = false
        }
    }

    fun addNewCard(subject: String, subTopic: String, front: String, back: String) {
        viewModelScope.launch {
            repository.addCard(
                FlashcardEntity(
                    subject = subject,
                    subTopic = subTopic,
                    frontText = front,
                    backText = back
                )
            )
        }
    }
}