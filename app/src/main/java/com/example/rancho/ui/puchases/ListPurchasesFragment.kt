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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rancho.MainActivity
import com.example.rancho.R
import com.example.rancho.adapter.ListPurchasesAdapter
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.FragmentListPurchasesBinding
import com.example.rancho.enum.SearchDate
import com.example.rancho.enum.SearchType
import com.example.rancho.enum.StatusPurchase
import com.example.rancho.model.Shopping
import com.example.rancho.util.DateUtil
import com.example.rancho.util.ViewModelInstance
import com.github.barteksc.pdfviewer.PDFView
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
    lateinit var purchase: Shopping
    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var editTextNewPurchase: EditText? = null
    private var cbPurchase: CheckBox? = null
    private var btnSpeak: ImageView? = null
    private var txtDate:TextView? = null
    private var btnDate:ImageView? = null

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


            btnSettings.setOnClickListener {
                findNavController().navigate(R.id.action_listPurchasesFragment_to_settingsFragment)
            }


            btnHelp.setOnClickListener {

                showPDF()

            }


            swipRecyclerPurchase.setOnRefreshListener {

                callRecyclerView(null)
                swipRecyclerPurchase.isRefreshing = false

            }


            btnSearchShopping.setOnClickListener {

               setPickerDate(SearchDate.SEARCH)

            }


        }

    }

    private fun setPickerDate(searchDate: SearchDate) {


        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(searchDate)
            }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()


    }


    private fun updateDateInView(searchDate: SearchDate) {
        val myDateFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myDateFormat, Locale.US)
        when(searchDate){
            SearchDate.SEARCH -> viewModel.setDateNewOrder(sdf.format(cal.time))
            SearchDate.ADD -> txtDate!!.text = sdf.format(cal.time)
        }
    }



    private fun showEditTextDialog(status: StatusPurchase) {

        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.item_edit_text_purchase, null)
        editTextNewPurchase = dialogLayout.findViewById<EditText>(R.id.editPurchaseName)
        cbPurchase = dialogLayout.findViewById<CheckBox>(R.id.cbPurchaseActive)
        btnSpeak = dialogLayout.findViewById<ImageView>(R.id.btnSpeak)
        txtDate = dialogLayout.findViewById<TextView>(R.id.txtDate)
        btnDate = dialogLayout.findViewById<ImageView>(R.id.btnDate)

        if (status == StatusPurchase.UPDATE) {
            editTextNewPurchase!!.setText(purchase.name)
            cbPurchase!!.isChecked = purchase.active
            txtDate!!.text = purchase.dateShopping
        } else {
            cbPurchase!!.isChecked = true
        }

        btnSpeak!!.setOnClickListener {
            speak()
        }

        btnDate!!.setOnClickListener {
            setPickerDate(SearchDate.ADD)
        }

        with(builder) {
            setTitle("Informe o nome do Supermercado")
            setPositiveButton("OK") { dialog, whith ->
                if (editTextNewPurchase!!.text.isNotEmpty()) {
                    if (status == StatusPurchase.SAVE) {
                        savePurchase(
                            editTextNewPurchase!!.text.trim().toString(),
                            cbPurchase!!.isChecked
                        )
                    } else {
                        updatePurchase(
                            editTextNewPurchase!!.text.trim().toString(),
                            cbPurchase!!.isChecked
                        )
                        viewModel.setUpdate(false)
                    }
                } else {
                    ShowMessage.showToast("Informe o nome do supermercado", requireContext())
                }

            }
            setView(dialogLayout)
            show()
        }

    }


    private fun showPDF(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.item_help_pdf, null)
        var pdfView = dialogLayout.findViewById<PDFView>(R.id.PDFView)

        pdfView.fromAsset("meu_rancho.pdf").load()

        with(builder) {
            setTitle("LEIA-ME")
            setPositiveButton("OK") { dialog, whith ->


            }
            setView(dialogLayout)
            show()
        }

    }


    private fun savePurchase(name: String, isActive: Boolean) {

       if(txtDate!!.text.isNotEmpty()){
           val shop = Shopping(
               name,
               DateUtil.getCurrentDate(cal),
               DateUtil.getCurrentTime(cal),
               isActive
           )

           GlobalScope.launch {
               ProductDatabase(requireContext()).getShoppingDao().addShopping(shop)

           }

           viewModel.setNewPurchase()
       }

    }

    private fun updatePurchase(name: String, isActive: Boolean) {

       if(txtDate!!.text.isNotEmpty()){
           val shop = Shopping(
               name,
               DateUtil.getCurrentDate(cal),
               DateUtil.getCurrentTime(cal),
               isActive
           )

           shop.id = purchase.id

           GlobalScope.launch {
               ProductDatabase(requireContext()).getShoppingDao().updateShopping(shop)

           }

           viewModel.setNewPurchase()
       }

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


        viewModel.updateStatus.observe(viewLifecycleOwner, {

            purchase = viewModel.mShopping!!
            if (it) {
                showEditTextDialog(StatusPurchase.UPDATE)
            }
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

                if (hist.size == 0) {
                    ShowMessage.showToast(
                        "Nenhuma compra foi encontrada dia $date",
                        requireContext()
                    )
                    callRecyclerView(null)
                }

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
                    editTextNewPurchase!!.setText(result?.get(0))
                    editTextNewPurchase!!.requestFocus()
                }
            }
        }

    }


}