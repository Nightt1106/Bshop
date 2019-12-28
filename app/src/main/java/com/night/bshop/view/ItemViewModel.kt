package com.night.bshop.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.night.bshop.model.Item
import com.night.bshop.view.FirestoreQueryLiveData

//LiveData
class  ItemViewModel : ViewModel(){
    private  var items = MutableLiveData<List<Item>>()
    private  var fireStoreQueryLiveData =
        FirestoreQueryLiveData()

    fun getItems (): FirestoreQueryLiveData {
        return fireStoreQueryLiveData
    }

    fun setCategory(categoryId : String){
        fireStoreQueryLiveData.setCategory(categoryId)
    }

}