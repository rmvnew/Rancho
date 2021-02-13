package com.example.rancho.util

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import java.util.*

enum class Language{
    SPANISH, PORTUGUESE,US
}

class SpeechManager(val context: Context) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null
    private var language: Language = Language.PORTUGUESE

    fun init(language: Language){
        this.language = language
        textToSpeech = TextToSpeech(context, this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speechToText(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


    override fun onInit(p0: Int) {
        when (p0) {
            TextToSpeech.SUCCESS -> {
                when(language){
                    Language.PORTUGUESE ->  textToSpeech?.language = Locale("pt", "BR")
                    Language.SPANISH -> textToSpeech?.language =  Locale("es", "ES")
                    Language.US -> textToSpeech?.language =  Locale("en", "US")
                }

            }
        }
    }

    fun stopSpeech() {
        textToSpeech?.apply {
            stop()
        }
    }

    fun shutdownSpeech(){
        textToSpeech?.apply {
            shutdown()
        }
    }

    fun speech(){
        textToSpeech?.apply {
            isSpeaking
        }
    }
}