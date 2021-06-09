package com.dicoding.android_capstoneproject.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.android_capstoneproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class HomeFragment() : Fragment() {
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCamera.setOnClickListener {
            camera()
        }
        btnGallery.setOnClickListener {
            gallery()
        }
        //btnUpload.setOnClickListener{
          //  upload()
        //}
    }



    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    private fun camera() {
       val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       startActivityForResult(intent, 2)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2) {
            var bmp = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bmp)
            uploadImagecamera(bmp)
        }else if(requestCode ==1){
            imageView.setImageURI(data?.data)
            

        }
    }
        private fun uploadImagecamera(imgBitmap: Bitmap) {
            val baos = ByteArrayOutputStream()
            val ref = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")

            imgBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
            val image = baos.toByteArray()
            ref.putBytes(image)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        ref.downloadUrl.addOnCompleteListener{
                            it.result?.let {
                                imageUri = it
                                val policy = ThreadPolicy.Builder()
                                    .permitAll().build()
                                StrictMode.setThreadPolicy(policy)
                                var client = OkHttpClient();
                                System.out.println(imageUri)
                                val httpUrl = HttpUrl.parse("http://35.232.229.221:5000/predict") ?: throw IllegalArgumentException("wrong url")
                                val httpUrlBuilder = httpUrl.newBuilder()
                                val requestBuilder = Request.Builder().url(httpUrlBuilder.build())
                                val jsonString = "{\"image_path\":\"$imageUri\"}"
                                val mediaTypeJson = MediaType.parse("application/json; charset=utf-8") ?: throw IllegalArgumentException("wrong media type")
                                requestBuilder.post(RequestBody.create(mediaTypeJson, jsonString));
                                val respon : Response = client.newCall(requestBuilder.build()).execute();
                                val result = JSONObject(respon.body()!!.string())
                                if(result["status"].toString()!="SUCCESS"){
                                    resultField.text = "Prediction Failed"
                                }
                                else{
                                    resultField.text = "Prediction : " + result["predict"]
                                }
                                imageView.setImageBitmap(imgBitmap)
                            }
                        }
                    }
                }
        }
}