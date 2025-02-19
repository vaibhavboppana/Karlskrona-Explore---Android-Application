package com.android.mapapp.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.android.mapapp.R
import com.android.mapapp.data.UserData
import com.android.mapapp.databinding.FragmentLoginBinding
import com.android.mapapp.databinding.FragmentSignupBinding
import com.android.mapapp.firebase.MainViewmodel
import com.android.mapapp.others.MyDialog
import com.android.mapapp.others.SharedPref
import java.util.*

class SignupFragment : Fragment(R.layout.fragment_signup) {

    lateinit var binding : FragmentSignupBinding
    lateinit var myDialog: MyDialog
    lateinit var viewmodel : MainViewmodel
    lateinit var sharedPref : SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignupBinding.bind(view)
        setUI(view)

    }

    private fun setUI(view: View) {

        viewmodel =
            ViewModelProvider(this).get(MainViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        sharedPref = SharedPref(requireContext())


        viewmodel.userCreatedLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myDialog.dismissProgressDialog()
            sharedPref.setUserData(it)
            Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_signupFragment_to_categoryFragment)
        })

        viewmodel.errorUserCreatedLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myDialog.dismissProgressDialog()
            myDialog.showErrorAlertDialog(it)
        })

        binding.tvSignup.setOnClickListener {

            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_signupFragment_to_loginFragment)

        }


        binding.cdNext.setOnClickListener {

            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            val phone = binding.edPhone.text.toString()
            val gender = binding.edGender.selectedItem.toString()
            val username = binding.edUsername.text.toString()



            if (email.isEmpty() && password.isEmpty() && phone.isEmpty() && gender.isEmpty() && username.isEmpty() ){
                Toast.makeText(requireContext(),"Please enter details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewmodel.createUser(UserData(
                 UUID.randomUUID().toString().substring(0,15),
                username,
                email,
                phone,
                password,
                gender
            ))

            myDialog.showProgressDialog("Signing up .... Please wait",this)

        }


    }

}