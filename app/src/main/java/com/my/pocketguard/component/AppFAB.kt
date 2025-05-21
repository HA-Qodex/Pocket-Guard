package com.my.pocketguard.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.my.pocketguard.ui.theme.ButtonColor

@Composable
fun AppFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        containerColor = ButtonColor,
//        contentColor = AppTheme.colors.onPrimary,
//        elevation = FloatingActionButtonDefaults.elevation(
//            defaultElevation = 8.dp,
//            pressedElevation = 12.dp,
//            hoveredElevation = 10.dp,
//            focusedElevation = 10.dp
//        ),
//        modifier = Modifier
//            .padding(16.dp)
//            .shadow(10.dp, RoundedCornerShape(16.dp))
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.size(28.dp)
        )
    }
}