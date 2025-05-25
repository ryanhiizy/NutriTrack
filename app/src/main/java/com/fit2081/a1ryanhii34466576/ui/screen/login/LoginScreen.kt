package com.fit2081.a1ryanhii34466576.ui.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val claimedUsers by loginViewModel.claimedUsers.collectAsState()
    val navTarget by loginViewModel.loginNavigationTarget.collectAsState()
    val selectedUserId by loginViewModel.selectedUserId.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val showPassword by loginViewModel.showPassword.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()
    val expanded by loginViewModel.dropdownExpanded.collectAsState()

    LaunchedEffect(navTarget) {
        // Navigate to the target screen if it is set
        navTarget?.let {
            navController.navigate(it) {
                popUpTo("login") { inclusive = true }
            }
            // Clear the navigation target after navigating
            loginViewModel.clearNavigationTarget()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Log In", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { loginViewModel.toggleDropdown() }
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
                onDismissRequest = { loginViewModel.toggleDropdown() }
            ) {
                claimedUsers.forEach {
                    DropdownMenuItem(
                        text = { Text(it.userId) },
                        onClick = { loginViewModel.onUserIdSelected(it.userId) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            label = { Text("Password") },
            singleLine = true,
            // Apply visual transformation based on password visibility
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            // Add icon for password visibility toggle
            trailingIcon = {
                IconButton(onClick = { loginViewModel.togglePasswordVisibility() }) {
                    Icon(
                        if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        null
                    )
                }
            },
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

        Button(
            onClick = { loginViewModel.attemptLogin {} },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Don't have an account? Register")
        }
    }
}
