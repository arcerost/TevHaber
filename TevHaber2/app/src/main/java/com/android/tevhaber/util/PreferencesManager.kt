package com.android.tevhaber.util

import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("favorites_pref", Context.MODE_PRIVATE)

    fun toggleFavorite(title: String) {
        val favorites = getFavorites().toMutableSet()
        if (favorites.contains(title)) {
            favorites.remove(title)
        } else {
            favorites.add(title)
        }
        sharedPreferences.edit().putStringSet("favorites", favorites).apply()
    }

    fun getFavorites(): Set<String> {
        return sharedPreferences.getStringSet("favorites", emptySet()) ?: emptySet()
    }

    fun isFavorite(title: String): Boolean {
        return getFavorites().contains(title)
    }
}
