package com.my.pocketguard.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.my.pocketguard.ui.theme.ButtonColor

@Composable
fun RoundedButton(
    onClick: () -> Unit,
    icon: ImageVector,
) {
    Button(
        modifier = Modifier.size(50.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors()
            .copy(containerColor = Color.White),
        onClick = onClick
    ) {
        Icon(
            icon,
            tint = ButtonColor,
            contentDescription = "dashboard_button", modifier = Modifier.size(25.dp)
        )
    }
}