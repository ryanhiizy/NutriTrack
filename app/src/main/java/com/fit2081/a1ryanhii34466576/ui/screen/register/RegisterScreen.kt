package com.fit2081.a1ryanhii34466576.ui.screen.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val unregisteredUsers by registerViewModel.unclaimedUsers.collectAsState()
    val selectedUserId by registerViewModel.selectedUserId.collectAsState()
    val name by registerViewModel.name.collectAsState()
    val phoneNumber by registerViewModel.phoneNumber.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val confirmPassword by registerViewModel.confirmPassword.collectAsState()
    val showPassword by registerViewModel.showPassword.collectAsState()
    val expanded by registerViewModel.expanded.collectAsState()
    val errorMessage by registerViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        registerViewModel.loadUnclaimedUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // User ID dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { registerViewModel.toggleDropdown() }
        ) {
            OutlinedTextField(
                value = selectedUserId,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select User ID") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { registerViewModel.toggleDropdown() }
            ) {
                unregisteredUsers.forEach {
                    DropdownMenuItem(
                        text = { Text(it.userId) },
                        onClick = { registerViewModel.onUserIdSelected(it.userId) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = { registerViewModel.onNameChange(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Phone Number
        OutlinedTextField(
            value = phoneNumber,
            // Ensure only digits are allowed
            onValueChange = { registerViewModel.onPhoneChange(it) },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { registerViewModel.onPasswordChange(it) },
            label = { Text("Password") },
            singleLine = true,
            // Toggle password visibility
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            // Add icon for password visibility toggle
            trailingIcon = {
                IconButton(onClick = { registerViewModel.togglePasswordVisibility() }) {
                    Icon(
                        if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
            label = { Text("Confirm Password") },
            singleLine = true,
            // Toggle password visibility
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {
                registerViewModel.registerUserIfValid {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Already have an account? Log in")
        }
    }
}
