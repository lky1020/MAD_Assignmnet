package com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Profile

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mad_assignment.Class.Staff
import com.example.mad_assignment.Class.User
import com.example.mad_assignment.Cust_Staff_Shared.Cust_Staff_Fragments.Login
import com.example.mad_assignment.Customer.Cust_Staff_Fragments.logout.LogoutFragment
import com.example.mad_assignment.MainActivity
import com.example.mad_assignment.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.popout_getinfo.view.*
import kotlinx.android.synthetic.main.popout_getname.view.*
import kotlinx.android.synthetic.main.popout_getname.view.iv_close
import java.util.*
import java.util.regex.Pattern


//belongs to fragment_profile.xml
class ProfileFragment : Fragment() {

    lateinit var iv_profile: CircleImageView
    lateinit var user_name:TextView
    lateinit var user_email:TextView
    lateinit var user_password:TextView
    lateinit var user_phoneNum:TextView

    var selectPhotoUri:Uri? = null

    companion object {
        var currentUser: User? = null

        //var for firebase storage
        var currentAcc = FirebaseAuth.getInstance()
        var user: FirebaseUser = currentAcc.currentUser

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //get current user data
        fetchCurrentUser()

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        //variable initialization
        var btn_select_photo: Button = root.findViewById(R.id.btn_select_photo)
        iv_profile = root.findViewById(R.id.iv_profile)
        user_name = root.findViewById(R.id.tv_user_name)
        user_email = root.findViewById(R.id.tv_user_email)
        user_password = root.findViewById(R.id.tv_user_password)
        user_phoneNum = root.findViewById(R.id.tv_user_phoneNum)

        //edit var
        val edit_name_icon:ImageView = root.findViewById(R.id.edit_name_icon)
        val tv_logout:TextView = root.findViewById(R.id.tv_logout)
        val tv_edit_info:TextView = root.findViewById(R.id.tv_edit_info)


        //edit user name fn
        edit_name_icon.setOnClickListener(){
            //Inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.popout_getname, null)

            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)

            mDialogView.edittext_new_name.setText(currentUser!!.name)
            //show dialog
            val  mAlertDialog = mBuilder.show()

            //confirm button
            mDialogView.btn_confirm_new_name.setOnClickListener(){
                var mNewName:EditText = mDialogView.edittext_new_name

                val newName = mNewName.text.toString().trim()
                val nameREG = "^([a-zA-Z /.])*\$"
                var pattern: Pattern = Pattern.compile(nameREG)
                fun CharSequence.isName() : Boolean = pattern.matcher(this).find()

                //validate phone num format
                if (TextUtils.isEmpty(newName)){
                    mNewName.error = "Enter new name"
                }else if(!(newName.isName())) {
                    mNewName.error = "Invalid Name Format"
                }else{
                    //update new name
                    val user = User(newName, currentUser!!.uid, currentUser!!.password, currentUser!!.phoneNum, currentUser!!.email, currentUser!!.role, currentUser!!.img)
                    saveUserToFirebaseDB(user)

                    Toast.makeText(context, "Change Name Successfully!", Toast.LENGTH_SHORT).show()
                    mAlertDialog.dismiss()

                }
            }

            //close icon
            mDialogView.iv_close.setOnClickListener(){
                //dismiss dialog
                mAlertDialog.dismiss()
            }
        }

