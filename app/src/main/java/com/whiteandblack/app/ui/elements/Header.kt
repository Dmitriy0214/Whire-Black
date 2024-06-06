package com.whiteandblack.app.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.whiteandblack.app.R
import com.whiteandblack.app.ui.theme.Peru
import com.whiteandblack.app.ui.theme.White
import com.whiteandblack.app.ui.theme.headerHeight
import com.whiteandblack.app.ui.theme.ltext
import com.whiteandblack.app.ui.theme.mIconSize
import com.whiteandblack.app.ui.theme.padding
import com.whiteandblack.app.utils.Const

@Composable
fun Header(
    backClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(Color.LightGray)
            .padding(padding)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon (
                painterResource(id = R.drawable.back),
                Const.BUTTON_DESCRIPTION,
                tint = Color.Black,
                modifier = Modifier
                    .size(mIconSize)
                    .clickable { backClick() }

            )
            Text(
                stringResource(id = R.string.back),
                color = Color.Black,
                fontSize = ltext,
                modifier = Modifier
                    .padding(padding)
                    .clickable { backClick() }
            )
        }
    }
}