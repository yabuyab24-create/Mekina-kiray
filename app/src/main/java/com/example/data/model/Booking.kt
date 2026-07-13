package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carId: Int,
    val carBrand: String,
    val carModel: String,
    val startDate: Long,
    val endDate: Long,
    val totalCost: Double,
    val renterName: String,
    val renterPhone: String,
    val status: String, // Pending, Confirmed, Completed, Cancelled
    val bookingDate: Long = System.currentTimeMillis()
)
