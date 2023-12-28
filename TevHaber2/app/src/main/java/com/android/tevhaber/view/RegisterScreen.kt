package com.android.tevhaber.view

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.android.tevhaber.database.UserDatabase
import com.android.tevhaber.database.UserInfo
import kotlinx.coroutines.runBlocking

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val db: UserDatabase = Room.databaseBuilder(context,UserDatabase::class.java,"UserInfo").build()
    val userDao = db.userDao()
    var pw by remember { mutableStateOf("") }
    var pw2 by remember { mutableStateOf("") }
    val control = remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Scaffold(Modifier.fillMaxSize(), topBar = {
    }, content = { pd ->
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(pd)
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .padding(pd), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding())
            TextField(value = userName, onValueChange = { userName = it}, label = {
                Text(
                    text = "Kullanıcı Adı",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 12.sp
                )
            })
            TextField(value = pw, onValueChange = { pw = it}, visualTransformation = PasswordVisualTransformation(), label = {
                Text(
                    text = "Parola",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 12.sp
                )
            })
            TextField(value = pw2, onValueChange = { pw2 = it}, visualTransformation = PasswordVisualTransformation(), label = {
                Text(
                    text = "Parola Tekrar",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 12.sp
                )
            })
            Spacer(modifier = Modifier.padding())
            Button(onClick = {
                control.value = !control.value
                focusManager.clearFocus()
            }, shape = RoundedCornerShape(10.dp)) {
                Text(text = "Kayıt Ol",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 14.sp,
                    color = Color.Black)
            }
            Spacer(modifier = Modifier.padding())
            Spacer(modifier = Modifier.padding())
        }
    }, bottomBar = {})
    if(control.value) {
        if (userName != "")
            if (pw != "")
                if (pw2 != "")
                {
                    runBlocking {
                        val userr = UserInfo(userName)
                        userDao.insert(userr)
                        navController.navigate("homeScreen"){
                            popUpTo("registerScreen") { inclusive = true }
                        }
                    }
                    control.value = !control.value
                }
                else {
                    Toast.makeText(context, "Şifreler uyumlu değil!", Toast.LENGTH_SHORT)
                        .show()
                    control.value = !control.value
                }
            else {
                Toast.makeText(context, "Şifre alanı boş bırakılamaz!", Toast.LENGTH_SHORT)
                    .show()
                control.value = !control.value
            }
        else {
            Toast.makeText(context, "Kullanıcı adı alanı boş bırakılamaz!", Toast.LENGTH_SHORT)
                .show()
            control.value = !control.value
        }
    }
}