package com.arewabeatz.ramadantafsir2021.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.arewabeatz.ramadantafsir2021.MainActivity
import com.arewabeatz.ramadantafsir2021.PlayerActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
import com.arewabeatz.ramadantafsir2021.R

private const val CHANNEL_ID = "notifi"

class FirebaseService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        lateinit var intent: Intent

        when {
            message.data["type"] == "song" -> {
                intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("audioTitle", message.data["audioTitle"] )
                intent.putExtra("audioUrl", message.data["audioUrl"] )
                intent.putExtra("audioId", message.data["audioId"] )
                intent.putExtra("audioArtist", message.data["audioArtist"] )
                intent.putExtra("audioImage", message.data["audioImage"] )
            }

            else -> {
                intent = Intent(this, MainActivity::class.java)
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotification(notificationManager)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["audioTitle"])
            .setContentText(" By ${message.data["audioDescription"]}")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_mic))
            .setSmallIcon(R.drawable.ic_mic)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(notificationManager: NotificationManager) {

        val channelName = "Notifications"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Tafsir Not"
            enableLights(true)
            //enableVibration(true)
            lightColor = Color.BLUE
        }

        notificationManager.createNotificationChannel(channel)
    }

}