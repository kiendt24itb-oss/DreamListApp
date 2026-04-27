package com.example.todolistapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.todolistapp.LocalWindowSizeClass

@Composable
fun SquaredBottomNav(navController: NavHostController) {
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    // --- LOGIC NHẬN DIỆN TRANG HIỆN TẠI ---
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    // --------------------------------------

    val barHeight = when (widthClass) {
        WindowWidthSizeClass.Expanded -> 85.dp
        else -> 75.dp
    }

    Box(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier
                .height(barHeight)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            tonalElevation = 15.dp
        ) {
            // Định nghĩa danh sách các item kèm Route tương ứng
            val items = listOf(
                // Triple(Tiêu đề, Icon, Màu sắc, Route thực tế)
                Triple("Trang chủ", Icons.Default.Home, DeepPurple to "Home"),
                Triple("Lịch", Icons.Default.CalendarMonth, OrangeStatus to "CalendarScreen"),
                Triple("", Icons.Default.Add, Color.Transparent to "AddDreamScreen"),
                Triple("Khám phá", Icons.Default.Explore, BlueStatus to "ExploreScreen"),
                Triple("Cài đặt", Icons.Default.Settings, GreenStatus to "SettingsScreen")
            )

            items.forEachIndexed { i, item ->
                if (i == 2) {
                    Spacer(Modifier.width(if (widthClass == WindowWidthSizeClass.Expanded) 100.dp else 60.dp))
                } else {
                    val targetRoute = item.third.second
                    val isSelected = currentRoute == targetRoute // Kiểm tra chuẩn xác theo route

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != targetRoute) {
                                navController.navigate(targetRoute) {
                                    // Giúp quản lý stack mượt mà, tránh lặp trang
                                    popUpTo("Home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.second,
                                contentDescription = item.first,
                                // CHỖ NÀY: Nếu chọn thì full màu, không thì mờ đi
                                tint = if (isSelected) item.third.first else item.third.first.copy(alpha = 0.4f),
                                modifier = Modifier.size(if (widthClass == WindowWidthSizeClass.Expanded) 30.dp else 26.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.first,
                                fontSize = if (widthClass == WindowWidthSizeClass.Expanded) 13.sp else 11.sp,
                                // CHỖ NÀY: Nếu chọn thì chữ đậm hơn (ExtraBold)
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isSelected) item.third.first else item.third.first.copy(alpha = 0.5f)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }

        // Nút FAB
        FloatingActionButton(
            onClick = {
                // LỆNH ĐIỀU HƯỚNG Ở ĐÂY
                navController.navigate("AddDreamScreen")
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-15).dp)
                .size(if (widthClass == WindowWidthSizeClass.Expanded) 72.dp else 62.dp),
            shape = CircleShape,
            containerColor = DeepPurple,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(35.dp))
        }

    }
}