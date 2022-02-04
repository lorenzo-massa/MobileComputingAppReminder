package com.lorenzo.mobilecomputinghw.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lorenzo.mobilecomputinghw.data.entity.Reminder
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
        composable(route = "home/{userName},{id}",
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                }, navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
                val userName = backStackEntry.arguments?.getString("userName") ?: ""
                val id = backStackEntry.arguments?.getLong("id")
            Home(
                navController = appState.navController,
                userLogged = userName,
                idLogged = id
            )
        }
        composable(route = "reminder") {
            Payment(onBackPress = appState::navigateBack)
        }
        composable(route = "profile/{userName},{id}",
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                }, navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val id = backStackEntry.arguments?.getLong("id")
            if (id != null) {
                Profile(
                    onBackPress = appState::navigateBack,
                    navController = appState.navController,
                    userLogged = userName,
                    idLogged = id
                )
            }
        }
    }
}