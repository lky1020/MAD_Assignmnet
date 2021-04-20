package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model


class Payment(val invoiceID:String, val name:String, val paidDateTime:String, val totalPayment:String, val paymentMethod:String, val status:String, val uid:String, val reserveID: String) {
    constructor() : this("", "", "", "", "", "", "", "")
}
