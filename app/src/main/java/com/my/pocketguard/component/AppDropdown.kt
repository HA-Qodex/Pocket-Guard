package com.my.pocketguard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.my.pocketguard.ui.theme.BackgroundColor
import com.my.pocketguard.ui.theme.appTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppDropdown(
    title: String,
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String = { it.toString() },
) {

    var isExpended = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpended.value,
        onExpandedChange = {
            isExpended.value = !isExpended.value
        }) {
        AppDisableTextField(
            modifier = Modifier
                .fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable, true),
            value = (selectedItem?.let(itemLabel) ?: "").toString(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended.value)
            },
            placeholder = title
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(color = BackgroundColor),
            expanded = isExpended.value,
            onDismissRequest = { isExpended.value = !isExpended.value }) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            itemLabel(it),
                            style = appTextStyle.copy(fontWeight = FontWeight.W400)
                        )
                    }, onClick = {
                        onItemSelected(it)
                        isExpended.value = false
                    })
            }
        }
    }
}