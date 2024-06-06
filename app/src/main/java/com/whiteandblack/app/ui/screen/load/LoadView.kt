package com.whiteandblack.app.ui.screen.load

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.whiteandblack.app.R
import com.whiteandblack.app.api.FirebaseRepository
import com.whiteandblack.app.data.storage.PrefManager
import com.whiteandblack.app.ui.navigation.Screen
import com.whiteandblack.app.ui.screen.main.MainViewModel
import com.whiteandblack.app.ui.theme.headerText
import com.whiteandblack.app.utils.BackPressHandler
import com.whiteandblack.app.utils.Const
import kotlinx.coroutines.delay

@Composable
fun LoadView(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {
    Countdown(1000L) {
        FirebaseRepository.checkAuth{
            if (it != null) {
                navController.navigate(Screen.Main.name)
                mainViewModel.getUserInfo()
                mainViewModel.loadHistory()
            }
            else navController.navigate(Screen.Login.name)
        }
    }

    UI()

    BackPressHandler{}
}

@Composable
private fun UI() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
            .background(White)
    ) {
        Text(
            stringResource(id = R.string.whiteandblack),
            fontSize = headerText,
            fontWeight = FontWeight.Bold,
            lineHeight = 70.sp,
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun Countdown(targetTime: Long, endEvent: () -> Unit) {
    var remainingTime by remember(targetTime) {
        mutableLongStateOf(targetTime - System.currentTimeMillis())
    }

    LaunchedEffect(remainingTime) {
        delay(targetTime)
        remainingTime = targetTime - System.currentTimeMillis()
        endEvent()
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    UI()
}