package com.example.cypher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cypher.databinding.FragmentReceiveFundsBinding
import android.content.Context
import android.content.ClipData
import android.content.ClipboardManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ReceiveFunds : BottomSheetDialogFragment() {
    private var _binding: FragmentReceiveFundsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentReceiveFundsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.copy.setOnClickListener {
            copyToClipboard(binding.walletAddress.text.toString())
        }


        return view
    }

    // Copy Wallet Address
    private fun copyToClipboard(text: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)

        // Show a toast message for feedback
        Toast.makeText(requireContext(), "Wallet Address Copied Successfully", Toast.LENGTH_SHORT)
            .show()
    }

}