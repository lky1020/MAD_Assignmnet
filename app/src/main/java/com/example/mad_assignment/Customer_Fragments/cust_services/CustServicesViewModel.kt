package com.example.mad_assignment.Customer_Fragments.cust_services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustServicesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Services Fragment"
    }
    val text: LiveData<String> = _text
}