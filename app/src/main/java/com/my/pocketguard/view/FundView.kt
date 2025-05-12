package com.my.pocketguard.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.pocketguard.R
import com.my.pocketguard.component.AppBar
import com.my.pocketguard.component.AppTextField
import com.my.pocketguard.navigation.AppRoutes
import com.my.pocketguard.ui.theme.ButtonColor
import com.my.pocketguard.ui.theme.Dimension.SizeL
import com.my.pocketguard.ui.theme.appTextStyle

@Composable
fun FundView(navController: NavController) {

    val fundTitle = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(topBar = { AppBar(navController, "Fund", route = AppRoutes.DASHBOARD.route) }) {
        Column(modifier = Modifier.padding(it).padding(horizontal = SizeL)) {
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Kg",
                onValueChange = { value ->
                    fundTitle.value = value
                },
                value = fundTitle.value,
                trailingIcon = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                isError = false,
                visualTransformation = VisualTransformation.None
            )
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Kg",
                onValueChange = { value ->
                    fundTitle.value = value
                },
                value = fundTitle.value,
                trailingIcon = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                isError = false,
                visualTransformation = VisualTransformation.None
            )
            Button(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors()
                    .copy(containerColor = ButtonColor),
                onClick = {
                    navController.navigate(AppRoutes.CATEGORY.route)
                }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painterResource(id = R.drawable.google),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        stringResource(R.string.google_login),
                        style = appTextStyle.copy(color = Color.White)
                    )
                }
            }
        }
//            AppTextField(
//                modifier = Modifier.fillMaxWidth().height(40.dp),
//                label = "Fund Title",
//                value = fundTitle.value,
//                onValueChange = {
//                    fundTitle.value = it
//                },
////                leadingIcon = Icon(Icons.Default.AccountBalanceWallet),
////                trailingIcon = TODO(),
//                supportingText = {Text("check")},
//                keyboardOptions = KeyboardOptions.Default,
//                visualTransformation = TODO()
//            )
    }
}