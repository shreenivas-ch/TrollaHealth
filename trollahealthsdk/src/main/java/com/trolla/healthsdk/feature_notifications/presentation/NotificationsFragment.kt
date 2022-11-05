package com.trolla.healthsdk.feature_notifications.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.NotificationsFragmentBinding
import org.koin.java.KoinJavaComponent.inject

class NotificationsFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    val notificationsViewModel: NotificationsViewModel by inject(
        NotificationsViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<NotificationsFragmentBinding>(
            inflater,
            R.layout.notifications_fragment,
            container,
            false
        )

        binding.viewModel = notificationsViewModel

        return binding.root
    }

}