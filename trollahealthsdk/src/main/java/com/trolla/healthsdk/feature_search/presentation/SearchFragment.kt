package com.trolla.healthsdk.feature_search.presentation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentSearchBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.feature_search.data.ModelSearchHistory
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

    var localSearchHistoryList = ArrayList<ModelSearchHistory>()
    lateinit var localSearchHistoryAdapter: GenericAdapter<ModelSearchHistory>

    lateinit var binding: FragmentSearchBinding

    val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentSearchBinding>(
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

        searchViewModel.localSearchHistoryLiveData.observe(viewLifecycleOwner) {
            localSearchHistoryList.clear()
            localSearchHistoryList.addAll(it)
            localSearchHistoryAdapter.notifyDataSetChanged()
            if (localSearchHistoryList.size == 0) {
                binding.txtLocalSearchHistoryTitle.hide()
            } else {
                binding.txtLocalSearchHistoryTitle.show()
            }
        }

        genericAdapter = GenericAdapter(
            R.layout.item_search, searchList
        )

        localSearchHistoryAdapter = GenericAdapter(
            R.layout.item_search_history, localSearchHistoryList
        )

        binding.rvLocalSearchHistory.adapter = localSearchHistoryAdapter

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

                binding.edtSearch.clearFocus()
                activity?.hidekeyboard(binding.edtSearch)

                var product_id = searchList[position].product_id
                var product_name = searchList[position].title

                var productDetailsFragment = ProductDetailsFragment.newInstance(
                    product_id,
                    product_name
                )

                (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)

                searchViewModel.addSearchToLocalSearchHistory(
                    ModelSearchHistory(
                        product_id,
                        product_name
                    )
                )
                searchViewModel.getLocalSearchHistory()
            }
        })

        localSearchHistoryAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

                activity?.hidekeyboard(binding.edtSearch)

                var product_id = localSearchHistoryList[position].id
                var product_name = localSearchHistoryList[position].title

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
            page = 1
            isLastPage = false

            if (it.toString().trim().isEmpty() || it.toString().trim().length < 3) {
                searchList.clear()
                genericAdapter.notifyDataSetChanged()
                binding.rlLocalSearchHistory.show()
            } else {
                searchStr = it.toString().trim()
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 500)

            }
        }

        binding.imgClear.setOnClickListener {
            binding.edtSearch.setText("")
        }

        binding.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
            activity?.hidekeyboard(binding.edtSearch)
        }

        searchViewModel.getLocalSearchHistory()

        return binding.root
    }

    private val runnable = Runnable {
        getSearch()
        binding.rlLocalSearchHistory.hide()
    }

    fun getSearch() {
        searchViewModel.search(searchStr, page.toString(), limit.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtSearch.requestFocus()
        activity?.showkeyboard(binding.edtSearch)
    }

}