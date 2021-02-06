package com.example.rancho.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rancho.databinding.ItemProductBinding
import com.example.rancho.model.Product
import java.text.NumberFormat

class ListProductAdapter():RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder>() {


    private var productList:List<Product> = emptyList()

    fun setProductList(list:List<Product>){
        this.productList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ListProductViewHolder(binding)
    }

    override fun getItemCount() = productList.size


    override fun onBindViewHolder(holder: ListProductViewHolder, position: Int) {

        var result = ""

        holder.binding.txtNameProduct.text = productList[position].productName
        holder.binding.txtQuantity.text = productList[position].productQuantity.toString()
        holder.binding.txtValue.text = "R$ ${String.format("%.2f",productList[position].productValue)}"

        if(productList[position].productDone){
            result = "Concluído"
        }else{
            result = "Aguardando"
        }
        holder.binding.txtStatusProduct.text = result

        holder.itemView.setOnClickListener {



        }

    }






    inner class ListProductViewHolder(val binding:ItemProductBinding):
        RecyclerView.ViewHolder(binding.root)



}