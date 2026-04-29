package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolistapp.ui.AboutAppScreen
import com.example.todolistapp.ui.AddDreamScreen
import com.example.todolistapp.ui.CalendarScreen
import com.example.todolistapp.ui.ExploreScreen
import com.example.todolistapp.ui.Home
import com.example.todolistapp.ui.Login
import com.example.todolistapp.ui.ProfileScreen
import com.example.todolistapp.ui.Register
import com.example.todolistapp.ui.SettingsScreen
import com.example.todolistapp.ui.SplashScreenContent
import com.example.todolistapp.ui.TermsOfServiceScreen

// Khai báo kho chứa kích thước màn hình
val LocalWindowSizeClass = staticCompositionLocalOf<WindowSizeClass> {
    error("No WindowSizeClass provided")
}
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Giữ nguyên Portrait nếu bạn chỉ muốn màn dọc
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val navController = rememberNavController()

            MaterialTheme {
                // ĐÂY LÀ CHỖ QUAN TRỌNG: Bao bọc tất cả bằng Provider
                CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        NavHost(
                            navController = navController,
                            startDestination = "splash"
                        ) {
                            composable("splash") {
                                SplashScreenContent(navController)
                            }

                            composable("Login") {
                                Login(navController)
                            }

                            composable("Register") {
                                Register(navController)
                            }

                            composable("Home") {
                                Home(navController)
                            }

                            composable("CalendarScreen") {
                                CalendarScreen(navController)
                            }

                            composable("AddDreamScreen") {
                                AddDreamScreen(navController)
                            }

                            composable("ExploreScreen") {
                                ExploreScreen(navController)
                            }

                            composable("SettingsScreen") {
                                SettingsScreen(navController)
                            }

                            composable("ProfileScreen") {
                                ProfileScreen(navController)
                            }

                            composable("TermsOfServiceScreen") {
                                TermsOfServiceScreen(navController)
                            }

                            composable("AboutAppScreen") {
                                AboutAppScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}