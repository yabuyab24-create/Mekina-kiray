package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Booking
import com.example.data.model.Car
import com.example.data.repository.CarRentalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarRentalViewModel(private val repository: CarRentalRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _maxPriceFilter = MutableStateFlow<Double?>(null)
    val maxPriceFilter: StateFlow<Double?> = _maxPriceFilter.asStateFlow()

    private val _isAmharic = MutableStateFlow(true) // Default to Amharic (true) as requested by the user, can switch to English
    val isAmharic: StateFlow<Boolean> = _isAmharic.asStateFlow()

    fun toggleLanguage() {
        _isAmharic.value = !_isAmharic.value
    }

    val allBookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredCars: StateFlow<List<Car>> = combine(
        repository.allCars,
        _searchQuery,
        _selectedCategory,
        _maxPriceFilter
    ) { cars, query, category, maxPrice ->
        cars.filter { car ->
            val matchesQuery = car.brand.contains(query, ignoreCase = true) ||
                    car.model.contains(query, ignoreCase = true) ||
                    car.description.contains(query, ignoreCase = true)
            val matchesCategory = category == "All" || car.category.equals(category, ignoreCase = true)
            val matchesPrice = maxPrice == null || car.pricePerDay <= maxPrice
            matchesQuery && matchesCategory && matchesPrice
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteCars: StateFlow<List<Car>> = repository.allCars
        .combine(MutableStateFlow(true)) { cars, _ ->
            cars.filter { it.isFavorite }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.prepopulateIfEmpty()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setMaxPriceFilter(price: Double?) {
        _maxPriceFilter.value = price
    }

    fun toggleFavorite(id: Int, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !isCurrentlyFavorite)
        }
    }

    fun bookCar(
        car: Car,
        renterName: String,
        renterPhone: String,
        startDate: Long,
        endDate: Long
    ) {
        viewModelScope.launch {
            val days = ((endDate - startDate) / (1000 * 60 * 60 * 24)).coerceAtLeast(1)
            val totalCost = car.pricePerDay * days
            val booking = Booking(
                carId = car.id,
                carBrand = car.brand,
                carModel = car.model,
                startDate = startDate,
                endDate = endDate,
                totalCost = totalCost,
                renterName = renterName,
                renterPhone = renterPhone,
                status = "Confirmed" // Instantly confirmed offline booking
            )
            repository.insertBooking(booking)
        }
    }

    fun cancelBooking(bookingId: Int, carId: Int) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, "Cancelled", carId)
        }
    }

    fun completeBooking(bookingId: Int, carId: Int) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, "Completed", carId)
        }
    }

    fun deleteBooking(bookingId: Int, carId: Int) {
        viewModelScope.launch {
            repository.deleteBookingById(bookingId, carId)
        }
    }

    fun hostCar(
        brand: String,
        model: String,
        category: String,
        pricePerDay: Double,
        transmission: String,
        fuelType: String,
        seats: Int,
        year: Int,
        plateNumber: String,
        ownerPhone: String,
        description: String
    ) {
        viewModelScope.launch {
            val car = Car(
                brand = brand,
                model = model,
                category = category,
                pricePerDay = pricePerDay,
                transmission = transmission,
                fuelType = fuelType,
                seats = seats,
                year = year,
                plateNumber = plateNumber,
                ownerPhone = ownerPhone,
                description = description
            )
            repository.insertCar(car)
        }
    }

    class Factory(private val repository: CarRentalRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarRentalViewModel::class.java)) {
                return CarRentalViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
