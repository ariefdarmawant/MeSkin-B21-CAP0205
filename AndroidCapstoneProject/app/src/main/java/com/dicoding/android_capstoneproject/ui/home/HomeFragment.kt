package com.dicoding.android_capstoneproject.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.android_capstoneproject.R
import com.dicoding.android_capstoneproject.databinding.FragmentHomeBinding
import com.google.firebase.analytics.connector.AnalyticsConnectorImpl.getInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream
import java.util.Calendar.getInstance


class HomeFragment : Fragment() {
    companion object{
        const val REQUEST_CAMERA = 100
    }
    private lateinit var imageUri: Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      val btn_Camera: Button = view.findViewById(R.id.btnCamera)
      btn_Camera.setOnClickListener{
          intentCamera()
      }
      //val btn_Gallery: Button = view.findViewById(R.id.btnGallery)
     // btn_Gallery.setOnClickListener{
       //   intentGalery() }
  }
    private fun intentCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {intent->
            activity?.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, REQUEST_CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== REQUEST_CAMERA&&resultCode==RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImage(imgBitmap)
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
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
                          imageView.setImageBitmap(imgBitmap)
                      }
                    }
                }
            }
    }



        
   }
