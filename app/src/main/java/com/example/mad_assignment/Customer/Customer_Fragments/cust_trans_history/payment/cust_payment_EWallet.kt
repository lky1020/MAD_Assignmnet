package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model.Payment
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cust_payment__e_wallet.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class cust_payment_EWallet : AppCompatActivity() {

    companion object {
        val PERMISSION_REQUEST_CODE: Int = 200
        var currentUser: User? = null
        var invoiceID: String = ""
        var pageHeight = 1120
        var pagewidth = 792
        var bmp: Bitmap? = null
        var scaledbmp: Bitmap? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_payment__e_wallet)

        bmp = BitmapFactory.decodeResource(resources, R.drawable.quadcorehms)
        scaledbmp = Bitmap.createScaledBitmap(bmp!!, 140, 140, false)

        fetchCurrentUser()

        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnPayNowEWallet.setOnClickListener {

            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setTitle("Confirm before purchase")
            alertBuilder.setMessage(
                    """
                                Customer Name: ${currentUser?.name.toString()}
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




        }

    }

    private fun generatePDF(invoiceID: String) {
        val myPdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val forLinePaint = Paint()
        val myPageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = myPdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas

        //Company Logo in PDF
        canvas.drawBitmap(scaledbmp!!, 600f, 40f, paint)

        //Company Name in PDF
        title.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        val color = ContextCompat.getColor(this, R.color.blue)
        title.textSize = 32f
        title.color = color
        title.letterSpacing = 0.02f
        canvas.drawText("QUAD CORE HOTEL MANAGEMENT", 59f, 70f, title)

        //Company address in PDF
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        val paintColor = ContextCompat.getColor(this, R.color.black)
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = paintColor
        canvas.drawText("165, Jalan Ampang,", 59f, 140f, paint)
        canvas.drawText("50450 Kuala Lumpur", 59f, 160f, paint)
        canvas.drawText("Wilayah Persekutuan Kuala Lumpur", 59f, 180f, paint)


        //BILL TO information
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 22f
        val billColor = ContextCompat.getColor(this, R.color.blue)
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = billColor
        canvas.drawText("BILL TO", 59f, 280f, paint)

        //username information
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        val nameColor = ContextCompat.getColor(this, R.color.black)
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = nameColor
        canvas.drawText("Joan Hau", 59f, 310f, paint)

        //Email information
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        val emailColor = ContextCompat.getColor(this, R.color.black)
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC)
        paint.color = emailColor
        canvas.drawText("lky1020@gmail.com", 138f, 340f, paint)


        //Invoive No information
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 22f
        val invoiceColor = ContextCompat.getColor(this, R.color.blue)
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("INVOICE #", 360f, 280f, paint)

        //Invoice No information data
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = nameColor
        canvas.drawText("12356456462131", 733f, 280f, paint)


        //receipt date information
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("RECEIPT DATE", 360f, 310f, paint)

        //receipt date information data
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = nameColor
        canvas.drawText("15/02/2021", 733f, 310f, paint)


        //PAYMENT METHOD information
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("PAYMENT METHOD", 360f, 340f, paint)

        //PAYMENT METHOD information data
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = nameColor
        canvas.drawText("Credit Card", 733f, 340f, paint)

        //STATUS information
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("STATUS", 360f, 370f, paint)

        //STATUS information data
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = nameColor
        canvas.drawText("SUCCESSFUL", 733f, 370f, paint)

        //Break line between content and header
        forLinePaint.style = Paint.Style.FILL_AND_STROKE
        val forLinePaintColor = ContextCompat.getColor(this, R.color.pink)
        forLinePaint.color = forLinePaintColor
        forLinePaint.pathEffect = DashPathEffect(floatArrayOf(0f, 0f), 0F)
        forLinePaint.strokeWidth = 2f

        canvas.drawLine(59f, 420f, 733f, 420f, forLinePaint)

        //title for each of the content
        //QTY information title
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("QTY", 59f, 450f, paint)

        //DESCRIPTION information title
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("DESCRIPTION", 230f, 450f, paint)

        //AMOUNT information title
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("AMOUNT", 733f, 450f, paint)

        //Break line between content and header
        canvas.drawLine(59f, 470f, 733f, 470f, forLinePaint)

        //QTY information data
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = nameColor
        canvas.drawText("1", 70f, 500f, paint)

        //DESC information data
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = nameColor
        canvas.drawText("faejgkskdgbjldsflgs", 150f, 500f, paint)

        //AMOUNT information data
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        paint.color = nameColor
        canvas.drawText("289.00", 733f, 500f, paint)

        //TOTAL AMOUNT information title
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("AMOUNT", 550f, 550f, paint)

        //TOTAL AMOUNT information data
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("RM 1000.00", 733f, 550f, paint)

        //THANK YOU
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 40f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD_ITALIC)
        paint.color = invoiceColor
        canvas.drawText("THANK YOU", 650f, 1000f, paint)


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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun savePaymentToFirebaseDatabase(){

        val uid = FirebaseAuth.getInstance().uid ?: ""
        invoiceID = UUID.randomUUID().toString()


        val ref = FirebaseDatabase.getInstance().getReference("/Payment/$uid/${invoiceID}")

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


    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
}