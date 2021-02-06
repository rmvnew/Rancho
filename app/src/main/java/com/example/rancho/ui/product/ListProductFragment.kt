package com.example.rancho.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rancho.R
import com.example.rancho.databinding.FragmentListProductBinding
import com.example.rancho.util.ViewModelInstance

class ListProductFragment : Fragment() {

    private var _binding : FragmentListProductBinding? = null
    private val binding : FragmentListProductBinding get() = _binding!!
    private lateinit var productViewModel : ProductViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListProductBinding.inflate(inflater,container,false)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        ViewModelInstance.setProductViewModel(productViewModel)


        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        actions()

    }

    private fun actions() {
        binding.apply {

            btnAddNewProduct.setOnClickListener {

                findNavController().navigate(R.id.action_listProductFragment_to_addProductFragment)

            }

        }
    }

}