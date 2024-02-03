package com.swipeapp.screens.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.swipeapp.R
import com.swipeapp.databinding.FragmentProductListBinding
import com.swipeapp.network.ResponseHandler
import com.swipeapp.screens.BasicDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

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
        receiveFlowUpdates()

        productListVM.getProductList(requireContext())

        binding.txtReload.setOnClickListener {
            productListVM.getProductList(requireContext())
        }

        return binding.root
    }

    private fun initViews() {
        binding.productList.adapter = productListAdapter
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