package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class CityRepository {
    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    init {
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            _cities.clear()

            snapshot?.documents?.forEach { document ->
                val city: City? = document.toObject(City::class.java)
                city!!.id = document.id
                if (city != null) {
                    _cities.add(city)
                }

            }
        }
    }
    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        val newDocRef = citiesRef.document()
        val cityWithId = city.copy(id = newDocRef.id)
        newDocRef.set(cityWithId)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        addCity(updatedCity)
        delCity(oldCity)
    }

    fun delCity(city:City) {
        citiesRef.document(city.id).delete()
    }
}