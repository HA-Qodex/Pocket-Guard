package com.my.pocketguard.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.my.pocketguard.ui.theme.BackgroundColor
import com.my.pocketguard.ui.theme.BackgroundColorLite
import com.my.pocketguard.ui.theme.Dimension.SmallSpacing
import com.my.pocketguard.ui.theme.Dimension.SmallText
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.appTextStyle

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: String = "",
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(SmallSpacing),
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        placeholder = {
            if (textAlign != TextAlign.Center) {
                Text(label, style = appTextStyle.copy(fontWeight = FontWeight.W400))
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, style = appTextStyle.copy(fontWeight = FontWeight.W400))
                }
            }
        },
        textStyle = appTextStyle.copy(fontWeight = FontWeight.W400, textAlign = textAlign),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            selectionColors = TextSelectionColors(
                handleColor = BackgroundColor,
                backgroundColor = BackgroundColor.copy(alpha = 0.4f)
            ),
            cursorColor = BackgroundColor,
//            focusedContainerColor = BackgroundColorLite,
            focusedBorderColor = BackgroundColor,
            unfocusedBorderColor = BackgroundColor,
            unfocusedContainerColor = Color.Transparent,
            errorBorderColor = RedColor
        ),
        isError = isError,
        supportingText = {
            if (supportingText != "") Text(
                supportingText,
                modifier = modifier.padding(bottom = SmallSpacing),
                style = appTextStyle.copy(color = RedColor, fontSize = SmallText)
            ) else null
        }
    )
}