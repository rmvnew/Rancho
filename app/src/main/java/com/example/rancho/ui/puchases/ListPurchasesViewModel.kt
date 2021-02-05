package com.example.rancho.ui.puchases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.model.Shopping
import com.example.rancho.util.DateUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListPurchasesViewModel : ViewModel() {

    private  var mDateOfPayment = MutableLiveData<String?>()
    val dateOfPayment : LiveData<String?> = mDateOfPayment




    fun saveNewPurchase(context: Context) {

        val shop = Shopping(
            DateUtil.getCurrentDate(),
            DateUtil.getCurrentTime(),
            true
        )

        GlobalScope.launch {
            ProductDatabase(context).getShoppingDao().addShopping(shop)
        }

    }





    fun setDateNewOrder(date: String?) {
        mDateOfPayment.value = date
    }


}