package com.fit2081.a1ryanhii34466576.ui.screen

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.fit2081.a1ryanhii34466576.CsvReader.getUserById
import com.fit2081.a1ryanhii34466576.User

@Composable
fun InsightsScreen(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val context = LocalContext.current
    val userId: String = navBackStackEntry.arguments?.getString("userId") ?: "Guest"
    val user: User? = getUserById(userId, context)

    Log.d("InsightsScreen", "User: $user")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Insights",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Food Quality Score",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user?.foodScore?.toString() ?: "N/A",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        NutritionProgressBar(
            label = "Discretionary",
            score = user?.discretionaryScore ?: 0.0,
            maxScore = 10
        )
        NutritionProgressBar(
            label = "Vegetables",
            score = user?.vegetableScore ?: 0.0,
            maxScore = 10
        )
        NutritionProgressBar(label = "Fruits", score = user?.fruitScore ?: 0.0, maxScore = 10)
        NutritionProgressBar(
            label = "Grains & Cereals",
            score = user?.grainsCerealsScore ?: 0.0,
            maxScore = 5
        )
        NutritionProgressBar(
            label = "Whole Grains",
            score = user?.wholeGrainsScore ?: 0.0,
            maxScore = 5
        )
        NutritionProgressBar(
            label = "Meat & Alternatives",
            score = user?.meatAlternativesScore ?: 0.0,
            maxScore = 10
        )
        NutritionProgressBar(
            label = "Dairy & Alternatives",
            score = user?.dairyScore ?: 0.0,
            maxScore = 10
        )
        NutritionProgressBar(label = "Water", score = user?.waterScore ?: 0.0, maxScore = 5)
        NutritionProgressBar(label = "Sodium", score = user?.sodiumScore ?: 0.0, maxScore = 10)
        NutritionProgressBar(label = "Sugar", score = user?.sugarScore ?: 0.0, maxScore = 10)
        NutritionProgressBar(label = "Alcohol", score = user?.alcoholScore ?: 0.0, maxScore = 5)
        NutritionProgressBar(
            label = "Saturated Fat",
            score = user?.saturatedFatScore ?: 0.0,
            maxScore = 5
        )
        NutritionProgressBar(
            label = "Unsaturated Fat",
            score = user?.unsaturatedFatScore ?: 0.0,
            maxScore = 5
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val shareIntent = Intent(ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "My Food Quality Score: ${user?.foodScore ?: "N/A"}"
                )
                context.startActivity(Intent.createChooser(shareIntent, "Share score via"))
            }) {
                Text("Share with someone")
            }
            Button(onClick = { }) {
                Text("Improve my diet")
            }
        }
    }
}

@Composable
fun NutritionProgressBar(label: String, score: Double, maxScore: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (score % 1 == 0.0) "${score.toInt()} / $maxScore" else "$score / $maxScore",
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
