package com.example.mad_assignment.Staff.Staff_Fragments.staff_manager.Main.Staff

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.staff_register_staff_dialog.view.*
import java.util.regex.Pattern

class StaffManagerFragment : Fragment() {

    private lateinit var etStaffName: EditText
    private lateinit var etStaffID: EditText
    private lateinit var etStaffEmail: EditText
    private lateinit var etStaffPassword: EditText
    private lateinit var etStaffPhoneNum: EditText
    private lateinit var imgUri: Uri
    private lateinit var ivStaffImg: ShapeableImageView
    private lateinit var imgURL: String
    private lateinit var uid: String
    private lateinit var accessRoom: CheckBox
    private lateinit var accessServicesFacilities: CheckBox
    private lateinit var accessHousekeeping: CheckBox
    private lateinit var accessCheckInOut: CheckBox

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.staff_fragment_manager, container, false)

        // Initialize the fragment
        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        ft.replace(R.id.framel_staff, StaffManagerOnlineFragment())
        ft.commit()

        val tvOnline: TextView = root.findViewById(R.id.tv_staff_online)
        val tvOffile: TextView = root.findViewById(R.id.tv_staff_offline)
        val etSearch: EditText = root.findViewById(R.id.et_manager_search)

        tvOnline.setOnClickListener {
            tvOnline.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvOnline.typeface = Typeface.DEFAULT_BOLD;

            tvOffile.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvOffile.typeface = Typeface.DEFAULT;

            etSearch.setText("")

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_staff, StaffManagerOnlineFragment())
            ft.commit()
        }

        tvOffile.setOnClickListener {
            tvOffile.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvOffile.typeface = Typeface.DEFAULT_BOLD;

            tvOnline.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            tvOnline.typeface = Typeface.DEFAULT;

            etSearch.setText("")

            val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            ft.replace(R.id.framel_staff, StaffManagerOfflineFragment())
            ft.commit()
        }

        root.setOnTouchListener { v, event ->
            etSearch.clearFocus()

            // Disable virtual k
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(etSearch.windowToken, 0)

            true
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegister: Button = view.findViewById(R.id.btn_register_staff)
        btnRegister.setOnClickListener {
            // Inflate the dialog
            val addDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.staff_register_staff_dialog, null)

            //Alert dialog builder
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(addDialogView)
                .setTitle("Register Staff")

            //show dialog
            val mAlertDialog = mBuilder.show()

            etStaffName = addDialogView.et_register_staff_name
            etStaffID = addDialogView.et_register_staff_id
            etStaffEmail = addDialogView.et_register_staff_email
            etStaffPassword = addDialogView.et_register_staff_password
            etStaffPhoneNum = addDialogView.et_register_staff_phoneNum
            ivStaffImg = addDialogView.iv_register_staff_img

            //Permission
            accessRoom = addDialogView.cb_manage_room
            accessServicesFacilities = addDialogView.cb_manage_services_facilities
            accessHousekeeping = addDialogView.cb_manage_housekeeping
            accessCheckInOut = addDialogView.cb_manage_check_in_out

            ivStaffImg.setOnClickListener {
                Toast.makeText(requireContext(), "Opening Gallery", Toast.LENGTH_SHORT).show()

                val i = Intent()
                i.type = "image/*"
                i.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
            }

            //Button Action
            addDialogView.btn_register_cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }

            addDialogView.btn_register_staff.setOnClickListener {
                if(etStaffName.text.toString() != "" && etStaffID.text.toString() != "" &&
                    etStaffEmail.text.toString() != "" && etStaffPassword.text.toString() != "" &&
                    etStaffPhoneNum.text.toString() != ""){

                    if(accessRoom.isChecked || accessServicesFacilities.isChecked ||
                        accessHousekeeping.isChecked || accessCheckInOut.isChecked){

                            if(validateEmailFormat(etStaffEmail.text.toString())){

                                val oriMap = drawableToBitamp(ivStaffImg.drawable!!)
                                val d = ContextCompat.getDrawable(requireContext(), R.drawable.ic_outline_plus);
                                val compareMap = drawableToBitamp(d!!)
                                
                                if(!oriMap!!.sameAs(compareMap)){
                                    mAlertDialog.dismiss()

                                    registerStaff(etStaffEmail.text.toString(), etStaffPassword.text.toString())

                                    Toast.makeText(requireActivity(), "Register Success", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(requireActivity(), "Please Select Image", Toast.LENGTH_SHORT).show()
                                }
                            }

                    }else{
                        Toast.makeText(requireActivity(), "Permisison Required", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(requireActivity(), "Invalid Input", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            imgUri = data.data!!

            ivStaffImg.setImageURI(imgUri)
        }
    }

    private fun validateEmailFormat(email: String): Boolean {
        val emailREG = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +"\\." +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +")+"
        val PATTERN: Pattern = Pattern.compile(emailREG)

        fun CharSequence.isEmail() : Boolean = PATTERN.matcher(this).find()

        //validate email format
        if(!(email.isEmail())){
            Toast.makeText(requireContext(), "Invalid Email", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun registerStaff(email: String, password: String) {
        val role = "Staff"
        val name = etStaffName.text.toString()
        val id = etStaffID.text.toString()
        val phoneNum = etStaffPhoneNum.text.toString()
        val roomPermisison = accessRoom.isChecked
        val servicesFacilitiesPermisison = accessServicesFacilities.isChecked
        val housekeepingPermisison = accessHousekeeping.isChecked
        val checkInOutPermisison = accessCheckInOut.isChecked

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Register", "Successfully created user with uid: ${it.result?.user?.uid}")
                uid = it.result?.user?.uid.toString()

                createUser(name, password, phoneNum, email, uid)

                registerStaffInformation(name, id, email, password, phoneNum, role,
                        roomPermisison, servicesFacilitiesPermisison, housekeepingPermisison, checkInOutPermisison, uid)
            }
    }

    private fun createUser(name:String, password:String, phoneNum:String, email:String, uid:String){

        val userRef = FirebaseDatabase.getInstance().getReference("/User/$uid")

        val role = "Staff"
        val user = User (name, uid, password, phoneNum, email, role, "")

        userRef.setValue(user)
    }

    private fun registerStaffInformation(name: String, id: String, email: String, password: String, phoneNum: String, role: String,
                                         roomPermisison: Boolean, servicesFacilitiesPermisison: Boolean,
                                         housekeepingPermisison: Boolean, checkInOutPermisison: Boolean, uid: String){

        val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("staff/$name")

        imageRef.putFile(imgUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    imgURL = it.toString()

                    val staffRef = FirebaseDatabase.getInstance().getReference("/Staff/")

                    val staff = Staff(name, id, email, password, phoneNum, imgURL, role,
                        "Offline", roomPermisison, servicesFacilitiesPermisison, housekeepingPermisison, checkInOutPermisison, uid)

                    staffRef.child("$name - $uid").setValue(staff)
                }
            }
            .addOnFailureListener{
                Toast.makeText(activity, "Fail to Upload Image", Toast.LENGTH_SHORT).show()
            }

    }

    private fun drawableToBitamp(drawable: Drawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }
}