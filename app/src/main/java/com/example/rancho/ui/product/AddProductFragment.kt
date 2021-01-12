package com.example.rancho.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
        productViewModel.updateProduct.observe(viewLifecycleOwner, Observer {
            productViewModel.nameButtonAction = productViewModel.setNameButtonAction(it)
            productViewModel.updateProductActive = it
            if(it == false) {
                binding.btnProductDelete.visibility = View.GONE
            }else{
                binding.btnProductDelete.visibility = View.VISIBLE
            }
        })
    }


}