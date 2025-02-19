package com.android.mapapp.ui.place

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.mapapp.R
import com.android.mapapp.databinding.FragmentCategoryBinding
import com.android.mapapp.databinding.FragmentViewplaceBinding
import com.android.mapapp.others.Constants
import com.bumptech.glide.Glide

class ViewPlaceFragment : Fragment(R.layout.fragment_viewplace) {

    lateinit var binding: FragmentViewplaceBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentViewplaceBinding.bind(view)
        setUI()

    }

    private fun setUI() {

        binding.fabLocation.setOnClickListener {
            val placeData = Constants.curPlaceData
          //  val gmmIntentUri = Uri.parse("geo:${placeData.geoPoint.latitude},${placeData.geoPoint.longitude}")
            val gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=loc:" + placeData.geoPoint.latitude + "," + placeData.geoPoint.longitude + " (" + "${placeData.name} \n ${placeData.description} " + ")")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(requireActivity().packageManager)?.let {
                startActivity(mapIntent)
            }
        }

        val data = Constants.curPlaceData

        binding.tvTitle.text = data.name
        binding.tvDescription.text = data.description
        binding.tvType.text = data.type

        Glide.with(requireActivity()).load(data.photo).into(binding.ivPlace)

        binding.ratingbar.rating = data.rating


    }

}