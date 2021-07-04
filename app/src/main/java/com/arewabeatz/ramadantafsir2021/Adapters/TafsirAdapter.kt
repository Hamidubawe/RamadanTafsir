package com.arewabeatz.ramadantafsir2021.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.arewabeatz.ramadantafsir2021.AudioModel
import com.arewabeatz.ramadantafsir2021.PlayerActivity
import com.arewabeatz.ramadantafsir2021.R
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class TafsirAdapter(val context: Context, private var songList: List<AudioModel>):
    RecyclerView.Adapter<TafsirAdapter.Holder>() {

    private val DEFAULT_VIEW_TYPE = 1
    private val NATIVE_AD_VIEW_TYPE = 2
    lateinit var mInterstitialAd: InterstitialAd


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        /*return if (viewType == DEFAULT_VIEW_TYPE){

            val view = LayoutInflater.from(context).inflate(R.layout.tafsir_layout, parent, false)
            Holder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(
                R.layout.native_ad_layout,
                parent,
                false
            )
            NativeAdHolder(view, context)
        }

         */

        val view = LayoutInflater.from(context).inflate(R.layout.tafsir_layout, parent, false)
        return Holder(view)

    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val songTitle: TextView = itemView.findViewById(R.id.songTitle)
        val songArtist: TextView = itemView.findViewById(R.id.songArtist)
        val albumArt: ImageView = itemView.findViewById(R.id.albumart)
        val menuMore: ImageView = itemView.findViewById(R.id.menuMore)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val song = songList[position]
        try {
            Picasso.get().load(song.audioImage).placeholder(R.drawable.ic_book)
                .into(holder.albumArt)
        } catch (e: Exception) {
            holder.albumArt.setImageResource(R.drawable.ic_book)
        }



        holder.songTitle.text = song.audioTitle
        holder.songArtist.text = song.malam


        holder.itemView.setOnClickListener {


            FirebaseDatabase.getInstance().reference.child("Plays")
                .child(songList[position].audioId).push().setValue("played")

            val i = Intent(context, PlayerActivity::class.java)
            i.putExtra("audioId", song.audioId)
            i.putExtra("audioUrl", song.audioUrl)
            i.putExtra("audioArtist", song.malam)
            i.putExtra("audioTitle", song.audioTitle)
            i.putExtra("audioImage", song.audioImage)
            context.startActivity(i)

        }

        holder.menuMore.setOnClickListener {

            val actions = arrayOf<CharSequence>(
                "Share Song"
            )

            val builder =
                AlertDialog.Builder(context)
                    .setTitle("Select Action")
                    .setItems(actions) { _, i ->
                        if (i == 0) {
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Download this awesome app and listen to ${songList[position].audioTitle}, by ${songList[position].malam} " +
                                            "Download the app for free on play store https://play.google.com/store/apps/details?id=com.arewabeatz"
                                )
                                type = "text/plain"
                            }

                            val sharedIntent = Intent.createChooser(intent, null)
                            context.startActivity(sharedIntent)
                        }

                    }
            builder.show()

        }

        /*if(mHolder.itemViewType == DEFAULT_VIEW_TYPE){

            val holder : Holder = mHolder as Holder

        }

         */

    }

    /*override fun getItemViewType(position: Int): Int {

        return if ((position + 1) %4 == 0 && (position + 1) != 0) {
            NATIVE_AD_VIEW_TYPE
        }else
            DEFAULT_VIEW_TYPE
    }*/


    override fun getItemCount(): Int {
        return songList.size
    }


    /*class NativeAdHolder(view: View, context: Context): RecyclerView.ViewHolder(view){

        private lateinit var template: TemplateView
        val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
            .forUnifiedNativeAd { ad: UnifiedNativeAd ->
                // Show the ad.
                val styles = NativeTemplateStyle.Builder()
                    .withMainBackgroundColor(ColorDrawable(Color.BLACK)).build()


                template = view.findViewById(R.id.my_template)
                template.setStyles(styles)
                template.setNativeAd(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    AdRequest.Builder().build()
                }

                override fun onAdLoaded() {
                    template.visibility = View.VISIBLE

                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build()).build()

            .loadAd(AdRequest.Builder().build())


    }*/


    /*class NativeAdHolder(view: View, context: Context): RecyclerView.ViewHolder(view){

        private lateinit var template: TemplateView
        var adRequest: AdRequest = AdRequest.Builder().build()
        val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd {
                val styles: NativeTemplateStyle =
                    NativeTemplateStyle.Builder()
                        .withMainBackgroundColor(ColorDrawable(Color.BLACK))
                        .build()
                val template: TemplateView = view.findViewById(R.id.my_template)
                template.setStyles(styles)
                template.setNativeAd(it)
            }
            .build()
        .loadAd(AdRequest.Builder().build())


           /* .forUnifiedNativeAd { ad: UnifiedNativeAd ->
                // Show the ad.
                val styles = NativeTemplateStyle.Builder()
                    .withMainBackgroundColor(ColorDrawable(Color.BLACK)).build()


                template = view.findViewById(R.id.my_template)
                template.setStyles(styles)
                template.setNativeAd(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    adRequest = AdRequest.Builder().build()
                }

                override fun onAdLoaded() {
                    template.visibility = View.VISIBLE
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build()).build()

            .loadAd(adRequest)*/


        /*val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        private val adView = inflater.inflate(R.layout.native_ad_layout, null) as NativeAdView
        var adRequest: AdRequest = AdRequest.Builder().build()

        val builder = AdLoader.Builder(context, "<your ad unit ID>")
            .forNativeAd { nativeAd ->

                val headlineView = adView.findViewById<TextView>(R.id.layout_header)
                headlineView.text = nativeAd.headline
                adView.headlineView = headlineView

                val contentView = adView.findViewById<TextView>(R.id.layout_content)
                headlineView.text = nativeAd.body
                adView.headlineView = contentView

                val buttonView = adView.findViewById<Button>(R.id.layout_button)
                headlineView.text = nativeAd.body
                adView.headlineView = buttonView

                val imageView = adView.findViewById<ImageView>(R.id.layout_photo)
                headlineView.text = nativeAd.body
                adView.headlineView = imageView

                adView.setNativeAd(nativeAd)

                view.removeAllViews()
                view.addView(adView)
            }
            .withAdListener(object : AdListener(){
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adView.visibility = View.VISIBLE
                }
            })
            .build()
            .loadAd(adRequest)*/
    }*/


}