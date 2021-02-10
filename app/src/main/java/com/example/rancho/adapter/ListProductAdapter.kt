package com.example.rancho.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.rancho.R
import com.example.rancho.databinding.ItemProductBinding
import com.example.rancho.model.Product
import com.example.rancho.ui.product.ListProductFragmentDirections
import java.text.NumberFormat

class ListProductAdapter() : RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder>() {
    var context: Context? = null

    private var productList: List<Product> = emptyList()

    fun setProductList(list: List<Product>,context: Context) {
        this.productList = list
        this.context = context
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListProductViewHolder(binding)
    }

    override fun getItemCount() = productList.size



    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ListProductViewHolder, position: Int) {

        var result = ""

        holder.binding.txtNameProduct.text = productList[position].productName
        holder.binding.txtQuantity.text = productList[position].productQuantity.toString()
        holder.binding.txtValue.text =
            "R$ ${String.format("%.2f", productList[position].productValue)}"

        if (productList[position].productDone) {
            result = "Concluído"
            holder.binding.cardStatusProduct.backgroundTintList =
                ContextCompat.getColorStateList(context!!,R.color.AZUL_STEEL_BLUE2)
           // holder.binding.txtStatusProduct.setTextColor(R.color.PRETO)
        } else {
            result = "Aguardando"
        }
        holder.binding.txtStatusProduct.text = result

        holder.itemView.setOnClickListener {

            val action =
                ListProductFragmentDirections.actionListProductFragmentToAddProductFragment(
                    productList[position]
                )
            Navigation.findNavController(it).navigate(action)

        }

    }


    inner class ListProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)


}