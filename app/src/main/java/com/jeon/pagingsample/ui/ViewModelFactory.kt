package com.jeon.pagingsample.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeon.pagingsample.data.repository.HotelRepository
import io.reactivex.disposables.CompositeDisposable

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotelViewModel::class.java)) {
            return HotelViewModel(HotelRepository.getInstance(application,CompositeDisposable())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}