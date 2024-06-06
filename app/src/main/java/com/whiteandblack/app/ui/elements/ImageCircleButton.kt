package com.whiteandblack.app.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.whiteandblack.app.ui.theme.buttonSize
import com.whiteandblack.app.utils.Const

@Composable
fun ImageCircleButton(
    image: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(buttonSize)
            .height(buttonSize)
            .clickable { onClick() },
    ) {
        Image(
            painter = image,
            contentDescription = Const.BUTTON_DESCRIPTION,
            contentScale = ContentScale.Crop,
        )
    }
}