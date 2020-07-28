package com.example.universaldietandhealthapp

class CalendarData (val date: String, var dailyweightchange: Double, val waterintake: String, val calorieintake: Double) {

    constructor() : this("", 0.0, "", 0.0)
    {

    }
}