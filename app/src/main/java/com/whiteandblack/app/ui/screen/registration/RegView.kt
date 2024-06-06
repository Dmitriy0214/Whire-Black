package com.whiteandblack.app.ui.screen.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.whiteandblack.app.R
import com.whiteandblack.app.ui.elements.DefButton
import com.whiteandblack.app.ui.elements.EditText
import com.whiteandblack.app.ui.navigation.Screen
import com.whiteandblack.app.ui.screen.main.MainViewModel
import com.whiteandblack.app.ui.theme.White
import com.whiteandblack.app.ui.theme.headerText
import com.whiteandblack.app.ui.theme.lpadding
import com.whiteandblack.app.ui.theme.xlpadding
import com.whiteandblack.app.utils.ToastHelper

@Composable
fun RegView(
    navController: NavController = NavController(LocalContext.current),
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    UI(
        onClickLogin = { mail, pass, repPass ->
            mainViewModel.reg(context, mail, pass, repPass) {
                if (it) navController.navigate(Screen.Main.name)
                else ToastHelper().show(context, context.getString(R.string.toastLoginError))
            }
        },
        back = {
            navController.navigate(Screen.Login.name)
        }
    )
}

@Composable
fun UI(
    onClickLogin: (String, String, String) -> Unit,
    back: () -> Unit
) {

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
            .padding(xlpadding)
    ){
        Logo()
        Auth(onClickLogin, back)
    }
}

@Composable
fun Logo() {
    Column {
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
fun Auth(
    onClick: (String, String, String) -> Unit,
    back: () -> Unit
) {
    var mail by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var repPass by remember { mutableStateOf("") }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditText(mail, stringResource(id = R.string.mail)) { mail = it }

        EditText(
            pass,
            stringResource(id = R.string.pass),
            Modifier.padding(top = lpadding),
            true,
        ) { pass = it }

        EditText(
            repPass,
            stringResource(id = R.string.repPass),
            Modifier.padding(top = lpadding),
            true,
        ) { repPass = it }


        DefButton(
            text = stringResource(id = R.string.registration),
            onClick = { onClick(mail, pass, repPass) },
            modifier = Modifier
                .padding(top = lpadding)
                .width(190.dp)
        )

        DefButton(
            text = stringResource(id = R.string.back),
            onClick = { back() },
            modifier = Modifier
                .padding(top = lpadding)
                .width(190.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Column (modifier = Modifier
        .fillMaxSize()
        .background(White)) {
        UI(
            onClickLogin = { _, _, _ ->

            },
            back = {}
        )
    }
}