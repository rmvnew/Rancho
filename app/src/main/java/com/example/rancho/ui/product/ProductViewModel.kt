package com.example.rancho.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel:ViewModel() {


    var nameButtonAction:String? = null
    var updateProductActive:Boolean? = null


    private var mUpdateProduct = MutableLiveData<Boolean>()
    val updateProduct:LiveData<Boolean> = mUpdateProduct



    fun setUpdateMode(bol:Boolean){
        mUpdateProduct.value = bol
    }


    fun setNameButtonAction(bol:Boolean):String{
        return when(bol){
            true -> "Atualizar"
            else -> "Registrar"
        }
    }


}