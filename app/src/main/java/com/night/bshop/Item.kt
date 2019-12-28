package com.night.bshop

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
@Entity
data class Item(
    var tittle:String,
    var price : Int,
    var imageUrl :String,
    //@PrimaryKey not null
    @PrimaryKey
    @get:Exclude var id : String,
    var category : String,
    var content : String?,
    var viewCount:Int) : Parcelable{
    constructor() : this("",0,"","","","",0)
}