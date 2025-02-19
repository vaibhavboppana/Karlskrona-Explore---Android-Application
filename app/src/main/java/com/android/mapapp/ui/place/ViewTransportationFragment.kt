package com.android.mapapp.ui.place

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.mapapp.R
import com.android.mapapp.databinding.FragmentCategoryBinding
import com.android.mapapp.databinding.FragmentViewplaceBinding
import com.android.mapapp.databinding.FragmentViewtransportationBinding
import com.android.mapapp.others.Constants
import com.bumptech.glide.Glide

class ViewTransportationFragment : Fragment(R.layout.fragment_viewtransportation) {

    lateinit var binding: FragmentViewtransportationBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentViewtransportationBinding.bind(view)
        setUI()

    }

    private fun setUI() {

        binding.fabLocation.setOnClickListener {
            val placeData = Constants.curPlaceData
            val gmmIntentUri = Uri.parse("geo:${placeData.geoPoint.latitude},${placeData.geoPoint.longitude}")
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