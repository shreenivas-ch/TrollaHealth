package com.trolla.healthsdk.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.PushnotificationDataModel
import java.util.*

class NotificationHandler {

    fun generateNotification(
        ctx: Context,
        pushnotificationDataModel: PushnotificationDataModel,
        notifyIntent: Intent
    ) {
        var notifyPendingIntent: PendingIntent? = null

        notifyIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val uniqueId = (System.currentTimeMillis() and 0xfffffff).toInt()
        notifyPendingIntent = PendingIntent.getActivity(
            ctx,
            uniqueId,
            notifyIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initNotificationChannels(ctx)
        }

        val channelId = ctx.getString(R.string.notification_channel_id_default)

        val builder = NotificationCompat.Builder(ctx, channelId)
        builder.setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_notification)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(if (pushnotificationDataModel.title.isNotEmpty()) pushnotificationDataModel.title else "${pushnotificationDataModel.type.capitalize()}")
            .setTicker(pushnotificationDataModel.title)
            .setColor(ContextCompat.getColor(ctx, R.color.primary_color))
            .setContentText(pushnotificationDataModel.body)
            .setContentIntent(notifyPendingIntent)

        builder.setStyle(NotificationCompat.BigTextStyle(builder))
            .setSmallIcon(R.drawable.ic_notification)

        builder.setAutoCancel(true)
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(pushnotificationDataModel.body))
        builder.setContentIntent(notifyPendingIntent)

        val nm = ctx.getSystemService(
            Context
                .NOTIFICATION_SERVICE
        ) as NotificationManager

        if (pushnotificationDataModel.big_image.isNotEmpty()) {

            Handler(Looper.getMainLooper()).post {

                Glide.with(ctx).asBitmap().load(pushnotificationDataModel.big_image)
                    .into(object : CustomTarget<Bitmap?>() {

                        override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            val bps = NotificationCompat.BigPictureStyle().bigPicture(resource)
                                .setBigContentTitle(pushnotificationDataModel.title)
                            bps.setSummaryText(pushnotificationDataModel.body)
                            builder.setStyle(bps)
                            builder.setLargeIcon(
                                BitmapFactory.decodeResource(
                                    ctx.resources,
                                    R.drawable.ic_notification
                                )
                            )
                            var notification2 = builder.build()
                            nm.notify(generateRandom(), notification2)
                        }
                    })
            }
        } else {
            var notification = builder.build()
            nm.notify(generateRandom(), notification)
        }

    }

    private fun initChannels(context: Context) {
        if (Build.VERSION.SDK_INT < 26) {
            return
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "default",
            "Channel name",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Channel description"
        notificationManager.createNotificationChannel(channel)
    }

    private fun generateRandom(): Int {
        val random = Random()
        return random.nextInt(9999 - 1000) + 1000
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNotificationChannels(context: Context) {

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        val channelIdOne = context.getString(R.string.notification_channel_id_default)
        val nameOne = context.getString(R.string.notification_channel_name_default)
        val descriptionOne = context.getString(R.string.notification_channel_description_default)
        val importanceOne = NotificationManager.IMPORTANCE_HIGH

        val channelOne = NotificationChannel(channelIdOne, nameOne, importanceOne)
        channelOne.description = descriptionOne
        channelOne.enableLights(true)
        channelOne.lightColor = ContextCompat.getColor(context, R.color.primary_color)
        channelOne.enableVibration(false)
        mNotificationManager!!.createNotificationChannel(channelOne)
    }
}