        //edit user info fn (without name)
        tv_edit_info.setOnClickListener(){
            //Inflate the dialog with custom view
            val mDialogView1 = LayoutInflater.from(context).inflate(R.layout.popout_getinfo, null)

            //AlertDialogBuilder
            val mBuilder1 = AlertDialog.Builder(context)
                .setView(mDialogView1)

            mDialogView1.edittext_new_phoneNum.setText(currentUser!!.phoneNum)

            //show dialog
            val  mAlertDialog1 = mBuilder1.show()

            //view password in text function

            mDialogView1.iv_hide_oldPassword.setOnClickListener(){
                //conveert to password type or text type
                convertPasswordType(mDialogView1.iv_hide_oldPassword, mDialogView1.edittext_old_password)
            }

            mDialogView1.iv_hide_newPassword1.setOnClickListener(){
                //conveert to password type or text type
                convertPasswordType(mDialogView1.iv_hide_newPassword1, mDialogView1.edittext_new_password1)
            }

            mDialogView1.iv_hide_newPassword2.setOnClickListener() {
                //conveert to password type or text type
                convertPasswordType(mDialogView1.iv_hide_newPassword2, mDialogView1.edittext_new_password2)
            }





            //confirm button
            mDialogView1.btn_confirm_new_info.setOnClickListener(){
                var mOldPassword:EditText = mDialogView1.edittext_old_password
                var mNewPs1:EditText = mDialogView1.edittext_new_password1
                var mNewPs2:EditText = mDialogView1.edittext_new_password2
                var mNewPhoneNum:EditText = mDialogView1.edittext_new_phoneNum

                //validation
                val isValid = validationForUserInfo(mOldPassword,mNewPs1,mNewPs2, mNewPhoneNum)
                if(isValid){
                    if( mNewPs2.text.toString() == currentUser!!.password && mNewPhoneNum.text.toString() == currentUser!!.phoneNum){
                        Toast.makeText(context, "No Changes is Made !", Toast.LENGTH_SHORT).show()
                        mAlertDialog1.dismiss()
                    }else {
                        //update new inputted info
                        val userInfo = User(
                            currentUser!!.name,
                            currentUser!!.uid,
                            mNewPs2.text.toString().trim(),
                            mNewPhoneNum.text.toString().trim(),
                            currentUser!!.email,
                            currentUser!!.role,
                            currentUser!!.img
                        )
                            //save new password to firebase storage
                            user.updatePassword(mNewPs2.text.toString())
                                    .addOnSuccessListener {
                                        //save to firebase db
                                        saveUserToFirebaseDB(userInfo)
                                        Toast.makeText(context, "Change Info Successfully !", Toast.LENGTH_SHORT)
                                                .show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Fail to update info !", Toast.LENGTH_SHORT)
                                                .show()
                                    }


                        mAlertDialog1.dismiss()
                    }
                }
            }

            //close icon
            mDialogView1.iv_close.setOnClickListener(){
                //dismiss dialog
                mAlertDialog1.dismiss()
            }
        }

        //add photo
        btn_select_photo.setOnClickListener(){
            Log.d("ProfileFragment", "Try to show photo selector")

            //get into gallery
            val intentAction = Intent(Intent.ACTION_PICK)
            intentAction.type = "image/*"
            startActivityForResult(intentAction, 0)
        }

        //logout
        tv_logout.setOnClickListener(){
            //pop out a confirmation message
            val confirmLogoutBox = AlertDialog.Builder(context)
            confirmLogoutBox.setTitle("LOGOUT")
            confirmLogoutBox.setMessage("Please click 'Confirm' to logout.")
            confirmLogoutBox.setPositiveButton("Confirm") { dialogInterface: DialogInterface, i: Int ->

                if(currentUser?.role == "Staff"){
                    updateStaffStatus(Login.currentUser!!)
                }

                Toast.makeText(context, "Logout Successfully!", Toast.LENGTH_SHORT).show()

                //logout to the MAIN MAIN page
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
            confirmLogoutBox.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(context, "Cancel Logout!", Toast.LENGTH_SHORT).show()
            }
            confirmLogoutBox.show()
        }

