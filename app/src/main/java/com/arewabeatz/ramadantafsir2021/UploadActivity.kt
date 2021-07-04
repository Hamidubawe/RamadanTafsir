package com.arewabeatz.ramadantafsir2021

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arewabeatz.ramadantafsir2021.Notification.NotificationData
import com.arewabeatz.ramadantafsir2021.Notification.PushNotification
import com.arewabeatz.ramadantafsir2021.Notification.RetrofitInstance
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UploadActivity : AppCompatActivity() {

    private lateinit var storageRef: StorageReference
    private lateinit var reference: DatabaseReference
    private val GALLERY_PICK = 101

    private lateinit var malamSpinner: Spinner
    private lateinit var audioUrl: String
    private var audioImageUrl: String = ""
    private lateinit var pd: ProgressDialog
    private var malam: String = ""
    private lateinit var audioUri: Uri
    private lateinit var audioId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)


        storageRef = FirebaseStorage.getInstance().reference.child("Tafsir")
        reference = FirebaseDatabase.getInstance().reference.child("Tafsir")

        malamSpinner = findViewById(R.id.malamSpinner)

        pd = ProgressDialog(this)
        pd.setCanceledOnTouchOutside(false)

        val malamList = arrayOf(
            "Nothing Selected",
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

        val malamAdapt = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, malamList)
        malamSpinner.adapter = malamAdapt

        malamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                malam = parent!!.getItemAtPosition(position).toString()
                Toast.makeText(this@UploadActivity, "$malam selected", Toast.LENGTH_SHORT)
                    .show()

                when (position) {
                    1 -> {
                        audioImageUrl = Constants.malamGadonKaya

                    }
                    2 -> {
                        audioImageUrl = Constants.malamBasheer
                    }
                    3 -> {
                        audioImageUrl = Constants.malamPantami
                        Toast.makeText(this@UploadActivity, "$audioImageUrl selected", Toast.LENGTH_SHORT)
                            .show()
                    }
                    4 -> {
                        audioImageUrl = Constants.malamDahiru
                    }
                    5 -> {
                        audioImageUrl = Constants.malamBalalau
                    }
                    6 -> {
                        audioImageUrl = Constants.malamRijiyarLemo
                    }
                    7 -> {
                        audioImageUrl = Constants.malamGombe
                    }
                    8 -> {
                        audioImageUrl = Constants.malamJingir
                    }
                    9 -> {
                        audioImageUrl = Constants.malamMansur
                    }
                    10 -> {
                        audioImageUrl = Constants.malamDaurawa
                    }
                }

                try {
                    albumCover.visibility = View.VISIBLE
                    Picasso.get().load(audioImageUrl).into(albumCover)
                } catch (e:Exception){
                    albumCover.visibility = View.GONE
                }

            }



        }


        addTafsir.setOnClickListener {
            val pickAudio = Intent()
            pickAudio.type = "audio/*"
            pickAudio.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(pickAudio, GALLERY_PICK)
        }

        publishBtn.setOnClickListener {

            if (malam.isNotEmpty() || songTitle.text.isNotEmpty() || songDescription.text.isNotEmpty()) {

                pd.show()
                uploadSOngToFirebase()

            } else {

                songTitle.error = "Field cannot be blank"
                songDescription.error = "Field cannot be blank"
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data!!.data != null) {
            audioUri = data.data!!


        }


    }

    private fun getFileExtension(uri: Uri): String? {

        val contentResolver = contentResolver
        val mimetype = MimeTypeMap.getSingleton()
        return mimetype.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadSOngToFirebase() {

        reference = FirebaseDatabase.getInstance().reference.child("Tafsir")
        audioId = reference.push().key!!
        val title = songTitle.text.toString()
        val description = songDescription.text.toString()

        val filepath = storageRef.child(audioId + "." + getFileExtension(audioUri))

        filepath.putFile(audioUri)
            .addOnSuccessListener {
                filepath.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        audioUrl = task.result.toString()

                        val audioModel = AudioModel(
                            "admin", audioId, title, audioUrl, malam, description,
                            audioImageUrl
                        )

                        reference.child(audioId).setValue(audioModel)
                            .addOnCompleteListener { ok ->
                                if (ok.isSuccessful) {

                                    PushNotification(
                                        NotificationData(audioId, malam, audioImageUrl, title, description, audioUrl, "song"), TOPIC).also {
                                        sendNotification(it)
                                    }


                                    Toast.makeText(
                                        this, "Uploaded successfully..",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    pd.dismiss()
                                    songDescription.setText("")
                                    songTitle.setText("")

                                } else {
                                    Toast.makeText(
                                        this, "Uploaded error..",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }
                    }
                }
            }
            .addOnProgressListener {
                pd.setMessage("Uploading: ${(it.bytesTransferred / it.totalByteCount) * 100.0}%..")
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, it.message,
                    Toast.LENGTH_LONG
                ).show()
                pd.dismiss()
            }

    }

    private fun sendNotification(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch{

        try {
            val response = RetrofitInstance.api.postNotification(notification)

            when {
                response.isSuccessful -> {

                }
                else -> {
                    Toast.makeText(
                        this@UploadActivity, response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
        catch (e: Exception){

        }

    }


}