package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentDashboardBannerBinding
import com.trolla.healthsdk.databinding.ItemBannerBinding

class DashboardBannerLayoutFragment:Fragment() {

    lateinit var binding: ItemBannerBinding
    private var url: String?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_banner,
            container,
            false
        )

        return binding.root
    }

    fun setData(url: String) {
        this.url = url
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(url != null){
            binding?.imageUrl = url
        }

    }
}