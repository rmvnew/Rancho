package com.example.rancho

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.rancho.databinding.ActivityMainBinding
import com.orhanobut.hawk.Hawk

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("InvalidWakeLockTag", "WakelockTimeout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        Hawk.init(this).build()

       window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
      // window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(binding.root)
    }


}