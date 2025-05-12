package com.my.pocketguard.component

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.PrimaryColor
import com.my.pocketguard.ui.theme.PrimaryColorLite
import com.my.pocketguard.ui.theme.appTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppDropdown(
    modifier: Modifier = Modifier,
    title: String,
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String = { it.toString() },
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: String = ""
) {
    var isExpended = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpended.value,
        onExpandedChange = {
            isExpended.value = !isExpended.value
        }) {
        AppDisableTextField(
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
            value = (selectedItem?.let(itemLabel) ?: "").toString(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended.value)
            },
            leadingIcon = leadingIcon,
            placeholder = title,
            onClick = {},
            isError = supportingText != "",
            supportingText = supportingText
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(color = PrimaryColorLite),
            expanded = isExpended.value,
            onDismissRequest = { isExpended.value = !isExpended.value }) {
            items.forEach {
                DropdownMenuItem(
                    modifier = Modifier.background(color = if (selectedItem == it) ButtonColor else Color.Transparent),
                    text = {
                        Text(
                            itemLabel(it),
                            style = appTextStyle
                        )
                    }, onClick = {
                        onItemSelected(it)
                        isExpended.value = false
                    })
            }
        }
    }
}