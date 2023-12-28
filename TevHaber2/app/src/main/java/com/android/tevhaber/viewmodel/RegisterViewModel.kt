package com.android.tevhaber.viewmodel

import androidx.lifecycle.ViewModel
import com.android.tevhaber.repository.TevHaberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel@Inject constructor(private val repository: TevHaberRepository) : ViewModel()