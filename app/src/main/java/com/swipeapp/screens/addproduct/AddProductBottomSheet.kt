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
        setupRatio(requireContext(),100)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_product, container, false)

        binding.selectedImgTxt.setOnClickListener {
            (activity as MainActivity).openImagePicker()
        }

        binding.txtAddProduct.setOnClickListener {
            if(binding.productNameInputEdt.text?.isNotEmpty() == true &&
                binding.productPriceInputEdt.text?.isNotEmpty() == true &&
                binding.productTaxInputEdt.text?.isNotEmpty() == true
            ) {
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

        binding.productTypeSpinner.adapter = ProductTypeAdapter(requireContext())
        binding.productTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if(position in ProductType.values().indices) {
                    selectedProductType = ProductType.values()[position]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        textInputValidators()

        return binding.root
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
//                    binding.productNameInputLayout.error = "Product Name must not be empty"
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

    fun setupRatio(context: Context, percetage: Int) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares

        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(binding.parentLayout)
        val layoutParams = binding.parentLayout.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight(context, percetage)
        binding.parentLayout.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(context: Context, percetage: Int): Int {
        return getWindowHeight(context) * percetage / 100
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