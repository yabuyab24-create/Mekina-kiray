package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brand: String,
    val model: String,
    val category: String, // SUV, Sedan, Hatchback, Luxury, Pickup
    val pricePerDay: Double, // in ETB
    val transmission: String, // Automatic, Manual
    val fuelType: String, // Benzine, Diesel, Electric
    val seats: Int,
    val year: Int,
    val plateNumber: String,
    val ownerPhone: String,
    val isAvailable: Boolean = true,
    val isFavorite: Boolean = false,
    val description: String
)
