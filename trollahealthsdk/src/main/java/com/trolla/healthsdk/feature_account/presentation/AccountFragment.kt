package com.trolla.healthsdk.feature_account.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.AccountFragmentBinding
import com.trolla.healthsdk.databinding.ProductsListFragmentBinding
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListViewModel
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    val accountViewModel: AccountViewModel by inject(
        AccountViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<AccountFragmentBinding>(
            inflater,
            R.layout.account_fragment,
            container,
            false
        )

        binding.viewModel = accountViewModel

        return binding.root

    }

}