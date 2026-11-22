package com.example.flashcardgo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,          // "Bahasa Jepang", "Bahasa Inggris", "Bahasa Indonesia"
    val subTopic: String,         // "Hiragana", "Benda di Kelas", "Posisi", "Teks Berita", dll.
    val frontText: String,        // Pertanyaan / Kosakata
    val backText: String,         // Jawaban / Arti / Penjelasan
    val imageResId: Int? = null   // Opsional: R.drawable.nama_gambar
)