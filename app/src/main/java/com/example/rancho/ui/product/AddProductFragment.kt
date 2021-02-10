package com.example.rancho.ui.product

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.FragmentAddProductBinding
import com.example.rancho.model.Product
import com.example.rancho.model.Shopping
import com.example.rancho.util.ViewModelInstance
import com.orhanobut.hawk.Hawk
import dominando.android.testeproduct.util.ShowMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding: FragmentAddProductBinding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private var id_shopping: Int? = null
    private var product :Product? = null
    private val REQUEST_CODE_SPEECH_INPUT = 100

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

        checkProduct()
        actions()

    }

    private fun checkProduct() {
        val args: AddProductFragmentArgs by navArgs()
        product = args.product

        binding.apply {

            product?.let {
                editProductName.setText(product!!.productName)
                editProductQuantity.setText(product!!.productQuantity.toString())
                editProductValue.setText(product!!.productValue.toString())
                cbProductDone.isChecked = product!!.productDone
            }

        }

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
                    prod.id = product!!.id

                    GlobalScope.launch {

                        ProductDatabase(requireContext()).getProductDao().updateProduct(prod)

                    }
                    Thread.sleep(1000)
                    productViewModel!!.setAction("update")
                    findNavController().popBackStack()

                } else {
                    ShowMessage.showToast("Informe o nome do produto", requireContext())
                }


            }


            btnDeleteProduct.setOnClickListener {
                if (editProductName.text.toString().isNotEmpty()) {

                    val prod = getProduct()
                    prod.id = product!!.id

                    AlertDialog.Builder(requireContext()).apply {
                        setTitle(prod.productName+" Selecionado!!")
                        setMessage("Deseja realmente deletar?")
                        setPositiveButton("Cancelar"){_,_ ->



                        }

                        setNegativeButton("Deletar"){_,_ ->

                            GlobalScope.launch {

                                ProductDatabase(requireContext()).getProductDao().deleteProduct(prod)

                            }
                            productViewModel!!.setAction("delete")
                            findNavController().popBackStack()

                        }
                    }.create().show()




                } else {
                    ShowMessage.showToast("Informe o nome do produto", requireContext())
                }

            }

            btnSpeak.setOnClickListener {
                speak()
            }


        }




    }

    private fun getProduct(): Product {
        binding.apply {

            return Product(
                id_shopping!!,
                editProductName.text.toString(),
                if(editProductQuantity.text.toString().isNullOrEmpty()){1}else{editProductQuantity.text.toString().toInt()},
                if(editProductValue.text.toString().isNullOrEmpty()){0.0}else{editProductValue.text.toString().replace(",", ".").toDouble()},
                cbProductDone.isChecked
            )

        }
    }


    private fun speak(){
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Olá fale alguma coisa!")

        try {
            startActivityForResult(mIntent,REQUEST_CODE_SPEECH_INPUT)
        }catch(ex: Exception){
            Toast.makeText(requireContext(),ex.message, Toast.LENGTH_SHORT).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){

            REQUEST_CODE_SPEECH_INPUT ->{
                if(resultCode == Activity.RESULT_OK && null != data){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    binding.editProductName.setText(result?.get(0))
                    binding.editProductQuantity.requestFocus()
                }
            }
        }

    }





}