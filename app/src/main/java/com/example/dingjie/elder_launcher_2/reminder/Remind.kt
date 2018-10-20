package com.example.dingjie.elder_launcher_2.reminder

class Remind {
    var thing = ""
    var hour = 0
    var min = 0
    var year = 2000
    var month = 1
    var day = 1
    constructor(thing : String , hour : Int, min : Int, year : Int, month : Int, day : Int){
        this.thing = thing
        this.hour = hour
        this.min = min
        this.year = year
        this.month = month
        this.day = day
    }
}