package com.android.tevhaber.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Scaffold(topBar = {

    }, content =
    { pad ->
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        }
        Column(modifier = Modifier
            .padding(pad)
            .focusRequester(focusRequester)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusRequester.freeFocus()
                    focusManager.clearFocus()
                })
            }
            .fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
            Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 15.dp, horizontal = 15.dp)) {
                Spacer(modifier = Modifier.padding(0.dp))
                Column(Modifier) {
                    TextField(value = username, onValueChange = { username = it}, label = {
                        Text(
                            text = "Kullanıcı adı",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 12.sp
                        )
                    })
                    TextField(value = password.value, onValueChange = { password.value = it}, visualTransformation = PasswordVisualTransformation(), label ={
                        Text("Şifre", style = MaterialTheme.typography.bodyLarge, fontSize = 12.sp)
                    })
                }
                Button(onClick = {
                    if(username.text != "")
                        if(password.value.text !=""  && password.value.text.length >= 6)
                        {
                            navController.navigate("homeScreen")
                        }
                        else
                            Toast.makeText(context,"Şifrenizi hatalı girdiniz!", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context,"Kullanıcı adınızı hatalı girdiniz!", Toast.LENGTH_SHORT).show()

                }, modifier = Modifier, shape = RoundedCornerShape(10.dp)) {
                    Text(text = "Giriş yap",
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.padding(0.dp))
                Spacer(modifier = Modifier.padding(0.dp))
                Row(modifier = Modifier
                    .clickable {
                        navController.navigate("registerScreen") {
                            popUpTo("loginScreen") { inclusive = true }
                        }
                    }) {
                    Text(text = "Kayıt Olmak İçin Tıkla",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    })
}