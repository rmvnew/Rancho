package com.example.rancho.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.ItemPurchaseBinding
import com.example.rancho.model.Shopping
import com.example.rancho.ui.puchases.ListPurchasesFragmentDirections
import com.example.rancho.ui.puchases.ListPurchasesViewModel
import com.example.rancho.util.ViewModelInstance
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListPurchasesAdapter() :
    RecyclerView.Adapter<ListPurchasesAdapter.ListPurchasesViewHolder>() {


    private var purchaseList: List<Shopping> = emptyList()
    lateinit var viewModel: ListPurchasesViewModel

    var context: Context? = null

    fun setPurchaseList(list:List<Shopping>,context: Context){
        this.purchaseList = list
        this.context = context
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPurchasesViewHolder {
        val binding = ItemPurchaseBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)

        return ListPurchasesViewHolder((binding))
    }

    override fun getItemCount() = purchaseList.size



    override fun onBindViewHolder(holder: ListPurchasesViewHolder, position: Int) {
        var status = ""
        viewModel = ViewModelInstance.getPurchaseViewModel()

        holder.binding.txtDatePurchase.text = purchaseList[position].dateShopping
        holder.binding.txtTimePurchase.text = purchaseList[position].timeShopping
        if(purchaseList[position].active){
            status = "Ativo"
        }else{
            status = "Inativo"
        }
        holder.binding.txtStatusPurchase.text = status

        holder.itemView.setOnClickListener {


            Hawk.put("purchase", purchaseList[position])
            val action = ListPurchasesFragmentDirections.actionListPurchasesFragmentToListProductFragment()
            Navigation.findNavController(it).navigate(action)

        }

        holder.itemView.setOnLongClickListener {


            AlertDialog.Builder(context).apply {
                setTitle(purchaseList[position].dateShopping+" Selecionado!!")
                setMessage("Deseja realmente deletar?")
                setPositiveButton("Cancelar"){_,_ ->



                }

                setNegativeButton("Deletar"){_,_ ->

                    GlobalScope.launch {
                        ProductDatabase(context!!).getShoppingDao().deleteShopping(purchaseList[position])

                    }

                    viewModel.setDeletePurchase("delete")

                }
            }.create().show()



            return@setOnLongClickListener true
        }

    }


    inner class ListPurchasesViewHolder(val binding: ItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root)
}