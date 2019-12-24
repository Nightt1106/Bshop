package com.night.bshop

data class Category(var id :String,var name : String){
    override fun toString(): String {
        return name
    }
}