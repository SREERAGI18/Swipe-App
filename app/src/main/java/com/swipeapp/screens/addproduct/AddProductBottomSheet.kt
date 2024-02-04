package com.swipeapp.screens.addproduct

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.swipeapp.R
import com.swipeapp.databinding.BottomSheetAddProductBinding
import com.swipeapp.network.models.AddProductRequest
import com.swipeapp.screens.MainActivity

class AddProductBottomSheet(
    private val listener: AddProductBottomSheetListener
): BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddProductBinding

    var selectedProductType:ProductType? = null
    var images = mutableListOf<String>()

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
                binding.productTaxInputEdt.text?.isNotEmpty() == true &&
                selectedProductType != null
            ) {
                selectedProductType?.type?.let { it1 ->
                    AddProductRequest(
                        productName = binding.productNameInputEdt.text.toString(),
                        productType = it1,
                        productPrice = binding.productPriceInputEdt.text.toString(),
                        productTax = binding.productTaxInputEdt.text.toString(),
                        productImages = images
                    )
                }?.let { it2 ->
                    listener.onAddProductClicked(it2)
                }
            }else {
                Toast.makeText(requireContext(), "Fill all the required fields", Toast.LENGTH_LONG).show()
            }
        }

        binding.productTypeSpinner.adapter = ProductTypeAdapter(requireContext())
        binding.productTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if(position-1 in ProductType.values().indices) {
                    selectedProductType = ProductType.values()[position-1]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        textInputValidators()

//        binding.gallery.setOnClickListener {
//            dismiss()
//            listener.onGallerySelected()
//        }
//
//        binding.takePicture.setOnClickListener {
//            dismiss()
//            listener.onCameraSelected()
//        }
//
//        binding.cancel.setOnClickListener {
//            dismiss()
//        }

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

}
interface AddProductBottomSheetListener{
    fun onAddProductClicked(addProductRequest: AddProductRequest)
}