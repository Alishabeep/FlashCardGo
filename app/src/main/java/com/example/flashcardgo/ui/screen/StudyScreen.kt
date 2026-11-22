package com.example.flashcardgo.ui.screen

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.flashcardgo.ui.component.FlipCardItem
import com.example.flashcardgo.ui.viewmodel.FlashcardViewModel
import com.example.flashcardgo.utils.SensorController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(viewModel: FlashcardViewModel) {
    val context = LocalContext.current
    val cards by viewModel.cards.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFlipped by viewModel.isFlipped.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    val subjects = listOf("Semua", "Bahasa Jepang", "Bahasa Inggris", "Bahasa Indonesia")

    var lightLevel by remember { mutableFloatStateOf(0f) }

    // Integrasi 2 Sensor: Accelerometer (Shake) & Light Sensor (Cahaya)
    DisposableEffect(context) {
        val sensorController = SensorController(
            context = context,
            onShake = {
                viewModel.shuffleCards()
                Toast.makeText(context, "🔄 Kartu diacak via Shake Sensor!", Toast.LENGTH_SHORT).show()
            },
            onLightChanged = { lux ->
                lightLevel = lux
            }
        )
        sensorController.register()
        onDispose { sensorController.unregister() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FlashCardGo") },
                actions = {
                    IconButton(onClick = { viewModel.shuffleCards() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Acak Kartu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Kartu")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Filter Mata Pelajaran
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.forEach { subject ->
                    FilterChip(
                        selected = selectedSubject == subject,
                        onClick = { viewModel.selectSubject(subject) },
                        label = { Text(subject) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (cards.isNotEmpty()) {
                val safeIndex = currentIndex.coerceIn(0, cards.lastIndex)
                Text(
                    text = "Kartu ${safeIndex + 1} dari ${cards.size}",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(12.dp))

                FlipCardItem(
                    card = cards[safeIndex],
                    isFlipped = isFlipped,
                    onFlip = { viewModel.flipCard() }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { viewModel.flipCard() }) {
                        Text(if (isFlipped) "Lihat Soal" else "Lihat Jawaban")
                    }
                    Button(onClick = { viewModel.nextCard() }) {
                        Text("Kartu Berikutnya")
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Belum ada kartu untuk kategori ini.")
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💡 Sensor Aktif: Kocok HP untuk mengacak kartu • Cahaya sekitar: ${lightLevel.toInt()} Lux",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Dialog Tambah Kartu Baru ke SQLite
        if (showAddDialog) {
            AddCardDialog(
                onDismiss = { showAddDialog = false },
                onSave = { subj, topic, front, back ->
                    viewModel.addNewCard(subj, topic, front, back)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var subject by remember { mutableStateOf("Bahasa Jepang") }
    var subTopic by remember { mutableStateOf("") }
    var front by remember { mutableStateOf("") }
    var back by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Kartu Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Mata Pelajaran") })
                OutlinedTextField(value = subTopic, onValueChange = { subTopic = it }, label = { Text("Sub-Topik (mis: Hiragana)") })
                OutlinedTextField(value = front, onValueChange = { front = it }, label = { Text("Pertanyaan / Kosakata") })
                OutlinedTextField(value = back, onValueChange = { back = it }, label = { Text("Jawaban / Penjelasan") })
            }
        },
        confirmButton = {
            Button(
                onClick = { if (front.isNotBlank() && back.isNotBlank()) onSave(subject, subTopic, front, back) }
            ) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}