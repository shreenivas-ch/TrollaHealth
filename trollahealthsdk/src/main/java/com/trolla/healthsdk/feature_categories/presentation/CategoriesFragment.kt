package com.trolla.healthsdk.feature_categories.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentCategoriesBinding
import com.trolla.healthsdk.feature_categories.data.CategoriesResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import org.koin.java.KoinJavaComponent.inject

class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    val categoriesViewModel: CategoriesViewModel by inject(
        CategoriesViewModel::class.java
    )

    var categoriesList = ArrayList<CategoriesResponse.Category>()
    lateinit var genericAdapter: GenericAdapter<CategoriesResponse.Category>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<FragmentCategoriesBinding>(
            inflater,
            R.layout.fragment_categories,
            container,
            false
        )

        binding.viewModel = categoriesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        categoriesViewModel.headerTitle.value = "Categories"
        categoriesViewModel.headerBottomLine.value = 1
        categoriesViewModel.headerBackButton.value = 0

        categoriesViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        genericAdapter = GenericAdapter(
            R.layout.item_category, categoriesList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                var productsFragment = ProductsListFragment.newInstance(
                    categoriesList[position].category_name,
                    categoriesList[position].category_id
                )
                (activity as DashboardActivity).addOrReplaceFragment(productsFragment, true)
            }
        })

        binding.productsList.adapter = genericAdapter

        categoriesViewModel.categoriesResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    categoriesList.clear()
                    categoriesList.addAll(it?.data?.data?.categories!!)
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

        categoriesViewModel.getCategories()

        return binding.root
    }

}