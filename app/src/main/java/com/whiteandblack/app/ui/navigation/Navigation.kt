package com.whiteandblack.app.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whiteandblack.app.ui.screen.history.HistoryView
import com.whiteandblack.app.ui.screen.load.LoadView
import com.whiteandblack.app.ui.screen.login.LoginView
import com.whiteandblack.app.ui.screen.main.MainView
import com.whiteandblack.app.ui.screen.operation.OperationView
import com.whiteandblack.app.ui.screen.registration.RegView
import com.whiteandblack.app.ui.theme.Peru
import com.whiteandblack.app.ui.theme.White

enum class Screen {
    Login,
    Main,
    Operation,
    History,
    Registration,
    Load
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Column (
        modifier = Modifier.background(White)
    ) {
        NavHost(modifier = Modifier.fillMaxSize(), navController = navController, startDestination = Screen.Load.name) {
            composable( route = Screen.Load.name) { EnterAnimation { LoadView(navController) } }
            composable( route = Screen.Login.name) { EnterAnimation { LoginView(navController) } }
            composable( route = Screen.Main.name) { EnterAnimation { MainView(navController) } }
            composable( route = Screen.Operation.name) { EnterAnimation { OperationView(navController) } }
            composable( route = Screen.History.name) { EnterAnimation { HistoryView(navController) } }
            composable( route = Screen.Registration.name) { EnterAnimation { RegView(navController) } }
        }
    }
}

@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = MutableTransitionState(
            initialState = false
        ).apply { targetState = true },
        modifier = Modifier,
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
    ) {
        content()
    }
}