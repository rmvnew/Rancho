package com.example.rancho.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rancho.databinding.FragmentSettingsBinding
import com.example.rancho.util.DataStoreUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!
    private var confLight: Boolean? = null
    private var confSpeech: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        actions()
        checkSettings()

    }

    private fun checkSettings() {

        MainScope().launch {

            confLight = DataStoreUtil(requireContext()).readBoolean("light")
            confSpeech = DataStoreUtil(requireContext()).readBoolean("speech")

            binding.apply {
                swLight.isChecked = confLight == true
                swSpeech.isChecked = confSpeech == true
            }

        }

    }

    private fun actions() {

        binding.apply {

            swLight.setOnCheckedChangeListener { _, isChecked ->

                MainScope().launch {
                    if (isChecked) {
                        swLight.setText("Ligado")

                        DataStoreUtil(requireContext()).saveBoolean("light", true)

                    } else {
                        swLight.setText("Desligado")

                        DataStoreUtil(requireContext()).saveBoolean("light", false)

                    }
                }

            }

            swSpeech.setOnCheckedChangeListener { _, isChecked ->
                MainScope().launch {
                    if (isChecked) {
                        swSpeech.setText("Ligado")

                        DataStoreUtil(requireContext()).saveBoolean("speech", true)

                    } else {
                        swSpeech.setText("Desligado")

                        DataStoreUtil(requireContext()).saveBoolean("speech", false)

                    }
                }
            }

        }

    }


}