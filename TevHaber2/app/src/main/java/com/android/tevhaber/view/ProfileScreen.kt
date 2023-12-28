package com.android.tevhaber.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.tevhaber.model.Article
import com.android.tevhaber.util.PreferencesManager
import com.android.tevhaber.util.Resource
import com.android.tevhaber.viewmodel.HomeViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    val favorites = preferencesManager.getFavorites().toList()
    val searchResults = viewModel.searchResults.collectAsState().value

    Scaffold(topBar = {}, content = { pad ->
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.secondary) {
        }
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(favorites) {
                favorites.forEach {
                    viewModel.searchNews(it)
                }
            }
            when (searchResults) {
                is Resource.Success -> {
                    LazyColumn {
                        items(searchResults.data ?: emptyList()) { article ->
                            FavoriteNewsItem(article = article, preferencesManager = preferencesManager)
                        }
                    }
                }
                is Resource.Error -> {
                    Text(searchResults.message ?: "An error occurred")
                }
            }
        }
    }, bottomBar = {})
}

@Composable
fun FavoriteNewsItem(article: Article, preferencesManager: PreferencesManager) {
    New(new = article, preferencesManager = preferencesManager)
}