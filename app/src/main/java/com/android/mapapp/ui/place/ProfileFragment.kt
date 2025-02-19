package com.android.mapapp.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.mapapp.MainActivity
import com.android.mapapp.R
import com.android.mapapp.data.UserData
import com.android.mapapp.databinding.FragmentProfileBinding
import com.android.mapapp.others.SharedPref


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var binding : FragmentProfileBinding
    lateinit var sharedPref : SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view)
        setUI(view)

    }

    private fun setUI(view: View) {

        sharedPref = SharedPref(requireContext())

        val userData = sharedPref.getUserData()

        binding.tvName.text = userData.name
        binding.tvEmail.text = userData.email
        binding.tvPhone.text = userData.phone

        binding.btSignout.setOnClickListener {
            sharedPref.setUserData(UserData())
            sharedPref.setUserLoginStatus(false)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

        binding.btDeleteaccount.setOnClickListener {
            sharedPref.setUserData(UserData())
            sharedPref.setUserLoginStatus(false)
            startActivity(Intent(requireContext(),MainActivity::class.java))
            requireActivity().finish()
        }

    }


}