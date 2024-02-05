package com.swipeapp.screens

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.swipeapp.BuildConfig
import com.swipeapp.R
import com.swipeapp.databinding.ActivityMainBinding
import com.swipeapp.network.ResponseHandler
import com.swipeapp.network.models.AddProductRequest
import com.swipeapp.screens.addproduct.AddProductBottomSheet
import com.swipeapp.screens.addproduct.AddProductBottomSheetListener
import com.swipeapp.screens.addproduct.AddProductResponseDialog
import com.swipeapp.screens.addproduct.ImagePickerDialog
import com.swipeapp.screens.addproduct.OnImagePickerClickListener
import com.swipeapp.utils.Constants
import com.swipeapp.utils.FileProcessing
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.Exception


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val mainVM:MainVM by viewModel()

    private var imagePickerDialog: ImagePickerDialog? = null
    private var dialogAddProductResponse: AddProductResponseDialog? = null

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setListeners()
        setStatusBarColor()
        receiveFlowUpdates()
    }

    private fun setListeners() {
        binding.fab.setOnClickListener {
            showAddProductBottomSheet()
        }
    }

    private fun setStatusBarColor() {

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    }

    private fun receiveFlowUpdates() {
        lifecycleScope.launch {
            mainVM.addProductResponse.collectLatest { response ->

                when(response) {
                    is ResponseHandler.Error -> {

                        addProductBottomSheet?.dismiss()
                        response.message?.let { message ->
                            BasicDialog(
                                title = "Error",
                                message = message
                            ).show(supportFragmentManager, "Error Dialog")
                        }
                    }
                    is ResponseHandler.Idle -> {

                    }
                    is ResponseHandler.Loading -> {
                        dialogAddProductResponse = AddProductResponseDialog()
                        dialogAddProductResponse?.show(supportFragmentManager, "Add Product Response")
                    }
                    is ResponseHandler.Success -> {
                        addProductBottomSheet?.dismiss()

                        dialogAddProductResponse?.binding?.apply {
                            progress.visibility = View.GONE
                            tickImg.visibility = View.VISIBLE
                            response.data?.message?.let { message ->
                                responseMessage.text = message
                                triggerNotification(message)
                            }
                            delay(2000)
                            dialogAddProductResponse?.dismiss()
                            refreshCurrentFragment()
                        }
                    }
                }
            }
        }
    }

    private fun triggerNotification(message:String) {

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /***
         * Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID.
         * This is required for compatibility with Android 8.0 (API level 26) and higher,
         * but is ignored by older versions.
         */
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.app_icon_round)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Automatically removes the notification when the user taps it.
                .setAutoCancel(true)

        try {
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, notificationBuilder.build())
        }catch (e:SecurityException) {
            e.printStackTrace()
        }
    }
    private fun refreshCurrentFragment() {
        navController.apply {
            currentDestination?.id?.let { id ->
                popBackStack(id,true)
                navigate(id)
            }
        }
    }

    fun openImagePicker() {
        if(isPermissionGranted()) {
            val listener = object : OnImagePickerClickListener {
                override fun onCameraClicked() {
                    openCamera()
                }

                override fun onGalleryClicked() {
                    openGallery()
                }
            }

            imagePickerDialog = ImagePickerDialog(listener)
            imagePickerDialog?.show(supportFragmentManager, "Image Picker Dialog")
        }else{
            requestStoragePermission()
        }
    }

    fun closeImagePickerDialog() {
        imagePickerDialog?.dismiss()
    }

    private val openGalleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {

                cropImage(it)
            }
        }
    }

    private fun openGallery() {
        val getImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getImageIntent.type = "image/*"
        openGalleryResult.launch(getImageIntent)
    }

    var latestTmpUri: Uri? = null

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                cropImage(uri)
            }
        }
    }

    private fun getFileUri(): Uri {
        val file = File("${filesDir}/", "product_pic.png")

        return FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", file)
    }

    private fun openCamera() {
        getFileUri().let { uri ->
            latestTmpUri = uri
            takeImageResult.launch(uri)
        }
    }

    private fun requestStoragePermission() {

        val permissions = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        } else {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
            )
        }

        requestMultiplePermissions.launch(permissions)
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        var permissionGranted = true

        permissions.entries.forEach {
            Log.d("DEBUG", "${it.key} = ${it.value}")
            if(!it.value) {
                permissionGranted = false
            }
        }

        if(permissionGranted) {
            openImagePicker()
        }else{
            Toast.makeText(
                this,
                "You must grant a write storage permission to use to upload profile picture",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun isPermissionGranted():Boolean {

        val requiredPermission1 = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Manifest.permission.READ_EXTERNAL_STORAGE
        } else {
            Manifest.permission.READ_MEDIA_IMAGES
        }
        val checkVal1 = ContextCompat.checkSelfPermission(this, requiredPermission1)

        val requiredPermission2 = Manifest.permission.CAMERA
        val checkVal2 = ContextCompat.checkSelfPermission(this, requiredPermission2)

        return checkVal1 == PackageManager.PERMISSION_GRANTED &&
            checkVal2 == PackageManager.PERMISSION_GRANTED
    }

    private var addProductBottomSheet: AddProductBottomSheet? = null

    private fun showAddProductBottomSheet() {

        val listener = object: AddProductBottomSheetListener {
            override fun onAddProductClicked(addProductRequest: AddProductRequest) {
                mainVM.addProduct(this@MainActivity, addProductRequest)
            }
        }

        addProductBottomSheet = AddProductBottomSheet(listener)

        addProductBottomSheet?.show(supportFragmentManager, "Product Image")
    }

    private fun cropImage(imageUri: Uri){

        CropImage.activity(imageUri)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setAspectRatio(1,1)
            .setFixAspectRatio(true)
            .setBorderCornerThickness(3f)
            .setBorderCornerColor(Color.CYAN)
            .setAllowRotation(false)
            .setAllowFlipping(false)
            .setOutputCompressQuality(10)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                resultUri?.let { uri ->
                    FileProcessing.getPath(this, uri)?.let { imagePath ->
                        val s = imagePath.split("/")
                        val fileName = s.last()

                        addProductBottomSheet?.apply {
                            binding.selectedImgTxt.text= fileName
                            images.add(imagePath)
                        }
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}