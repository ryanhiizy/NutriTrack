package com.fit2081.a1ryanhii34466576.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fit2081.a1ryanhii34466576.CsvReader
import com.fit2081.a1ryanhii34466576.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val userData: List<User> = CsvReader.loadUserData(context)

    var selectedUserId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isUserValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Log in",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Referenced from: https://medium.com/@german220291/building-a-custom-exposed-dropdown-menu-with-jetpack-compose-d65232535bf2
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedUserId,
                onValueChange = {},
                label = { Text(text = "Select User ID") },
                trailingIcon = {
                    TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                userData.forEach { user: User ->
                    DropdownMenuItem(
                        text = { Text(text = user.userId) },
                        onClick = {
                            selectedUserId = user.userId
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
        )

        if (!isUserValid) {
            Text(
                text = "Incorrect phone number",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val user = userData.find { it.userId == selectedUserId }

                if (user != null && user.phoneNumber == phoneNumber) {
                    isUserValid = true
                    val userPref =
                        context.getSharedPreferences(selectedUserId, Context.MODE_PRIVATE)

                    if (userPref.contains("selectedCategories")) {
                        navController.navigate("home/$selectedUserId") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    } else {
                        navController.navigate("questionnaire/$selectedUserId") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }

                } else {
                    isUserValid = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Continue")
        }
    }
}
