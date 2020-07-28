package com.example.universaldietandhealthapp

class MealData (val date: String, val mealname: String, val mealcalories: Int, val mealfat: Int, val mealcarbs: Int) {

    constructor() : this("", "", 0, 0, 0)
    {

    }
}