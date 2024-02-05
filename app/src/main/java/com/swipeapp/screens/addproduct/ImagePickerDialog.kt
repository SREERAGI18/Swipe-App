package com.swipeapp.screens.addproduct

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.swipeapp.R
import com.swipeapp.databinding.DialogProductPicBinding

class ImagePickerDialog(val listener: OnImagePickerClickListener): DialogFragment() {

    lateinit var binding: DialogProductPicBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_product_pic,
            null, false
        )

        setListeners()

        return activity?.let {
            // Use the Builder class for convenient dialog construction

            val builder = MaterialAlertDialogBuilder(it, R.style.MaterialAlertDialog_rounded)
            builder.setView(binding.root)

            isCancelable = true

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setListeners() {
        binding.camera.setOnClickListener {
            listener.onCameraClicked()
            dismiss()
        }

        binding.gallery.setOnClickListener {
            listener.onGalleryClicked()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // to remove transparent black background
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

}

interface OnImagePickerClickListener {

    fun onCameraClicked()
    fun onGalleryClicked()
}