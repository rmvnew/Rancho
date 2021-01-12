package com.example.rancho.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {


    var nameButtonPurchaseAction: String? = null
    var updatePurchaseActive: Boolean? = null
    var nameButtonItemAction: String? = null
    var updateItemActive: Boolean? = null


    private var mUpdatePurchase = MutableLiveData<Boolean>()
    val updatePurchase: LiveData<Boolean> = mUpdatePurchase

    private var mUpdateItem = MutableLiveData<Boolean>()
    val updateItem: LiveData<Boolean> = mUpdateItem


    fun setUpdateItem(bol:Boolean){
        mUpdateItem.value = bol
    }

    fun setUpdatePurchaseMode(bol: Boolean) {
        mUpdatePurchase.value = bol
    }


    fun setNameButtonAction(bol: Boolean): String {
        return when (bol) {
            true -> "Atualizar"
            else -> "Registrar"
        }
    }


}