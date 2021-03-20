package com.example.mad_assignment.Staff_Fragments.staff_trans_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StaffTransHistoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Transaction History Fragment"
    }
    val text: LiveData<String> = _text
}