package com.fit2081.a1ryanhii34466576.ui.screen.questionnaire

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.fit2081.a1ryanhii34466576.R
import java.util.Calendar
import java.util.Locale

val personas = listOf(
    "Health Devotee" to "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.",
    "Mindful Eater" to "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.",
    "Wellness Striver" to "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.",
    "Balance Seeker" to "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",
    "Health Procrastinator" to "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.",
    "Food Carefree" to "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat."
)

val personaImages = listOf(
    R.drawable.persona1,
    R.drawable.persona2,
    R.drawable.persona3,
    R.drawable.persona4,
    R.drawable.persona5,
    R.drawable.persona6
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireScreen(
    navController: NavHostController,
    userId: String,
    viewModel: QuestionnaireViewModel = viewModel()
) {
    val context = LocalContext.current

    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val dropdownSelectedPersona by viewModel.dropdownSelectedPersona.collectAsState()
    val biggestMealTime by viewModel.biggestMealTime.collectAsState()
    val sleepTime by viewModel.sleepTime.collectAsState()
    val wakeUpTime by viewModel.wakeUpTime.collectAsState()
    val showModal by viewModel.showModal.collectAsState()
    val selectedPersona by viewModel.selectedPersona.collectAsState()
    val personaDescription by viewModel.personaDescription.collectAsState()
    val dropdownExpanded by viewModel.dropdownExpanded.collectAsState()

    val foodCategories = listOf(
        "Fruits", "Vegetables", "Grains", "Red Meat", "Seafood",
        "Poultry", "Fish", "Eggs", "Nuts/Seeds"
    )

    LaunchedEffect(userId) {
        viewModel.loadFromRepository(context, userId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
        ) {
            Text(
                "Food Intake Questionnaire",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Text(
                "Tick the food categories you can eat",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))

            Column {
                // Display food categories in rows of 3
                foodCategories.chunked(3).forEach { row ->
                    // Create a row for each chunk
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // For each item in the row, create a checkbox and label
                        row.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = selectedCategories.contains(item),
                                    onCheckedChange = { viewModel.toggleCategory(item) },
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(item, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            Text("Your Persona", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))

            // Display personas in rows of 3
            personas.chunked(3).forEach { row ->
                // Create a row for each chunk
                Row(modifier = Modifier.fillMaxWidth()) {
                    // For each item in the row, create a button
                    row.forEach { (persona, description) ->
                        Button(
                            onClick = { viewModel.selectPersona(persona, description) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(
                                persona,
                                fontSize = 10.sp,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { viewModel.toggleDropdown() },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = dropdownSelectedPersona,
                    onValueChange = {},
                    label = { Text("Select Persona") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(dropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { viewModel.toggleDropdown() }
                ) {
                    personas.forEach { (persona, _) ->
                        DropdownMenuItem(
                            text = { Text(persona) },
                            onClick = { viewModel.setDropdownPersona(persona) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            Text("Timings", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))

            TimeInput("What time do you have your biggest meal?", biggestMealTime) {
                viewModel.setTime("meal", it)
            }
            TimeInput("What time do you go to sleep?", sleepTime) {
                viewModel.setTime("sleep", it)
            }
            TimeInput("What time do you wake up?", wakeUpTime) {
                viewModel.setTime("wake", it)
            }
        }

        Button(
            onClick = {
                viewModel.saveIntake(
                    context = context,
                    userId = userId,
                    onSuccess = {
                        navController.navigate("home/$userId") {
                            popUpTo("questionnaire/$userId") { inclusive = true }
                        }
                    },
                    onError = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text("Save")
        }
    }

    PersonaModal(
        showDialog = showModal,
        onDismiss = { viewModel.dismissModal() },
        selectedPersona = selectedPersona,
        personaDescription = personaDescription
    )
}

@Composable
fun TimeInput(label: String, time: String, onTimePicked: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), fontSize = 12.sp)

        OutlinedTextField(
            value = time,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .width(120.dp)
                .padding(top = 4.dp),
            leadingIcon = {
                IconButton(onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            onTimePicked(
                                String.format(
                                    Locale.getDefault(),
                                    "%02d:%02d",
                                    hour,
                                    minute
                                )
                            )
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Time Input Icon")
                }
            }
        )
    }
}

@Composable
private fun PersonaModal(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    selectedPersona: String,
    personaDescription: String
) {
    if (showDialog) {
        // Find the index of the selected persona to get the corresponding image
        val personaIndex = personas.indexOfFirst { it.first == selectedPersona }
        // Get the image resource ID based on the index, or return if not found
        val imageResId = personaImages.getOrNull(personaIndex) ?: return

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = { Button(onClick = onDismiss) { Text("Dismiss") } },
            title = { Text(text = selectedPersona) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "$selectedPersona Image",
                        modifier = Modifier
                            .size(128.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = personaDescription, textAlign = TextAlign.Justify)
                }
            }
        )
    }
}
