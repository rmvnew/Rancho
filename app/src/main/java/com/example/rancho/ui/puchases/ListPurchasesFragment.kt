package com.example.rancho.ui.puchases

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rancho.MainActivity
import com.example.rancho.R
import com.example.rancho.adapter.ListPurchasesAdapter
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.FragmentListPurchasesBinding
import com.example.rancho.enum.SearchType
import com.example.rancho.enum.StatusPurchase
import com.example.rancho.model.Shopping
import com.example.rancho.util.DateUtil
import com.example.rancho.util.ViewModelInstance
import dominando.android.testeproduct.util.ShowMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListPurchasesFragment : Fragment() {

    private var _binding: FragmentListPurchasesBinding? = null
    private val binding: FragmentListPurchasesBinding get() = _binding!!
    private lateinit var viewModel: ListPurchasesViewModel
    private var cal = Calendar.getInstance()
    lateinit var purchase : Shopping
    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var editTextNewPurchase: EditText? = null
    private var cbPurchase:CheckBox? = null
    private var btnSpeak:ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListPurchasesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ListPurchasesViewModel::class.java)
        ViewModelInstance.setPurchaseViewModel(viewModel)
        (activity as MainActivity).supportActionBar?.hide()



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.setDateNewOrder(null)

        actions()
        observer()

    }

    private fun actions() {

        binding.apply {


            btnAddNewProduct.setOnClickListener {

                showEditTextDialog(StatusPurchase.SAVE)

            }


            btnSearchShopping.setOnClickListener {

                val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker, year: Int, monthOfYear: Int,
                        dayOfMonth: Int
                    ) {
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateDateInView()
                    }
                }

                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()

            }
        }

    }

    private fun showEditTextDialog(status:StatusPurchase) {

        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.edit_text_purchase, null)
        editTextNewPurchase = dialogLayout.findViewById<EditText>(R.id.editPurchaseName)
        cbPurchase = dialogLayout.findViewById<CheckBox>(R.id.cbPurchaseActive)
        btnSpeak = dialogLayout.findViewById<ImageView>(R.id.btnSpeak)

        if(status == StatusPurchase.UPDATE){
            editTextNewPurchase!!.setText(purchase.name)
            cbPurchase!!.isChecked = purchase.active
        }

        btnSpeak!!.setOnClickListener {
            speak()
        }

        with(builder) {
            setTitle("Informe o nome do Supermercado")
            setPositiveButton("OK") { dialog, whith ->
                if (editTextNewPurchase!!.text.isNotEmpty()) {
                    if(status == StatusPurchase.SAVE){
                        savePurchase(editTextNewPurchase!!.text.trim().toString(),cbPurchase!!.isChecked)
                    }else{
                        updatePurchase(editTextNewPurchase!!.text.trim().toString(),cbPurchase!!.isChecked)
                    }
                } else {
                    ShowMessage.showToast("Informe o nome do supermercado", requireContext())
                }

            }
            setView(dialogLayout)
            show()
        }

    }


    private fun savePurchase(name: String,isActive:Boolean) {
        val shop = Shopping(
            name,
            DateUtil.getCurrentDate(),
            DateUtil.getCurrentTime(),
            isActive
        )

        GlobalScope.launch {
            ProductDatabase(requireContext()).getShoppingDao().addShopping(shop)

        }

        viewModel.setNewPurchase()
    }
    private fun updatePurchase(name: String,isActive:Boolean) {
        val shop = Shopping(
            name,
            DateUtil.getCurrentDate(),
            DateUtil.getCurrentTime(),
            isActive
        )

        shop.id = purchase.id

        GlobalScope.launch {
            ProductDatabase(requireContext()).getShoppingDao().updateShopping(shop)

        }

        viewModel.setNewPurchase()
    }



    private fun updateDateInView() {
        val myDateFormat = "dd/MM/yyyy" // mention the format you need
        val myTimeFormat = "HH:mm:ss" // mention the format you need
        val sdf = SimpleDateFormat(myDateFormat, Locale.US)
        val stf = SimpleDateFormat(myTimeFormat, Locale.US)
        viewModel.setDateNewOrder(sdf.format(cal.getTime()))
    }

    private fun observer() {
        viewModel.dateOfPayment.observe(viewLifecycleOwner, androidx.lifecycle.Observer {


            if (it.isNullOrEmpty()) {
                callRecyclerView(null)

            } else {
                callRecyclerView(it, SearchType.DATE)

            }

        })

        viewModel.saved.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (it) {
                callRecyclerView(null)
            }

        })

        viewModel.deletePurchase.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            callRecyclerView(null)
        })


        viewModel.updateStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {


            purchase = it
            showEditTextDialog(StatusPurchase.UPDATE)

        })
    }


    private fun callRecyclerView(date: String?, type: SearchType = SearchType.ALL) {

        setupRecyclerView()

        val listPurchase = ListPurchasesAdapter()


        lifecycleScope.launch {

            if (type == SearchType.ALL && date == null) {

                val hist = ProductDatabase(requireContext()).getShoppingDao().getAllShoppings()
                listPurchase.setPurchaseList(hist, requireContext())

            } else if (type == SearchType.DATE && date != null) {

                val hist =
                    ProductDatabase(requireContext()).getShoppingDao().getAllShoppingsThisDate(date)
                listPurchase.setPurchaseList(hist, requireContext())

            }

        }

        binding.recyclerShopp.adapter = listPurchase

    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerShopp.setHasFixedSize(true)
            recyclerShopp.layoutManager = StaggeredGridLayoutManager(
                1,
                StaggeredGridLayoutManager.VERTICAL

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
                    editTextNewPurchase!!.setText(result?.get(0))
                    editTextNewPurchase!!.requestFocus()
                }
            }
        }

    }


}