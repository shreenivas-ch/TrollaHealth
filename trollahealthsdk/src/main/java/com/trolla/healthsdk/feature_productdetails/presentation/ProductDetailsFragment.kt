package com.trolla.healthsdk.feature_productdetails.presentation

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.CustomBindingAdapter
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.ProductDetailsFragmentBinding
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productslist.data.RefreshProductListEvent
import com.trolla.healthsdk.utils.*
import kotlinx.android.synthetic.main.product_details_fragment.*
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject

class ProductDetailsFragment : Fragment() {

    val productDetailsViewModel: ProductDetailsViewModel by inject(
        ProductDetailsViewModel::class.java
    )

    val cartViewModel: CartViewModel by sharedViewModel()

    val title by lazy {
        arguments?.let {
            it.getString("title")
        }
    }

    val extrasProductId by lazy {
        arguments?.let {
            it.getInt("id")
        }
    }

    var productid = ""

    lateinit var binding: ProductDetailsFragmentBinding

    var cartQuantity = 0

    var sizesList = ArrayList<DashboardResponse.ProductVariantValues>()
    lateinit var sizesAdapter: GenericAdapter<DashboardResponse.ProductVariantValues>

    var otherOptionsList = ArrayList<DashboardResponse.ProductVariantValues>()
    lateinit var otherOptionsAdapter: GenericAdapter<DashboardResponse.ProductVariantValues>

    companion object {
        fun newInstance(id: Int, title: String): ProductDetailsFragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putInt("id", id)
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

        productid = extrasProductId.toString()

        binding.commonHeader.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }

        sizesAdapter = GenericAdapter(
            R.layout.item_product_size, sizesList
        )

        otherOptionsAdapter = GenericAdapter(
            R.layout.item_product_size, otherOptionsList
        )

        binding.rvSizes.adapter = sizesAdapter
        binding.rvOtherOptions.adapter = otherOptionsAdapter

        sizesAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                if (sizesList[position].product_id.toString() != productid) {
                    productid = sizesList[position].product_id.toString()
                    productDetailsViewModel.getProductDetails(productid)
                }
            }
        })

        otherOptionsAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                productid = otherOptionsList[position].product_id.toString()
                productDetailsViewModel.getProductDetails(productid)
            }
        })

        productDetailsViewModel.getProductDetailsResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    val response = productDetailsViewModel.getProductDetailsResponseLiveData.value
                    productDetailsViewModel.dashboardProduct.value = response?.data?.data?.detail!!

                    binding.llProductDetails.show()
                    binding.cardCartActions.show()

                    var dashboardProduct = response?.data?.data?.detail!!
                    binding.txtTitle.text = dashboardProduct.title
                    binding.txtManufacturer.text = dashboardProduct.manufacturer_name
                    binding.txtPrice.text =
                        String.format(getString(R.string.amount_string), dashboardProduct.mrp)
                    binding.txtPrice.paintFlags =
                        binding.txtPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    if (dashboardProduct.rx_type == "NON-RX" || dashboardProduct.rx_type == "") {
                        binding.txtDiscountedPrice.text = String.format(
                            getString(R.string.amount_string),
                            dashboardProduct.sale_price
                        )
                    } else {
                        binding.txtDiscountedPrice.text = String.format(
                            getString(R.string.amount_string),
                            dashboardProduct.rx_offer_mrp
                        )
                    }

                    if (binding.txtPrice.text == binding.txtDiscountedPrice.text) {
                        binding.txtPrice.hide()
                    } else {
                        binding.txtPrice.show()
                    }

                    CustomBindingAdapter.setOfferText(binding.txtOfferText, dashboardProduct)

                    if (dashboardProduct.expiry_date.isNullOrEmpty() || dashboardProduct.expiry_date == "null") {
                        binding.llExpiry.hide()
                    } else {
                        binding.txtExpiryDate.text = dashboardProduct.expiry_date
                    }

                    if (dashboardProduct.country_origin.isNullOrEmpty() || dashboardProduct.country_origin == "null") {
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
                    it?.data?.data?.variants?.let { variants ->
                        processVariants(variants)
                    }

                    cartViewModel.getCartDetails()
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

        productDetailsViewModel.getProductDetails(productid)

        cartViewModel.cartDetailsResponseLiveData.observe(
            viewLifecycleOwner
        ) {

            LogUtil.printObject("----->product details fragment: cartDetailsResponseLiveData")

            when (it) {
                is Resource.Success -> {

                    processCartData(it?.data?.data?.cart?.products ?: arrayListOf())

                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )

                }
            }
        }

        cartViewModel.addToCartResponseLiveData.observe(
            viewLifecycleOwner
        ) {

            LogUtil.printObject("----->product details fragment: addToCartResponseLiveData")

            when (it) {
                is Resource.Success -> {

                    /*(activity as DashboardActivity).cartViewModel.addToCartResponseLiveData.value =
                        it*/
                    processCartData(it?.data?.data?.cart?.products ?: arrayListOf())

                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        binding.txtAddToCart.setOnClickListener {
            var newQuantity = 1
            cartViewModel.addToCart(productid.toInt(), newQuantity, from = "product details")
        }

        activity?.hidekeyboard(binding.root)

        binding.txtGotoCart.setOnClickListener {
            var cartFragment = CartFragment.newInstance()
            (activity as DashboardActivity).addOrReplaceFragment(cartFragment, true)
        }

        return binding.root
    }

    private fun processCartData(products: java.util.ArrayList<GetCartDetailsResponse.CartProduct>) {
        cartQuantity = 0

        var isProductedAddedToCart = false
        for (i in products.indices) {
            if (!isProductedAddedToCart) {
                if (products[i].product.product_id.toString() == productid) {
                    isProductedAddedToCart = true
                    cartQuantity = products[i].qty
                }
            }
        }

        if (!isProductedAddedToCart) {
            binding.txtAddToCart.show()
            binding.txtGotoCart.hide()
        } else {
            binding.txtAddToCart.hide()
            binding.txtGotoCart.show()
        }
    }

    private fun processVariants(variants: ArrayList<DashboardResponse.ProductVariant>) {
        sizesList.clear()
        otherOptionsList.clear()

        for (i in variants.indices) {
            if (variants[i].variant_name.lowercase() == "sizes") {
                for (j in variants[i].values.indices) {
                    //if (variants[i].values[j].product_id.toString() != productid) {
                    var tmpVariant = variants[i].values[j]
                    tmpVariant.currentProducId = productid
                    sizesList.add(tmpVariant)
                    //}
                }
            } else {
                for (j in variants[i].values.indices) {
                    //if (variants[i].values[j].product_id.toString() != productid) {
                    var tmpVariant = variants[i].values[j]
                    tmpVariant.currentProducId = productid
                    otherOptionsList.add(tmpVariant)
                    //}
                }
            }
        }


        sizesAdapter.notifyDataSetChanged()
        otherOptionsAdapter.notifyDataSetChanged()

        if (sizesList.size > 1) {
            binding.llSizes.show()
        } else {
            binding.llSizes.hide()
        }

        /* Not showing other options in v1 */
        if (otherOptionsList.size == 0) {
            binding.llOtherOptions.hide()
        } else {
            binding.llOtherOptions.hide()
        }
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

        if (productImages.size == 0) {
            binding.llProductImages.hide()
            binding.defaultImage.show()
        } else {
            binding.llProductImages.show()
            binding.defaultImage.hide()
        }

    }

    var product_brief = ""
    var description = ""
    var short_description = ""
    var long_description = ""

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

        binding.tabLayout.removeAllTabs()
        strDescription = ""
        strContraindications = ""
        strSafetyAdvice = ""
        str_how_drug_works = ""
        str_missed_dose = ""
        str_quick_tips = ""
        str_drug_interactions = ""
        str_benefits = ""
        str_storage_conditions = ""
        str_uses = ""
        str_ingredients = ""
        str_side_effects = ""


        product_brief = dashboardProduct.product_brief
        description = dashboardProduct.description
        short_description = dashboardProduct.short_description
        long_description = dashboardProduct.long_description

        strDescription += if (product_brief.isNullOrEmpty() || product_brief == "null") "" else product_brief
        strDescription += if (description.isNullOrEmpty() || description == "null") "" else description
        strDescription += if (short_description.isNullOrEmpty() || short_description == "null") "" else short_description
        strDescription += if (long_description.isNullOrEmpty() || long_description == "null") "" else long_description

        if (!strDescription.isNullOrEmpty()) {
            binding.tabLayout.addTab(
                binding.tabLayout.newTab().setText("Product Description")
            )
            setDescriptionText(strDescription, true)

        }

        if (!dashboardProduct.contraindications.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Contraindications"))
            strContraindications = dashboardProduct.contraindications
            if (binding.txtProductDescription.text.isNullOrEmpty()) {
                binding.txtProductDescription.text = strContraindications
            }

            setDescriptionText(strContraindications, true)
        }

        if (!dashboardProduct.safety_advice.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Safety Advice"))
            strSafetyAdvice = dashboardProduct.safety_advice

            setDescriptionText(strSafetyAdvice, true)
        }

        if (!dashboardProduct.how_drug_works.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("How Drug Works"))
            str_how_drug_works = dashboardProduct.how_drug_works

            setDescriptionText(str_how_drug_works, true)
        }

        if (!dashboardProduct.missed_dose.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Missed Dose"))
            str_missed_dose = dashboardProduct.missed_dose

            setDescriptionText(str_missed_dose, true)
        }

        if (!dashboardProduct.quick_tips.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Quick Tips"))
            str_quick_tips = dashboardProduct.quick_tips

            setDescriptionText(str_quick_tips, true)
        }

        if (!dashboardProduct.drug_interactions.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Drug Interactions"))
            str_drug_interactions = dashboardProduct.drug_interactions

            setDescriptionText(str_drug_interactions, true)
        }

        if (!dashboardProduct.benefits.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Benefits"))
            str_benefits = dashboardProduct.benefits

            setDescriptionText(str_benefits, true)
        }

        if (!dashboardProduct.storage_conditions.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Storage Conditions"))
            str_storage_conditions = dashboardProduct.storage_conditions

            setDescriptionText(str_storage_conditions, true)
        }

        if (!dashboardProduct.uses.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Uses"))
            str_uses = dashboardProduct.uses

            setDescriptionText(str_uses, true)
        }

        if (!dashboardProduct.ingredients.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Ingredients"))
            str_ingredients = dashboardProduct.ingredients

            setDescriptionText(str_ingredients, true)
        }

        if (!dashboardProduct.side_effects.isNullOrEmpty()) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Side Effects"))
            str_side_effects = dashboardProduct.side_effects

            setDescriptionText(str_side_effects, true)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.text) {
                    "Product Description" -> {
                        setDescriptionText(strDescription)
                    }
                    "Contraindications" -> {
                        setDescriptionText(strContraindications)
                    }
                    "Safety Advice" -> {
                        setDescriptionText(strSafetyAdvice)
                    }
                    "How Drug Works" -> {
                        setDescriptionText(str_how_drug_works)
                    }
                    "Missed Dose" -> {
                        setDescriptionText(str_missed_dose)
                    }
                    "Quick Tips" -> {
                        setDescriptionText(str_quick_tips)
                    }
                    "Drug Interactions" -> {
                        setDescriptionText(str_drug_interactions)
                    }
                    "Benefits" -> {
                        setDescriptionText(str_benefits)
                    }
                    "Storage Conditions" -> {
                        setDescriptionText(str_storage_conditions)
                    }
                    "Uses" -> {
                        setDescriptionText(str_uses)
                    }
                    "Ingredients" -> {
                        setDescriptionText(str_ingredients)
                    }
                    "Side Effects" -> {
                        setDescriptionText(str_side_effects)
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    fun setDescriptionText(str: String, isFirstTime: Boolean = false) {
        if (isFirstTime) {
            if (binding.txtProductDescription.text.toString().trim().isNullOrEmpty()) {
                binding.txtProductDescription.text = str
            }
        } else {
            binding.txtProductDescription.text = str
        }
    }

}