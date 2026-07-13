package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import com.example.ui.localization.Locales
import com.example.data.db.AppDatabase
import com.example.data.repository.CarRentalRepository
import com.example.ui.screens.BookingsScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HostScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CarRentalViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Room Database, DAO and Repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CarRentalRepository(database.carDao(), database.bookingDao())

        // Create ViewModel utilizing Factory
        val viewModel: CarRentalViewModel by viewModels {
            CarRentalViewModel.Factory(repository)
        }

        setContent {
            MyApplicationTheme {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

enum class Screen {
    Explore,
    MyBookings,
    Favorites,
    HostPortal
}

@Composable
fun MainAppContainer(viewModel: CarRentalViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.Explore) }
    val isAmharic by viewModel.isAmharic.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E11)),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .testTag("bottom_nav_bar"),
                containerColor = Color(0xFF16151A),
                tonalElevation = 8.dp
            ) {
                // Explore / Catalog
                NavigationBarItem(
                    selected = currentScreen == Screen.Explore,
                    onClick = { currentScreen = Screen.Explore },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = if (isAmharic) Locales.navExploreAm else Locales.navExploreEn,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (isAmharic) Locales.navExploreAm else Locales.navExploreEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = navigationBarItemColors(),
                    modifier = Modifier.testTag("nav_explore")
                )

                // Bookings
                NavigationBarItem(
                    selected = currentScreen == Screen.MyBookings,
                    onClick = { currentScreen = Screen.MyBookings },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = if (isAmharic) Locales.navBookingsAm else Locales.navBookingsEn,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (isAmharic) Locales.navBookingsAm else Locales.navBookingsEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = navigationBarItemColors(),
                    modifier = Modifier.testTag("nav_bookings")
                )

                // Favorites
                NavigationBarItem(
                    selected = currentScreen == Screen.Favorites,
                    onClick = { currentScreen = Screen.Favorites },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = if (isAmharic) Locales.navFavoritesAm else Locales.navFavoritesEn,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (isAmharic) Locales.navFavoritesAm else Locales.navFavoritesEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = navigationBarItemColors(),
                    modifier = Modifier.testTag("nav_favorites")
                )

                // Host Own Car
                NavigationBarItem(
                    selected = currentScreen == Screen.HostPortal,
                    onClick = { currentScreen = Screen.HostPortal },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = if (isAmharic) Locales.navHostAm else Locales.navHostEn,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (isAmharic) Locales.navHostAm else Locales.navHostEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = navigationBarItemColors(),
                    modifier = Modifier.testTag("nav_host")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0F0E11))
        ) {
            Crossfade(
                targetState = currentScreen,
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.Explore -> HomeScreen(viewModel = viewModel)
                    Screen.MyBookings -> BookingsScreen(viewModel = viewModel)
                    Screen.Favorites -> FavoritesScreen(viewModel = viewModel)
                    Screen.HostPortal -> HostScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color.Black,
    selectedTextColor = Color(0xFFD4AF37),
    unselectedIconColor = Color.Gray,
    unselectedTextColor = Color.Gray,
    indicatorColor = Color(0xFFD4AF37)
)
