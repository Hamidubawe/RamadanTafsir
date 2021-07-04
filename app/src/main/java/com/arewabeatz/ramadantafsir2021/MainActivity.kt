package com.arewabeatz.ramadantafsir2021

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.arewabeatz.ramadantafsir2021.Fragments.MalamFragment
import com.arewabeatz.ramadantafsir2021.Fragments.TafsirFragment
import com.arewabeatz.ramadantafsir2021.Notification.ReminderBroadcast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

const val TOPIC = "/topics/Notifications"
class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var bottomNav : BottomNavigationView
    }
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        scheduleNotification()

        setSupportActionBar(mainToolbar)
        mainToolbar.overflowIcon!!.setColorFilter(
            ContextCompat.getColor(this, R.color.gnt_white),
            PorterDuff.Mode.SRC_ATOP
        )


        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        //setting  up banner ads
        mAdView = findViewById(R.id.mAdView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        bottomNav = findViewById(R.id.bottomNavigation)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, MalamFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        bottomNav.setOnNavigationItemSelectedListener { item ->

            when(item.itemId){


                R.id.tafsirs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, TafsirFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }

                R.id.malam -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, MalamFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }

                /*R.id.addTafsir -> {
                    val i = Intent(this, UploadActivity::class.java)
                    startActivity(i)
                }

                 */

            }
            true

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Inflate the menu; //this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.share -> {

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Download this awesome Tafsir app from playstore and listen to various tafsirs "
                    )
                    type = "text/plain"
                }

                val sharedIntent = Intent.createChooser(intent, null)
                startActivity(sharedIntent)
            }

            R.id.feedBack -> {
                val email = Intent(Intent.ACTION_SEND)
                email.type = "text/email"
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf("hamidubawe9@gmail.com"))
                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback Ramadan Tafsir App")
                email.putExtra(Intent.EXTRA_TEXT, "Assalamu Alaikum," + " ")
                email.setPackage("com.google.android.gm")
                startActivity(Intent.createChooser(email, "Send Feedback:"))
            }


            /*R.id.support -> {

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
                startActivity(browserIntent)
            }

             */
            R.id.exit -> {
                super.onBackPressed();

            }
            R.id.aboutMe -> {
                val i = Intent(this, DevActivity::class.java)
                startActivity(i)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun scheduleNotification() {

        val intent = Intent(this, ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val currentTime = System.currentTimeMillis()
        val alarmTime = 18800000

        alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + alarmTime, pendingIntent)
    }
}