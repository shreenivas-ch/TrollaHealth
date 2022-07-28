package com.trolla.healthsdk.feature_categories.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.CategoriesFragmentBinding
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    val categoriesViewModel: CategoriesViewModel by inject(
        CategoriesViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<CategoriesFragmentBinding>(
            inflater,
            R.layout.categories_fragment,
            container,
            false
        )

        binding.viewModel = categoriesViewModel

        return binding.root
    }

}