package com.android.tevhaber.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.tevhaber.model.Article
import com.android.tevhaber.util.PreferencesManager
import com.android.tevhaber.util.Resource
import com.android.tevhaber.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = Unit){
        viewModel.getAllCategoryNews()
    }
    val scaffoldState = rememberScaffoldState()
    val categories = listOf("business", "entertainment", "general", "health", "science", "sports", "technology")
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    Scaffold(scaffoldState = scaffoldState, drawerContent = {
        DrawerContent(categories = categories, onCategorySelected = { category ->
            viewModel.setSelectedCategory(category)
            scope.launch { scaffoldState.drawerState.close() }
        })
    }, topBar = {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menü")
            }
            IconButton(onClick = { navController.navigate("profileScreen") }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "profile")
            }
        }
    }){ pad ->
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.secondary) {
        }
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(viewModel = viewModel)
            CategoryNewsList(preferencesManager, viewModel)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryNewsList(preferencesManager: PreferencesManager, viewModel: HomeViewModel) {
    val selectedCategory = viewModel.selectedCategory.collectAsState().value
    val newsByCategory = viewModel.newsByCategory.collectAsState().value
    val searchResults = viewModel.searchResults.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value

    LazyColumn {
        when {
            errorMessage.isNotEmpty() -> {
                item { Text(errorMessage, color = Color.Red) }
            }
            searchResults is Resource.Success && !searchResults.data.isNullOrEmpty() -> {
                items(searchResults.data) { article ->
                    New(article, preferencesManager)
                }
            }
            selectedCategory != null -> {
                newsByCategory[selectedCategory]?.let { articles ->
                    items(articles) { article ->
                        New(article, preferencesManager)
                    }
                }
            }
            else -> {
                newsByCategory.forEach { (category, articles) ->
                    stickyHeader {
                        CategoryHeader(category)
                    }
                    items(articles) { article ->
                        New(article, preferencesManager)
                    }
                }
            }
        }
    }
}



@Composable
fun CategoryHeader(category: String) {
    Text(
        text = category,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    )
}

@Composable
fun DrawerContent(categories: List<String>, onCategorySelected: (String) -> Unit) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
        categories.forEach { category ->
            Text(
                text = category,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategorySelected(category) }
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun New(new: Article, preferencesManager: PreferencesManager) {
    val title = new.title
    val description = new.description
    var isFavorite by remember { mutableStateOf(preferencesManager.isFavorite(new.title ?: "")) }
    var check by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Surface(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp), color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(10.dp)) {
        Box(
                modifier = Modifier
                    .size(350.dp, 200.dp)
                    .clickable {
                        scope.launch {
                            check = !check
                        }
                    }, contentAlignment = Alignment.TopStart
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        if (title != null) {
                            Text(text = title)
                        }
                    }
                    Row {
                        if (description != null) {
                            Text(text = description)
                        }
                    }
                    IconButton(onClick = {
                        preferencesManager.toggleFavorite(new.title ?: "")
                        isFavorite = !isFavorite
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorilere Ekle/Çıkar"
                        )
                    }
                }
            }
        }
    if(check)
    {
        Popup(alignment = Alignment.Center, properties = PopupProperties(dismissOnBackPress = true, dismissOnClickOutside = true), onDismissRequest = {
            check = !check
        }) {
            Box(modifier = Modifier
                .size(400.dp, 500.dp)
                .background(Color.Cyan, shape = RoundedCornerShape(15.dp))){
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
                    new.title?.let { Text(text = it, color= Color.Black, fontSize = 25.sp) }
                    Spacer(modifier = Modifier.padding(0.dp))
                    new.description?.let { Text(text = it, color= Color.Black, fontSize = 20.sp) }
                    Spacer(modifier = Modifier.padding(0.dp))
                    new.urlToImage?.let { Image(rememberAsyncImagePainter(model = it), contentDescription = "image") }
                }
            }
        }
    }
}

@Composable
fun SearchBar(viewModel: HomeViewModel) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (searchQuery.isNotBlank()) {
                    viewModel.searchNews(searchQuery)
                }
            }
        )
    )
}