package com.example.rancho.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.rancho.dao.ProductDatabase
import com.example.rancho.databinding.ItemPurchaseBinding
import com.example.rancho.model.Shopping
import com.example.rancho.ui.puchases.ListPurchasesFragmentDirections
import com.example.rancho.ui.puchases.ListPurchasesViewModel
import com.example.rancho.util.ViewModelInstance
import com.orhanobut.hawk.Hawk
import dominando.android.testeproduct.util.ShowMessage
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

        holder.binding.txtSupermarket.text = purchaseList[position].name
        holder.binding.txtDatePurchase.text = purchaseList[position].dateShopping
        holder.binding.txtTimePurchase.text = purchaseList[position].timeShopping
        if(purchaseList[position].active){
            status = "Ativo"
        }else{
            status = "Concluído"
        }
        holder.binding.txtStatusPurchase.text = status

        holder.itemView.setOnClickListener {

            if(purchaseList[position].active){

                Hawk.put("purchase", purchaseList[position])
                val action = ListPurchasesFragmentDirections.actionListPurchasesFragmentToListProductFragment()
                Navigation.findNavController(it).navigate(action)

            }else{
                ShowMessage.showToast("Esta compra já foi concluida!!",context!!)
            }

        }

        holder.itemView.setOnLongClickListener {


            AlertDialog.Builder(context).apply {
                setTitle(purchaseList[position].name+" Selecionado!!")
                setMessage("Deseja realmente deletar?")
                setPositiveButton("Editar"){_,_ ->

                viewModel.setShopping(purchaseList[position])
                viewModel.setUpdate(true)

                }

                setNegativeButton("Deletar"){_,_ ->

                    GlobalScope.launch {

                        ProductDatabase(context!!).getShoppingDao().deleteShopping(purchaseList[position])
                        ProductDatabase(context!!).getProductDao().deleteAllProducts(purchaseList[position].id.toString())

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