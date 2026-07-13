package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.localization.Locales
import com.example.ui.viewmodel.CarRentalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostScreen(
    viewModel: CarRentalViewModel,
    modifier: Modifier = Modifier
) {
    val isAmharic by viewModel.isAmharic.collectAsState()

    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("SUV") }
    var pricePerDay by remember { mutableStateOf("") }
    var transmission by remember { mutableStateOf("Automatic") }
    var fuelType by remember { mutableStateOf("Benzine") }
    var seats by remember { mutableStateOf("5") }
    var year by remember { mutableStateOf("2020") }
    var plateNumber by remember { mutableStateOf("") }
    var ownerPhone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var transmissionExpanded by remember { mutableStateOf(false) }
    var fuelExpanded by remember { mutableStateOf(false) }

    var successMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val categories = listOf("SUV", "Sedan", "Hatchback", "Luxury", "Pickup")
    val transmissions = listOf("Automatic", "Manual")
    val fuelTypes = listOf("Benzine", "Diesel", "Electric")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E11))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = if (isAmharic) Locales.hostYourVehicleAm else Locales.hostYourVehicleEn,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            )
            Text(
                text = if (isAmharic) Locales.listYourCarAm else Locales.listYourCarEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        if (successMessage) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFF10B981)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isAmharic) Locales.carListedSuccessAm else Locales.carListedSuccessEn,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isAmharic) Locales.carListedDescAm else Locales.carListedDescEn,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        successMessage = false
                        brand = ""
                        model = ""
                        pricePerDay = ""
                        plateNumber = ""
                        ownerPhone = ""
                        description = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37), contentColor = Color.Black)
                ) {
                    Text(if (isAmharic) Locales.hostAnotherAm else Locales.hostAnotherEn, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Brand & Model Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text(if (isAmharic) Locales.brandInputAm else Locales.brandInputEn) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("host_brand_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text(if (isAmharic) Locales.modelInputAm else Locales.modelInputEn) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("host_model_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = outlinedColors()
                    )
                }

                // Category & Daily Price Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isAmharic) Locales.categoryLabelAm else Locales.categoryLabelEn) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            colors = outlinedColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false },
                            modifier = Modifier.background(Color(0xFF1E1D22))
                        ) {
                            categories.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = Color.White) },
                                    onClick = {
                                        category = option
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = pricePerDay,
                        onValueChange = {
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                pricePerDay = it
                            }
                        },
                        label = { Text(if (isAmharic) Locales.ratePerDayAm else Locales.ratePerDayEn) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("host_price_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = outlinedColors()
                    )
                }

                // Transmission & Fuel Type dropdowns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Transmission Dropdown
                    ExposedDropdownMenuBox(
                        expanded = transmissionExpanded,
                        onExpandedChange = { transmissionExpanded = !transmissionExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = transmission,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isAmharic) Locales.gearboxAm else Locales.gearboxEn) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = transmissionExpanded) },
                            colors = outlinedColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = transmissionExpanded,
                            onDismissRequest = { transmissionExpanded = false },
                            modifier = Modifier.background(Color(0xFF1E1D22))
                        ) {
                            transmissions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = Color.White) },
                                    onClick = {
                                        transmission = option
                                        transmissionExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Fuel Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = fuelExpanded,
                        onExpandedChange = { fuelExpanded = !fuelExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = fuelType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isAmharic) Locales.fuelTypeAm else Locales.fuelTypeEn) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fuelExpanded) },
                            colors = outlinedColors(),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = fuelExpanded,
                            onDismissRequest = { fuelExpanded = false },
                            modifier = Modifier.background(Color(0xFF1E1D22))
                        ) {
                            fuelTypes.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = Color.White) },
                                    onClick = {
                                        fuelType = option
                                        fuelExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Seats, Year & Plates
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = seats,
                        onValueChange = { seats = it },
                        label = { Text(if (isAmharic) Locales.seatsAm else Locales.seatsEn) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.8f),
                        shape = RoundedCornerShape(8.dp),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        label = { Text(if (isAmharic) Locales.yearAm else Locales.yearEn) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.8f),
                        shape = RoundedCornerShape(8.dp),
                        colors = outlinedColors()
                    )

                    OutlinedTextField(
                        value = plateNumber,
                        onValueChange = { plateNumber = it },
                        label = { Text(if (isAmharic) Locales.plateNoAm else Locales.plateNoEn) },
                        placeholder = { Text("e.g. AA 3-A123") },
                        modifier = Modifier.weight(1.4f),
                        shape = RoundedCornerShape(8.dp),
                        colors = outlinedColors()
                    )
                }

                // Phone & Description
                OutlinedTextField(
                    value = ownerPhone,
                    onValueChange = { ownerPhone = it },
                    label = { Text(if (isAmharic) Locales.hostContactAm else Locales.hostContactEn) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("host_phone_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = outlinedColors()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(if (isAmharic) Locales.descInputAm else Locales.descInputEn) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("host_desc_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = outlinedColors(),
                    maxLines = 4
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val price = pricePerDay.toDoubleOrNull()
                        val seatsNum = seats.toIntOrNull()
                        val yearNum = year.toIntOrNull()

                        if (brand.trim().isEmpty() || model.trim().isEmpty() ||
                            plateNumber.trim().isEmpty() || ownerPhone.trim().isEmpty() ||
                            description.trim().isEmpty() || price == null ||
                            seatsNum == null || yearNum == null
                        ) {
                            errorMessage = if (isAmharic) Locales.fillAllErrorAm else Locales.fillAllErrorEn
                        } else {
                            errorMessage = ""
                            viewModel.hostCar(
                                brand = brand.trim(),
                                model = model.trim(),
                                category = category,
                                pricePerDay = price,
                                transmission = transmission,
                                fuelType = fuelType,
                                seats = seatsNum,
                                year = yearNum,
                                plateNumber = plateNumber.trim().uppercase(java.util.Locale.ROOT),
                                ownerPhone = ownerPhone.trim(),
                                description = description.trim()
                            )
                            successMessage = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_car_listing"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4AF37),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Host Car",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAmharic) Locales.publishListingAm else Locales.publishListingEn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun outlinedColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFFD4AF37),
    unfocusedBorderColor = Color(0xFF2C2B30),
    focusedLabelColor = Color(0xFFD4AF37),
    unfocusedLabelColor = Color.Gray,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = Color(0xFF16151A),
    unfocusedContainerColor = Color(0xFF16151A)
)
