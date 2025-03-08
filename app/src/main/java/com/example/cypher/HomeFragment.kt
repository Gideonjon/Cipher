package com.example.cypher

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.cypher.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var isHidden = false
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val walletAmount = binding.walletAmount
        val originalAmount = walletAmount.text.toString()  // Save original amount

        binding.visibilityIcon.setOnClickListener {
            if (isHidden) {
                walletAmount.text = originalAmount  // Show original amount
                binding.visibilityIcon.setImageResource(R.drawable.visibility) // Change to open eye icon
            } else {
                walletAmount.text = "$ ****"  // Hide amount
                binding.visibilityIcon.setImageResource(R.drawable.visibility_off) // Change to closed eye icon
            }
            isHidden = !isHidden  // Toggle state
        }
        setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.white))


        return view

    }
    private fun setStatusBarColor(color: Int) {
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.window.statusBarColor = color
            }
        }
    }




}