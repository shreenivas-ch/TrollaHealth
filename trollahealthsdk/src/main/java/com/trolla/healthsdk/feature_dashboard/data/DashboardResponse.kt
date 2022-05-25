package com.trolla.healthsdk.feature_dashboard.data

import java.lang.ref.SoftReference

data class DashboardResponse(
    val homePagePositionsList: List<HomePagePositionsListItem>,
    val popularProdList: PopularProductsListItem,
    val recommendedProdList: PopularProductsListItem,
    val newArrivalProdList: PopularProductsListItem
)
{
    data class HomePagePositionsListItem(
        val position:Int,
        val id:Int,
        val name:String,
        val img_url:String,
        val banner_data:List<BannerData>
    )
    {
        data class BannerData(
            val id:Int,
            val placeholder_name:String,
            val banner_url:String,
            val web_banner_url:String,
            val posiiton:Int,
            val placeholder_url:String,

            val tag_id:Int,
            val tag_value:String,
            val concern_url:String,

            val category_id:Int,
            val category_name:String,
            val image_url:String,
            val category_url:String,

        )
    }

    data class PopularProductsListItem(
        val total_records:Int,
        val product_list:List<DashboardProduct>
    )

    data class DashboardProduct(
     val product_id:Int,
     val group_name:String,
     val title:String,
     val product_name:String,
     val hsn_code:String,
     val product_qty:String,
     val short_description:String,
     val long_description:String,
     val packing_form:String,
     val num_of_imgs:String,
     val price_to_retailer:String,
     val group_id:String,
     val mrp:String,
     val purchase_price:String,
     val shipping_cost:String,
     val is_free_product:String,
     val current_stock:String,
     val warehouse_id:String,
     val supplier_id:String,
     val unit:String,
     val volumetric_weight:String,
     val deadweight:String,
     val cgst:String,
     val sgst:String,
     val igst:String,
     val product_group_id:String,
     val is_active:String,
     val product_variant_name:String,
     val manufacture_date:String,
     val sku_code:String,
    val is_main_product:String,
    val product_group_name:String,
    val main_product_id:String,
    val created_format:String,
    val main_product_name:String,
    val product_alias:String,
    val vendor_type:String,
    val vegetarian_type:String,
    val rx_type:String,
    val is_online:String,
    val channel_id:String,
    val shopby_brand:String,
    val brand_name:String,
    val brand_img:String,
    val shopby_condition:String,
    val schedule_type:String,
    val loose_sales_allowed:String,
    val form:String,
    val pack_size:String,
    val product_url:String,
    val discount:String,
    val discount_type:String,
    val sale_price:String,
    val rx_offer_mrp:String,
    val rx_offer_desc:String,
    val category:String,
    val category_head:String,
    val category_class:String,
    val country_origin:String,
    val banned_product:String,
    val drop_shipment:String,
    val seller_code:String,
    val rx_wsp:String,
    val rating:String,
    val is_favourite:String,
    val is_otc:String,
    val out_of_stock:String,
    val eta:String,
    val stock_status:String,
    val is_set:String,
    val prescription_class:String,
    val discount_class:String,
    val optional_class:String,
    val is_perishable:String,
    val only_at_store:String,
    val product_img:List<String>,
    val product_video:List<String>,
    )
}