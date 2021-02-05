package com.example.rancho.enum

enum class DaysOfWeek(val id:Int, val nameWeek : String, val businessDay:Boolean) {


    MONDAY(2,"segunda",true),
    TUESDAY(3,"Terça",true),
    WEDNESDAY(4,"Quarta",true),
    THURSDAY(5,"Quinta",true),
    FRIDAY(6,"Sexta",true),
    SATURDAY(7,"Sábado",false),
    SUNDAY(1,"Domindo",false)

}