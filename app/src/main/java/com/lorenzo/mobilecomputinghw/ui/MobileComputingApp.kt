package com.lorenzo.mobilecomputinghw.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.lorenzo.mobilecomputinghw.ui.editReminder.EditReminder
import com.lorenzo.mobilecomputinghw.ui.home.Home
import com.lorenzo.mobilecomputinghw.ui.login.Login
import com.lorenzo.mobilecomputinghw.ui.map.ReminderLocationMap
import com.lorenzo.mobilecomputinghw.ui.profile.Profile
import com.lorenzo.mobilecomputinghw.ui.reminder.Reminder
import kotlin.reflect.KFunction2

@ExperimentalPermissionsApi
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
        composable(route = "home/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id")
            Home(
                navController = appState.navController,
                idLogged = id ?: 0
            )
        }
        composable(route = "reminder") {
            Reminder(onBackPress = appState::navigateBack, navController = appState.navController)
        }
        composable(route = "map") {
            ReminderLocationMap(navController = appState.navController)
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
            Profile(
                onBackPress = appState::navigateBack,
                navController = appState.navController,
                userLogged = userName,
                idLogged = id ?: 0
            )
        }
        composable(route = "editReminder/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")
            EditReminder(
                onBackPress = appState::navigateBack,
                reminderId = id ?: 0
            )
        }
    }
}