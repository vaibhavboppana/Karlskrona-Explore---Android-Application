package com.android.mapapp.ui.place

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mapapp.R
import com.android.mapapp.adapter.PlaceAdapter
import com.android.mapapp.data.PlaceData
import com.android.mapapp.data.TRANSPORTATION
import com.android.mapapp.databinding.FragmentHomeBinding
import com.android.mapapp.databinding.FragmentLoginBinding
import com.android.mapapp.firebase.MainViewmodel
import com.android.mapapp.others.Constants
import com.android.mapapp.others.MyDialog
import com.android.mapapp.ui.map.MapsActivity
import com.bumptech.glide.Glide

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var binding : FragmentHomeBinding
    lateinit var myDialog: MyDialog
    lateinit var viewmodel : MainViewmodel
    lateinit var placeAdapter: PlaceAdapter
    var places  = mutableListOf<PlaceData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        setUI(view)

    }

    private fun setUI(view: View) {

        viewmodel =
            ViewModelProvider(this).get(MainViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        val glide = Glide.with(requireActivity())

        placeAdapter = PlaceAdapter(glide)


        binding.rvPlaces.adapter = placeAdapter
        binding.rvPlaces.layoutManager = LinearLayoutManager(requireContext())

        viewmodel.getPlaces()


        viewmodel.placesLive.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visibility = View.GONE
            placeAdapter.places = it.filter { it.type == Constants.curcategory }.toMutableList()
            placeAdapter.placesList = it.filter { it.type == Constants.curcategory }
            places = it.filter { it.type == Constants.curcategory }.toMutableList()
        })


        viewmodel.errorPlacesLive.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visibility = View.GONE
            myDialog.showErrorAlertDialog(it)
        })

        binding.fabAddplace.setOnClickListener {
            if (Constants.curcategory != TRANSPORTATION){
                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_addPlaceFragment)
            }else{
                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_addTransportationFragment)
            }
         }

        binding.edSearch.addTextChangedListener {

            if(it != null){
                placeAdapter.filter(it.toString())
            }

        }

        placeAdapter.setOnItemClickListener {
            Constants.curPlaceData = it
            if (Constants.curcategory != TRANSPORTATION){
                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_viewPlaceFragment)
            }else{
                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_homeFragment_to_viewTransportationFragment)
            }
//            val intent = Intent(requireContext(),MapsActivity::class.java)
//            startActivity(intent)
        }


        setSpinner(binding.spinnerTypes, listOf("Tourist attractions","Accommodation","Restaurant"))


    }

    fun setSpinner(spinner: Spinner, spinnerList : List<String>) {
        val adapter = object :
            ArrayAdapter<Any>(
                requireContext(), R.layout.sp_layout,
                spinnerList
            ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also {
                    if (position == spinner.selectedItemPosition) {
                        it.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_200))
                    }
                }
            }
        }
        adapter.setDropDownViewResource(R.layout.sp_layout)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val places2 = places.filter { it.type == spinner.selectedItem.toString() }
                placeAdapter.places = places2.toMutableList()
                placeAdapter.placesList = places2


            }
        }
    }

}