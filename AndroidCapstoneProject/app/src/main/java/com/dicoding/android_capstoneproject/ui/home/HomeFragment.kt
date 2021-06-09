package com.dicoding.android_capstoneproject.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.dicoding.android_capstoneproject.R
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.jar.Manifest


class HomeFragment() : Fragment() {


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
        }else if(requestCode ==1){
            imageView.setImageURI(data?.data)

        }
    }
}