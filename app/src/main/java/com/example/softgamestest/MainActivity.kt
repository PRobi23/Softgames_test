package com.example.softgamestest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NotificationCompat
import com.example.softgamestest.datetimeprovider.DateProviderImpl
import com.example.softgamestest.delay.DelayProviderImpl
import com.example.softgamestest.javasriptinterfaces.WebAppInterface
import com.example.softgamestest.ui.theme.SoftgamesTestTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoftgamesTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        LoadWebView()
                    }

                }
            }
        }
    }
}

@Composable
fun LoadWebView() {
    val jsFile = "file:///android_asset/webview.html"

    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            addJavascriptInterface(
                WebAppInterface(
                    dateProvider = DateProviderImpl(),
                    delayProvider = DelayProviderImpl(),
                    context = context,
                    notificationBuilder = NotificationCompat.Builder(context, "1")
                        .setSmallIcon(androidx.core.R.drawable.notification_bg_low)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(R.string.notification_body))
                        .setContentIntent(createPendingIntent(context)),
                    notificationManager = createNotificationChannel(context)
                ), "Android"
            )
            loadUrl(jsFile)
        }
    }, update = {
        it.loadUrl(jsFile)
    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SoftgamesTestTheme {
        LoadWebView()
    }
}

private fun createPendingIntent(context: Context): PendingIntent {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
}

private fun createNotificationChannel(context: Context): NotificationManager? {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Register the channel with the system
        context.getSystemService(
            NotificationManager::class.java
        )
    } else {
        null
    }
}