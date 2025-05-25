package com.fit2081.a1ryanhii34466576.ui.screen.settings

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(
    navController: NavHostController,
    userId: String,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current

    val userName by settingsViewModel.userName.collectAsState()
    val phoneNumber by settingsViewModel.phoneNumber.collectAsState()
    val editableName by settingsViewModel.editableName.collectAsState()
    val editablePhone by settingsViewModel.editablePhone.collectAsState()
    val editablePassword by settingsViewModel.editablePassword.collectAsState()
    val showPassword by settingsViewModel.showPassword.collectAsState()
    val activeField by settingsViewModel.activeEditField.collectAsState()
    val editError by settingsViewModel.editError.collectAsState()

    LaunchedEffect(userId) {
        settingsViewModel.loadUser(context, userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Settings",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("ACCOUNT", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))

        IconFieldRow(Icons.Default.Badge, "User ID", userId, editable = false)
        IconFieldRow(Icons.Default.Person, "Name", userName) {
            settingsViewModel.openFieldEditDialog(EditField.NAME)
        }
        IconFieldRow(Icons.Default.Phone, "Phone", phoneNumber) {
            settingsViewModel.openFieldEditDialog(EditField.PHONE)
        }
        IconFieldRow(Icons.Default.Lock, "Password", "••••••") {
            settingsViewModel.openFieldEditDialog(EditField.PASSWORD)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text("OTHER SETTINGS", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))

        SettingActionRow(Icons.AutoMirrored.Filled.Logout, "Logout") {
            settingsViewModel.logout(context) {
                navController.navigate("welcome") {
                    popUpTo("home/$userId") { inclusive = true }
                }
            }
        }

        SettingActionRow(Icons.Default.MedicalServices, "Clinician Login") {
            navController.navigate("clinicianLogin") {
                popUpTo("home/$userId") { inclusive = true }
            }
        }

        SettingActionRow(Icons.AutoMirrored.Filled.ListAlt, "View Questionnaire History") {
            navController.navigate("foodIntakeHistory/$userId")
        }
    }

    when (activeField) {
        EditField.NAME -> EditFieldDialog(
            title = "Name",
            value = editableName,
            errorMessage = editError,
            onValueChange = settingsViewModel::onEditableNameChange,
            onDismiss = settingsViewModel::closeFieldEditDialog,
            onSave = {
                settingsViewModel.saveSingleField(context, EditField.NAME) {
                    Log.e("SettingsScreen", it)
                }
            }
        )

        EditField.PHONE -> EditFieldDialog(
            title = "Phone",
            value = editablePhone,
            errorMessage = editError,
            onValueChange = settingsViewModel::onEditablePhoneChange,
            onDismiss = settingsViewModel::closeFieldEditDialog,
            onSave = {
                settingsViewModel.saveSingleField(context, EditField.PHONE) {
                    Log.e("SettingsScreen", it)
                }
            }
        )

        EditField.PASSWORD -> EditFieldDialog(
            title = "Password",
            value = editablePassword,
            errorMessage = editError,
            onValueChange = settingsViewModel::onEditablePasswordChange,
            onDismiss = settingsViewModel::closeFieldEditDialog,
            onSave = {
                settingsViewModel.saveSingleField(context, EditField.PASSWORD) {
                    Log.e("SettingsScreen", it)
                }
            },
            isPassword = true,
            showPassword = showPassword,
            togglePasswordVisibility = settingsViewModel::togglePasswordVisibility
        )

        null -> {}
    }
}

@Composable
fun IconFieldRow(
    icon: ImageVector,
    label: String,
    value: String,
    editable: Boolean = true,
    onEdit: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
        // Show edit button only if editable and onEdit is provided
        if (editable && onEdit != null) {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit $label")
            }
        }
    }
}

@Composable
fun SettingActionRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, modifier = Modifier.weight(1f))
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate to $label"
        )
    }
}

@Composable
fun EditFieldDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    togglePasswordVisibility: () -> Unit = {},
    errorMessage: String? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit $title") },
        confirmButton = { Button(onClick = onSave) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        text = {
            Column {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(title) },
                    keyboardOptions = KeyboardOptions.Default,
                    // If it's a password field, allow toggling visibility
                    visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    // Add trailing icon for password visibility toggle
                    trailingIcon = if (isPassword) {
                        {
                            IconButton(onClick = togglePasswordVisibility) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (!errorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.padding(top = 2.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    )
}
