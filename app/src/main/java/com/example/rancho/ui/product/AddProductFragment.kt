package com.example.rancho.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rancho.databinding.FragmentAddProductBinding


class AddProductFragment : Fragment() {

    private var _binding : FragmentAddProductBinding? = null
    private val binding : FragmentAddProductBinding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddProductBinding.inflate(inflater,container,false)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)




        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.productViewModel = productViewModel


        observe()

    }



    private fun observe(){



    }


}