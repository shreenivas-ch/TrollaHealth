package com.trolla.healthsdk.feature_productslist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardViewModel
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import okhttp3.internal.notify
import org.koin.java.KoinJavaComponent.inject

class ProductsListFragment() : Fragment() {

    val title by lazy {
        arguments?.let {
            it.getString("title")
        }
    }

    val id by lazy {
        arguments?.let {
            it.getString("id")
        }
    }

    val productsListViewModel: ProductsListViewModel by inject(ProductsListViewModel::class.java)

    companion object {
        fun newInstance(title: String, id: String): ProductsListFragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("id", id)
            var productsListFragment = ProductsListFragment()
            productsListFragment.arguments = bundle
            return productsListFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<ProductsListFragmentBinding>(
            inflater,
            R.layout.products_list_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = productsListViewModel

        productsListViewModel.headerTitle.value = title

        val genericAdapter = GenericAdapter<DashboardProduct>(
            R.layout.item_dashboard_recommended_product
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(view.context, "Clicked at row $position", Toast.LENGTH_LONG).show()
            }

            override fun onAddToCartClick(view: View, position: Int) {
                Toast.makeText(view.context, "Add to cart clicked at $position", Toast.LENGTH_LONG).show()
            }

        })
        binding.productsList.adapter = genericAdapter

        productsListViewModel.productsListResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    val response = productsListViewModel.productsListResponseLiveData.value
                    genericAdapter.addItems(response?.data?.data?.product_list!!)
                    genericAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        productsListViewModel.progressStatus.observe(viewLifecycleOwner)
        {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        productsListViewModel.getProductsList(id.toString())

        return binding.root

    }

}