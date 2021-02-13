package com.example.rancho.util

object PluralWordsUtil {

    fun setStringPlural(word:String):String{

        val letterOne = word.substring(word.length-1)
        val letterTwo = word.substring(word.length-2)
        val letterThree = word.substring(word.length-3)
        var newWord = ""


        if(word.equals("pão")){
            newWord = "pães"
        }else if(letterTwo == "ão"){
            newWord = word.substring(0,word.length-2)+"ões"
        }else if(letterOne == "a" || letterOne == "e" || letterOne == "i" || letterOne == "o" || letterOne == "u"){
            newWord = word+"s"
        }else if(letterOne == "r"||letterOne == "z"||letterOne == "s"){
            newWord = word+"es"
        }else if(letterTwo == "al"||letterTwo == "el"||letterTwo == "ol"||letterTwo == "ul"){
            newWord = word.substring(0,word.length-1)+"is"
        }else if(letterTwo == "il"){
            newWord = word.substring(0,word.length-1)+"s"
        }else if(letterOne == "m"|| letterOne == "n"){
            newWord = word.substring(0,word.length-1)+"ns"
        }


        return newWord
    }

}