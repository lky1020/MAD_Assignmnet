package com.example.mad_assignment.Customer_Fragments.cust_facilities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustFacilitiesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Facilities Fragment"
    }
    val text: LiveData<String> = _text
}