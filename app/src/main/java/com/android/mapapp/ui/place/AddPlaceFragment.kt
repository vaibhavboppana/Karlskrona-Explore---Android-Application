package com.android.mapapp.ui.place

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.android.mapapp.R
import com.android.mapapp.data.*
import com.android.mapapp.databinding.FragmentAddplaceBinding
import com.android.mapapp.firebase.MainViewmodel
import com.android.mapapp.others.Constants
import com.android.mapapp.others.MyDialog
import com.android.mapapp.others.SharedPref
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

const val REQUEST_PRODUCT_IMAGE = 101
class AddPlaceFragment : Fragment(R.layout.fragment_addplace) {


    lateinit var binding : FragmentAddplaceBinding
    lateinit var myDialog: MyDialog
    lateinit var viewmodel : MainViewmodel
    var placeImageUri : Uri? = null
    private val firebaseStorage = Firebase.storage.reference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddplaceBinding.bind(view)
        setUI(view)

    }

    private fun setUI(view: View) {

        viewmodel =
            ViewModelProvider(this).get(MainViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.cdPlace.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, REQUEST_PRODUCT_IMAGE)
            }
        }

        val items = getPlaceTypes()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.edPlacetype.setAdapter(adapter)



        binding.cdNext.setOnClickListener {
            if (placeImageUri != null){
            val fileName = UUID.randomUUID().toString().substring(0,15)
            uploadImage(fileName,placeImageUri!!)
        }else{
            Toast.makeText(requireContext(), "Please add image", Toast.LENGTH_SHORT).show()
        }
        }

        viewmodel.placesCreatedLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myDialog.dismissProgressDialog()
            Toast.makeText(requireContext(),"Place added",Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        })

        viewmodel.errorPlacesCreatedLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myDialog.dismissProgressDialog()
            myDialog.showErrorAlertDialog(it)
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){

            when(requestCode){
                REQUEST_PRODUCT_IMAGE -> {
                    data?.data?.let {
                        placeImageUri = it
                        binding.ivPlace.setImageURI(it)
                    }
                }
            }

        }
    }


    private fun addPlace(image : String){
        val id = UUID.randomUUID().toString().substring(0,15)
        val locationName = binding.edLocation.text.toString()
        val description = binding.edDescription.text.toString()
        val type = binding.edPlacetype.text.toString()
        val lat = binding.edLat.text.toString().toDouble()
        val long = binding.edLong.text.toString().toDouble()
        val rating = binding.edRating.text.toString().toFloat()
        if (locationName.isEmpty() || description.isEmpty() || type.isEmpty() || lat.isNaN() || long.isNaN()){
            Toast.makeText(requireContext(), "Please enter all details", Toast.LENGTH_SHORT).show()
            return
        }
        val placeData   = PlaceData(
            id,locationName,description, GeoPoint(lat,long),type,SharedPref(requireContext()).getUserData().id,rating,image
        )

        viewmodel.createPlace(placeData)

        myDialog.showProgressDialog("Adding place",this)

    }


    private fun getPlaceTypes(): List<String> {
        return   listOf(TOURISTATTRACTION, ACCOMODATION, RESTAURANT, GROCERY, SHOPPING)
    }

    private fun uploadImage(fileName : String,it : Uri) {

        CoroutineScope(Dispatchers.Main).launch {
            firebaseStorage.child("placesImages/$fileName").putFile(it).addOnSuccessListener {
                firebaseStorage.child("placesImages/$fileName").downloadUrl.addOnSuccessListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        addPlace(it.toString())
                    }
                }.addOnFailureListener {

                    myDialog.dismissProgressDialog()
                    myDialog.showErrorAlertDialog(it.message)
                }
            }.addOnFailureListener {

                myDialog.dismissProgressDialog()
                myDialog.showErrorAlertDialog(it.message)
            }
        }


    }

}