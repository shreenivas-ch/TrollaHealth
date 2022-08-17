package com.trolla.healthsdk.feature_search.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentSearchBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.ui_utils.PaginationScrollListener2
import com.trolla.healthsdk.utils.*
import org.koin.java.KoinJavaComponent.inject

class SearchFragment : Fragment() {

    var page = TrollaConstants.PAGINATION_DEFAULT_INITIAL_PAGE
    var limit = TrollaConstants.PAGINATION_DEFAULT_LIMIT

    var isLoading = false
    var isLastPage = false

    var searchStr = ""

    companion object {
        fun newInstance() = SearchFragment()
    }

    val searchViewModel: SearchViewModel by inject(
        SearchViewModel::class.java
    )

    var searchList = ArrayList<DashboardResponse.DashboardProduct>()
    lateinit var genericAdapter: GenericAdapter<DashboardResponse.DashboardProduct>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<FragmentSearchBinding>(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )

        binding.viewModel = searchViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        searchViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
            isLoading = it
        }

        genericAdapter = GenericAdapter(
            R.layout.item_search, searchList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

                activity?.hidekeyboard(binding.edtSearch)

                var product_id = searchList[position].product_id
                var product_name = searchList[position].product_name

                var productDetailsFragment = ProductDetailsFragment.newInstance(
                    product_id,
                    product_name
                )

                (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)
            }
        })

        binding.rvSearch.adapter = genericAdapter
        binding.rvSearch.addOnScrollListener(object :
            PaginationScrollListener2(binding.rvSearch.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                //page += 1
                //getSearch()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })

        searchViewModel.searchResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (page == 1) {
                        searchList.clear()
                    }
                    var prod1 = it?.data?.data?.product_list_1 ?: arrayListOf()
                    var prod2 = it?.data?.data?.product_list ?: arrayListOf()
                    searchList.addAll(prod1)
                    searchList.addAll(prod2)
                    genericAdapter.notifyDataSetChanged()

                    if (prod1.isNullOrEmpty() && prod2.isNullOrEmpty()) {
                        isLastPage = true
                    }
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        binding.edtSearch.addTextChangedListener {
            searchStr = it.toString().trim()
            page = 1
            isLastPage = false

            if (searchStr.isEmpty() || searchStr.length < 3) {
                searchList.clear()
                genericAdapter.notifyDataSetChanged()
            } else {
                getSearch()
            }
        }

        binding.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }

        binding.edtSearch.requestFocus()
        activity?.showkeyboard(binding.edtSearch)

        return binding.root
    }

    fun getSearch() {
        searchViewModel.search(searchStr, page.toString(), limit.toString())
    }

}