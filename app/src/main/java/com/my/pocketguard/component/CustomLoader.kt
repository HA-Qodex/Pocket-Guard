package com.my.pocketguard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.my.pocketguard.ui.theme.PrimaryColorLite
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.appTextStyle

@Composable
fun CustomLoader(
) {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .background(color = PrimaryColorLite, shape = RoundedCornerShape(8.dp))
                .width(150.dp)
                .height(130.dp)
                .border(width = 1.dp,shape = RoundedCornerShape(8.dp),color = ButtonColor),
//                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = ButtonColor
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Please wait...", style = appTextStyle)
            }
        }
    }
}