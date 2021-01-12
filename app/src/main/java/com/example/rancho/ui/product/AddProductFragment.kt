package com.example.rancho.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.rancho.databinding.FragmentAddProductBinding
import com.example.rancho.util.ViewModelInstance

class AddProductFragment : Fragment() {

    private var _binding : FragmentAddProductBinding? = null
    private val binding : FragmentAddProductBinding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddProductBinding.inflate(inflater,container,false)





        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        productViewModel = ViewModelInstance.getProductViewModel()
        binding.productViewModel = productViewModel
        observe()
    }



    private fun observe(){

        /**
         * update purchase
         */

        productViewModel.updatePurchase.observe(viewLifecycleOwner, Observer {
            productViewModel.nameButtonPurchaseAction = productViewModel.setNameButtonAction(it)
            productViewModel.updatePurchaseActive = it
            if(it == false) {
                binding.btnPurchaseDelete.visibility = View.GONE
            }else{
                binding.btnPurchaseDelete.visibility = View.VISIBLE
            }
        })


        /**
         * update item
         */

        productViewModel.updateItem.observe(viewLifecycleOwner, Observer {
            productViewModel.nameButtonItemAction = productViewModel.setNameButtonAction(it)+" Item"
            if(it == false){
                binding.btnItemDelete.visibility = View.GONE
            }else{
                binding.btnItemDelete.visibility = View.VISIBLE
            }
        })

    }


}