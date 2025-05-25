package com.fit2081.a1ryanhii34466576.ui.screen.foodIntakeHistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.a1ryanhii34466576.data.model.FoodIntake
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FoodIntakeHistoryScreen(
    userId: String,
    viewModel: FoodIntakeHistoryViewModel = viewModel()
) {
    val foodIntakes by viewModel.foodIntakes.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadFoodIntakes(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Questionnaire History",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (foodIntakes.isEmpty()) {
            Text("No records found.")
        } else {
            LazyColumn {
                items(foodIntakes) { intake ->
                    // Only allow deletion if there are multiple food intakes
                    val isDeletable = foodIntakes.size > 1

                    FoodIntakeCard(
                        intake = intake,
                        isDeletable = isDeletable,
                        onDelete = { viewModel.deleteFoodIntake(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun FoodIntakeCard(intake: FoodIntake, isDeletable: Boolean, onDelete: (FoodIntake) -> Unit) {
    val formattedDate = remember(intake.timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.format(Date(intake.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Delete button only visible if isDeletable is true
            if (isDeletable) {
                IconButton(
                    onClick = { onDelete(intake) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Display the food intake details
            Column(modifier = Modifier.padding(16.dp)) {
                Text("$formattedDate", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Sleep Time: ${intake.sleepTime}")
                Text("Wake Up Time: ${intake.wakeUpTime}")
                Text("Biggest Meal Time: ${intake.mealTime}")
                Text("Persona: ${intake.persona}")
                Text("Categories: ${intake.selectedCategories.replace(",", ", ")}")
            }
        }

    }
}
