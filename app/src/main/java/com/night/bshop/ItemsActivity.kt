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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.night.bshop.model.Category
import com.night.bshop.model.Item
import com.night.bshop.view.ItemHolder
import com.night.bshop.view.ItemViewModel
import kotlinx.android.synthetic.main.activity_items.*



class ItemsActivity : AppCompatActivity() {

//    private lateinit var adapter :FirestoreRecyclerAdapter<Item,ItemHolder>
    private val TAG = ItemsActivity::class.java.simpleName
    private var categories = mutableListOf<Category>()
    private lateinit var adapter: ItemAdapter
    private lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        //setupAdapter()

        FirebaseFirestore.getInstance().collection("categories")
            .get().addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    task.result?.let {
                        categories.add(Category("", "不分類"))
                        for( doc in it){
                            categories.add(
                                Category(
                                    doc.id,
                                    doc.data["name"].toString()
                                )
                            )
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
                                position: Int,
                                p3: Long
                            ) {
                               //setupAdapter
                                itemViewModel.setCategory(categories[position].id)
                            }
                        }
                    }
                }
            }
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter(mutableListOf())
        recycler.adapter = adapter
        itemViewModel = ViewModelProviders.of(this)
            .get(ItemViewModel::class.java)
        itemViewModel.getItems().observe(this, androidx.lifecycle.Observer {
            Log.d(TAG,"observer: ${it.size}")

            adapter.items = it
            adapter.notifyDataSetChanged()
            /*list.forEach {
                ItemDatabase.getDatabase(this)?.getItemDao()
                    ?.addItem(it)
            }
            ItemDatabase.getDatabase(this)?.getItemDao()
                ?.getItems()
                ?.forEach {
                    Log.d(TAG,"Room  ${it.id} /${it.tittle}")
                }*/
        })
        //setupAapter
    }

    inner class ItemAdapter(var items: List<Item>) : RecyclerView.Adapter<ItemHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.bindTo(items.get(position))
            holder.itemView.setOnClickListener {
                itemClicked(items.get(position),position)
            }
        }
    }

    private fun itemClicked(item: Item, position: Int) {
        Log.d("itemClicked","${item.tittle} / $position")
        val intent = Intent(this,DetailActivity::class.java)
        intent.putExtra("ITEM", item)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}
