package com.example.ui.screens.learn

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.WordEntity
import com.example.ui.LinguaViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    viewModelFactory: LinguaViewModelFactory,
    onNavigateBack: () -> Unit
) {
    val viewModel: LearnViewModel = viewModel(factory = viewModelFactory)
    val words by viewModel.words.collectAsState()
    
    // Simple state to iterate through words
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    
    // For simplicity, just use the current category words
    val activeWords = words.filter { it.status == "LEARNING" || it.status == "DIFFICULT" }.ifEmpty { words }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Flashcards", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (activeWords.isEmpty()) {
                Text(
                    text = "No words to review in this category.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            } else {
                Text(
                    text = "${currentIndex + 1} / ${activeWords.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                val currentWord = activeWords[currentIndex]
                
                FlashcardItem(
                    word = currentWord,
                    isFlipped = isFlipped,
                    onCardClick = { isFlipped = !isFlipped },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (isFlipped) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.updateWordStatus(currentWord, "DIFFICULT")
                                isFlipped = false
                                if (currentIndex < activeWords.size - 1) currentIndex++ else onNavigateBack()
                            },
                            modifier = Modifier.weight(1f).height(64.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Text("Hard", fontWeight = FontWeight.Bold)
                        }
                        
                        Button(
                            onClick = {
                                viewModel.updateWordStatus(currentWord, "KNOW_IT")
                                isFlipped = false
                                if (currentIndex < activeWords.size - 1) currentIndex++ else onNavigateBack()
                            },
                            modifier = Modifier.weight(1f).height(64.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Easy", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = { isFlipped = true },
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Show Answer", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardItem(
    word: WordEntity,
    isFlipped: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "rotation"
    )

    Card(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Front side
                Text(
                    text = word.nativeWord,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                // Back side (flipped text to correct orientation)
                Column(
                    modifier = Modifier
                        .graphicsLayer { rotationY = 180f }
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = word.translation,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = word.pronunciation,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    if (word.exampleSentence.isNotEmpty()) {
                        Text(
                            text = "\"${word.exampleSentence}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
