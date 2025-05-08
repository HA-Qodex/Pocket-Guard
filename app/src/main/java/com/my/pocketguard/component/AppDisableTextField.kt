package com.my.pocketguard.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.my.pocketguard.ui.theme.BackgroundColor
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.Dimension.SmallSpacing
import com.my.pocketguard.ui.theme.appTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDisableTextField(
    modifier: Modifier,
    value: String,
    trailingIcon: @Composable (() -> Unit),
    placeholder: String
) {

    val interactionSource = remember { MutableInteractionSource() }
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {},
        maxLines = 1,
        singleLine = true,
        enabled = false,
        textStyle = appTextStyle.copy(fontWeight = FontWeight.W400),
//        decorationBox = {
//            TextFieldDefaults.DecorationBox(
//                value = value,
//                innerTextField = it,
//                enabled = false,
//                singleLine = true,
//                visualTransformation = VisualTransformation.None,
//                interactionSource = interactionSource,
//                shape = RoundedCornerShape(SmallSpacing),
//                placeholder = {
//                    Text(
//                        text = placeholder,
//                        style = appTextStyle.copy(fontWeight = FontWeight.W400)
//                    )
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    disabledContainerColor = Color.Transparent,
//                    disabledBorderColor = BackgroundColor,
//                    disabledTrailingIconColor = ButtonColor,
//                ),
//                trailingIcon = trailingIcon,
//                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
//                    top = 0.dp,
//                    bottom = 0.dp,
//                    end = 0.dp,
//                ),
//            )
//        }
    )
}