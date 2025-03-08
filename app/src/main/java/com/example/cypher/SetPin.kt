package com.example.cypher

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.cypher.databinding.FragmentSetPinBinding
import com.example.cypher.models.RetrofitClient
import com.example.cypher.models.UserLogin
import com.example.cypher.utils.DecryptWalletResponse
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SetPin : Fragment() {
    private var _binding: FragmentSetPinBinding? = null
    private val binding get() = _binding!!
    private val passcode = StringBuilder()
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentSetPinBinding.inflate(inflater, container, false)

        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttons = listOf(
            binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6,
            binding.btn7, binding.btn8, binding.btn9,
            binding.btn0
        )

        buttons.forEach { button ->
            button.setOnClickListener { onNumberClick(button.text.toString()) }
        }

        binding.btnBackspace.setOnClickListener { onBackspaceClick() }
    }

    private fun onNumberClick(number: String) {
        if (passcode.length < 4) {
            passcode.append(number)
            updateDots()

            if (passcode.length == 4) {

                verifyPasscode()

            }
        }
    }

    private fun onBackspaceClick() {
        if (passcode.isNotEmpty()) {
            passcode.deleteCharAt(passcode.length - 1)
            updateDots()
        }
    }

    private fun updateDots() {
        val dots = listOf(
            binding.dot1, binding.dot2, binding.dot3, binding.dot4
        )
        dots.forEachIndexed { index, dot ->
            dot.alpha = if (index < passcode.length) 1f else 0.3f
        }
    }

    private fun verifyPasscode() {
        val enteredPasscode = passcode.toString()
        showProgressBar(R.layout.progress_bar)
        RetrofitClient.instance(requireContext()).userLogin(UserLogin(enteredPasscode))
            .enqueue(object : Callback<DecryptWalletResponse> {
                override fun onResponse(
                    call: Call<DecryptWalletResponse>,
                    response: Response<DecryptWalletResponse>
                ) {
                    hideProgressBar()

                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {

                        } else {
                            showSnackBar("Error: Response body is null")
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        showSnackBar("Error: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<DecryptWalletResponse>, t: Throwable) {
                    hideProgressBar()
                    showSnackBar("Error: ${t.message}")


                }

            })

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }


}