package com.example.cypher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cypher.databinding.FragmentDataBinding


class Data : Fragment() {
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.arrow.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }


}