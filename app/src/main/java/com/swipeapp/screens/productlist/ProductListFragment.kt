package com.swipeapp.screens.productlist

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.swipeapp.R
import com.swipeapp.databinding.FragmentProductListBinding
import com.swipeapp.network.ResponseHandler
import com.swipeapp.screens.BasicDialog
import com.swipeapp.utils.hideSoftKeyboard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ProductListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductListFragment : Fragment() {

    private val productListVM:ProductListVM by viewModel()
    private val productListAdapter by lazy { ProductListAdapter() }

    private lateinit var binding:FragmentProductListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)

        initViews()
        setListeners()
        receiveFlowUpdates()

        getProducts()

        return binding.root
    }

    private fun getProducts() {
        productListVM.getProductList(requireContext())
    }

    private fun initViews() {
        binding.productList.adapter = productListAdapter
    }

    private fun setListeners() {
        binding.txtReload.setOnClickListener {
            getProducts()
        }

        binding.searchProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if(newText.isNotEmpty()) {
                        productListVM.searchProduct("$newText%")
                    }else{
                        getProducts()
                    }
                }
                return true
            }
        })

        val searchEdt = binding.searchProduct.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEdt.setText("")
        searchEdt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event?.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    requireActivity().hideSoftKeyboard()
                    searchEdt.clearFocus()
                    return true
                }
                // Return true if you have consumed the action, else false.
                return false
            }
        })
    }

    private fun receiveFlowUpdates() {
        lifecycleScope.launch {
            productListVM.productList.collectLatest { response ->
                binding.progress.visibility = View.GONE
                binding.txtReload.visibility = View.GONE
                when(response) {
                    is ResponseHandler.Error -> {

                        response.message?.let { message ->
                            BasicDialog(
                                title = "Error",
                                message = message
                            ).show(childFragmentManager, "Error Dialog")

                            if(message == requireContext().getString(R.string.no_internet_msg)) {
                                binding.txtReload.visibility = View.VISIBLE
                            }
                        }
                    }
                    is ResponseHandler.Idle -> {

                    }
                    is ResponseHandler.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                    }
                    is ResponseHandler.Success -> {
                        response.data?.let {
                            productListAdapter.updateProducts(it)
                        }
                    }
                }
            }
        }
    }
}