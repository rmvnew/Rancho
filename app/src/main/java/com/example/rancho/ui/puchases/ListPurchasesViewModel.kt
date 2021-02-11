package com.example.rancho.ui.puchases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rancho.model.Shopping


class ListPurchasesViewModel : ViewModel() {

    private  var mDateOfPayment = MutableLiveData<String?>()
    val dateOfPayment : LiveData<String?> = mDateOfPayment

    private var mSaved = MutableLiveData<Boolean>()
    val saved : LiveData<Boolean> = mSaved

    private var mDeletePurchase = MutableLiveData<String>()
    val deletePurchase : LiveData<String> = mDeletePurchase

    private var mUpdateStatus = MutableLiveData<Boolean>()
    val updateStatus : LiveData<Boolean> = mUpdateStatus


    fun setNewPurchase() {

        Thread.sleep(1000)
        mSaved.value = true

    }

    var mShopping:Shopping? = null



    fun setDateNewOrder(date: String?) {
        mDateOfPayment.value = date
    }

    fun setDeletePurchase(status: String) {
        Thread.sleep(1000)
        mDeletePurchase.value = status
    }

    fun setShopping(shop:Shopping) {
       mShopping = shop
    }

    fun setUpdate(update: Boolean) {
        mUpdateStatus.value = update
    }


}