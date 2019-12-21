package com.night.bshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_items.*

class ItemsActivity : AppCompatActivity() {

    private lateinit var adapter :FirestoreRecyclerAdapter<Item,ItemHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)

        val query = FirebaseFirestore.getInstance()
            .collection("items")
            .limit(10)

        val options : FirestoreRecyclerOptions<Item> = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query,Item::class.java)
            .build()

        adapter = object : FirestoreRecyclerAdapter<Item,ItemHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
                val view= LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row,parent,false)
                return ItemHolder(view)
            }

            override fun onBindViewHolder(holder: ItemHolder, position: Int, item: Item) {
                holder.bindTo(item)
                holder.itemView.setOnClickListener {
                    itemClicked(item,position)
                }
            }

        }

        recycler.adapter = adapter

        Log.d("onCreate","$query")
    }

    private fun itemClicked(item: Item, position: Int) {
        Log.d("itemClicked","${item.tittle} / $position")
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
