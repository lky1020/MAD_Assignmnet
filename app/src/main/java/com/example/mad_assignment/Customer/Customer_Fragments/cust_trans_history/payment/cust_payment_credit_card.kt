package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.braintreepayments.cardform.view.CardForm
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model.Payment
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class cust_payment_credit_card : AppCompatActivity() {

    companion object {
        val PERMISSION_REQUEST_CODE: Int = 200
        var currentUser: User? = null
        var invoiceID: String = ""
        var pageHeight = 1120
        var pagewidth = 792
        var bmp: Bitmap? = null
        var scaledbmp:Bitmap? = null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_payment_credit_card)

        bmp = BitmapFactory.decodeResource(resources, R.drawable.quadcorehms)
        scaledbmp = Bitmap.createScaledBitmap(bmp!!, 140, 140, false)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fetchCurrentUser()

        // checking for permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
        val cardForm = findViewById<CardForm>(R.id.card_form)
        val buy: Button = findViewById(R.id.btnBuy)

        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .postalCodeRequired(true)
            .mobileNumberRequired(true)
            .mobileNumberExplanation("SMS is required on this number")
            .setup(this)

        cardForm.cvvEditText.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD



        buy.setOnClickListener {

            if (cardForm.isValid()) {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setTitle("Confirm before purchase")
                alertBuilder.setMessage(
                        """
                                Card number: ${cardForm.cardNumber}
                                Card expiry date: ${cardForm.expirationDateEditText.text.toString()}
                                Card CVV: ${cardForm.cvv}
                                Postal code: ${cardForm.postalCode}
                                Phone number: ${cardForm.mobileNumber}
                                """.trimIndent()
                )
                alertBuilder.setPositiveButton(
                        "Confirm"
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()

                    savePaymentToFirebaseDatabase()

                    Toast.makeText(this, "Thank you for purchase", Toast.LENGTH_LONG).show()

                    generatePDF(invoiceID)
                    val intent = Intent(this, cust_payment_transaction_details::class.java)
                    startActivity(intent)
                }
                alertBuilder.setNegativeButton(
                        "Cancel"
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                val alertDialog = alertBuilder.create()
                alertDialog.show()
            } else {
                Toast.makeText(this, "Please complete the form", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun generatePDF(invoiceID: String) {
        val myPdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val forLinePaint = Paint()
        val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = myPdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas

        canvas.drawBitmap(scaledbmp!!, 56f, 40f, paint)
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.textSize = 25f
        paint.textSize = 20f
        title.textAlign = Paint.Align.RIGHT
        canvas.drawText("QUAD CORE HOTEL MANAGEMENT",400f,100f,title)
        title.textAlign = Paint.Align.LEFT
        canvas.drawText("165, Jalan Ampang, Kuala Lumpur", 209f,120f,title)
        canvas.drawText("50450 Kuala Lumpur", 209f,140f,title)
        canvas.drawText("Wilayah Persekutuan Kuala Lumpur", 209f,160f,title)

        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        title.textSize = 15.5f

        title.textAlign = Paint.Align.CENTER
        canvas.drawText("THANK YOU", 396f, 560f, title);





        myPdfDocument.finishPage(myPage)
        val file = File(Environment.getExternalStorageDirectory(), "QUAD CORE RECEIPT$invoiceID.pdf")

        try {
            myPdfDocument.writeTo(FileOutputStream(file))
            Log.d("Receipt", "Successfully Store the PDF file in local folder")

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        myPdfDocument.close()
    }

    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 = ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
        val permission2 = ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d("payment", "Current User ${currentUser?.name}")
                Log.d("payment", "Current User ${currentUser?.uid}")

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun savePaymentToFirebaseDatabase(){

        val uid = FirebaseAuth.getInstance().uid ?: ""
        invoiceID = UUID.randomUUID().toString()


        val ref = FirebaseDatabase.getInstance().getReference("/Payment/$uid/$invoiceID")

        val totalPayment = "2000"
        val paymentMethod = "Credit Card"
        val status = "Success"

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)


        val payment = Payment(invoiceID, currentUser?.name!!, formatted.toString(), totalPayment, paymentMethod, status)



        ref.setValue(payment)
                .addOnSuccessListener {
                    Log.d("payment", "Finally we saved the payment to Firebase Database")
                }
                .addOnFailureListener {
                    Log.d("payment", "Failed to set value to database: ${it.message}")
                }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

