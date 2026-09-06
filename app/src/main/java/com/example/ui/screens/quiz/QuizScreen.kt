package com.example.ui.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.WordEntity
import com.example.ui.LinguaViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModelFactory: LinguaViewModelFactory,
    onNavigateBack: () -> Unit
) {
    val viewModel: QuizViewModel = viewModel(factory = viewModelFactory)
    
    val currentWord by viewModel.currentWord.collectAsState()
    val options by viewModel.options.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val score by viewModel.score.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    
    val coroutineScope = rememberCoroutineScope()
    
    // We need to fetch all words to pass to nextQuestion, this is a bit of a hack for compose
    // but works for this offline app
    LaunchedEffect(Unit) {
        viewModel.loadRandomQuiz()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Quiz", fontWeight = FontWeight.Bold) },
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
            if (isFinished) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Quiz Complete!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "You scored $score / 5",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "+${score * 5} XP",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Finish", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            } else if (currentWord != null) {
                Text(
                    text = "Question ${currentIndex + 1} of 5",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "What is the translation for:",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = currentWord!!.nativeWord,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                options.forEach { option ->
                    val isSelected = selectedAnswer == option
                    val isCorrect = option == currentWord!!.translation
                    
                    val containerColor = when {
                        selectedAnswer == null -> MaterialTheme.colorScheme.surface
                        isSelected && isCorrect -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        isSelected && !isCorrect -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                        !isSelected && isCorrect -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.surface
                    }
                    
                    val borderColor = when {
                        selectedAnswer == null -> MaterialTheme.colorScheme.outline
                        isSelected && isCorrect -> MaterialTheme.colorScheme.secondary
                        isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                        !isSelected && isCorrect -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.outline
                    }
                    
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                        onClick = { 
                            if (selectedAnswer == null) {
                                viewModel.selectAnswer(option)
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                if (selectedAnswer != null) {
                    Button(
                        onClick = { viewModel.nextQuestion() },
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Next", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
