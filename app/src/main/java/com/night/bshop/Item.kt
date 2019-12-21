package com.night.bshop

import android.widget.ImageView

data class Item(var tittle:String, var price : Int,var imageUrl :String){
    constructor() : this("",0,"")
}