package com.fit2081.a1ryanhii34466576.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun BottomBar(navController: NavHostController, userId: String) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            BottomBarButton(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                label = "Home",
                onClick = {
                    navController.navigate("home/$userId") {
                        popUpTo("home/$userId") {
                            inclusive = true
                        } // Inclusive to avoid multiple home pages in the back stack
                    }
                },
                modifier = Modifier.weight(1f)
            )

            BottomBarButton(
                icon = { Icon(Icons.Filled.Insights, contentDescription = "Insights") },
                label = "Insights",
                onClick = {
                    navController.navigate("insights/$userId") {
                        popUpTo("home/$userId")
                    }
                },
                modifier = Modifier.weight(1f)
            )

            BottomBarButton(
                icon = { Icon(Icons.Filled.SupportAgent, contentDescription = "NutriCoach") },
                label = "NutriCoach",
                onClick = {
                    navController.navigate("nutricoach/$userId") {
                        popUpTo("home/$userId")
                    }
                },
                modifier = Modifier.weight(1f)
            )

            BottomBarButton(
                icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                label = "Settings",
                onClick = {
                    navController.navigate("settings/$userId") {
                        popUpTo("home/$userId")
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BottomBarButton(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick, modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            icon()
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

