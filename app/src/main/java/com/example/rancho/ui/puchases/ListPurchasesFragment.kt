package com.example.rancho.ui.puchases

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rancho.adapter.ListPurchasesAdapter
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.FragmentListPurchasesBinding
import com.example.rancho.enum.SearchType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListPurchasesFragment : Fragment() {

    private var _binding : FragmentListPurchasesBinding? = null
    private val binding : FragmentListPurchasesBinding get() = _binding!!
    private lateinit var viewModel : ListPurchasesViewModel
    private var cal = Calendar.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListPurchasesBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(ListPurchasesViewModel::class.java)




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
            btnAddNewPurchase.setOnClickListener {
                viewModel.saveNewPurchase(requireContext())
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
    }




    private fun callRecyclerView(date: String?, type: SearchType = SearchType.ALL) {

        setupRecyclerView()
        val listPurchase = ListPurchasesAdapter()


        lifecycleScope.launch {

            if (type == SearchType.ALL && date == null) {

                val hist = ProductDatabase(requireContext()).getShoppingDao().getAllShoppings()
                listPurchase.setPurchaseList(hist)

            } else if (type == SearchType.DATE && date != null) {

                val hist =
                    ProductDatabase(requireContext()).getShoppingDao().getAllShoppingsThisDate(date)
                listPurchase.setPurchaseList(hist)

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


}