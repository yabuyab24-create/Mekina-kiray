package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatReclineExtra
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.ui.localization.Locales
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Car
import com.example.ui.viewmodel.CarRentalViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: CarRentalViewModel,
    modifier: Modifier = Modifier
) {
    val cars by viewModel.filteredCars.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val maxPriceFilter by viewModel.maxPriceFilter.collectAsState()
    val isAmharic by viewModel.isAmharic.collectAsState()

    var selectedCarForDetails by remember { mutableStateOf<Car?>(null) }
    var selectedCarForBooking by remember { mutableStateOf<Car?>(null) }

    val categories = listOf("All", "SUV", "Sedan", "Hatchback", "Luxury", "Pickup")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E11)), // Dark slate background
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Section with generated Image Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                // Background Gradient + Hero Image
                val context = LocalContext.current
                val resourceId = context.resources.getIdentifier(
                    "mekina_kiray_hero", "drawable", context.packageName
                )
                if (resourceId != 0) {
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = "Mekina Kiray Hero Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Fallback visual design if image isn't available yet
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFD4AF37), Color(0xFF1A1A24))
                                )
                            )
                    )
                }

                // Shadow overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x99000000),
                                    Color(0xFF0F0E11)
                                )
                            )
                        )
                )

                // Immersive Language Switcher Toggle on Hero Section
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .clickable { viewModel.toggleLanguage() }
                            .clip(RoundedCornerShape(16.dp)),
                        color = Color(0xCC16151A)
                    ) {
                        Text(
                            text = if (isAmharic) "English 🇬🇧" else "አማርኛ 🇪🇹",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD4AF37),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                // Welcome texts overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        color = Color(0xFFD4AF37),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Text(
                            text = if (isAmharic) Locales.appNameAm else Locales.appNameEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = if (isAmharic) Locales.appNameAm else Locales.appNameEn,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = if (isAmharic) Locales.sloganAm else Locales.sloganEn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.LightGray,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        // Search and Filter Row
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text(if (isAmharic) Locales.searchPlaceholderAm else Locales.searchPlaceholderEn, color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color(0xFFD4AF37)
                        )
                    },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            Text(
                                text = if (isAmharic) Locales.clearAm else Locales.clearEn,
                                modifier = Modifier
                                    .clickable { viewModel.setSearchQuery("") }
                                    .padding(8.dp),
                                color = Color(0xFFD4AF37),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD4AF37),
                        unfocusedBorderColor = Color(0xFF2C2B30),
                        focusedContainerColor = Color(0xFF16151A),
                        unfocusedContainerColor = Color(0xFF16151A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Price limit slider details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAmharic) Locales.priceFilterAm else Locales.priceFilterEn,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(null, 2000.0, 4000.0, 8000.0).forEach { limit ->
                            val isSelected = maxPriceFilter == limit
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color(0xFFD4AF37) else Color(0xFF1D1C22))
                                    .clickable { viewModel.setMaxPriceFilter(limit) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (limit == null) {
                                        if (isAmharic) Locales.allAm else Locales.allEn
                                    } else "< ${limit.toInt()} ETB",
                                    color = if (isSelected) Color.Black else Color.Gray,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Horizontal Category Chips Selector
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) Color(0xFFD4AF37) else Color(0xFF16151A)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color(0xFFD4AF37) else Color(0xFF2C2B30),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { viewModel.setSelectedCategory(category) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("category_chip_$category")
                    ) {
                        Text(
                            text = if (category == "All") {
                                if (isAmharic) Locales.allAm else Locales.allEn
                            } else category,
                            color = if (isSelected) Color.Black else Color.LightGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // List Header / Count
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${if (isAmharic) Locales.availableVehiclesAm else Locales.availableVehiclesEn} (${cars.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        // Empty State
        if (cars.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "No Cars",
                        modifier = Modifier.size(64.dp),
                        tint = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isAmharic) Locales.noCarsAm else Locales.noCarsEn,
                        color = Color.Gray,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (isAmharic) Locales.adjustFiltersAm else Locales.adjustFiltersEn,
                        color = Color.DarkGray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Vehicles Grid List
        items(cars, key = { it.id }) { car ->
            CarListItem(
                car = car,
                isAmharic = isAmharic,
                onFavoriteToggle = { viewModel.toggleFavorite(car.id, car.isFavorite) },
                onClick = { selectedCarForDetails = car },
                onRentClick = { selectedCarForBooking = car }
            )
        }
    }

    // Modal Details Dialog
    selectedCarForDetails?.let { car ->
        CarDetailsDialog(
            car = car,
            isAmharic = isAmharic,
            onDismiss = { selectedCarForDetails = null },
            onFavoriteToggle = { viewModel.toggleFavorite(car.id, car.isFavorite) },
            onRentClick = {
                selectedCarForDetails = null
                selectedCarForBooking = car
            }
        )
    }

    // Modal Booking/Reservation form Dialog
    selectedCarForBooking?.let { car ->
        BookingFormDialog(
            car = car,
            isAmharic = isAmharic,
            onDismiss = { selectedCarForBooking = null },
            onSubmitBooking = { name, phone, start, end ->
                viewModel.bookCar(car, name, phone, start, end)
                selectedCarForBooking = null
            }
        )
    }
}

@Composable
fun CarListItem(
    car: Car,
    isAmharic: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    onRentClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
            .testTag("car_item_${car.brand}_${car.model}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16151A)),
        border = borderStrokeForCar(car.isAvailable)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image/Header visual representing the car
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF23222A), Color(0xFF100F13))
                        )
                    )
            ) {
                // Stylized text/logo representation instead of generic missing photos
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Car Logo",
                        modifier = Modifier.size(44.dp),
                        tint = if (car.isAvailable) Color(0xFFD4AF37) else Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${car.brand} ${car.model}".uppercase(Locale.ROOT),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                }

                // Favorite icon overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .background(Color(0x7F000000), RoundedCornerShape(50))
                            .size(36.dp)
                            .testTag("fav_btn_${car.id}")
                    ) {
                        Icon(
                            imageVector = if (car.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (car.isFavorite) Color(0xFFD4AF37) else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Rent Price badge overlay
                Surface(
                    color = Color(0xFFD4AF37),
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${car.pricePerDay.toInt()} ETB",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                        Text(
                            text = if (isAmharic) " / ቀን" else " / day",
                            fontSize = 11.sp,
                            color = Color(0xFF4A4A4A),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Availability badge overlay
                Surface(
                    color = if (car.isAvailable) Color(0xFF34D399) else Color(0xFFEF4444),
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = if (car.isAvailable) {
                            if (isAmharic) Locales.availableAm else Locales.availableEn
                        } else {
                            if (isAmharic) Locales.rentedAm else Locales.rentedEn
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Specs Row + Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "${car.year} • ${car.category}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = car.description,
                    maxLines = 2,
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Technical spec tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SpecTag(icon = Icons.Default.SettingsSuggest, label = car.transmission)
                        SpecTag(icon = Icons.Default.LocalGasStation, label = car.fuelType)
                        SpecTag(icon = Icons.Default.AirlineSeatReclineExtra, label = "${car.seats} ${if (isAmharic) Locales.seatsCountAm else Locales.seatsCountEn}")
                    }

                    if (car.isAvailable) {
                        Button(
                            onClick = onRentClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD4AF37),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("rent_now_btn_${car.id}")
                        ) {
                            Text(
                                text = if (isAmharic) Locales.rentButtonAm else Locales.rentButtonEn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = {},
                            enabled = false,
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Color(0xFF252429),
                                disabledContentColor = Color.DarkGray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = if (isAmharic) Locales.unavailableAm else Locales.unavailableEn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpecTag(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(14.dp),
            tint = Color(0xFFD4AF37)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun borderStrokeForCar(isAvailable: Boolean): androidx.compose.foundation.BorderStroke? {
    return if (isAvailable) {
        androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2B30))
    } else {
        androidx.compose.foundation.BorderStroke(1.dp, Color(0x33EF4444))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CarDetailsDialog(
    car: Car,
    isAmharic: Boolean,
    onDismiss: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onRentClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Color(0xFF2D2B33), RoundedCornerShape(24.dp)),
            color = Color(0xFF16151A)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header image representation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF282730), Color(0xFF111014))
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Car",
                            modifier = Modifier.size(54.dp),
                            tint = Color(0xFFD4AF37)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = car.brand,
                            color = Color(0xFFD4AF37),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = car.model,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }

                    // Top Action overlays
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .background(Color(0x99000000), RoundedCornerShape(50))
                                .size(36.dp)
                        ) {
                            Text("×", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }

                        IconButton(
                            onClick = onFavoriteToggle,
                            modifier = Modifier
                                .background(Color(0x99000000), RoundedCornerShape(50))
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (car.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (car.isFavorite) Color(0xFFD4AF37) else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Price Overlay
                    Surface(
                        color = Color(0xFFD4AF37),
                        shape = RoundedCornerShape(topStart = 16.dp),
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${car.pricePerDay.toInt()} ETB",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                            Text(
                                text = if (isAmharic) " / ቀን" else " / day",
                                fontSize = 11.sp,
                                color = Color(0xFF222222)
                            )
                        }
                    }
                }

                // Details Specs Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = if (isAmharic) Locales.vehicleSpecsAm else Locales.vehicleSpecsEn,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DetailChip(icon = Icons.Default.SettingsSuggest, title = if (isAmharic) Locales.gearboxAm else Locales.gearboxEn, value = car.transmission)
                        DetailChip(icon = Icons.Default.LocalGasStation, title = if (isAmharic) Locales.fuelTypeAm else Locales.fuelTypeEn, value = car.fuelType)
                        DetailChip(icon = Icons.Default.AirlineSeatReclineExtra, title = if (isAmharic) Locales.seatsAm else Locales.seatsEn, value = "${car.seats} ${if (isAmharic) Locales.seatsCountAm else Locales.seatsCountEn}")
                        DetailChip(icon = Icons.Default.CalendarMonth, title = if (isAmharic) Locales.yearModelAm else Locales.yearModelEn, value = "${car.year}")
                        DetailChip(icon = Icons.Default.Numbers, title = if (isAmharic) Locales.plateNoAm else Locales.plateNoEn, value = car.plateNumber)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isAmharic) Locales.descriptionAm else Locales.descriptionEn,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = car.description,
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1F1D24), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalPhone,
                            contentDescription = "Owner Contact",
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isAmharic) Locales.hostContactAm else Locales.hostContactEn,
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = car.ownerPhone,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Rent action button
                    if (car.isAvailable) {
                        Button(
                            onClick = onRentClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("details_rent_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD4AF37),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookOnline,
                                contentDescription = "Book",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAmharic) Locales.rentButtonAm else Locales.rentButtonEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        Button(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Color(0xFF252429),
                                disabledContentColor = Color.DarkGray
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (isAmharic) Locales.rentedAm else Locales.rentedEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailChip(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .background(Color(0xFF1E1D22), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF2C2B30), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(14.dp),
            tint = Color(0xFFD4AF37)
        )
        Column {
            Text(text = title, color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            Text(text = value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFormDialog(
    car: Car,
    isAmharic: Boolean,
    onDismiss: () -> Unit,
    onSubmitBooking: (name: String, phone: String, startTimestamp: Long, endTimestamp: Long) -> Unit
) {
    var renterName by remember { mutableStateOf("") }
    var renterPhone by remember { mutableStateOf("") }
    var rentalDays by remember { mutableStateOf("3") }

    var isError by remember { mutableStateOf(false) }

    val daysCount = rentalDays.toIntOrNull() ?: 1
    val totalPrice = car.pricePerDay * daysCount

    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val today = Calendar.getInstance()
    val startDateStr = formatter.format(today.time)

    val endCalendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, daysCount)
    }
    val endDateStr = formatter.format(endCalendar.time)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFF2C2B30), RoundedCornerShape(20.dp)),
            color = Color(0xFF16151A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = if (isAmharic) Locales.confirmBookingAm else Locales.confirmBookingEn,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Text(
                    text = if (isAmharic) "${car.brand} ${car.model} ለመከራየት እያዘጋጁ ነው" else "You are renting ${car.brand} ${car.model}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input fields
                OutlinedTextField(
                    value = renterName,
                    onValueChange = { renterName = it },
                    label = { Text(if (isAmharic) Locales.yourFullNameAm else Locales.yourFullNameEn) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("renter_name_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD4AF37),
                        unfocusedBorderColor = Color(0xFF2C2B30),
                        focusedLabelColor = Color(0xFFD4AF37),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = renterPhone,
                    onValueChange = { renterPhone = it },
                    label = { Text(if (isAmharic) Locales.phoneNumberAm else Locales.phoneNumberEn) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("renter_phone_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD4AF37),
                        unfocusedBorderColor = Color(0xFF2C2B30),
                        focusedLabelColor = Color(0xFFD4AF37),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = rentalDays,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            rentalDays = it
                        }
                    },
                    label = { Text(if (isAmharic) Locales.rentalDurationAm else Locales.rentalDurationEn) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rental_days_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD4AF37),
                        unfocusedBorderColor = Color(0xFF2C2B30),
                        focusedLabelColor = Color(0xFFD4AF37),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Date overview details box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1D22), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isAmharic) Locales.startDateAm else Locales.startDateEn, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(startDateStr, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isAmharic) Locales.endDateAm else Locales.endDateEn, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(endDateStr, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFF2C2B30))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isAmharic) Locales.totalPriceAm else Locales.totalPriceEn, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${totalPrice.toInt()} ETB",
                            color = Color(0xFFD4AF37),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                if (isError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isAmharic) Locales.inputErrorAm else Locales.inputErrorEn,
                        color = Color.Red,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                    ) {
                        Text(if (isAmharic) Locales.cancelAm else Locales.cancelEn, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (renterName.trim().isEmpty() || renterPhone.trim().isEmpty()) {
                                isError = true
                            } else {
                                isError = false
                                val start = today.timeInMillis
                                val end = endCalendar.timeInMillis
                                onSubmitBooking(renterName, renterPhone, start, end)
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("confirm_booking_submit"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD4AF37),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isAmharic) Locales.confirmRideAm else Locales.confirmRideEn, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
