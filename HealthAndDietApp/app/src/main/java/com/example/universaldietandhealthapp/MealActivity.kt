package com.example.universaldietandhealthapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_meal.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MealActivity : AppCompatActivity() {

    lateinit var editTextMealName : EditText
    lateinit var editTextMealCalories : EditText
    lateinit var editTextMealFat : EditText
    lateinit var editTextMealCarbs : EditText
    lateinit var ref : DatabaseReference
    val currentuserid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    lateinit var image_view: ImageView

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view

            image_view.setImageURI(image_uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        val food1Pic = findViewById<ImageView>(R.id.ivMealImage)

        ref = FirebaseDatabase.getInstance().getReference("users")

        editTextMealName = edtMealName
        editTextMealCalories = edtCalories
        editTextMealFat = edtFat
        editTextMealCarbs = edtCarbs

        btnUpdateMealInformation.setOnClickListener{
            //Do something when button is clicked
            saveMealInformation()
        }

        btnImageOfMeal.setOnClickListener {
            image_view = food1Pic
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists())
                {
                    for (u in p0.children)
                    {
                        if (u.child("Profile").exists())
                        {
                            val user = u.child("Profile").getValue(PIData::class.java)

                            if (u.child("Meals").exists())
                            {
                                val meal = u.child("Meals").getValue(MealData::class.java)

                                if(user!!.currentuserid == currentuserid)
                                {
                                    editTextMealName.setText(meal!!.mealname)
                                    editTextMealCalories.setText(meal!!.mealcalories.toString())
                                    editTextMealFat.setText(meal!!.mealfat.toString())
                                    editTextMealCarbs.setText(meal!!.mealcarbs.toString())
                                }
                            }
                        }
                        else
                        {
                            val meal = u.child("Meals").getValue(MealData::class.java)

                            editTextMealName.setText(meal!!.mealname)
                            editTextMealCalories.setText(meal!!.mealcalories.toString())
                            editTextMealFat.setText(meal!!.mealfat.toString())
                            editTextMealCarbs.setText(meal!!.mealcarbs.toString())
                        }
                    }
                }
            }
        })
    }

    private fun saveMealInformation()
    {
        val mealname = editTextMealName.text.toString().trim()
        val mealcalories = editTextMealCalories.text.toString().trim()
        val mealfat = editTextMealFat.text.toString().trim()
        val mealcarbs = editTextMealCarbs.text.toString().trim()

        if (mealname.isEmpty())
        {
            editTextMealName.error = "Please enter a meal name"
            return
        }
        if (mealcalories.isEmpty())
        {
            editTextMealCalories.error = "Please enter an amount of calories"
            return
        }
        if (mealfat.isEmpty())
        {
            editTextMealFat.error = "Please enter a fat %"
            return
        }
        if (mealcarbs.isEmpty())
        {
            editTextMealCarbs.error = "Please enter a carbs %"
            return
        }

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        val meals = "Meals"
        val mealinformation = MealData(currentDate.toString(), mealname, mealcalories.toInt(), mealfat.toInt(), mealcarbs.toInt())

        ref.child(currentuserid).child(meals).setValue(mealinformation).addOnCompleteListener {
            Toast.makeText(this, "Meal information saved successfully", Toast.LENGTH_LONG).show()
        }
    }

    /*val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent1() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            ivMealImage.setImageBitmap(imageBitmap)
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent2() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    //...
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }*/
}