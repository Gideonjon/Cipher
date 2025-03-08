package com.example.cypher

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.cypher.databinding.FragmentDataBinding
import com.example.cypher.models.BillsClient
import com.example.cypher.utils.DataBuyResponse
import com.example.cypher.utils.DataPlan
import com.example.cypher.utils.DataPlanResponse
import com.example.cypher.utils.NetworkResponse
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Data : Fragment() {
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!
    private var dialog: Dialog? = null
    private val networks = mutableListOf<String>()
    private val networkIds = mutableListOf<String>()
    private val plans = mutableListOf<DataPlan>()


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

        binding.phoneNumberEt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))

        // Fetch available networks (No need to pass authToken)
        getNetworkList()

        binding.payBtn.setOnClickListener {
            if (validateInputs()) {
                val selectedNetworkId =
                    networkIds.getOrNull(networks.indexOf(binding.networkName.text.toString()))
                val selectedPlan =
                    plans.firstOrNull { it.plan == binding.bundleName.text.toString() }

                if (selectedNetworkId != null && selectedPlan != null) {
                    buyData(selectedNetworkId, selectedPlan)
                } else {
                    showToast("Please select a valid network and data plan.")
                }
            }
        }

        return view
    }

    private fun buyData(networkId: String, selectedPlan: DataPlan) {
        val phone = binding.phoneNumberEt.text.toString().trim()
        showProgressBar(R.layout.progress_bar)

        BillsClient.instance().buyData(
            networkId,
            phone,
            selectedPlan.plan_id,
            selectedPlan.price.toString(),
            selectedPlan.plan
        ).enqueue(object : Callback<DataBuyResponse> {
            override fun onResponse(
                call: Call<DataBuyResponse>,
                response: Response<DataBuyResponse>
            ) {
                hideProgressBar()
                when {
                    response.code() == 403 -> showToast("Unauthorized access. Please log in again.")
                    response.isSuccessful -> showToast("Data purchase was successful!")
                    else -> showToast("Cipher Protocol is down")

                }
            }

            override fun onFailure(call: Call<DataBuyResponse>, t: Throwable) {
                hideProgressBar()
                Log.e("Data", "Error: ${t.localizedMessage}")
                showToast("Data purchase failed. Check your internet connection.")
            }
        })
    }

    private fun getNetworkList() {
        showProgressBar(R.layout.progress_bar)
        BillsClient.instance().getNetworkList()
            .enqueue(object : Callback<NetworkResponse> {
                override fun onResponse(
                    call: Call<NetworkResponse>,
                    response: Response<NetworkResponse>
                ) {
                    hideProgressBar()
                    if (response.isSuccessful) {
                        response.body()?.data?.let { networksData ->
                            networks.clear()
                            networkIds.clear()
                            networksData.forEach {
                                networks.add(it.network)
                                networkIds.add(it.id)
                            }
                            setupNetworkDropdown()
                        }
                    } else {
                        showToast("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                    hideProgressBar()
                    showToast("Failed to load networks. Please try again.")
                }
            })
    }

    private fun setupNetworkDropdown() {
        val networkIcons = mapOf(
            "MTN" to R.drawable.mtn,
            "Glo" to R.drawable.glo,
            "Airtel" to R.drawable.airtel,
            "9mobile" to R.drawable.etisalat
        )
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.bank,
            networks
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.bank, parent, false)
                val iconView = view.findViewById<ImageView>(R.id.networkIcon)
                val textView = view.findViewById<TextView>(R.id.networkName)

                textView.text = getItem(position)
                iconView.setImageResource(
                    networkIcons[getItem(position)] ?: R.drawable.logo
                )

                return view
            }
        }

        binding.networkName.setAdapter(adapter)

        binding.networkName.setOnItemClickListener { _, _, position, _ ->
            val selectedNetworkId = networkIds[position]
            clearPlanDropdown()
            fetchPlansForNetwork(selectedNetworkId)
        }
    }

    private fun fetchPlansForNetwork(networkId: String) {
        showProgressBar(R.layout.progress_bar)
        BillsClient.instance().getDataPlan()
            .enqueue(object : Callback<DataPlanResponse> {
                override fun onResponse(
                    call: Call<DataPlanResponse>,
                    response: Response<DataPlanResponse>
                ) {
                    hideProgressBar()
                    if (response.isSuccessful) {
                        response.body()?.data?.let { plansData ->
                            plans.clear()
                            plans.addAll(plansData.filter { it.network_id == networkId })
                            setupPlanDropdown()
                        }
                    } else {
                        showToast("Failed to fetch plans: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DataPlanResponse>, t: Throwable) {
                    showToast("Error fetching plans: ${t.message}")
                }
            })
    }

    private fun setupPlanDropdown() {
        val planNames = plans.map { it.plan }
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                planNames
            )
        binding.bundleName.setAdapter(adapter)
    }

    private fun validateInputs(): Boolean {
        if (binding.phoneNumberEt.text.toString().length != 11) {
            showToast("Please enter a valid 11-digit phone number.")
            return false
        }

        if (binding.networkName.text.toString().isEmpty()) {
            showToast("Please select a network.")
            return false
        }

        if (binding.bundleName.text.toString().isEmpty()) {
            showToast("Please select a data plan.")
            return false
        }

        return true
    }

    private fun clearPlanDropdown() {
        binding.bundleName.setText("")
        binding.bundleName.setAdapter(null)
        binding.phoneNumberEt.setText("")
        plans.clear()
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
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}