package com.example.universaldietandhealthapp

class PIData(val currentuserid: String, val namesurname: String, val age: Int, var height: Double, var weight: Double){

    constructor() : this("", "", 0, 0.0, 0.0)
    {

    }
}