package com.example.rancho.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rancho.model.Product
import com.example.rancho.model.ProductInCart

class ProductViewModel : ViewModel() {

    private var mActions = MutableLiveData<String>()
    val actions : LiveData<String> = mActions

    private var mTotalValue = MutableLiveData<List<Product>>()
    val totalValue : LiveData<List<Product>> = mTotalValue

    private var mProductLack = MutableLiveData<ProductInCart>()
    val productLack : LiveData<ProductInCart> = mProductLack



    fun setAction(action: String) {
        mActions.value = action
    }

    fun setTotalValue(hist: List<Product>) {
        mTotalValue.value = hist
    }

    fun setProductLack(productLack: ProductInCart) {
        mProductLack.value = productLack
    }


}