package com.aman.bootcamp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import kotlin.math.log
import android.R.attr.start
import android.animation.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class SplashActivity : AppCompatActivity() {

    lateinit var viewTwo: View
    lateinit var logo: ImageView
    lateinit var buildings: ImageView
    lateinit var clNotification: ConstraintLayout
    lateinit var clLocation: ConstraintLayout
    lateinit var clStyle: ConstraintLayout
    val channelId: String = "NotificationChannel"
    val channelName: String = "NotificationChannelName"
    val NOTIFICATION_ID: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initMembers()
        animations()
        setListeners()
    }

    private fun initMembers() {
        viewTwo = findViewById(R.id.viewTwo)
        logo = findViewById(R.id.circle)
        buildings = findViewById(R.id.buildings)
        clNotification = findViewById(R.id.clNotification)
        clLocation = findViewById(R.id.clLocation)
        clStyle = findViewById(R.id.clStyle)
    }

    private fun animations() {
        val animator = ObjectAnimator.ofFloat(logo, View.ROTATION, -360f, 0f)
        //animator.target = logo
        animator.duration = 3000
        animator.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                viewTwo.background  = resources.getDrawable(R.color.colorAccent)
                clLocation.visibility = View.VISIBLE
                clNotification.visibility = View.VISIBLE
                clStyle.visibility = View.VISIBLE
            }
        })
        animator.start()
        val fadeOut = ObjectAnimator.ofFloat(buildings, "alpha", .5f, .1f)
        fadeOut.duration = 2000
        val fadeIn = ObjectAnimator.ofFloat(buildings, "alpha", .1f, .5f)
        fadeIn.duration = 2000

        val mAnimationSet = AnimatorSet()
        mAnimationSet.play(fadeIn).after(fadeOut)
        mAnimationSet.start()
    }



    private fun setListeners() {
        clStyle.setOnClickListener({
            val intent = Intent(this, StyleActivity::class.java)
            finish()
            startActivity(intent)

        })
        clLocation.setOnClickListener({
            val intent = Intent(this, MapActivity::class.java)
            finish()
            startActivity(intent)

        })
        clNotification.setOnClickListener({
            createNotification()
        })
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

            val contentIntent = Intent(this, SplashActivity::class.java)
            val contentPendingIntent = PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                contentIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            val notificationBuilder = NotificationCompat.Builder(this, channelId)

            val notification = notificationBuilder.setAutoCancel(true)
                .setContentTitle("Bootcamp Challenge Notification")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(contentPendingIntent)
                .build()

            notificationManager.notify(123, notificationBuilder.build())

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}

