package com.fit2081.a1ryanhii34466576.ui.screen

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fit2081.a1ryanhii34466576.R

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
fun QuestionnaireScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("FoodIntakePrefs", Context.MODE_PRIVATE)

    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var selectedPersona by remember { mutableStateOf("") }
    var dropdownSelectedPersona by remember { mutableStateOf("") }
    var biggestMealTime by remember { mutableStateOf("") }
    var sleepTime by remember { mutableStateOf("") }
    var wakeUpTime by remember { mutableStateOf("") }
    var personaDescription by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val foodCategories = listOf(
        "Fruits",
        "Vegetables",
        "Grains",
        "Red Meat",
        "Seafood",
        "Poultry",
        "Fish",
        "Eggs",
        "Nuts/Seeds"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Food Intake Questionnaire", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Tick all the food categories you can eat", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                foodCategories.chunked(3).forEach { rowItems ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowItems.forEach { category ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            ) {
                                Checkbox(
                                    checked = selectedCategories.contains(category),
                                    onCheckedChange = {
                                        selectedCategories = if (it) {
                                            selectedCategories + category
                                        } else {
                                            selectedCategories - category
                                        }
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = category, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Your Persona", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "People can be broadly classified into 6 different types based on their eating preferences. Click on each button below to find out the different types, and select the type that best fits you!",
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                personas.chunked(3).forEach { rowItems ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowItems.forEach { (persona, description) ->
                            Button(
                                onClick = {
                                    selectedPersona = persona
                                    personaDescription = description
                                    showDialog = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            ) {
                                Text(text = persona, fontSize = 7.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = dropdownSelectedPersona,
                    onValueChange = {},
                    label = { Text(text = "Select Persona") },
                    trailingIcon = {
                        TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    personas.forEach { (persona, _) ->
                        DropdownMenuItem(
                            text = { Text(text = persona) },
                            onClick = {
                                dropdownSelectedPersona = persona
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Timings", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 280.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "What time of day approx. do you normally eat your biggest meal?",
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp
                    )
                    OutlinedTextField(
                        value = biggestMealTime,
                        onValueChange = { biggestMealTime = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(80.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "What time of day approx. do you go to sleep at night?",
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp
                    )
                    OutlinedTextField(
                        value = sleepTime,
                        onValueChange = { sleepTime = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(80.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "What time of day approx. do you wake up in the morning?",
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp
                    )
                    OutlinedTextField(
                        value = wakeUpTime,
                        onValueChange = { wakeUpTime = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(80.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                saveData(
                    sharedPreferences,
                    selectedCategories,
                    selectedPersona,
                    biggestMealTime,
                    sleepTime,
                    wakeUpTime
                )
                Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show()
                navController.navigate("home")
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Save")
        }
    }

    PersonaModal(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        selectedPersona = selectedPersona,
        personaDescription = personaDescription
    )
}

@Composable
private fun PersonaModal(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    selectedPersona: String,
    personaDescription: String
) {
    if (showDialog) {
        val personaIndex = personas.indexOfFirst { it.first == selectedPersona }
        val imageResId = personaImages[personaIndex]

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Dismiss")
                }
            },
            title = { Text(text = selectedPersona) },
            text = {
                Column {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "$selectedPersona Image",
                        modifier = Modifier
                            .size(128.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = personaDescription)
                }
            }
        )
    }
}

private fun saveData(
    sharedPreferences: SharedPreferences,
    selectedCategories: Set<String>,
    selectedPersona: String,
    biggestMealTime: String,
    sleepTime: String,
    wakeUpTime: String
) {
    with(sharedPreferences.edit()) {
        putStringSet("selectedCategories", selectedCategories)
        putString("selectedPersona", selectedPersona)
        putString("biggestMealTime", biggestMealTime)
        putString("sleepTime", sleepTime)
        putString("wakeUpTime", wakeUpTime)
        apply()
    }
}