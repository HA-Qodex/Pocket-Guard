package com.my.pocketguard.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.Dimension.SizeXS
import com.my.pocketguard.ui.theme.WhiteColor
import com.my.pocketguard.ui.theme.appTextStyle

@Composable
fun AppButton(text: String,
              onClick: () -> Unit,
              modifier: Modifier = Modifier,
              backgroundColor: Color = ButtonColor,
              contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(SizeXS),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text, style = appTextStyle.copy(color = WhiteColor))
    }
}