package com.example.mad_assignment.Customer_Fragments.cust_housekeeping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustHousekeepingModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Housekeeping Fragment"
    }
    val text: LiveData<String> = _text
}