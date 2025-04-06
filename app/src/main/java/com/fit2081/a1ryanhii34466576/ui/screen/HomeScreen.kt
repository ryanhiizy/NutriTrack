package com.fit2081.a1ryanhii34466576.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.fit2081.a1ryanhii34466576.CsvReader.getUserById
import com.fit2081.a1ryanhii34466576.User

@Composable
fun HomeScreen(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val context = LocalContext.current
    val userId: String = navBackStackEntry.arguments?.getString("userId") ?: "Guest"
    val user: User? = getUserById(userId, context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hello, User $userId!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { navController.navigate("questionnaire/$userId") }) {
                Text(text = "Edit Questionnaire")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                    text = "Your Food Quality Score",
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

        Text(
            text = "What is the Food Quality Score?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines. It helps you identify strengths and areas for improvement in your diet.\n\nThis score considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for healthier choices.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify
        )
    }
}
