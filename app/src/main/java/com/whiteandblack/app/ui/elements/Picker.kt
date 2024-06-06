package com.whiteandblack.app.ui.elements

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.whiteandblack.app.ui.theme.Black
import com.whiteandblack.app.ui.theme.Gray
import com.whiteandblack.app.ui.theme.White
import com.whiteandblack.app.ui.theme.mShape
import com.whiteandblack.app.ui.theme.textFieldHeight
import com.whiteandblack.app.ui.theme.textFieldWidth
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException

data class Category(
    val name: String,
)

data class Storage(
    val name: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerBottomSheet(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    list: MutableList<Category>,
    callback: (String) -> Unit
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(false)

    TextField(
        value = value,
        label = { Text(hint, color = Gray) },
        onValueChange = { },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = White,
            focusedTextColor = Black,
            unfocusedTextColor = Black,
            disabledTextColor = Black
        ),
        enabled = false,
        modifier = modifier
            .height(textFieldHeight)
            .width(textFieldWidth)
            .clip(RoundedCornerShape(mShape))
            .clickable {
                showBottomSheet = true
            }
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                items(list.size) { index ->
                    Row(
                        modifier = Modifier
                            .clickable {
                                callback(list[index].name)
                                showBottomSheet = false
                            }
                            .padding(10.dp)
                    ) {
                        Text(
                            text = list[index].name,
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .weight(2f)
                        )
                    }
                }
            }
        }
    }
}

class Picker  {
    companion object {
        fun getRoomList(context: Context, id: Int): MutableList<Storage> {
            val text = getStringFromRawRes(context, id)
            val type = object : TypeToken<List<Storage>>() {}.type
            return Gson().fromJson(text, type)
        }

        fun getCategoryList(context: Context, id: Int): MutableList<Category> {
            val text = getStringFromRawRes(context, id)
            val type = object : TypeToken<List<Category>>() {}.type
            return Gson().fromJson(text, type)
        }

        private fun getStringFromRawRes(context: Context, rawRes: Int): String? {
            val inputStream: InputStream = try {
                context.resources.openRawResource(rawRes)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
                return null
            }
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            try {
                while (inputStream.read(buffer).also { length = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, length)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            } finally {
                try {
                    inputStream.close()
                    byteArrayOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            val resultString: String = try {
                byteArrayOutputStream.toString("UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                return null
            }
            return resultString
        }
    }
}