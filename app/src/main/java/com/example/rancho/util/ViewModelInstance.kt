package com.example.rancho.util

import com.example.rancho.ui.product.ProductViewModel
import com.example.rancho.ui.puchases.ListPurchasesViewModel

object ViewModelInstance {

    private var productViewModel:Any? = null
    private var purchaseViewModel:Any? = null


    fun setPurchaseViewModel(viewModel:ListPurchasesViewModel){
        this.purchaseViewModel = viewModel
    }

    fun getPurchaseViewModel():ListPurchasesViewModel{
        return purchaseViewModel as ListPurchasesViewModel
    }

    fun setProductViewModel(viewModel: ProductViewModel){
        this.productViewModel = viewModel
    }


    fun getProductViewModel():ProductViewModel{
        return  productViewModel as ProductViewModel
    }

}