package com.example.data.repository

import com.example.data.db.BookingDao
import com.example.data.db.CarDao
import com.example.data.model.Booking
import com.example.data.model.Car
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CarRentalRepository(
    private val carDao: CarDao,
    private val bookingDao: BookingDao
) {
    val allCars: Flow<List<Car>> = carDao.getAllCars()
    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookings()

    fun getCarById(id: Int): Flow<Car?> = carDao.getCarById(id)

    suspend fun insertCar(car: Car): Long = carDao.insertCar(car)

    suspend fun updateCar(car: Car) = carDao.updateCar(car)

    suspend fun toggleFavorite(id: Int, isFavorite: Boolean) = carDao.updateFavorite(id, isFavorite)

    suspend fun updateAvailability(id: Int, isAvailable: Boolean) = carDao.updateAvailability(id, isAvailable)

    suspend fun deleteCarById(id: Int) = carDao.deleteCarById(id)

    suspend fun insertBooking(booking: Booking): Long {
        // Also update car availability to false when booked!
        carDao.updateAvailability(booking.carId, false)
        return bookingDao.insertBooking(booking)
    }

    suspend fun updateBookingStatus(id: Int, status: String, carId: Int) {
        bookingDao.updateBookingStatus(id, status)
        // If booking is Cancelled or Completed, free up the car again!
        if (status == "Cancelled" || status == "Completed") {
            carDao.updateAvailability(carId, true)
        } else if (status == "Confirmed") {
            carDao.updateAvailability(carId, false)
        }
    }

    suspend fun deleteBookingById(id: Int, carId: Int) {
        bookingDao.deleteBookingById(id)
        // Free up car when booking is deleted
        carDao.updateAvailability(carId, true)
    }

    // Pre-populate database if it is empty
    suspend fun prepopulateIfEmpty() {
        val currentCars = carDao.getAllCars().first()
        if (currentCars.isEmpty()) {
            val defaultCars = listOf(
                Car(
                    brand = "Toyota",
                    model = "Vitz",
                    category = "Hatchback",
                    pricePerDay = 1200.0,
                    transmission = "Manual",
                    fuelType = "Benzine",
                    seats = 5,
                    year = 2016,
                    plateNumber = "AA 3-A12045",
                    ownerPhone = "+251911123456",
                    description = "Highly fuel-efficient and compact, perfect for navigating Addis Ababa's busy streets and parking in tight spots."
                ),
                Car(
                    brand = "Toyota",
                    model = "Corolla",
                    category = "Sedan",
                    pricePerDay = 1800.0,
                    transmission = "Automatic",
                    fuelType = "Benzine",
                    seats = 5,
                    year = 2018,
                    plateNumber = "AA 3-B98124",
                    ownerPhone = "+251922345678",
                    description = "Comfortable, smooth, and incredibly reliable. Great for daily commutes, business trips, and city travel."
                ),
                Car(
                    brand = "Toyota",
                    model = "RAV4",
                    category = "SUV",
                    pricePerDay = 3500.0,
                    transmission = "Automatic",
                    fuelType = "Benzine",
                    seats = 5,
                    year = 2020,
                    plateNumber = "AA 2-C10492",
                    ownerPhone = "+251911987654",
                    description = "Popular compact SUV with high ground clearance, spacious boot space, and excellent ride height for road safety."
                ),
                Car(
                    brand = "Hyundai",
                    model = "Elantra",
                    category = "Sedan",
                    pricePerDay = 2000.0,
                    transmission = "Automatic",
                    fuelType = "Benzine",
                    seats = 5,
                    year = 2019,
                    plateNumber = "AA 3-A45781",
                    ownerPhone = "+251910445566",
                    description = "Sleek and premium design. Loaded with excellent features, dual air conditioning, and a smooth quiet ride."
                ),
                Car(
                    brand = "Suzuki",
                    model = "DZire",
                    category = "Sedan",
                    pricePerDay = 1500.0,
                    transmission = "Automatic",
                    fuelType = "Benzine",
                    seats = 5,
                    year = 2021,
                    plateNumber = "AA 3-C55902",
                    ownerPhone = "+251912889900",
                    description = "Virtually brand new and extremely fuel-efficient. Great maneuverability and very popular for modern rentals in Addis."
                ),
                Car(
                    brand = "Toyota",
                    model = "Land Cruiser V8",
                    category = "Luxury",
                    pricePerDay = 8500.0,
                    transmission = "Automatic",
                    fuelType = "Diesel",
                    seats = 7,
                    year = 2021,
                    plateNumber = "AA 2-VIP001",
                    ownerPhone = "+251900112233",
                    description = "The ultimate luxury SUV. Full leather interior, bulletproof reliability, massive engine power, and high-end security. Perfect for VIP guest escorts or long-distance countryside tours."
                ),
                Car(
                    brand = "Ford",
                    model = "Ranger",
                    category = "Pickup",
                    pricePerDay = 4000.0,
                    transmission = "Manual",
                    fuelType = "Diesel",
                    seats = 5,
                    year = 2018,
                    plateNumber = "AA 4-D54921",
                    ownerPhone = "+251911776655",
                    description = "A rugged double-cabin utility truck with 4WD capabilities. Extremely useful for transporting cargo, off-road terrain, or countryside trips."
                )
            )
            for (car in defaultCars) {
                carDao.insertCar(car)
            }
        }
    }
}
