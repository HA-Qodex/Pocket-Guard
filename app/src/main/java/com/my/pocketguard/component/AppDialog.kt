package com.my.pocketguard.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.pocketguard.ui.theme.PrimaryColorLite
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.RedColor
import com.my.pocketguard.ui.theme.appTextStyle

@Composable
fun AppDialog(
    title: String,
    text: String,
    confirm: String,
    icon: ImageVector = Icons.Outlined.WarningAmber,
    iconColor: Color = RedColor,
    showDialog: MutableState<Boolean>
) {
        AlertDialog(
            modifier = Modifier.wrapContentSize(),
            containerColor = PrimaryColorLite,
            icon = { Icon(icon,
                modifier = Modifier.size(50.dp),
                tint = iconColor, contentDescription = "error") },
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = title, style = appTextStyle.copy(fontSize = 20.sp))
                }
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { Text(text = text, textAlign = TextAlign.Center, style = appTextStyle) }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog.value = false },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColor,
                    )
                ) {
                    Text(text = confirm, style = appTextStyle)
                }
            },
            onDismissRequest = { }
        )
}