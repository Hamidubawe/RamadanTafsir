package com.arewabeatz.ramadantafsir2021.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arewabeatz.ramadantafsir2021.Adapters.MalamAdapter
import com.arewabeatz.ramadantafsir2021.R
import kotlinx.android.synthetic.main.fragment_malam.view.*


class MalamFragment : Fragment() {

    private var malam = arrayOf(
        "Sheikh Abdallah Gadon Kaya",
        "Sheikh Basheer Sokoto",
        "Sheikh Ali Pantami",
        "Sheikh Dahiru Bauchi",
        "Sheikh Abdullahi Balalau",
        "Sheikh Muhammad Rijiyar lemo",
        "Sheikh Kabiru Gombe",
        "Sheikh Sani Yahaya Jingir",
        "Sheikh Mansur Sokoto",
        "Sheikh Ibrahim Daurawa",
        )

    private var image = intArrayOf(
        R.drawable.gadonkaya, R.drawable.basheer, R.drawable.pantami, R.drawable.dahirubauchi,
        R.drawable.balalau, R.drawable.rijiyarlemu, R.drawable.kabirugombe, R.drawable.jingir,
        R.drawable.mansursokoto, R.drawable.daurawa
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_malam, container, false)

        val linearLayoutManager = GridLayoutManager(context, 2)
        val recyclerView = view.findViewById<RecyclerView>(R.id.malamRecycler)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)


        val adapter = MalamAdapter(context!!, malam, image)
        recyclerView.adapter = adapter
        view.shimmer_layout.stopShimmer()
        view.shimmer_layout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        return view
    }

}