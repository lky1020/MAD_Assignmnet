package com.example.mad_assignment.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogoutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Click Logout button to logout out"
    }
    val text: LiveData<String> = _text
}