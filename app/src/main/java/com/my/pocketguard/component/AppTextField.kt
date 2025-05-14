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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.my.pocketguard.ui.theme.PrimaryColor
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.Dimension.TextS
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.appTextStyle

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    label: String,
    value: String,
    maxLines: Int = 1,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: String = "",
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(SizeXS),
        trailingIcon = trailingIcon,
        singleLine = maxLines == 1,
        maxLines = maxLines,
        leadingIcon = leadingIcon,
        placeholder = {
            if (textAlign != TextAlign.Center) {
                Text(label, style = appTextStyle)
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, style = appTextStyle)
                }
            }
        },
        label = { Text(label) },
        textStyle = appTextStyle.copy(textAlign = textAlign),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            selectionColors = TextSelectionColors(
                handleColor = PrimaryColor,
                backgroundColor = PrimaryColor.copy(alpha = 0.4f)
            ),
            cursorColor = PrimaryColor,
            focusedLabelColor = PrimaryColor,
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = PrimaryColor,
            unfocusedContainerColor = Color.Transparent,
            errorBorderColor = RedColor
        ),
        isError = isError,
        supportingText = {
            if (isError) Text(
                supportingText,
                modifier = modifier.padding(bottom = SizeXS),
                style = appTextStyle.copy(color = RedColor, fontSize = TextS)
            ) else null
        }
    )
}