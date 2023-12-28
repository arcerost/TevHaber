package com.android.tevhaber

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.tevhaber.database.UserDatabase
import com.android.tevhaber.ui.theme.TevHaberTheme
import com.android.tevhaber.view.HomeScreen
import com.android.tevhaber.view.LoginScreen
import com.android.tevhaber.view.ProfileScreen
import com.android.tevhaber.view.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TevHaberTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val userDao = db.userDao()
                    val startDes = remember { mutableStateOf<String?>(null) }
                    LaunchedEffect(key1 = userDao) {
                        var user: Boolean? = null
                        if (userDao.anyData() != 0) {
                            user = true
                        }
                        else{
                            Log.d("tevhaber","kullanıcı verisi bulunamadı, main activity")
                        }
                        startDes.value =
                            if(user == true){
                                "homeScreen"
                            }
                            else {
                                "loginScreen"
                            }
                    }
                    if(startDes.value != null) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = startDes.value!!
                        )
                        {
                            composable("registerScreen")
                            {
                                RegisterScreen(navController)
                            }
                            composable("homeScreen")
                            {
                                HomeScreen(navController)
                            }
                            composable("loginScreen"){
                                LoginScreen(navController)
                            }
                            composable("profileScreen"){
                                ProfileScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
