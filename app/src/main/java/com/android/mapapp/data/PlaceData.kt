package com.android.mapapp.data

import com.google.firebase.firestore.GeoPoint

data class PlaceData(
    var id : String = "",
    var name : String = "",
    var description : String = "",
    var geoPoint : GeoPoint = GeoPoint(0.0,0.0),
    var type : String = "",
    var postedBy : String = "",
    var rating : Float = 0f,
    var photo : String = ""
)

const val ACCOMODATION  = "Accomodation"
const val RESTAURANT  = "Restaurant"
const val TOURISTATTRACTION  = "Tourist Attraction"
const val TRANSPORTATION = "Transportation"
const val GROCERY = "Grocery"
const val SHOPPING = "Shopping"