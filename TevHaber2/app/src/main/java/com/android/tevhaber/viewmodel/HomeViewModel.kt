package com.android.tevhaber.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tevhaber.model.Article
import com.android.tevhaber.repository.TevHaberRepository
import com.android.tevhaber.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val repository: TevHaberRepository) : ViewModel() {
    val errorMessage = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<String?>(null)
    val newsByCategory = MutableStateFlow<Map<String, List<Article>>>(emptyMap())
    val searchResults = MutableStateFlow<Resource<List<Article>>>(Resource.Success(emptyList()))
    val allNews = MutableStateFlow<Resource<List<Article>>>(Resource.Success(emptyList()))

    fun searchNews(query: String) {
        viewModelScope.launch {
            val result = repository.searchNews(query)
            searchResults.value = when (result) {
                is Resource.Success -> {
                    Resource.Success(result.data?.articles ?: emptyList())
                }
                is Resource.Error -> {
                    Resource.Error(result.message ?: "An error occurred")
                }
            }
        }
    }
    fun getAllCategoryNews() {
        viewModelScope.launch {
            when (val result = repository.getAllCategoryTopHeadlines()) {
                is Resource.Success -> {
                    newsByCategory.value = result.data ?: emptyMap()
                }
                is Resource.Error -> {
                    errorMessage.value = result.message.toString()
                }
            }
        }
    }
    fun setSelectedCategory(category: String) {
        selectedCategory.value = category
        getAllCategoryNews()
    }
    init {
        getAllCategoryNews()
    }
}