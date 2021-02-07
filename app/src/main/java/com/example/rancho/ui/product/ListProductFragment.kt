package com.example.rancho.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rancho.R
import com.example.rancho.adapter.ListProductAdapter
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.FragmentListProductBinding
import com.example.rancho.model.Shopping
import com.example.rancho.util.ViewModelInstance
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.launch

class ListProductFragment : Fragment() {

    private var _binding : FragmentListProductBinding? = null
    private val binding : FragmentListProductBinding get() = _binding!!
    private lateinit var productViewModel : ProductViewModel
    private var id_shopping: Int? = null

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

        val shop = Hawk.get<Shopping>("purchase")
        id_shopping = shop.id
        productViewModel.setAction("load")

        actions()
        observer()

    }

    private fun observer() {
        productViewModel.actions.observe(viewLifecycleOwner, Observer {

            callRecyclerView()

        })


        productViewModel.totalValue.observe(viewLifecycleOwner, Observer {

            var total = 0.0
           it.forEach { data ->
             if(data.productDone){
                 total +=  (data.productValue * data.productQuantity)
             }
           }

            binding.txtTotalValue.setText("R$ "+String.format("%.2f",total))

        })
    }

    private fun actions() {
        binding.apply {

            btnAddNewProduct.setOnClickListener {

                findNavController().navigate(R.id.action_listProductFragment_to_addProductFragment)

            }

        }
    }


    private fun callRecyclerView() {

        setupRecyclerView()
        val listProduct = ListProductAdapter()

        lifecycleScope.launch {

                val hist = ProductDatabase(requireContext()).getProductDao().getAllProducts(id_shopping.toString())
                listProduct.setProductList(hist,requireContext())
                productViewModel.setTotalValue(hist)

        }

        binding.recyclerProduct.adapter = listProduct

    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerProduct.setHasFixedSize(true)
            recyclerProduct.layoutManager = StaggeredGridLayoutManager(
                1,
                StaggeredGridLayoutManager.VERTICAL

            )
        }
    }

}