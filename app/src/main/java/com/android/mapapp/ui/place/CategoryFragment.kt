package com.android.mapapp.ui.place

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.mapapp.R
import com.android.mapapp.data.*
import com.android.mapapp.databinding.FragmentCategoryBinding
import com.android.mapapp.others.Constants

class CategoryFragment : Fragment(R.layout.fragment_category) {


    lateinit var binding : FragmentCategoryBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCategoryBinding.bind(view)
        setUI()

    }

    private fun setUI() {

        binding.ctTouristattraction.setOnClickListener {
            Constants.curcategory = TOURISTATTRACTION
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_categoryFragment_to_homeFragment)
        }

        binding.ctRestaurants.setOnClickListener {
            Constants.curcategory = RESTAURANT
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_categoryFragment_to_homeFragment)
        }


        binding.ctAccomodations.setOnClickListener {
            Constants.curcategory = ACCOMODATION
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_categoryFragment_to_homeFragment)
        }


        binding.ctTransportation.setOnClickListener {
            Constants.curcategory = TRANSPORTATION
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_categoryFragment_to_homeFragment)
        }



        binding.ctGrocery.setOnClickListener {
            Constants.curcategory = GROCERY
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_categoryFragment_to_homeFragment)
        }

        binding.ctShopping.setOnClickListener {
            Constants.curcategory = SHOPPING
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_categoryFragment_to_homeFragment)
        }


        binding.ivProfile.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_categoryFragment_to_profileFragment)
        }


    }


}