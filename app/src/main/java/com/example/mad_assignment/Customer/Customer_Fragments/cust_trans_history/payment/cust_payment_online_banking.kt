package com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Customer.Booking.Class.Reservation
import com.example.mad_assignment.Customer.Customer_Fragments.cust_trans_history.payment.model.Payment
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cust_payment_online_banking.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class cust_payment_online_banking : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE: Int = 200
        var currentUser: User? = null
        var invoiceID: String = ""
        var pageHeight = 1120
        var pagewidth = 792
        var bmp: Bitmap? = null
        var scaledbmp: Bitmap? = null
        var elementCount: Int = 0
        var currentReserved: Reservation? = null
        var totalPrice: String? = ""
        var paymentMethod = ""
        var paidBy = "Online Banking"
    }

    lateinit var selectedReservationDataList: ArrayList<Reservation>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_payment_online_banking)

        fetchCurrentReservationData()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun fetchCurrentReservationData() {

        val currentReservation: String? = intent.getStringExtra("reservedID")
        Log.d("ReservationID", "$currentReservation")
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Reservation/$uid/$currentReservation")

        ref.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                currentReserved = snapshot.getValue(Reservation::class.java)

                totalPrice = String.format("%.2f", currentReserved?.totalPrice)
                assignDataIntoText()
                Log.d("PaymentReserve", "Current Data $totalPrice")


                callFunctionDirect()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

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
    private fun callFunctionDirect() {
        bmp = BitmapFactory.decodeResource(resources, R.drawable.quadcorehms)
        scaledbmp = Bitmap.createScaledBitmap(bmp!!, 140, 140, false)

        fetchCurrentUser()

        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        assignInvoiceID()




        btnPayNow.setOnClickListener {

            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setTitle("Confirm before purchase")
            alertBuilder.setMessage(
                    """
                                Customer Name: ${currentUser?.name.toString()}
                                Date Reserved: ${currentReserved?.dateReserved}
                                Reservation Details:
                                Check In Date: ${currentReserved?.checkInDate}
                                Check Out Date:  ${currentReserved?.checkOutDate}
                                No of Guest:  ${currentReserved?.guest}
                                No of Nights: ${currentReserved?.nights}
                                Total Price: RM $totalPrice
                                Payment Method: $paidBy
                                """.trimIndent()
            )
            alertBuilder.setPositiveButton(
                    "Confirm"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()

                savePaymentToFirebaseDatabase("Success")

                Toast.makeText(this, "Thank you for purchase", Toast.LENGTH_LONG).show()

                generatePDF(invoiceID)
                val uid = FirebaseAuth.getInstance().uid
                val ref = FirebaseDatabase.getInstance().getReference("Reservation/$uid/${currentReserved!!.reservationID}")
                val sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE)
                val reservation = Reservation(
                        currentReserved!!.reservationID,
                        uid,
                        currentReserved!!.custName,
                        currentReserved!!.custImg,
                        currentReserved!!.guest,
                        convertLongToDate1(sharedPreferences.getLong("startDate", 0)),
                        convertLongToDate1(sharedPreferences.getLong("endDate", 0)),
                        currentReserved!!.nights,
                        currentReserved!!.breakfast,
                        currentReserved!!.reservationDetail,
                        currentReserved!!.totalPrice,
                        "paid",
                        currentReserved!!.dateReserved,
                )

                ref.setValue(reservation)
                        .addOnSuccessListener {
                            Log.d("confirm book", "Successfully Payment")
                        }
                        .addOnFailureListener{
                            Log.d("confirm book", "Fail Payment")
                        }



                val intent = Intent(this, cust_payment_transaction_details::class.java)
                intent.putExtra("reservedID", currentReserved!!.reservationID)
                checkBoxselected()
                intent.putExtra("paymentMethod", paymentMethod)
                startActivity(intent)
            }
            alertBuilder.setNegativeButton(
                    "Cancel"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                savePaymentToFirebaseDatabase("Fail")
                Toast.makeText(this, "Payment has been Cancel", Toast.LENGTH_LONG).show()
                val intent = Intent(this, cust_payment_transaction_details::class.java)
                intent.putExtra("reservedID", currentReserved!!.reservationID)
                checkBoxselected()
                intent.putExtra("paymentMethod", paymentMethod)
                startActivity(intent)
            }
            val alertDialog = alertBuilder.create()
            alertDialog.show()




        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun savePaymentToFirebaseDatabase(status: String) {

        val uid = FirebaseAuth.getInstance().uid ?: ""

        val ref = FirebaseDatabase.getInstance().getReference("/Payment/$uid/${invoiceID}")

        when {
            rbpbe.isChecked -> {
                paymentMethod = "Public Bank"
            }
            rbMaybank.isChecked -> {
                paymentMethod = "Maybank"
            }
            rbCIMB.isChecked -> {
                paymentMethod = "CIMB Bank"
            }
            rbOthers.isChecked -> {
                paymentMethod = "Other Bank"
            }
        }

        val totalPayment = currentReserved!!.totalPrice.toString()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        val payment = Payment(invoiceID, currentUser?.name!!, formatted.toString(), totalPayment, paymentMethod, status)


        ref.setValue(payment).addOnSuccessListener {
            Log.d("payment", "Finally we saved the payment to Firebase Database")
        }.addOnFailureListener {
            Log.d("payment", "Failed to set value to database: ${it.message}")
        }
    }

    private fun assignDataIntoText() {
        val passText = "RM $totalPrice"

        tvSubtotalValueOnlineBanking.text = passText
        tvTotalAmountValueOnlineBanking.text = passText
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToDate1(date: Long?): String {
        val format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
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

    private fun assignInvoiceID(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Payment/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                elementCount = snapshot.childrenCount.toInt()
                val count = snapshot.childrenCount + 10000
                invoiceID = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })



    }

    private fun generatePDF(invoiceID: String) {
        val myPdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val forLinePaint = Paint()
        val myPageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = myPdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas
        var amountIndexY = 0f
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
        canvas.drawText(currentUser!!.name, 59f, 310f, paint)

        //Email information
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        val emailColor = ContextCompat.getColor(this, R.color.black)
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC)
        paint.color = emailColor
        canvas.drawText(currentUser!!.email, 138f, 340f, paint)


        //Invoice No information
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
        canvas.drawText("INV $invoiceID", 733f, 280f, paint)


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
        canvas.drawText(currentReserved!!.dateReserved.toString(), 733f, 310f, paint)


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
        canvas.drawText(paymentMethod, 733f, 340f, paint)

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
        canvas.drawText("PAID", 733f, 370f, paint)

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



        val countItem = currentReserved!!.reservationDetail!!.size.minus(1)

        var indexPositionY1 = 500f
        var indexPositionY2 = 550f
        for (i in 0..countItem){

            //QTY information data
            paint.textAlign = Paint.Align.LEFT
            paint.textSize = 20f
            paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            paint.color = nameColor
            canvas.drawText(currentReserved!!.reservationDetail!![i].qty.toString(), 70f, indexPositionY1, paint)

            //DESC information data
            paint.textAlign = Paint.Align.LEFT
            paint.textSize = 20f
            paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            paint.color = nameColor
            canvas.drawText(currentReserved!!.reservationDetail!![i].roomType!!.roomType.toString(), 150f, indexPositionY1, paint)

            //AMOUNT information data
            paint.textAlign = Paint.Align.RIGHT
            paint.textSize = 20f
            paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            paint.color = nameColor
            val priceText = "RM ${currentReserved!!.reservationDetail?.get(i)!!.subtotal!!.format(2)}"
            canvas.drawText(priceText, 733f, indexPositionY1, paint)



            indexPositionY1 += 25f
            indexPositionY2 += 25f

            amountIndexY = indexPositionY2



        }

        val letextIndex = amountIndexY + 50f
        //TOTAL AMOUNT information data
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        val subTotalText = "RM ${currentReserved!!.totalPrice!!.format(2)}"
        canvas.drawText(subTotalText, 733f, amountIndexY, paint)


        //TOTAL AMOUNT information title
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        paint.color = invoiceColor
        canvas.drawText("AMOUNT", 550f, amountIndexY, paint)

        //Break line between content and header
        forLinePaint.style = Paint.Style.FILL_AND_STROKE
        forLinePaint.color = forLinePaintColor
        forLinePaint.pathEffect = DashPathEffect(floatArrayOf(0f, 0f), 0F)
        forLinePaint.strokeWidth = 2f

        canvas.drawLine(59f, letextIndex, 733f, letextIndex, forLinePaint)

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

    private fun checkBoxselected(){
        when {
            rbpbe.isChecked -> {
                paymentMethod = "Public Bank"
            }
            rbMaybank.isChecked -> {
                paymentMethod = "Maybank"
            }
            rbCIMB.isChecked -> {
                paymentMethod = "CIMB Bank"
            }
            rbOthers.isChecked -> {
                paymentMethod = "Other Bank"
            }
        }
    }
}


