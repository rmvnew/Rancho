package com.example.rancho.ui.product

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rancho.R
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.FragmentAddProductBinding
import com.example.rancho.model.Product
import com.example.rancho.model.Shopping
import com.example.rancho.util.*
import com.orhanobut.hawk.Hawk
import dominando.android.testeproduct.util.ShowMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding: FragmentAddProductBinding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private var id_shopping: Int? = null
    private var id_product: Int = 0
    private var product: Product? = null
    private var addToCart: Boolean = false
    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var confSpeech: Boolean? = null
    private lateinit var speechManager: SpeechManager
    private var typeOfMeasure: String? = null
    private var valueSpType: Int? = null
    private var btnYes: Button? = null
    private var btnNo: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        speechManager = SpeechManager(requireContext()).apply { init(Language.PORTUGUESE) }
        productViewModel = ViewModelInstance.getProductViewModel()


        val shop = Hawk.get<Shopping>("purchase")
        id_shopping = shop.id

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.productViewModel = productViewModel

        checkProduct()
        actions()
        setSpinner()

    }

    private fun checkProduct() {
        val args: AddProductFragmentArgs by navArgs()
        product = args.product

        binding.apply {

            product?.let {
                id_product = product!!.id
                editProductName.setText(product!!.productName)

                if (product!!.productValue != 0.0) {
                    editProductValue.setText(product!!.productValue.toString())
                } else {
                    editProductValue.setText("")
                }
                addToCart = product!!.productDone

                Log.i("res", "==>>" + product!!.typeOfMeasure)
                when (product!!.typeOfMeasure) {
                    "Und" -> {
                        valueSpType = 0
                        editProductQuantity.setText(product!!.productQuantity.toInt().toString())
                        Log.i("res", "==>> 0")
                    }
                    "Kg" -> {
                        valueSpType = 1
                        editProductQuantity.setText(product!!.productQuantity.toString())
                        Log.i("res", "==>> 1")
                    }
                }


            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun actions() {

        binding.apply {

            btnAddProduct.setOnClickListener {

                checkSave()

            }

            btnDeleteProduct.setOnClickListener {
                if (editProductName.text.toString().isNotEmpty()) {
                    if (product != null) {
                        val prod = getProduct()
                        prod.id = product!!.id

                        AlertDialog.Builder(requireContext()).apply {
                            setTitle(prod.productName + " Selecionado!!")
                            setMessage("Deseja realmente deletar?")
                            setPositiveButton("Cancelar") { _, _ ->

                            }

                            setNegativeButton("Deletar") { _, _ ->

                                GlobalScope.launch {

                                    ProductDatabase(requireContext()).getProductDao()
                                        .deleteProduct(prod)

                                }
                                productViewModel!!.setAction("delete")
                                product = null
                                findNavController().popBackStack()

                            }
                        }.create().show()
                    } else {
                        ShowMessage.showToast(
                            "O produto não esta salvo,\n Impossível deletar",
                            requireContext()
                        )
                    }
                } else {
                    ShowMessage.showToast("Informe o nome do produto", requireContext())
                }

            }

            btnSpeak.setOnClickListener {
                speak()
            }

        }


    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun notifyIncludedProduct(prod: Product) {

        MainScope().launch {
            confSpeech = DataStoreUtil(requireContext()).readBoolean("speech")

            if (confSpeech == true) {
                if (prod.productQuantity == 1.0) {
                    speechManager.speechToText("${prod.productName} foi adicionado a sua lista")
                } else {
                    speechManager.speechToText(
                        "${prod.productQuantity} ${
                            PluralWordsUtil.setStringPlural(
                                prod.productName
                            )
                        } foram adicionados a sua lista"
                    )
                }
            }

        }

    }


    private fun checkFields(): Boolean {

        var res = false

        binding.apply {

            if (!editProductQuantity.text.isNullOrBlank() && !editProductValue.text.isNullOrBlank()) {
                res = true
            }

        }

        return res
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkSave() {

        if (checkFields()) {
            val builder = AlertDialog.Builder(requireContext())
            val inflater: LayoutInflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.item_add_to_cart, null)
            builder.setView(dialogLayout)
            val alert = builder.show()
            btnYes = dialogLayout.findViewById<Button>(R.id.btnYes)
            btnNo = dialogLayout.findViewById<Button>(R.id.btnNo)

            btnYes!!.setOnClickListener {

                addToCart = true
                save()
                alert.dismiss()

            }


            btnNo!!.setOnClickListener {

                addToCart = false
                save()
                alert.dismiss()

            }
        } else {
            save()
        }


    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun save() {

        var isSaving = false

        binding.apply {

            if (editProductName.text.toString().isNotEmpty()) {

                val prod = getProduct()

                GlobalScope.launch {

                    if (id_product == 0) {
                        isSaving = true
                        ProductDatabase(requireContext()).getProductDao().addProduct(prod)

                    } else {
                        prod.id = product!!.id
                        ProductDatabase(requireContext()).getProductDao()
                            .updateProduct(prod)
                    }
                }

                Thread.sleep(300)

                if (isSaving) {
                    notifyIncludedProduct(prod)
                    productViewModel!!.setAction("add")
                    animationOk()
                } else {
                    findNavController().popBackStack()
                }

            } else {
                ShowMessage.showToast("Informe o nome do produto", requireContext())
            }

        }

    }


    private fun animationOk() {

        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.item_confirmation, null)
        builder.setView(dialogLayout)
        val alert = builder.show()

        Handler(Looper.getMainLooper()).postDelayed({
            alert.dismiss()
            findNavController().popBackStack()
        }, 1800)

    }

    private fun getProduct(): Product {
        binding.apply {

            return Product(
                id_shopping!!,
                editProductName.text.toString(),
                if (editProductQuantity.text.toString().isNullOrEmpty()) {
                    1.0
                } else {
                    editProductQuantity.text.toString().replace(",", ".").toDouble()
                },
                if (editProductValue.text.toString().isNullOrEmpty()) {
                    0.0
                } else {
                    editProductValue.text.toString().replace(",", ".").toDouble()
                },
                addToCart,
                typeOfMeasure ?: "Und"
            )

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
                    binding.editProductName.setText(result?.get(0))
                    binding.editProductQuantity.requestFocus()
                }
            }
        }

    }


    fun setSpinner() {

        val listYears = listOf("Und", "Kg")

        binding.spType.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listYears)



        valueSpType?.let {
            binding.spType.setSelection(it)
        }
        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                typeOfMeasure = binding.spType.selectedItem.toString()
                when (typeOfMeasure) {
                    "Und" -> {
                        binding.editProductQuantity.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                    "Kg" -> {
                        binding.editProductQuantity.inputType =
                            (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)
                        binding.editProductQuantity.setKeyListener(
                            DigitsKeyListener.getInstance(
                                "0123456789.,"
                            )
                        )
                    }
                }

            }
        }
    }

}