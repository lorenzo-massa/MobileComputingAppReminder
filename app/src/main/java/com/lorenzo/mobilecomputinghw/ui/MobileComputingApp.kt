package com.lorenzo.mobilecomputinghw.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lorenzo.mobilecomputinghw.ui.home.Home
import com.lorenzo.mobilecomputinghw.ui.login.Login
import com.lorenzo.mobilecomputinghw.ui.payment.Payment
import com.lorenzo.mobilecomputinghw.ui.profile.Profile

@Composable
fun MobileComputingApp(
    appState: MobileComputingAppState = rememberMobileComputingAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            Login(navController = appState.navController)
        }
        composable(route = "home/{userName}",
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
                val userName = backStackEntry.arguments?.getString("userName") ?: ""
            Home(
                navController = appState.navController,
                userLogged = userName
            )
        }
        composable(route = "reminder") {
            Payment(onBackPress = appState::navigateBack)
        }
        composable(route = "home/{userName}",
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            Home(
                navController = appState.navController,
                userLogged = userName
            )
        }
        composable(route = "profile/{userName}",
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            Profile(
                onBackPress = appState::navigateBack,
                navController = appState.navController,
                userLogged = userName
            )
        }
    }
}