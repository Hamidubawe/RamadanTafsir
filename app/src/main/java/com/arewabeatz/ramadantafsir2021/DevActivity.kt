package com.arewabeatz.ramadantafsir2021

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dev.*

class DevActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev)
        socialButtons()

        userPhoto.setImageResource(R.drawable.myphoto)
    }

    private fun socialButtons() {

        instagramBtn.setOnClickListener {

            val uri = Uri.parse("http://instagram.com/hamidubawe")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.setPackage("com.instagram.android")

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/hamidubawe"))
                )
            }


        }

        facebookBtn.setOnClickListener {

            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://fb.com/hubawe")))
        }

        twitterBtn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                Uri.parse("http://twitter.com/hamidubawe"))
            )
        }
        whatsAppBtn.setOnClickListener {

            val url = "https://wa.me/2348108730033?text=Hello "
            val contact = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(url))
                .setPackage("com.whatsapp")
            startActivity(contact)
        }
    }
}