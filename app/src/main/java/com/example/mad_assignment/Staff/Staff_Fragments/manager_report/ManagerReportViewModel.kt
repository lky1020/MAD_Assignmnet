package com.example.mad_assignment.Staff_Fragments.manager_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ManagerReportViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Report Fragment"
    }
    val text: LiveData<String> = _text
}