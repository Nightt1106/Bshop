package com.night.bshop

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row.view.*


class ItemHolder(view: View) : RecyclerView.ViewHolder(view){

    var tittleText : TextView = view.item_tittle
    var priceText : TextView = view.item_price
    var image = view.item_image
    var viewCount = view.item_view_count

    fun bindTo(item: Item) {
        tittleText.text = item.tittle
        priceText.text = item.price.toString()
        Glide.with(itemView.context)
            .load(item.imageUrl)
            .apply(RequestOptions().override(400))
            .into(image)
        viewCount.text = item.viewCount.toString()
    }
}