package com.example.mad_assignment.Customer_Fragments.cust_housekeeping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mad_assignment.Customer.Customer_Fragments.cust_housekeeping.Class.Housekeeping

class CustHousekeepingModel() : ViewModel() {

    private val _housekeeping = MutableLiveData<ArrayList<Housekeeping>>()
    private val housekeepingList = ArrayList<Housekeeping>()

    val housekeeping : LiveData<ArrayList<Housekeeping>>
        get()= _housekeeping

    init{
        housekeepingList.add(Housekeeping("Title1", "Description"))
        housekeepingList.add(Housekeeping("Title2", "Description"))
        housekeepingList.add(Housekeeping("Title3", "Description"))
        housekeepingList.add(Housekeeping("Title4", "Description"))
        housekeepingList.add(Housekeeping("Title5", "Description"))
        housekeepingList.add(Housekeeping("Title6", "Description"))
        housekeepingList.add(Housekeeping("Title7", "Description"))
        housekeepingList.add(Housekeeping("Title8", "Description"))

        _housekeeping.value = housekeepingList
    }

    fun getHousekeepingList(): MutableLiveData<ArrayList<Housekeeping>> {
        return _housekeeping
    }
}