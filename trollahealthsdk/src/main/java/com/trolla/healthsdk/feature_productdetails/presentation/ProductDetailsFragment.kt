package com.trolla.healthsdk.feature_productdetails.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.ProductDetailsFragmentBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.utils.*
import org.koin.java.KoinJavaComponent.inject

class ProductDetailsFragment : Fragment() {

    val productDetailsViewModel: ProductDetailsViewModel by inject(
        ProductDetailsViewModel::class.java
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

    lateinit var binding: ProductDetailsFragmentBinding

    companion object {
        fun newInstance(id: String, title: String): ProductDetailsFragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("id", id)
            var productsListFragment = ProductDetailsFragment()
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
            R.layout.product_details_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = productDetailsViewModel

        productDetailsViewModel.headerBackButton.value = 1
        productDetailsViewModel.headerTitle.value = ""

        binding.commonHeader.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }

        productDetailsViewModel.getProductDetailsResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    val response = productDetailsViewModel.getProductDetailsResponseLiveData.value
                    productDetailsViewModel.dashboardProduct.value = response?.data?.data?.detail!!

                    binding.llProductDetails.show()

                    var dashboardProduct = response?.data?.data?.detail!!
                    binding.txtTitle.text = dashboardProduct.title
                    binding.txtManufacturer.text = dashboardProduct.manufacturer_name
                    if (dashboardProduct.expiry_date.isNullOrEmpty()) {
                        binding.llExpiry.hide()
                    } else {
                        binding.txtExpiryDate.text = dashboardProduct.expiry_date
                    }

                    if (dashboardProduct.country_origin.isNullOrEmpty()) {
                        binding.llCountryOfOrigin.hide()
                    } else {
                        binding.txtCountryOfOrigin.text = dashboardProduct.country_origin
                    }

                    if (dashboardProduct.controlled_faqs.isNullOrEmpty()) {
                        binding.txtFaq.hide()
                        binding.txtFaqLabel.hide()
                    } else {
                        binding.txtFaq.text = dashboardProduct.controlled_faqs
                    }

                    binding.viewRx.setVisibilityOnBoolean(
                        dashboardProduct.rx_type == "NON-RX" || dashboardProduct.rx_type == "",
                        false
                    )
                    manageProductImages(dashboardProduct.product_img)
                    createTabs(dashboardProduct)
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        productDetailsViewModel.progressStatus.observe(viewLifecycleOwner)
        {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        productDetailsViewModel.getProductDetails(id.toString())

        return binding.root
    }

    private fun manageProductImages(productImages: ArrayList<String>) {
        val sliderAdapter = ProductImagesAdapter(
            requireActivity(),
            productImages
        )

        var pager = binding.productsViewPager?.apply {
            adapter = sliderAdapter
            startAutoScroll()
            interval = 10000
            isCycle = true
            clipToPadding = false
        }

        binding.bannerDotsIndicator?.setViewPager(pager as ViewPager)
    }

    var strDescription = ""
    var strContraindications = ""
    var strSafetyAdvice = ""
    var str_how_drug_works = ""
    var str_missed_dose = ""
    var str_quick_tips = ""
    var str_drug_interactions = ""
    var str_benefits = ""
    var str_storage_conditions = ""
    var str_uses = ""
    var str_ingredients = ""
    var str_side_effects = ""

    private fun createTabs(dashboardProduct: DashboardResponse.DashboardProduct) {
        if (dashboardProduct.product_brief.isNullOrEmpty() && dashboardProduct.description.isNullOrEmpty() && dashboardProduct.short_description.isNullOrEmpty() && dashboardProduct.long_description.isNullOrEmpty()

        )
        else {
            binding.tabLayout.addTab(
                binding.tabLayout.newTab().setText(getString(R.string.product_description))
            )

            strDescription =
                dashboardProduct.product_brief + "\n\n" + dashboardProduct.description + "\n\n" + dashboardProduct.short_description + "\n\n" + dashboardProduct.long_description
        }

        if (!dashboardProduct.contraindications.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Contraindications"))
            strContraindications = dashboardProduct.contraindications
        }

        if (!dashboardProduct.safety_advice.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Safety Advice"))
            strSafetyAdvice = dashboardProduct.safety_advice
        }

        if (!dashboardProduct.how_drug_works.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("How Drug Works"))
            str_how_drug_works = dashboardProduct.how_drug_works
        }

        if (!dashboardProduct.missed_dose.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Missed Dose"))
            str_missed_dose = dashboardProduct.missed_dose
        }

        if (!dashboardProduct.quick_tips.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Quick Tips"))
            str_quick_tips = dashboardProduct.quick_tips
        }

        if (!dashboardProduct.drug_interactions.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Drug Interactions"))
            str_drug_interactions = dashboardProduct.drug_interactions
        }

        if (!dashboardProduct.benefits.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Benefits"))
            str_benefits = dashboardProduct.benefits
        }

        if (!dashboardProduct.storage_conditions.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Storage Conditions"))
            str_storage_conditions = dashboardProduct.storage_conditions
        }

        if (!dashboardProduct.uses.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Uses"))
            str_uses = dashboardProduct.uses
        }

        if (!dashboardProduct.ingredients.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Ingredients"))
            str_ingredients = dashboardProduct.ingredients
        }

        if (!dashboardProduct.side_effects.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Side Effects"))
            str_side_effects = dashboardProduct.side_effects
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

}