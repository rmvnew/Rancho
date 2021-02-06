package com.example.rancho.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {

    private var mActions = MutableLiveData<String>()
    val actions : LiveData<String> = mActions



    fun setAction(action: String) {
        mActions.value = action
    }


}