package com.example.rancho.ui.product

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rancho.R
import com.example.rancho.adapter.ListProductAdapter
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.FragmentListProductBinding
import com.example.rancho.enum.SearchType
import com.example.rancho.model.Product
import com.example.rancho.model.Shopping
import com.example.rancho.util.ViewModelInstance
import com.orhanobut.hawk.Hawk
import dominando.android.testeproduct.util.ShowMessage
import kotlinx.coroutines.launch
import java.util.*


class ListProductFragment : Fragment() {

    private var _binding: FragmentListProductBinding? = null
    private val binding: FragmentListProductBinding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private var id_shopping: Int? = null
    private var editTextSearchProduct: EditText? = null
    private var btnSpeak: ImageView? = null
    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var alert: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListProductBinding.inflate(inflater, container, false)
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

            prepareSearch(SearchType.ALL)

        })


        productViewModel.totalValue.observe(viewLifecycleOwner, Observer {

            var total = 0.0
            it.forEach { data ->
                if (data.productDone) {
                    total += (data.productValue * data.productQuantity)
                }
            }

            binding.txtTotalValue.setText("R$ " + String.format("%.2f", total))

        })

        productViewModel.productLack.observe(viewLifecycleOwner, Observer {

            val result = "${it.lack} / ${it.total}"

            binding.txtQuantityDone.text = result

        })
    }


    private fun prepareSearch(search: SearchType, query: String = "") {

        val listProduct = ListProductAdapter()
        var hist: List<Product> = emptyList()

        lifecycleScope.launch {

            when (search) {
                SearchType.ALL -> {
                    hist = ProductDatabase(requireContext()).getProductDao().getAllProducts(
                        id_shopping.toString()
                    )
                }
                SearchType.NAME -> {
                    hist = ProductDatabase(requireContext()).getProductDao()
                        .getProductByName("%$query%")

                    if (hist.size == 0) {
                        ShowMessage.showToast(
                            "$query não foi encontrado ",
                            requireContext()
                        )
                        prepareSearch(SearchType.ALL)
                    }
                }
            }




            listProduct.setProductList(hist, requireContext())
            productViewModel.setTotalValue(hist)

            callRecyclerView(listProduct)

        }

    }


    private fun actions() {
        binding.apply {

            btnAddNewProduct.setOnClickListener {

                findNavController().navigate(R.id.action_listProductFragment_to_addProductFragment)

            }

            btnSearchProduct.setOnClickListener {

                showEditTextDialog()


            }

            swipRecyclerPorduct.setOnRefreshListener {
                prepareSearch(SearchType.ALL)

                swipRecyclerPorduct.isRefreshing = false
            }


        }
    }


    private fun showEditTextDialog() {

        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.item_search_product, null)
        editTextSearchProduct = dialogLayout.findViewById<EditText>(R.id.editProductName)
        btnSpeak = dialogLayout.findViewById<ImageView>(R.id.btnSpeakProduct)


//        editTextSearchProduct!!.addTextChangedListener {
//            prepareSearch(SearchType.NAME, editTextSearchProduct!!.text.toString())
//        }

        btnSpeak!!.setOnClickListener {
            speak()
        }

        alert = with(builder) {
            setTitle("Informe o nome do Produto")
            setPositiveButton("OK") { dialog, whith ->
                prepareSearch(SearchType.NAME, editTextSearchProduct!!.text.toString())
            }
            setView(dialogLayout)
            show()
        }

    }


    private fun speak() {
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Olá fale alguma coisa!")

        try {
            startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
        } catch (ex: Exception) {
            Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    // editTextSearchProduct!!.setText(result?.get(0))
                    prepareSearch(SearchType.NAME, result?.get(0)!!)
                    alert!!.dismiss()
                }
            }
        }

    }


    private fun callRecyclerView(listProduct: ListProductAdapter) {

        setupRecyclerView()

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