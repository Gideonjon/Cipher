package com.example.cypher

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.cypher.databinding.FragmentSplashScreenBinding


class SplashScreen : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        val view = binding.root

// Load animations
        val logoAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.combined_logo_animation)

        // Start animations
        binding.imageView.startAnimation(logoAnimation)

        Handler().postDelayed({

            findNavController().navigate(R.id.action_splashScreen_to_onboardingScreen)


        }, 5000)


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}