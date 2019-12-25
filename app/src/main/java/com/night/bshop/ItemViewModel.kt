package com.night.bshop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//LiveData
class  ItemViewModel : ViewModel(){
    private  var items = MutableLiveData<List<Item>>()
    private  var fireStoreQueryLiveData = FirestoreQueryLiveData()

    fun getItems (): FirestoreQueryLiveData {
        return fireStoreQueryLiveData
    }

    fun setCategory(categoryId : String){
        fireStoreQueryLiveData.setCategory(categoryId)
    }

}