        return root
    }

    //validation user new info
    private fun validationForUserInfo(mOldPassword:EditText,mNewPs1:EditText,mNewPs2:EditText, mNewPhoneNum:EditText):Boolean{
        //initialized
        val oldPsw = mOldPassword.text.toString().trim()
        val newPsw1 = mNewPs1.text.toString().trim()
        val newPsw2 = mNewPs2.text.toString().trim()
        val newPhoneNum = mNewPhoneNum.text.toString().trim()

        var isValid = true

        //validation for new phone num
        if (TextUtils.isEmpty(newPhoneNum)){
            mNewPhoneNum.error = "Phone Number is required"
            return false
        }else{
            //set phone num format and validate
            val phoneREG = "^[0-9]{10,11}$"
            var phoneNum_pattern: Pattern = Pattern.compile(phoneREG)
            fun CharSequence.isPhoneNumber() : Boolean = phoneNum_pattern.matcher(this).find()

            //validate phone num format
            if(!(newPhoneNum.isPhoneNumber())){
                if(newPhoneNum.contains('-')){
                    mNewPhoneNum.error = "Phone number do not contains '-'"
                    return false
                }else{
                    mNewPhoneNum.error = "Invalid Phone Number"
                    return false
                }
            }
        }

        //validation for old ps, new ps 1 & new ps 2
        if(!(TextUtils.isEmpty(oldPsw)) && !(TextUtils.isEmpty(newPsw1)) && !(TextUtils.isEmpty(newPsw2)) ){
            if(oldPsw == currentUser!!.password){ //valid old ps
                if(newPsw1 == oldPsw){
                    mNewPs1.error = "Old and New Password cannot be the same"
                    isValid = false
                }else if(newPsw1 != newPsw2){
                    mNewPs2.error = "Both New Password Field must be same"
                    isValid = false
                }else if(newPsw1.length < 6){
                    mNewPs1.error = "Password must more than 6 characters"
                    isValid = false
                }
            }else{
                mOldPassword.error = "Invalid Old Password"
                isValid = false
            }

        }else if (!(TextUtils.isEmpty(oldPsw)) || !(TextUtils.isEmpty(newPsw1)) ||!(TextUtils.isEmpty(newPsw2)) ) {
            if(TextUtils.isEmpty(oldPsw)){
                mOldPassword.error = "Old Password is Required"
                isValid = false
            }else if(oldPsw != currentUser!!.password){
                mOldPassword.error = "Invalid Old Password"
                isValid = false
            }
            if(TextUtils.isEmpty(newPsw1)){
                mNewPs1.error = "New Password is Required"
                isValid = false
            }
            if(TextUtils.isEmpty(newPsw2)){
                mNewPs2.error = "Pls Enter New Password Again"
                isValid = false
            }
        }else{
            mNewPs2.setText(currentUser!!.password)
        }

        return isValid
    }

    //change password view type
    private fun convertPasswordType(icon_eyes:ImageView, password:EditText){
        if(password.transformationMethod == HideReturnsTransformationMethod.getInstance()){
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            icon_eyes.setImageResource(R.drawable.ic_hide_psw)
        }else{
            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            icon_eyes.setImageResource(R.drawable.ic_show_psw)
        }
    }


    //select photo in phone
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //proceed and check what the selected image was...
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("ProfileFragment", "Profile image is selected")
            selectPhotoUri = data.data

            //upload the data img into Firebase
            uploadImgToFirebaseStorage(selectPhotoUri!!)
        }
    }


    //upload img to firebase storage and firebase
    private fun uploadImgToFirebaseStorage(imguri: Uri) {
        if (imguri == null) return

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/User/$uid/$filename")

         ref.putFile(imguri!!)
                .addOnSuccessListener {
                    Log.d("PROFILE FRAGMENT", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("PROFILE FRAGMENT", "File Location: $it")
                        Toast.makeText(context, "Your photo is uploaded successfully", Toast.LENGTH_LONG).show()

                        val user = User(currentUser!!.name, currentUser!!.uid, currentUser!!.password, currentUser!!.phoneNum, currentUser!!.email, currentUser!!.role, it.toString())
                        saveUserToFirebaseDB(user)
                    }
                }
                .addOnFailureListener {
                    Log.d("PROFILE FRAGMENT", "Failed to upload image to storage: ${it.message}")
                    Toast.makeText(context, "Your photo is fail to upload", Toast.LENGTH_LONG)
                        .show()
                }
    }

    //upload selected img with all data to firebase db
    private fun saveUserToFirebaseDB(user: User){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
       // val user = User(currentUser!!.name, currentUser!!.uid, currentUser!!.password, currentUser!!.phoneNum, currentUser!!.email, currentUser!!.role, imgurl)

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("PROFILE FRAGMENT", "User Info Updated - to Firebase Database")
                }
                .addOnFailureListener {
                    Log.d("PROFILE FRAGMENT", "Failed to set value to database: ${it.message}")
                }
    }

    //get info from firebase
    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)

                user_name.text = currentUser!!.name
                user_email.text = currentUser!!.email
                user_password.text = currentUser!!.password
                user_phoneNum.text = currentUser!!.phoneNum

                if (currentUser!!.img != null) {
                    Picasso.get().load(currentUser!!.img).into(iv_profile)
                } else {
                    iv_profile.setImageResource(R.drawable.ic_account_photo)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    //upfate staff status
    private fun updateStaffStatus(currentUser: User){

        val query: Query = FirebaseDatabase.getInstance().getReference("Staff")
                .orderByChild("uid")
                .equalTo(currentUser.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for(i in snapshot.children){
                        val staff = i.getValue(Staff::class.java)

                        if (staff != null) {
                            if(staff.uid == currentUser.uid){
                                val updateStaffStatus = Staff(currentUser.name, staff.id, staff.email, staff.password, staff.phoneNum, staff.img, staff.role,
                                        "Offline", staff.accessRoom, staff.accessServicesFacilities,
                                        staff.accessHousekeeping, staff.accessCheckInOut, staff.uid)

                                snapshot.ref.child("${staff.name} - ${staff.uid}").setValue(updateStaffStatus)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
