package com.example.softgamestest.javasriptinterfaces

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.webkit.JavascriptInterface
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.softgamestest.datetimeprovider.DateProvider
import com.example.softgamestest.delay.DelayProvider
import java.time.LocalDate
import java.time.format.DateTimeParseException


/**
 * Triggered from the web-view
 */
class WebAppInterface(
    private val delayProvider: DelayProvider,
    private val dateProvider: DateProvider,
    private val context: Context,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManager?
) {

    /***
     * Concatenate the two given string synchronously.
     * @param firstName First name of the user
     * @param lastName Last name of the user
     */
    @JavascriptInterface
    fun concatenateTwoString(firstName: String, lastName: String): String = firstName.plus(lastName)

    /***
     * Calculate age of the user.
     * @param dateInString Birth of the date
     */
    @JavascriptInterface
    fun calculateAge(dateInString: String): String {
        return try {
            val userBirth = LocalDate.parse(dateInString)
            val actualDate = dateProvider.getCurrentLocalDate()

            delayProvider.delay(5000)
            (actualDate.year - userBirth.year).toString()
        } catch (e: DateTimeParseException) {
            "Unknown"
        }
    }

    /***
     * Shows notification
     */
    @JavascriptInterface
    fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        delayProvider.delay(7000)
        (context as Activity).finish()

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(1, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(com.example.softgamestest.R.string.channel_name)
            val descriptionText =
                context.getString(com.example.softgamestest.R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}