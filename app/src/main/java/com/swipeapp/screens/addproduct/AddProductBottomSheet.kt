package com.swipeapp.screens.addproduct

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowMetrics
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.swipeapp.R
import com.swipeapp.databinding.BottomSheetAddProductBinding
import com.swipeapp.network.models.AddProductRequest
import com.swipeapp.screens.MainActivity


class AddProductBottomSheet(
    private val listener: AddProductBottomSheetListener
): BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddProductBinding

    var selectedProductType = ProductType.PRODUCT
    var images = mutableListOf<String>()

    override fun onStart() {
        super.onStart()
        // setup bottom sheet for full screen
        setupRatio(requireContext(),100)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_product, container, false)

        binding.productTypeSpinner.adapter = ProductTypeAdapter(requireContext())

        setListeners()
        textInputValidators()

        return binding.root
    }

    private fun setListeners() {
        binding.selectedImgTxt.setOnClickListener {
            (activity as MainActivity).openImagePicker()
        }

        binding.txtAddProduct.setOnClickListener {
            if(areDetailsValid()) {
                // send data to main activity
                listener.onAddProductClicked(
                    AddProductRequest(
                        productName = binding.productNameInputEdt.text.toString(),
                        productType = selectedProductType.type,
                        productPrice = binding.productPriceInputEdt.text.toString(),
                        productTax = binding.productTaxInputEdt.text.toString(),
                        productImages = images
                    )
                )
            }else {
                Toast.makeText(requireContext(), "Fill all the required fields", Toast.LENGTH_LONG).show()
            }
        }

        binding.productTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if(position in ProductType.values().indices) {
                    selectedProductType = ProductType.values()[position]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun areDetailsValid():Boolean {

        binding.productTaxInputEdt.text?.toString()?.toDouble()?.let { taxRate ->
            return (binding.productNameInputEdt.text?.isNotEmpty() == true &&
                binding.productPriceInputEdt.text?.isNotEmpty() == true &&
                binding.productTaxInputEdt.text?.isNotEmpty() == true &&
                taxRate in 0.0..100.0)
        }

        return false
    }

    private fun textInputValidators() {
        binding.productNameInputEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(newText?.isEmpty() == true) {
                    binding.productNameInputLayout.isErrorEnabled = true
                    binding.productNameInputLayout.error = "Product name must not be empty"
                }else{
                    binding.productNameInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.productPriceInputEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(newText?.isEmpty() == true) {
                    binding.productPriceInputLayout.isErrorEnabled = true
                    binding.productPriceInputLayout.error = "Product price must not be empty"
                }else{
                    binding.productPriceInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.productTaxInputEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(newText?.isEmpty() == true) {
                    binding.productTaxInputLayout.isErrorEnabled = true
                    binding.productTaxInputLayout.error = "Tax rate must not be empty"
                }else{
                    binding.productTaxInputLayout.isErrorEnabled = false

                    newText?.toString()?.toDouble()?.let { taxRate ->
                        if(taxRate !in 0.0 .. 100.0) {
                            binding.productTaxInputLayout.isErrorEnabled = true
                            binding.productTaxInputLayout.error = "Tax rate must be between 0.0 to 100.0"
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun setupRatio(context: Context, percentage: Int) {
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(binding.parentLayout)
        val layoutParams = binding.parentLayout.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight(context, percentage)
        binding.parentLayout.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(context: Context, percentage: Int): Int {
        return getWindowHeight(context) * percentage / 100
    }

    private fun getWindowHeight(context: Context): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}
interface AddProductBottomSheetListener{
    fun onAddProductClicked(addProductRequest: AddProductRequest)
}