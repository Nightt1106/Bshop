package com.night.bshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_items.*
import java.util.*

class ItemsActivity : AppCompatActivity() {

    private lateinit var adapter :FirestoreRecyclerAdapter<Item,ItemHolder>
    private var categories = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        setupAdapter()

        FirebaseFirestore.getInstance().collection("categories")
            .get().addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    task.result?.let {
                        categories.add(Category("","不分類"))
                        for( doc in it){
                            categories.add(Category(doc.id, doc.data["name"].toString()))
                        }
                        spinner.adapter = ArrayAdapter<Category>(
                            this,
                            android.R.layout.simple_spinner_item,
                            categories)
                            .apply {
                                setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                            }
                        spinner.setSelection(0,false)
                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                setupAdapter()
                            }
                        }
                    }
                }
            }



    }

    private fun setupAdapter() {

        val selected = spinner.selectedItemPosition

        // if(query value change) restart listening

        var query = if(selected > 0){
            adapter.stopListening()
            FirebaseFirestore.getInstance()
                .collection("items")
                .whereEqualTo("category" ,categories[selected].id)
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        } else {
            FirebaseFirestore.getInstance()
                .collection("items")
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }



        val options: FirestoreRecyclerOptions<Item> = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query, Item::class.java)
            .build()

        adapter = object : FirestoreRecyclerAdapter<Item, ItemHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row, parent, false)
                return ItemHolder(view)
            }

            override fun onBindViewHolder(holder: ItemHolder, position: Int, item: Item) {
                //*****
                item.id = snapshots.getSnapshot(position).id

                holder.bindTo(item)
                holder.itemView.setOnClickListener {
                    itemClicked(item, position)
                }
            }

        }
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.startListening()
    }

    private fun itemClicked(item: Item, position: Int) {
        Log.d("itemClicked","${item.tittle} / $position")
        val intent = Intent(this,DetailActivity::class.java)
        intent.putExtra("ITEM", item)
        startActivity(intent)
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
