package com.arewabeatz.ramadantafsir2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arewabeatz.ramadantafsir2021.Adapters.TafsirAdapter
import com.arewabeatz.ramadantafsir2021.Fragments.TafsirFragment
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_malam.*

class MalamActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    companion object {
        lateinit var songList: List<AudioModel>
    }

    private lateinit var reference: DatabaseReference
    private lateinit var tafsirAdapter: TafsirAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var mAdView: AdView

    private var malam = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_malam)
        setSupportActionBar(malamToolbar)

        //setting  up banner ads
        mAdView = findViewById(R.id.mAdView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        malam = intent.getStringExtra("malam")!!

        supportActionBar!!.title = malam

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        shimmerLayout = findViewById(R.id.shimmer_layout)
        shimmerLayout.visibility = View.VISIBLE
        shimmerLayout.startShimmer()

        recyclerView = findViewById(R.id.malamTafsirRecycler)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        songList = ArrayList()

        reference = FirebaseDatabase.getInstance().reference.child("Tafsir")
        TafsirFragment.songList = ArrayList()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (ds in snapshot.children) {

                        val audio: AudioModel? = ds.getValue(AudioModel::class.java)
                        if (audio!!.malam == malam){
                            (songList as ArrayList<AudioModel>).add(audio)
                        }


                    }

                    tafsirAdapter = TafsirAdapter(this@MalamActivity, songList)
                    recyclerView.adapter = tafsirAdapter
                    tafsirAdapter.notifyDataSetChanged()
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }




            }

            override fun onCancelled(error: DatabaseError) {


            }
        })
    }
}