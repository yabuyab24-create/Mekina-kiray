package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.ui.localization.Locales
import com.example.ui.viewmodel.CarRentalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookingsScreen(
    viewModel: CarRentalViewModel,
    modifier: Modifier = Modifier
) {
    val bookings by viewModel.allBookings.collectAsState()
    val isAmharic by viewModel.isAmharic.collectAsState()
    var bookingToDelete by remember { mutableStateOf<Booking?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E11))
    ) {
        // Simple header title
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = if (isAmharic) Locales.myBookingsAm else Locales.myBookingsEn,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            )
            Text(
                text = if (isAmharic) Locales.trackRentalsAm else Locales.trackRentalsEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        if (bookings.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = "No Rentals",
                    modifier = Modifier.size(72.dp),
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isAmharic) Locales.noBookingsAm else Locales.noBookingsEn,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isAmharic) Locales.exploreCatalogAm else Locales.exploreCatalogEn,
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
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bookings, key = { it.id }) { booking ->
                    BookingListItem(
                        booking = booking,
                        isAmharic = isAmharic,
                        onCancelClick = { viewModel.cancelBooking(booking.id, booking.carId) },
                        onCompleteClick = { viewModel.completeBooking(booking.id, booking.carId) },
                        onDeleteClick = { bookingToDelete = booking }
                    )
                }
            }
        }
    }

    // Confirmation dialog for deleting a booking from local history
    bookingToDelete?.let { booking ->
        AlertDialog(
            onDismissRequest = { bookingToDelete = null },
            containerColor = Color(0xFF16151A),
            title = {
                Text(
                    text = if (isAmharic) "የኪራይ መዝገብ ይሰረዝ?" else "Delete Booking Record?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isAmharic) {
                        "ይህ እርምጃ የ${booking.carBrand} ${booking.carModel} የኪራይ መዝገብን ከመሳሪያዎ ታሪክ ላይ ያጠፋዋል። ኪራዩ አሁንም ንቁ ከሆነ መኪናው ነፃ ይሆናል።"
                    } else {
                        "This will remove the booking reference for ${booking.carBrand} ${booking.carModel} from your offline device history. If the booking is active, the vehicle will be freed."
                    },
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            },
            dismissButton = {
                TextButton(onClick = { bookingToDelete = null }) {
                    Text(if (isAmharic) "ዝጋ" else "Close", color = Color.Gray)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteBooking(booking.id, booking.carId)
                        bookingToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text(if (isAmharic) Locales.deleteAm else Locales.deleteEn, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun BookingListItem(
    booking: Booking,
    isAmharic: Boolean,
    onCancelClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val startStr = formatter.format(Date(booking.startDate))
    val endStr = formatter.format(Date(booking.endDate))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("booking_card_${booking.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16151A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2B30))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Vehicle details + Status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.carBrand,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4AF37)
                    )
                    Text(
                        text = booking.carModel,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                StatusBadge(status = booking.status, isAmharic = isAmharic)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF2C2B30)))
            Spacer(modifier = Modifier.height(12.dp))

            // Renter details
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Renter",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAmharic) "ተከራይ: ${booking.renterName}" else "Renter: ${booking.renterName}",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalPhone,
                    contentDescription = "Phone",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAmharic) "ስልክ: ${booking.renterPhone}" else "Contact: ${booking.renterPhone}",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dates section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1D22), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Rental Period",
                        tint = Color(0xFFD4AF37),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$startStr - $endStr",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Costs + Operations footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(if (isAmharic) Locales.totalPriceAm else Locales.totalPriceEn, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${booking.totalCost.toInt()} ETB",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFD4AF37)
                    )
                }

                // Conditional Interactive Actions
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (booking.status == "Confirmed") {
                        Button(
                            onClick = onCompleteClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(if (isAmharic) Locales.completeButtonAm else Locales.completeButtonEn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onCancelClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEF4444),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp).testTag("cancel_booking_${booking.id}")
                        ) {
                            Text(if (isAmharic) Locales.cancelAm else Locales.cancelEn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Deleted button for history list cleanup
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier
                                .background(Color(0xFF211F24), RoundedCornerShape(8.dp))
                                .size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete from history",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String, isAmharic: Boolean) {
    val bgColor = when (status) {
        "Confirmed" -> Color(0x2210B981)
        "Completed" -> Color(0x223B82F6)
        "Cancelled" -> Color(0x22EF4444)
        else -> Color(0x22F59E0B)
    }
    val fgColor = when (status) {
        "Confirmed" -> Color(0xFF10B981)
        "Completed" -> Color(0xFF3B82F6)
        "Cancelled" -> Color(0xFFEF4444)
        else -> Color(0xFFF59E0B)
    }
    val statusText = when (status) {
        "Confirmed" -> if (isAmharic) Locales.statusConfirmedAm else Locales.statusConfirmedEn
        "Completed" -> if (isAmharic) Locales.statusCompletedAm else Locales.statusCompletedEn
        "Cancelled" -> if (isAmharic) Locales.statusCancelledAm else Locales.statusCancelledEn
        else -> status
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = statusText,
            color = fgColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
