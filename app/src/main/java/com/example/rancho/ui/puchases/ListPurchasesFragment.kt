package com.example.rancho.ui.puchases

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rancho.MainActivity
import com.example.rancho.R
import com.example.rancho.databinding.FragmentListPurchasesBinding
import com.example.rancho.ui.product.ProductViewModel
import com.example.rancho.util.ViewModelInstance

class ListPurchasesFragment : Fragment() {

    private var _binding : FragmentListPurchasesBinding? = null
    private val binding : FragmentListPurchasesBinding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListPurchasesBinding.inflate(inflater,container,false)

        (activity as MainActivity).supportActionBar?.hide()
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ViewModelInstance.setProductViewModel(productViewModel)
        binding.btnAddNewPurchase.setOnClickListener {
            findNavController().navigate(R.id.action_listPurchasesFragment_to_addProductFragment)
            productViewModel.setUpdateMode(false)
        }

    }


}