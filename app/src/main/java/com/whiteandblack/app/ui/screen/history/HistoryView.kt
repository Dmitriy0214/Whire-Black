package com.whiteandblack.app.ui.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.whiteandblack.app.R
import com.whiteandblack.app.ui.navigation.Screen
import com.whiteandblack.app.ui.screen.main.MainViewModel
import com.whiteandblack.app.ui.theme.Black
import com.whiteandblack.app.ui.elements.Header
import com.whiteandblack.app.ui.theme.Gray
import com.whiteandblack.app.ui.theme.Peru
import com.whiteandblack.app.ui.theme.White
import com.whiteandblack.app.ui.theme.lpadding
import com.whiteandblack.app.ui.theme.ltext
import com.whiteandblack.app.ui.theme.mShape
import com.whiteandblack.app.ui.theme.padding
import com.whiteandblack.app.ui.theme.text
import com.whiteandblack.app.utils.BackPressHandler
import com.whiteandblack.app.utils.Const

@Composable
fun HistoryView(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        mainViewModel.loadHistory()
    }

    UI(
        backClick = {
            navController.navigate(Screen.Main.name)
        }
    )
    BackPressHandler {}
}

@Composable
private fun UI(
    backClick: () -> Unit
) {
    val hList = MainViewModel.historyList.collectAsState()
    val list = hList.value.reversed()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.LightGray,
                        White,
                        Color.LightGray
                    )
                )
            )
    ){

        Header(backClick = backClick)

        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(lpadding)
        ){
            items(list.size) {

                val sum = if (list[it].type) (list[it].sum.toInt() * -1) else list[it].sum

                Column(
                    modifier = Modifier
                        .shadow(10.dp, RoundedCornerShape(mShape))
                        .background(Color.White, RoundedCornerShape(mShape))
                        .fillMaxSize()
                        .height(70.dp)
                        .padding(start = lpadding, top = padding, end = lpadding)
                ) {
                    Row (
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = list[it].date,
                            fontSize = text,
                            color = Gray
                        )
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Row{
                            Text(
                                text = sum.toString(),
                                fontSize = ltext,
                                color = Black
                            )
                            Text(
                                text = " ${stringResource(id = R.string.rub)}",
                                fontSize = ltext,
                                color = Black
                            )
                        }
                        Text(
                            text = list[it].category,
                            fontSize = ltext,
                            color = Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigatorPreview() {
    UI{}
}