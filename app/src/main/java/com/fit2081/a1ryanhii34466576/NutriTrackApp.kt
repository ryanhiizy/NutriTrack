package com.fit2081.a1ryanhii34466576

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fit2081.a1ryanhii34466576.ui.component.BottomBar
import com.fit2081.a1ryanhii34466576.ui.screen.*

@Composable
fun NutriTrackApp() {
    val navController = rememberNavController()

    // Referenced from: https://stackoverflow.com/a/67603294
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val userId = navBackStackEntry?.arguments?.getString("userId")

    Scaffold(
        bottomBar = {
            if (navBackStackEntry?.destination?.route?.startsWith("home/") == true ||
                navBackStackEntry?.destination?.route?.startsWith("insights/") == true
            ) {
                userId?.let { BottomBar(navController = navController, userId = it) }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable("welcome") {
                WelcomeScreen(navController = navController)
            }
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("questionnaire/{userId}") { backStackEntry ->
                QuestionnaireScreen(
                    navController = navController,
                    navBackStackEntry = backStackEntry
                )
            }
            composable("home/{userId}") { backStackEntry ->
                HomeScreen(
                    navController = navController,
                    navBackStackEntry = backStackEntry
                )
            }
            composable("insights/{userId}") { backStackEntry ->
                InsightsScreen(
                    navController = navController,
                    navBackStackEntry = backStackEntry
                )
            }
        }
    }
}
