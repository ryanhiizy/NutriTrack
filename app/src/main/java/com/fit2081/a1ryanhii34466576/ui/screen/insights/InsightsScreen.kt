package com.fit2081.a1ryanhii34466576.ui.screen.insights

import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun InsightsScreen(
    navController: NavController,
    userId: String,
    viewModel: InsightsViewModel = viewModel()
) {
    val context = LocalContext.current
    val patient by viewModel.patient.collectAsState()

    LaunchedEffect(userId) { viewModel.loadPatient(userId) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Insights: Food Score",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total Food Quality Score", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = patient?.foodScore?.toString() ?: "N/A",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        fun score(s: Double?) = s ?: 0.0
        NutritionProgressBar("Discretionary", score(patient?.discretionaryScore), 10)
        NutritionProgressBar("Vegetables", score(patient?.vegetableScore), 10)
        NutritionProgressBar("Fruits", score(patient?.fruitScore), 10)
        NutritionProgressBar("Grains & Cereals", score(patient?.grainsCerealsScore), 5)
        NutritionProgressBar("Whole Grains", score(patient?.wholeGrainsScore), 5)
        NutritionProgressBar("Meat & Alternatives", score(patient?.meatAlternativesScore), 10)
        NutritionProgressBar("Dairy & Alternatives", score(patient?.dairyAlternativesScore), 10)
        NutritionProgressBar("Water", score(patient?.waterScore), 5)
        NutritionProgressBar("Sodium", score(patient?.sodiumScore), 10)
        NutritionProgressBar("Sugar", score(patient?.sugarScore), 10)
        NutritionProgressBar("Alcohol", score(patient?.alcoholScore), 5)
        NutritionProgressBar("Saturated Fat", score(patient?.saturatedFatScore), 5)
        NutritionProgressBar("Unsaturated Fat", score(patient?.unsaturatedFatScore), 5)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val shareIntent = Intent(ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "My Food Quality Score: ${patient?.foodScore ?: "N/A"}"
                    )
                    context.startActivity(
                        Intent.createChooser(shareIntent, "Share score via")
                    )
                }
            ) { Text("Share with someone") }

            Button(
                onClick = {
                    navController.navigate("nutricoach/$userId")
                }
            ) { Text("Improve my diet") }
        }
    }
}

@Composable
fun NutritionProgressBar(label: String, score: Double, maxScore: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                // Display score as integer if it's a whole number, otherwise show as float
                text =
                    if (score % 1 == 0.0) "${score.toInt()} / $maxScore"
                    else "$score / $maxScore",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        LinearProgressIndicator(
            progress = { (score / maxScore).toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
