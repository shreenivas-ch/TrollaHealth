package com.trolla.healthsdk.feature_prescriptionupload.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentPrescriptionUploadBinding
import org.koin.java.KoinJavaComponent.inject

class PrescriptionsUploadFragment : Fragment() {

    val prescriptionsUploadViewModel: PrescriptionsUploadViewModel by inject(
        PrescriptionsUploadViewModel::class.java
    )

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

    lateinit var binding: FragmentPrescriptionUploadBinding

    companion object {
        fun newInstance(id: String, title: String): PrescriptionsUploadFragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("id", id)
            var productsListFragment = PrescriptionsUploadFragment()
            productsListFragment.arguments = bundle
            return productsListFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_prescription_upload,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = prescriptionsUploadViewModel

        return binding.root
    }

}