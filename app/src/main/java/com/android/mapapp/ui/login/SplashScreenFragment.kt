package com.android.mapapp.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.mapapp.R
import com.android.mapapp.others.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment(R.layout.fragment_splashscreen) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {

            delay(2500)

            val sharedPref = SharedPref(requireContext())

            if (sharedPref.getUserLoginStatus()){
                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_splashScreenFragment_to_categoryFragment)
            }else{
                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_splashScreenFragment_to_loginFragment)
            }

        }

    }

}