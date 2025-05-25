package com.fit2081.a1ryanhii34466576

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fit2081.a1ryanhii34466576.data.local.DatabaseSeeder
import com.fit2081.a1ryanhii34466576.ui.component.BottomBar
import com.fit2081.a1ryanhii34466576.ui.screen.clinicianDashboard.ClinicianDashboardScreen
import com.fit2081.a1ryanhii34466576.ui.screen.clinicianLogin.ClinicianLoginScreen
import com.fit2081.a1ryanhii34466576.ui.screen.foodIntakeHistory.FoodIntakeHistoryScreen
import com.fit2081.a1ryanhii34466576.ui.screen.home.HomeScreen
import com.fit2081.a1ryanhii34466576.ui.screen.insights.InsightsScreen
import com.fit2081.a1ryanhii34466576.ui.screen.login.LoginScreen
import com.fit2081.a1ryanhii34466576.ui.screen.login.LoginViewModel
import com.fit2081.a1ryanhii34466576.ui.screen.nutricoach.NutriCoachScreen
import com.fit2081.a1ryanhii34466576.ui.screen.questionnaire.QuestionnaireScreen
import com.fit2081.a1ryanhii34466576.ui.screen.register.RegisterScreen
import com.fit2081.a1ryanhii34466576.ui.screen.settings.SettingsScreen
import com.fit2081.a1ryanhii34466576.ui.screen.welcome.WelcomeScreen

class NutriTrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Seed the Room database with CSV data on first launch
        DatabaseSeeder.seedDatabaseIfNeeded(this)
    }
}

@Composable
fun NutriTrackAppUI() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    val loggedInUserId by loginViewModel.loggedInUserId.collectAsState()
    val navTarget by loginViewModel.loginNavigationTarget.collectAsState()

    // Routes to show the bottom bar
    val showBottomBar =
        currentRoute?.substringBefore("/") in setOf(
            "home",
            "insights",
            "nutricoach",
            "settings",
            "clinicianLogin",
            "clinicianDashboard",
            "foodIntakeHistory"
        )

    // Determine the start destination
    val startDestination = remember(isLoggedIn, loggedInUserId, navTarget) {
        when {
            isLoggedIn && !navTarget.isNullOrBlank() -> navTarget!!
            isLoggedIn && loggedInUserId != null -> "home/$loggedInUserId"
            else -> "welcome"
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                // Only render bottom bar if user ID is available
                val userId = navBackStackEntry?.arguments?.getString("userId") ?: return@Scaffold
                BottomBar(navController, userId)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable("welcome") {
                WelcomeScreen(navController)
            }
            composable("login") {
                LoginScreen(navController)
            }
            composable("register") {
                RegisterScreen(navController)
            }
            composable("questionnaire/{userId}") { entry ->
                val uid = entry.arguments?.getString("userId") ?: return@composable
                QuestionnaireScreen(navController, uid)
            }
            composable("home/{userId}") { entry ->
                val uid = entry.arguments?.getString("userId") ?: return@composable
                HomeScreen(navController, uid)
            }
            composable("insights/{userId}") { entry ->
                val uid = entry.arguments?.getString("userId") ?: return@composable
                InsightsScreen(navController, uid)
            }
            composable("nutricoach/{userId}") { entry ->
                val uid = entry.arguments?.getString("userId") ?: return@composable
                NutriCoachScreen(uid)
            }
            composable("clinicianLogin") {
                ClinicianLoginScreen(navController)
            }
            composable("clinicianDashboard") {
                ClinicianDashboardScreen()
            }
            composable("settings/{userId}") { entry ->
                val uid = entry.arguments?.getString("userId") ?: return@composable
                SettingsScreen(navController, uid)
            }
            composable("foodIntakeHistory/{userId}") { entry ->
                val uid = entry.arguments?.getString("userId") ?: return@composable
                FoodIntakeHistoryScreen(uid)
            }
        }
    }
}
