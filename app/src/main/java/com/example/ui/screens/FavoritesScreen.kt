package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Car
import com.example.ui.localization.Locales
import com.example.ui.viewmodel.CarRentalViewModel

@Composable
fun FavoritesScreen(
    viewModel: CarRentalViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteCars by viewModel.favoriteCars.collectAsState()
    val isAmharic by viewModel.isAmharic.collectAsState()
    var selectedCarForDetails by remember { mutableStateOf<Car?>(null) }
    var selectedCarForBooking by remember { mutableStateOf<Car?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E11))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = if (isAmharic) Locales.favoriteCarsAm else Locales.favoriteCarsEn,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            )
            Text(
                text = if (isAmharic) Locales.personalizedAm else Locales.personalizedEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        if (favoriteCars.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "No Favorites",
                    modifier = Modifier.size(72.dp),
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isAmharic) Locales.noBookmarkedAm else Locales.noBookmarkedEn,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isAmharic) Locales.tapHeartAm else Locales.tapHeartEn,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(favoriteCars, key = { it.id }) { car ->
                    CarListItem(
                        car = car,
                        isAmharic = isAmharic,
                        onFavoriteToggle = { viewModel.toggleFavorite(car.id, car.isFavorite) },
                        onClick = { selectedCarForDetails = car },
                        onRentClick = { selectedCarForBooking = car }
                    )
                }
            }
        }
    }

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
