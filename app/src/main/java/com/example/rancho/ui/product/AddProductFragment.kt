package com.example.rancho.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.FragmentAddProductBinding
import com.example.rancho.model.Product
import com.example.rancho.model.Shopping
import com.example.rancho.util.ViewModelInstance
import com.orhanobut.hawk.Hawk
import dominando.android.testeproduct.util.ShowMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding: FragmentAddProductBinding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private var id_shopping: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        productViewModel = ViewModelInstance.getProductViewModel()

        val shop = Hawk.get<Shopping>("purchase")
        id_shopping = shop.id

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.productViewModel = productViewModel


        observe()
        actions()

    }

    private fun actions() {

        binding.apply {

            btnAddProduct.setOnClickListener {

                if (editProductName.text.toString().isNotEmpty()) {

                    val prod = getProduct()

                    GlobalScope.launch {

                        ProductDatabase(requireContext()).getProductDao().addProduct(prod)

                    }
                    productViewModel!!.setAction("add")
                    findNavController().popBackStack()

                } else {
                    ShowMessage.showToast("Informe o nome do produto", requireContext())
                }

            }

            btnUpdateProduct.setOnClickListener {

                if (editProductName.text.toString().isNotEmpty()) {

                    val prod = getProduct()

                    GlobalScope.launch {

                        ProductDatabase(requireContext()).getProductDao().updateProduct(prod)

                    }
                    productViewModel!!.setAction("update")
                    findNavController().popBackStack()

                } else {
                    ShowMessage.showToast("Informe o nome do produto", requireContext())
                }


            }


        }

    }

    private fun getProduct(): Product {
        binding.apply {

            return Product(
                id_shopping!!,
                editProductName.text.toString(),
                editProductQuantity.text.toString().toInt(),
                editProductValue.text.toString().replace(",", ".").toDouble(),
                cbProductDone.isChecked
            )

        }
    }


    private fun observe() {


    }


}