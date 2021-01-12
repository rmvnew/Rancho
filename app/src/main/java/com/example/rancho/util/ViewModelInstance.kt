package com.example.rancho.util

import com.example.rancho.ui.product.ProductViewModel

object ViewModelInstance {

    private var productViewModel:Any? = null


    fun setProductViewModel(viewModel: ProductViewModel){
        this.productViewModel = viewModel
    }


    fun getProductViewModel():ProductViewModel{
        return  productViewModel as ProductViewModel
    }

}