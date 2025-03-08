package com.example.cypher

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cypher.databinding.FragmentAirtimeBinding


class Airtime : Fragment() {
    private var _binding: FragmentAirtimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAirtimeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.arrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.phoneNumberEt.filters = arrayOf(InputFilter.LengthFilter(11))

        binding.payBtn.setOnClickListener {


        }


        return view
    }


}