package com.fit2081.a1ryanhii34466576.ui.screen.nutricoach

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun NutriCoachScreen(userId: String, viewModel: NutriCoachViewModel = viewModel()) {
    val patient by viewModel.currentPatient.collectAsState()
    val isOptimal by viewModel.isFruitScoreOptimal.collectAsState()
    val fruitInput by viewModel.fruitInput.collectAsState()
    val fruitInfo by viewModel.fruitInfo.collectAsState()
    val fruitError by viewModel.error.collectAsState()
    val message by viewModel.motivationalMessage.collectAsState()
    val savedTips by viewModel.savedTips.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()
    val isLoadingTip by viewModel.isLoadingTip.collectAsState()
    val isLoadingFruit by viewModel.isLoadingFruit.collectAsState()
    val showDialog by viewModel.showTipsDialog.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "NutriCoach",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // AI motivational message
        patient?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Tip", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    Button(onClick = { viewModel.generateMotivationalMessage(it) }) {
                        Text("Generate Motivational Message")
                    }

                    if (isLoadingTip) {
                        // Show loading indicator while fetching message
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // Display the motivational message
                    message?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, textAlign = TextAlign.Justify)
                    }

                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.openTipsDialog() }) {
                        Text("Show All Saved Tips")
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Fruit score
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Fruit Score", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                when (isOptimal) {
                    true -> Text("Fruit score is optimal ✅")
                    false -> Text("Fruit score is sub-optimal ❌")
                    null -> Text("Loading score...")
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        if (isOptimal == true) {
            // Image display when fruit score is optimal
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("You're doing great!", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    imageUrl?.let { url ->
                        // State to manage loading indicator
                        var isLoading by remember { mutableStateOf(true) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Show loading indicator while image is loading
                            if (isLoading) {
                                CircularProgressIndicator()
                            }

                            // Display the image
                            AsyncImage(
                                model = url,
                                contentDescription = "Motivational Image",
                                modifier = Modifier.fillMaxWidth(),
                                onLoading = { isLoading = true },
                                onSuccess = { isLoading = false },
                                onError = { isLoading = false }
                            )
                        }
                    }
                }
            }
        } else if (isOptimal == false) {
            // Show fruit lookup section when not optimal
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Fruit Lookup", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = fruitInput,
                        onValueChange = viewModel::onFruitInputChange,
                        label = { Text("Enter fruit name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(onClick = { viewModel.fetchFruitInfo() }) {
                        Text("Search Fruit Info")
                    }

                    if (isLoadingFruit) {
                        // Show loading indicator while fetching fruit info
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(12.dp))
                        }
                    }

                    // Display fruit information or error
                    fruitError?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    // Display fruit information if available
                    fruitInfo?.let {
                        Spacer(Modifier.height(8.dp))
                        Text("Fruit: ${it.name}")
                        Text("Family: ${it.family}")
                        Text("Order: ${it.order}")
                        Text("Genus: ${it.genus}")
                        Text("Calories: ${it.nutritions.calories}")
                        Text("Fat: ${it.nutritions.fat}")
                        Text("Sugar: ${it.nutritions.sugar}")
                        Text("Carbs: ${it.nutritions.carbohydrates}")
                        Text("Protein: ${it.nutritions.protein}")
                    }
                }
            }
        }
    }

    // Scrollable saved tips dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.closeTipsDialog() },
            confirmButton = {
                TextButton(onClick = { viewModel.closeTipsDialog() }) {
                    Text("Dismiss")
                }
            },
            title = { Text("Saved Tips") },
            text = {
                Box(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        savedTips.forEach { tip ->
                            Text(
                                text = "• $tip",
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}
