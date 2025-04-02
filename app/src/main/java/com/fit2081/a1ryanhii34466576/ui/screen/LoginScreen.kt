package com.fit2081.a1ryanhii34466576.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fit2081.a1ryanhii34466576.data.CsvReader
import com.fit2081.a1ryanhii34466576.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var selectedUserId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val context: Context = LocalContext.current
    val userData: List<User> =
        CsvReader.loadUserData(context) // Load user data from CSV

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Login Screen", style = MaterialTheme.typography.titleLarge)
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
                label = { Text(text = "ID") },
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

        // Text field for Phone Number
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        Button(
            onClick = {
                val user = userData.find { it.userId == selectedUserId }

                if (user != null && user.phoneNumber == phoneNumber) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                    navController.navigate("questionnaire")
                } else {
                    Toast.makeText(context, "Incorrect Credentials", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            Text(text = "Continue")
        }
    }
}
