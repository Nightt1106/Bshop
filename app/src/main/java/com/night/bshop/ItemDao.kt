package com.night.bshop

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//Database access object
@Dao
interface ItemDao{
    @Query("select * from Item order by viewCount")
    fun getItems(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Item)
}