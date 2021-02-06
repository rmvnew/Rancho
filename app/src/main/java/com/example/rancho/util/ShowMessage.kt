package dominando.android.testeproduct.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object ShowMessage {

    fun showToast(text:String,context: Context){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }

    fun showSnack(text: String,seconds:Int,view:View){
        Snackbar.make(view,text,seconds).show()
    }

}