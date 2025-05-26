package com.example.myshop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myshop.Model.SliderModel
import com.example.myshop.Repository.MainRepository

class MainViewModel():ViewModel() {
    private val repository = MainRepository()
    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        return repository.loadBanner()
    }
}