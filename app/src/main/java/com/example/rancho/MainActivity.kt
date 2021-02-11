package com.example.rancho

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.rancho.databinding.ActivityMainBinding
import com.orhanobut.hawk.Hawk

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        Hawk.init(this).build()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        setContentView(binding.root)
    }


}