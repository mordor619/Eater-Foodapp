package com.example.eater

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.amulyakhare.textdrawable.TextDrawable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Profile : AppCompatActivity() {
    lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //change email id in profile screen
        changeEmailProfile()

        //working with display picture in profile screen
        workingWithProfileImage()

        //change member history value
        memberHistoryCalculate()

        val changeEmailId = findViewById<Button>(R.id.changeEmailId)
        val loginHistory = findViewById<Button>(R.id.loginHistory)
        val deleteAccount = findViewById<Button>(R.id.deleteAccount)
        val logout = findViewById<Button>(R.id.logout)

        val imageViewProfile = findViewById<ImageView>(R.id.imageViewProfile)

        imageViewProfile.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            startActivityForResult(takePictureIntent, 10)
        }

        changeEmailId.setOnClickListener {
            val intent = Intent(this, changemail::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext, "change email", Toast.LENGTH_LONG).show()
        }

        loginHistory.setOnClickListener {
            val intent = Intent(this, LoginHistoryActivity::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext, "login history", Toast.LENGTH_LONG).show()
        }

        deleteAccount.setOnClickListener {
            val intent = Intent(this, deleteUser::class.java)
            startActivity(intent)
            //Toast.makeText(applicationContext, "delete account", Toast.LENGTH_LONG).show()
        }

        logout.setOnClickListener {
//            val intent = Intent(this, Profile::class.java)
//            startActivity(intent)
//            Toast.makeText(applicationContext, "logout", Toast.LENGTH_LONG).show()

            var editor: SharedPreferences.Editor = preference.edit()
            editor.clear()
            editor.commit()

            deleteImageFile()


        }

    }

    private fun deleteImageFile() {
        val f = File("/data/data/com.example.eater/app_imageDirectory", "profileImage.jpg")
        val deleted = f.delete()
        if (deleted || !f.exists()) {

            Toast.makeText(this@Profile, "Logout Successfully", Toast.LENGTH_LONG).show()

            val loginIntent3 = Intent(this, login::class.java)
            this.startActivity(loginIntent3)

            //clear shared preference and then logout
        } else {
            Toast.makeText(applicationContext, "Some error occurred", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, DishList::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            val capturedImage = data?.extras?.get("data") as Bitmap

            val imageView = findViewById<ImageView>(R.id.imageViewProfile)
            imageView.setImageBitmap(capturedImage)

            saveToInternalStorage(capturedImage)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {

        val cw = ContextWrapper(applicationContext)

        // path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("imageDirectory", Context.MODE_PRIVATE)

        // Create imageDir
        val mypath = File(directory, "profileImage.jpg")

        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(mypath)

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return directory.getAbsolutePath()

    }

    private fun workingWithProfileImage() {

        preference = (application as MyDishApplication).preference

        val profileEmailIdValue = preference.getString("email", "abc")
        val photoText = profileEmailIdValue?.substring(0, 1)?.toUpperCase()

        val profilePhoto = findViewById<ImageView>(R.id.imageViewProfile)

        val f = File("/data/data/com.example.eater/app_imageDirectory", "profileImage.jpg")

        if (f.exists()) {
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            profilePhoto.setImageBitmap(b)
        } else {
            val drawableDrawerImage = TextDrawable.builder()
                .buildRound(photoText, Color.RED) // radius in px

            profilePhoto.setImageDrawable(drawableDrawerImage)
        }

    }

    private fun memberHistoryCalculate() {

        preference = (application as MyDishApplication).preference

        val profileMemberSinceValue = preference.getLong("l", 123)

        val profileMemberSince = findViewById<TextView>(R.id.profileMemberSince)

        val sdf = SimpleDateFormat("dd-MMM-YYYY")
        val netDate = Date(profileMemberSinceValue)
        val displayThisDate = sdf.format(netDate)

        profileMemberSince.text = "Member since: $displayThisDate"
    }

    private fun changeEmailProfile() {

        preference = (application as MyDishApplication).preference

        val profileEmailIdValue = preference.getString("email", "abc")

        val profileEmailId = findViewById<TextView>(R.id.profileEmailId)
        profileEmailId.text = profileEmailIdValue
    }

}