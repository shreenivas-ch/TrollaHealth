package com.trolla.healthsdk.data

data class PushnotificationDataModel(
        var title: String = "",
        var body: String = "",
        var type: String = "",
        var element_id: String = "",
        var big_image: String = "",
        var cod_id: String = "",
        var channel_id: String = "",
        var sender_id: String = "",
        var url: String = "",
        var conversationCount: String = "",
        var is_subscribed: String = ""
)