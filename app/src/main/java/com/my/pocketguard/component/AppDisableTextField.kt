package com.my.pocketguard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.Dimension.TextS
import com.my.pocketguard.ui.theme.PrimaryColor
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.appTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDisableTextField(
    modifier: Modifier,
    value: String,
    isError: Boolean = false,
    supportingText: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        modifier = modifier.clickable(
            onClick =  onClick,
            indication = null,
            interactionSource = interactionSource
        ),
        value = value,
        onValueChange = {},
        maxLines = 1,
        singleLine = true,
        enabled = false,
        textStyle = appTextStyle,
        label = { Text(placeholder, color = PrimaryColor) },
        placeholder = {
            Text(placeholder, style = appTextStyle)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor =  if (isError) RedColor else PrimaryColor,
            disabledTrailingIconColor = PrimaryColor,
            errorBorderColor = RedColor
        ),
        isError = isError,
        supportingText = {
            if (isError) {
            Text(
                supportingText,
                modifier = modifier.padding(bottom = SizeXS),
                style = appTextStyle.copy(color = RedColor, fontSize = TextS))
        } else null
        }
    )
}