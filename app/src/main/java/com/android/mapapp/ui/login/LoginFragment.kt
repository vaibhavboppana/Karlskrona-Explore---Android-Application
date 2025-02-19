package com.android.mapapp.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.android.mapapp.R
import com.android.mapapp.databinding.FragmentLoginBinding
import com.android.mapapp.firebase.MainViewmodel
import com.android.mapapp.others.MyDialog
import com.android.mapapp.others.SharedPref

class LoginFragment : Fragment(R.layout.fragment_login) {

    lateinit var binding : FragmentLoginBinding
    lateinit var myDialog: MyDialog
    lateinit var viewmodel : MainViewmodel
    lateinit var sharedPref : SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)
        setUI(view)

    }

    private fun setUI(view: View) {


        viewmodel =
            ViewModelProvider(this).get(MainViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        sharedPref = SharedPref(requireContext())


        viewmodel.userLoginLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myDialog.dismissProgressDialog()
            sharedPref.setUserData(it)
            Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_loginFragment_to_categoryFragment)
        })

        viewmodel.errorUserLoginLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            myDialog.dismissProgressDialog()
            myDialog.showErrorAlertDialog(it)
        })

        binding.tvSignup.setOnClickListener {

            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main).navigate(R.id.action_loginFragment_to_signupFragment)

        }


        binding.cdNext.setOnClickListener {

            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()


            if (email.isEmpty() && password.isEmpty()){
                Toast.makeText(requireContext(),"Please enter details",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewmodel.loginUser(email,password)

            myDialog.showProgressDialog("Signing In .... Please wait",this)

        }



    }

}