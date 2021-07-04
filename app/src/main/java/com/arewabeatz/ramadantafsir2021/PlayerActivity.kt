package com.arewabeatz.ramadantafsir2021

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jean.jcplayer.model.JcAudio
import com.example.jean.jcplayer.view.JcPlayerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player.*
import java.io.File


class PlayerActivity : AppCompatActivity() {

    private var audioId = ""
    private var audioTitle = ""
    private var audioArtist = ""
    private var audioUrl = ""
    private var audioImage= ""

    private lateinit var jcPlayer: JcPlayerView
    private val jcAudio: ArrayList<JcAudio> = ArrayList()

    private lateinit var mAdView: AdView
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var mRewardedAd: RewardedAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        InterstitialAd.load(this,"ca-app-pub-1305037545238983/1617651420", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                //mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                //Log.d(TAG, 'Ad was loaded.')
                mInterstitialAd = interstitialAd
            }
        })

        audioId = intent.getStringExtra("audioId")!!
        audioUrl = intent.getStringExtra("audioUrl")!!
        audioArtist = intent.getStringExtra("audioArtist")!!
        audioTitle = intent.getStringExtra("audioTitle")!!
        audioImage = intent.getStringExtra("audioImage")!!

        try{
            songArtistjc.text = audioArtist
            Picasso.get().load(audioImage).placeholder(R.drawable.ic_book).into(musicPhoto)
            getPlayedTimes(0, playedTimes)
        }catch (e:Exception){
            songArtistjc.text = "Akramakallah"
            musicPhoto.setImageResource(R.drawable.ic_play)
        }

        jcPlayer = findViewById(R.id.jcPlayer)

        jcAudio.add(JcAudio.createFromURL(audioTitle, audioUrl))
        jcPlayer.initPlaylist(jcAudio, null)

        if (jcPlayer.isPlaying) {
            jcPlayer.pause()
            jcPlayer.playAudio(jcAudio[0])
        } else {

            jcPlayer.playAudio(jcAudio[0])


        }
        jcPlayer.createNotification(R.drawable.ic_mic)

        shareSongButton.setOnClickListener {

            if (mInterstitialAd != null){
                mInterstitialAd.show(this)
            }

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Download this Ramadan Tafsir app from playstore and listen to $audioTitle}, by $audioArtist} " +
                            "Download the app for free on play store"
                )
                type = "text/plain"
            }

            val sharedIntent = Intent.createChooser(intent, null)
            startActivity(sharedIntent)
        }

        downloadBtn.setOnClickListener {

            if (mInterstitialAd != null){
                mInterstitialAd.show(this)
            }

            Dexter.withContext(this@PlayerActivity).withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_PHONE_STATE
            ).withListener(object : MultiplePermissionsListener {


                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    downloadSongToStorage()
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {

                    permissionToken.continuePermissionRequest()

                }
            }).check()

        }

    }

    private fun getPlayedTimes(position: Int, play: TextView) {

        val ref = FirebaseDatabase.getInstance().reference.child("Plays")
            .child(audioId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val playCount = snapshot.childrenCount.toInt()
                    play.text = playCount.toString()
                }

            }

            override fun onCancelled(error: DatabaseError) {


            }
        })

    }

    private fun downloadSongToStorage() {

        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(audioUrl)
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Downloading...")
        progressDialog.setMessage(title)
        progressDialog.isIndeterminate = true
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)
        progressDialog.show()

        val rootPath = File(Environment.getExternalStorageDirectory(), "My Tafsir")

        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }

        val localFile = File(rootPath, "$title | Arewabeatz.mp4")

        storageRef.getFile(localFile)
            .addOnProgressListener {
                val progress = (it.bytesTransferred / it.totalByteCount) * 100.0
                progressDialog.setMessage("Uploading: $progress%..")
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Download Successfully completed to $rootPath",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

}