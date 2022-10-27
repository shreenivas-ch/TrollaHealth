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
import com.trolla.healthsdk.feature_dashboard.data.UpdateCartCountInBottomNavigationEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListFragment
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
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

                var filterBy = "category_id"
                var id = categoriesList[position].wf_id

                var productsFragment = ProductsListFragment.newInstance(
                    categoriesList[position].name,
                    id,
                    filterBy
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

        (activity as DashboardActivity).cartViewModel.cartDetailsResponseLiveData.value.let {
            updateCartCount(it?.data?.data?.cart?.products?.size?:0)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateCartCountInBottomNavigation(updateCartCountInBottomNavigationEvent: UpdateCartCountInBottomNavigationEvent) {
        updateCartCount(updateCartCountInBottomNavigationEvent.count)
    }

    fun updateCartCount(count: Int) {
        if (count == 0) {
            categoriesViewModel.headerCartIcon.value = 0
        } else {
            categoriesViewModel.headerCartIcon.value = 1
            categoriesViewModel.headerCartCount.value = count
        }
    }

}