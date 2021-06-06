package com.dicoding.android_capstoneproject.ui.home

import android.os.Bundle
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


class HomeFragment : Fragment() , View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)
       val btn_Camera: Button = view.findViewById(R.id.btnCamera)
        btn_Camera.setOnClickListener(this)
        val btn_Gallery: Button = view.findViewById(R.id.btnGallery)
        btn_Gallery.setOnClickListener(this)
    }

    override fun onClick(v: View) {
    }
}