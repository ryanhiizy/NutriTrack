package com.fit2081.a1ryanhii34466576.ui.screen.clinicianLogin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun ClinicianLoginScreen(
    navController: NavHostController,
    viewModel: ClinicianLoginViewModel = viewModel()
) {
    val keyInput by viewModel.keyInput.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Clinician Login",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = keyInput,
            onValueChange = viewModel::onKeyChange,
            label = { Text("Clinician Key") },
            placeholder = { Text("Enter your clinician key") },
            isError = errorState,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorState) {
            Text(
                text = "Incorrect key. Please try again.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.attemptLogin(
                    onSuccess = { navController.navigate("clinicianDashboard") },
                    onFailure = {} // Already handled via state
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.width(8.dp))
            Text("Login")
        }
    }
}
