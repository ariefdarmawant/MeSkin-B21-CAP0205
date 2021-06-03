package com.dicoding.android_capstoneproject.ui.Detect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.android_capstoneproject.databinding.FragmentDetectBinding


class DetectFragment : Fragment() {

    private lateinit var detectViewModel: DetectViewModel
    private var _binding: FragmentDetectBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detectViewModel =
            ViewModelProvider(this).get(DetectViewModel::class.java)

        _binding = FragmentDetectBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}