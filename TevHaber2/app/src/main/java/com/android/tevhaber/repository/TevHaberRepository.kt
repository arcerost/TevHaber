package com.android.tevhaber.repository

import com.android.tevhaber.model.Article
import com.android.tevhaber.model.NewsResponse
import com.android.tevhaber.service.TevHaberService
import com.android.tevhaber.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class TevHaberRepository @Inject constructor(private val api: TevHaberService) {
    suspend fun getNews() : Resource<NewsResponse> {
        return try {
            val response = api.getEverything("apple")
            when (response.status) {
                "ok" -> Resource.Success(response)
                else -> Resource.Error("Register Error.")
            }
        } catch (e: Exception){
            Resource.Error(e.message.toString())
        }
    }

    suspend fun searchNews(query: String): Resource<NewsResponse> {
        return try {
            val response = api.search(query, "title")
            when(response.status){
                "ok" -> Resource.Success(response)
                else -> Resource.Error("Search Error")
            }
        }
        catch (e: Exception){
            Resource.Error(e.message.toString())
        }
    }

    private val categories = listOf("business", "entertainment", "general", "health", "science", "sports", "technology")

    suspend fun getAllCategoryTopHeadlines(): Resource<Map<String, List<Article>>> {
        val allHeadlines = mutableMapOf<String, List<Article>>()
        try {
            categories.forEach { category ->
                val response = api.getCategoryTopHeadlines(category)
                when(response.status) {
                    "ok" -> allHeadlines[category] = response.articles ?: emptyList()
                    else -> return Resource.Error("Error fetching news for category: $category")
                }
            }
            return Resource.Success(allHeadlines)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }
}