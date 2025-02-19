package com.android.mapapp.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mapapp.data.PlaceData
import com.android.mapapp.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewmodel : ViewModel() {


    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val usersCollection = firestore.collection("users")
    private val placesCollection = firestore.collection("places")


    private var _userCreatedLive = MutableLiveData<UserData>()
    var userCreatedLive : LiveData<UserData> = _userCreatedLive
    private var _errorUserCreatedLive = MutableLiveData<String>()
    var errorUserCreatedLive : LiveData<String> = _errorUserCreatedLive


    fun createUser(userData: UserData) =  viewModelScope.launch(Dispatchers.IO) {

        try {


                usersCollection.document(userData.id).set(userData).addOnSuccessListener {
                    _userCreatedLive.postValue(userData)
                }.addOnFailureListener {
                    _errorUserCreatedLive.postValue(it.message)
                }

        } catch (e: Exception) {

            _errorUserCreatedLive.postValue(e.message)
        }

    }


    private var _userLoginLive = MutableLiveData<UserData>()
    var userLoginLive : LiveData<UserData> = _userLoginLive
    private var _errorUserLoginLive = MutableLiveData<String>()
    var errorUserLoginLive : LiveData<String> = _errorUserLoginLive


    fun loginUser(email: String, password: String) =  viewModelScope.launch(Dispatchers.IO) {

        try {


            val users = usersCollection.whereEqualTo("email",email).whereEqualTo("password",password).get().await().toObjects(UserData::class.java)

            if (users.isNotEmpty()){
                _userLoginLive.postValue(users[0])
            }else{
                _errorUserLoginLive.postValue("Invalid Credentials")
            }


        } catch (e: Exception) {

            _errorUserLoginLive.postValue(e.message)
        }

    }

    private var _placesCreatedLive = MutableLiveData<PlaceData>()
    var placesCreatedLive : LiveData<PlaceData> = _placesCreatedLive
    private var _errorPlacesCreatedLive = MutableLiveData<String>()
    var errorPlacesCreatedLive : LiveData<String> = _errorPlacesCreatedLive

    fun createPlace(placeData: PlaceData) =  viewModelScope.launch(Dispatchers.IO) {

        try {

            placesCollection.document(placeData.id).set(placeData).addOnSuccessListener {
                _placesCreatedLive.postValue(placeData)
            }.addOnFailureListener {
                _errorPlacesCreatedLive.postValue(it.message)
            }

        } catch (e: Exception) {
            _errorPlacesCreatedLive.postValue(e.message)
        }
    }

    private var _placesLive = MutableLiveData<List<PlaceData>>()
    var placesLive : MutableLiveData<List<PlaceData>> = _placesLive
    private var _errorPlacesLive = MutableLiveData<String>()
    var errorPlacesLive : LiveData<String> = _errorPlacesLive

    fun getPlaces() = viewModelScope.launch(Dispatchers.IO){

        try {
            val data =  placesCollection.get().await().toObjects(PlaceData::class.java)

            data.let {
                _placesLive.postValue(it)
            }

        }catch (e: Exception){
            _errorPlacesLive.postValue(e.message)
        }

    }



}