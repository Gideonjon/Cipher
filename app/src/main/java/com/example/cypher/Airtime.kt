package com.example.cypher

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cypher.databinding.FragmentAirtimeBinding
import com.google.android.material.snackbar.Snackbar
import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.cypher.models.BillsClient
import com.example.cypher.utils.AirtimeBuyResponse
import com.example.cypher.utils.NetworkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Airtime : Fragment() {
    private var _binding: FragmentAirtimeBinding? = null
    private val binding get() = _binding!!
    private var dialog: Dialog? = null
    private var selectedNetworkCode: String? = null


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

        val sharedPreferences =
            requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", "") ?: ""

        if (authToken.isNotEmpty()) {
            getNetworkList(authToken)
        } else {
            Toast.makeText(requireContext(), R.string.error_auth_token_missing, Toast.LENGTH_SHORT)
                .show()
        }




        binding.payBtn.setOnClickListener {
            if (validateFields()) {
                makeAirtimePurchase(authToken)
            }
        }


        return view
    }

    private fun validateFields(): Boolean {
        val network = binding.networkName.text.toString().trim()
        val phoneNumber = binding.phoneNumberEt.text.toString().trim()
        val amount = binding.amountEt.text.toString().trim()

        if (network.isEmpty()) {
            showToast("Please select a network.")
            return false
        }

        if (phoneNumber.isEmpty() || phoneNumber.length != 11 || !phoneNumber.all { it.isDigit() }) {
            showToast("Enter a valid 11-digit phone number.")
            return false
        }

        if (amount.isEmpty() || !amount.all { it.isDigit() } || amount.toInt() < 100) {
            showToast("Amount must be a valid number and at least 100.")
            return false
        }


        return true
    }

    private fun makeAirtimePurchase(authToken: String) {
        val phoneNumber = binding.phoneNumberEt.text.toString().trim()
        val amount = binding.amountEt.text.toString().trim()

        showProgressBar(R.layout.progress_bar)
        BillsClient.instance(requireContext())
            .buyAirtime(
                "Bearer $authToken",
                selectedNetworkCode!!,
                phoneNumber,
                amount,
            ).enqueue(object : Callback<AirtimeBuyResponse> {
                override fun onResponse(
                    call: Call<AirtimeBuyResponse>,
                    response: Response<AirtimeBuyResponse>
                ) {
                    hideProgressBar()
                    if (response.code() == 403) {
                        Toast.makeText(
                            requireContext(),
                            "Unauthorized access. Please log in again.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Optionally redirect the user to the login screen
                    } else if (response.isSuccessful) {
                        showToast("Airtime Was Successful")
                    } else {
                        showToast("Cipher Protocol Is Down")

                    }
                }


                override fun onFailure(call: Call<AirtimeBuyResponse>, t: Throwable) {

                    hideProgressBar()
                    Log.e("Airtime", "Error: ${t.localizedMessage}")
                    showToast("Airtime purchase failed. Check your internet connection.")
                }
            })

    }


    private fun getNetworkList(authToken: String) {
        showProgressBar(R.layout.progress_bar)
        BillsClient.instance(requireContext()).getNetworkList("Bearer $authToken")
            .enqueue(object : Callback<NetworkResponse> {
                override fun onResponse(
                    call: Call<NetworkResponse>,
                    response: Response<NetworkResponse>
                ) {
                    hideProgressBar()
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.data?.let { networks ->
                            val sortedNetworks = networks.sortedBy { it.network }
                            val networkNames = sortedNetworks.map { it.network }
                            val networkCodes = sortedNetworks.associateBy({ it.network }, { it.id })
                            setupNetworkDropdown(networkNames, networkCodes)
                        }
                    } else {
                        Log.e(
                            "Airtime",
                            "Response Code: ${response.code()}, Message: ${response.message()}"
                        )
                        showToast("Response Code: ${response.code()}, Message: ${response.message()}")
                    }


                }


                override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                    hideProgressBar()
                    showToast("Failed to load networks. Please try again.")

                }


            })


    }


    private fun setupNetworkDropdown(
        networkNames: List<String>,
        networkCodes: Map<String, String>
    ) {
        val networkIcons = mapOf(
            "MTN" to R.drawable.mtn,
            "Glo" to R.drawable.glo,
            "Airtel" to R.drawable.airtel,
            "9mobile" to R.drawable.etisalat
        )
        val arrayAdapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.bank,
            networkNames
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val inflater = LayoutInflater.from(context)
                val view =
                    convertView ?: inflater.inflate(R.layout.bank, parent, false)
                val icon = view.findViewById<ImageView>(R.id.networkIcon)
                val name = view.findViewById<TextView>(R.id.networkName)

                val networkName = getItem(position)
                name.text = networkName
                icon.setImageResource(networkIcons[networkName] ?: R.drawable.logo)
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return getView(position, convertView, parent)
            }
        }


        binding.networkName.setAdapter(arrayAdapter)

        binding.networkName.setOnItemClickListener { _, _, position, _ ->
            val selectedNetwork = networkNames[position]
            selectedNetworkCode = networkCodes[selectedNetwork]
        }
    }


    private fun showProgressBar(layoutResId: Int) {
        dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(layoutResId)
            setCanceledOnTouchOutside(false)
            show()
        }


        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                dialog?.dismiss()
            }
        }.start()
    }

    private fun hideProgressBar() {
        dialog?.dismiss()
    }

    private fun showToast(message: String) {
        val rootView = binding.root
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}