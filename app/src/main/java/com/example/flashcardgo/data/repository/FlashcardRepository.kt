package com.example.flashcardgo.data.repository

import com.example.flashcardgo.data.local.FlashcardDao
import com.example.flashcardgo.data.local.FlashcardEntity
import kotlinx.coroutines.flow.Flow

class FlashcardRepository(private val dao: FlashcardDao) {
    val allCards: Flow<List<FlashcardEntity>> = dao.getAllCards()

    fun getCardsBySubject(subject: String): Flow<List<FlashcardEntity>> {
        return dao.getCardsBySubject(subject)
    }

    suspend fun addCard(card: FlashcardEntity) {
        dao.insertCard(card)
    }
}