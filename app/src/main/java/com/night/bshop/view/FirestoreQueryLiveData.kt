package com.night.bshop.view

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import com.night.bshop.model.Item

class FirestoreQueryLiveData : LiveData<List<Item>>(), EventListener<QuerySnapshot> {

    lateinit var registation : ListenerRegistration

    var query = FirebaseFirestore.getInstance()
        .collection("items")
        .orderBy("viewCount", Query.Direction.DESCENDING)
        .limit(10)

    var isRegistration = false

    override fun onActive() {
        registation = query.addSnapshotListener(this )
        isRegistration = true
    }

    override fun onInactive() {
        super.onInactive()
        if(isRegistration){
            registation.remove()
        }
    }

    override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if(querySnapshot != null &&  !querySnapshot.isEmpty){
            val list = mutableListOf<Item>()
            for (doc in querySnapshot.documents){
                val item = doc.toObject(Item::class.java)?: Item()
                item.id = doc.id
                list.add(item)
            }
            value = list
        }
    }

    fun setCategory(categoryId: String) {
        if(isRegistration){
            registation.remove()
            isRegistration = false
        }
        if(categoryId.length > 0){
            query = FirebaseFirestore.getInstance()
                .collection("items")
                .whereEqualTo("category",categoryId)
                .orderBy("viewCount",Query.Direction.DESCENDING)
                .limit(10)
        } else {
            query = FirebaseFirestore.getInstance()
                .collection("items")
                .orderBy("viewCount",Query.Direction.DESCENDING)
                .limit(10)
        }
        registation = query.addSnapshotListener(this)
        isRegistration = true
    }
}