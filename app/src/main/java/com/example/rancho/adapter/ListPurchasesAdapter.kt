package com.example.rancho.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.rancho.databinding.ItemPurchaseBinding
import com.example.rancho.model.Shopping
import com.example.rancho.ui.puchases.ListPurchasesFragmentDirections
import com.orhanobut.hawk.Hawk

class ListPurchasesAdapter() :
    RecyclerView.Adapter<ListPurchasesAdapter.ListPurchasesViewHolder>() {


    private var purchaseList: List<Shopping> = emptyList()

    fun setPurchaseList(list:List<Shopping>){
        this.purchaseList = list
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

        holder.binding.txtDatePurchase.text = purchaseList[position].dateShopping
        holder.binding.txtTime.text = purchaseList[position].timeShopping
        if(purchaseList[position].active){
            status = "Ativo"
        }else{
            status = "Inativo"
        }
        holder.binding.txtActive.text = status

        holder.itemView.setOnClickListener {

//            AlertDialog.Builder(it.context).apply {
//                setTitle(purchaseList[position].dateShopping + " Selecionado!!")
//                setMessage("Usar esse cliente ou editar?")
//
//                setPositiveButton("Usar") { _, _ ->
//
//                    Hawk.put("purchase", purchaseList[position])
//
//
//                }
//
//                setNegativeButton("Editar") { _, _ ->
//
//                    Hawk.put("client", purchaseList[position])
//                    val action = ListPurchasesFragmentDirections.actionListPurchasesFragmentToListProductFragment()
//                    Navigation.findNavController(it).navigate(action)
//
//
//                }
//            }.create().show()

            Hawk.put("purchase", purchaseList[position])
            val action = ListPurchasesFragmentDirections.actionListPurchasesFragmentToListProductFragment()
            Navigation.findNavController(it).navigate(action)

        }

    }


    inner class ListPurchasesViewHolder(val binding: ItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root)
}