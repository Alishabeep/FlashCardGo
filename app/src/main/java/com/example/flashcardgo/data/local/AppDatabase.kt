package com.example.flashcardgo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [FlashcardEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flashcardgo_db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database.flashcardDao())
                }
            }
        }

        suspend fun populateInitialData(dao: FlashcardDao) {
            val initialCards = listOf(
                // ==========================================
                // 1. BAHASA JEPANG (10+ Kartu: Hiragana & Kelas)
                // ==========================================
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Hiragana Vokal",
                    frontText = "あ",
                    backText = "Dibaca: A\nContoh kata: あさ (Asa = Pagi)"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Hiragana Vokal",
                    frontText = "い",
                    backText = "Dibaca: I\nContoh kata: いぬ (Inu = Anjing)"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Hiragana Vokal",
                    frontText = "う",
                    backText = "Dibaca: U\nContoh kata: うみ (Umi = Laut)"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Hiragana Vokal",
                    frontText = "え",
                    backText = "Dibaca: E\nContoh kata: えき (Eki = Stasiun)"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Hiragana Baris K",
                    frontText = "か - き - く - け - こ",
                    backText = "Dibaca: Ka - Ki - Ku - Ke - Ko\nContoh: かばん (Kaban = Tas)"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Benda di Kelas",
                    frontText = "つくえ (Tsukue)",
                    backText = "Artinya: Meja belajar / Meja tulis"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Benda di Kelas",
                    frontText = "いす (Isu)",
                    backText = "Artinya: Kursi"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Benda di Kelas",
                    frontText = "えんぴつ (Enpitsu) & ぺん (Pen)",
                    backText = "Enpitsu = Pensil\nPen = Pulpen / Pen"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Posisi Benda",
                    frontText = "うえ (Ue) / した (Shita)",
                    backText = "Ue = Di atas\nShita = Di bawah\nContoh: Tsukue no ue (Di atas meja)"
                ),
                FlashcardEntity(
                    subject = "Bahasa Jepang",
                    subTopic = "Posisi Benda",
                    frontText = "なか (Naka) / そと (Soto)",
                    backText = "Naka = Di dalam\nSoto = Di luar\nContoh: Hako no naka (Di dalam kotak)"
                ),

                // ==========================================
                // 2. BAHASA INGGRIS KELAS 11 (10+ Kartu)
                // ==========================================
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Analytical Exposition",
                    frontText = "What is the social function of Analytical Exposition?",
                    backText = "To persuade the reader or listener that there is an important issue that needs attention."
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Analytical Exposition",
                    frontText = "Mention the 3 generic structures of Analytical Exposition!",
                    backText = "1. Thesis\n2. Arguments\n3. Reiteration (Conclusion)"
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Offering Help",
                    frontText = "How do you politely offer help to a friend?",
                    backText = "\"Can I help you?\" or \"Would you like some assistance?\""
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Offering Help",
                    frontText = "Give an example of accepting an offer of help!",
                    backText = "\"Yes, please, that would be very kind of you.\" or \"Thank you, I appreciate it!\""
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Expression of Opinion",
                    frontText = "What is the expression used to ask for someone's opinion?",
                    backText = "\"What do you think about...?\" or \"How do you feel regarding this matter?\""
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Passive Voice",
                    frontText = "Change into Passive Voice: 'The teacher explains the lesson.'",
                    backText = "\"The lesson is explained by the teacher.\""
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Passive Voice",
                    frontText = "What is the formula of Simple Present Tense in Passive Voice?",
                    backText = "Subject + is/am/are + Verb 3 (Past Participle) + by agent."
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Personal Letter",
                    frontText = "What are the key parts of a Personal Letter?",
                    backText = "1. Date & Address\n2. Salutation\n3. Introduction\n4. Body\n5. Closure & Sign-off"
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Conditional Sentence",
                    frontText = "What type of Conditional is this: 'If it rains, I will stay at home.'",
                    backText = "Conditional Type 1 (Real possibility in the future: If + Present, Future Simple)."
                ),
                FlashcardEntity(
                    subject = "Bahasa Inggris",
                    subTopic = "Cause and Effect",
                    frontText = "What connectors are commonly used for Cause and Effect?",
                    backText = "Because of, Due to, Owing to (followed by noun) / Because, Since, As (followed by clause)."
                ),

                // ==========================================
                // 3. BAHASA INDONESIA KELAS 11 (10+ Kartu)
                // ==========================================
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Teks Berita",
                    frontText = "Apa pengertian dari Teks Berita?",
                    backText = "Teks yang melaporkan kejadian, peristiwa, atau informasi aktual dan faktual yang sedang atau telah terjadi."
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Teks Berita",
                    frontText = "Sebutkan unsur-unsur berita (ADIKSIMBA / 5W+1H)!",
                    backText = "Apa, Di mana, Kapan, Siapa, Mengapa, dan Bagaimana."
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Teks Berita",
                    frontText = "Jelaskan struktur 'Kepala Berita' (Lead) dalam berita!",
                    backText = "Bagian awal berita yang memuat informasi paling penting dan mencakup inti dari keseluruhan peristiwa."
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Teks Prosedur",
                    frontText = "Apa tujuan utama dari Teks Prosedur?",
                    backText = "Memberikan petunjuk, panduan, atau instruksi kepada pembaca tentang cara membuat atau melakukan sesuatu secara langkah demi langkah."
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Teks Prosedur",
                    frontText = "Sebutkan struktur teks prosedur!",
                    backText = "1. Tujuan\n2. Alat dan Bahan\n3. Langkah-langkah\n4. Penutup / Simpulan"
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Teks Eksplanasi",
                    frontText = "Apa yang dimaksud dengan Teks Eksplanasi?",
                    backText = "Teks yang menjelaskan proses terjadinya fenomena alam, sosial, ilmu pengetahuan, atau budaya."
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Teks Eksplanasi",
                    frontText = "Apa ciri kebahasaan yang menonjol pada Teks Eksplanasi?",
                    backText = "Menggunakan konjungsi kausalitas (sebab-akibat) dan kronologis (waktu)."
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Ceramah",
                    frontText = "Apa perbedaan utama Ceramah, Pidato, dan Khotbah?",
                    backText = "Ceramah bersifat memberikan nasihat/pengetahuan agama atau umum kepada pendengar umum, pidato formal kelembagaan, dan khotbah terikat ibadah khusus."
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Karya Ilmiah",
                    frontText = "Apa saja bagian awal (pendahuluan) dalam sebuah Karya Ilmiah?",
                    backText = "Latar belakang masalah, rumusan masalah, tujuan penelitian, dan manfaat penelitian."
                ),
                FlashcardEntity(
                    subject = "Bahasa Indonesia",
                    subTopic = "Resensi Buku",
                    frontText = "Sebutkan unsur-unsur penting dalam meresensi sebuah buku!",
                    backText = "1. Judul resensi\n2. Identitas buku\n3. Sinopsis\n4. Keunggulan & kekurangan\n5. Kesimpulan"
                )
            )
            dao.insertAll(initialCards)
        }
    }
}