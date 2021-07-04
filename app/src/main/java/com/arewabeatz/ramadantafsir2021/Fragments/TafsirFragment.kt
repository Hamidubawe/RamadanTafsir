package com.arewabeatz.ramadantafsir2021.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arewabeatz.ramadantafsir2021.Adapters.TafsirAdapter
import com.arewabeatz.ramadantafsir2021.AudioModel
import com.arewabeatz.ramadantafsir2021.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.*


class TafsirFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView

    companion object {
        lateinit var songList: List<AudioModel>
    }

    private lateinit var reference: DatabaseReference
    private lateinit var tafsirAdapter: TafsirAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tafsir, container, false)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        shimmerLayout = view.findViewById(R.id.shimmer_layout)
        shimmerLayout.visibility = View.VISIBLE
        shimmerLayout.startShimmer()

        recyclerView = view.findViewById(R.id.tafsirRecycler)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)

        reference = FirebaseDatabase.getInstance().reference.child("Tafsir")
        songList = ArrayList()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (ds in snapshot.children) {

                        val song: AudioModel? = ds.getValue(AudioModel::class.java)
                        (songList as ArrayList<AudioModel>).add(song!!)

                        if (context != null){
                            tafsirAdapter = TafsirAdapter(context!!, songList)
                            recyclerView.adapter = tafsirAdapter
                            tafsirAdapter.notifyDataSetChanged()
                            shimmerLayout.stopShimmer()
                            shimmerLayout.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                        }

                    }



                }




            }

            override fun onCancelled(error: DatabaseError) {


            }
        })

        return view
    }


}