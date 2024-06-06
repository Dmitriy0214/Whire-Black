package com.whiteandblack.app.ui.screen.operation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.whiteandblack.app.R
import com.whiteandblack.app.data.storage.models.History
import com.whiteandblack.app.ui.navigation.Screen
import com.whiteandblack.app.ui.screen.main.MainViewModel
import com.whiteandblack.app.ui.elements.DefButton
import com.whiteandblack.app.ui.elements.EditText
import com.whiteandblack.app.ui.elements.Header
import com.whiteandblack.app.ui.elements.Picker
import com.whiteandblack.app.ui.elements.PickerBottomSheet
import com.whiteandblack.app.ui.theme.Peru
import com.whiteandblack.app.ui.theme.White
import com.whiteandblack.app.ui.theme.lpadding
import com.whiteandblack.app.utils.BackPressHandler
import com.whiteandblack.app.utils.Const
import com.whiteandblack.app.utils.ToastHelper
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun OperationView(
    navController: NavController = NavController(LocalContext.current),
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
    val currentDate = sdf.format(Date())

    UI(
        mainViewModel = mainViewModel,
        operationClick = { sum, category, type ->
            mainViewModel.insertHistory(History(
                sum = sum.toString(),
                category = category,
                type = type == Const.PAY,
                date = currentDate
            ))
            navController.navigate(Screen.Main.name)

            ToastHelper().show(
                context,
                if (MainViewModel.operationType.value == Const.PAY)
                    context.getString(R.string.toastPaySuccess)
                else
                    context.getString(R.string.toastTopUpSuccess)
            )
        },
        backClick = {
            navController.navigate(Screen.Main.name)
        }
    )
    BackPressHandler {
        navController.navigate(Screen.Main.name)
    }
}

@Composable
private fun UI(
    mainViewModel: MainViewModel,
    operationClick: (Int, String, String) -> Unit = {_, _, _ ->},
    backClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.LightGray,
                        White
                    )
                )
            )
    ){
        val context = LocalContext.current
        val scoreText = mainViewModel.scoreText.collectAsState()
        val selectedCategory = mainViewModel.categoryName.collectAsState()
        val type = MainViewModel.operationType.collectAsState()

        Header(backClick = backClick)

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ){
            EditText(
                text = scoreText.value,
                hint = stringResource(id = R.string.enterSum),
                onChange = {
                    mainViewModel.scoreText.value = it
                },
                number = true
            )

            PickerBottomSheet(
                value = selectedCategory.value,
                hint = stringResource(id = R.string.category),
                list = Picker.getCategoryList(context, R.raw.category),
                callback = {
                    mainViewModel.categoryName.value = it
                },
                modifier = Modifier.padding(top = lpadding)
            )

            DefButton(
                text = stringResource(
                    id = if (type.value == Const.PAY)
                            R.string.payButton
                        else
                            R.string.topUpButton
                ),
                modifier = Modifier.padding(top = lpadding),
                onClick = {
                    if (scoreText.value.isNotEmpty())
                        operationClick(scoreText.value.toInt(), selectedCategory.value, type.value)
                    else
                        ToastHelper().show(context, context.getString(R.string.toastEnterSum))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigatorPreview() {
    UI(
        mainViewModel = hiltViewModel(),
        backClick = { }
    )
}