package com.example.mad_assignment.Customer.Customer_Fragments.cust_home

class Cust_Home_Item {
    var title:String? = null
    var img:Int? = null
    var des:String?=null
    var link:String?=null
    constructor(name:String, image:Int, des:String, link:String){
        this.title = name
        this.img = image
        this.des = des
        this.link = link
    }
}