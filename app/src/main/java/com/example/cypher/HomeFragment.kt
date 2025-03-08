package com.example.cypher

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.cypher.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar


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

        binding.airtime.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_airtime2)
        }
        binding.airtime.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_data)
        }
        binding.buy.setOnClickListener {
            showSnackBar("Coming Soon!!")
        }
        binding.send.setOnClickListener {
            showSnackBar("Coming Soon!!")

        }
        binding.receive.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_receiveFunds)
        }

        binding.medical.setOnClickListener {
            showSnackBar("Coming Soon!!")

        }

        binding.merchant.setOnClickListener {
            showSnackBar("Coming Soon!!")

        }

        binding.addition.setOnClickListener {
            showSnackBar("Coming Soon!!")

        }

        binding.electricity.setOnClickListener {
            showSnackBar("Coming Soon!!")

        }

        binding.voucher.setOnClickListener {
            showSnackBar("Coming Soon!!")

        }





        return view

    }

    private fun showSnackBar(response: String) {
        val rootView = binding.root
        Snackbar.make(rootView, response, Snackbar.LENGTH_SHORT).show()
    }

    private fun setStatusBarColor(color: Int) {
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.window.statusBarColor = color
            }
        }
    }


}