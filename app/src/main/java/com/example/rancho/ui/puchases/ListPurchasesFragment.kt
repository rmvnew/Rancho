package com.example.rancho.ui.puchases

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rancho.MainActivity
import com.example.rancho.databinding.FragmentListPurchasesBinding

class ListPurchasesFragment : Fragment() {

    private var _binding : FragmentListPurchasesBinding? = null
    private val binding : FragmentListPurchasesBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListPurchasesBinding.inflate(inflater,container,false)
//        (activity as MainActivity).supportActionBar?.title = "Principal"
//        (activity as MainActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.DKGRAY))
        (activity as MainActivity).supportActionBar?.hide()

        return binding.root
    }


}