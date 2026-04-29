package com.example.todolistapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R

@Composable
fun Home(navController: NavHostController, userAvatarUri: android.net.Uri? = null) {
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    // --- BIẾN ĐIỀU CHỈNH THÍCH ỨNG ---
    val hPadding = if (widthClass == WindowWidthSizeClass.Compact) 24.dp else 40.dp
    val headerFontSize = if (widthClass == WindowWidthSizeClass.Compact) 26.sp else 34.sp
    val upcomingHeight = if (widthClass == WindowWidthSizeClass.Expanded) 180.dp else 145.dp

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {

        // 1. LỚP NỀN (Background Image với Gradient che mờ)
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(if (widthClass == WindowWidthSizeClass.Compact) 0.55f else 0.45f)) {
            Image(
                painter = painterResource(id = R.drawable.bg_h),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().alpha(0.85f)
            )
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, PurpleBg.copy(alpha = 0.5f), PurpleBg),
                    startY = 600f
                )
            ))
        }

        // 2. NỘI DUNG CHÍNH TRÊN MÀN HÌNH
        Column(modifier = Modifier.fillMaxSize()) {

            // Header: Ảnh đại diện & Lời chào
            HeaderSection(hPadding, headerFontSize, userAvatarUri)

            // Tiêu đề: Sắp đến hạn
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = hPadding, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(DeepPurple, CircleShape)
                        .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.HourglassEmpty, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "Sắp đến hạn",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = if (widthClass == WindowWidthSizeClass.Compact) 20.sp else 24.sp,
                    style = TextStyle(shadow = Shadow(Color.Black.copy(0.4f), blurRadius = 8f))
                )
            }

            // Danh sách Card Sắp đến hạn (LazyRow)
            LazyRow(
                contentPadding = PaddingValues(horizontal = hPadding, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(upcomingHeight)
            ) {
                items(5) { MinimalUpcomingCard(widthClass) }
            }

            // Thân bài: Filter & Danh sách Task
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(PurpleBg)
                    .padding(top = 12.dp)
            ) {
                ProfessionalFilterSection(hPadding)
                SectionTitleWithSort("Công việc", hPadding)
            }

            // Danh sách các Task (Có thể cuộn)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                repeat(10) { index -> FinalTaskItem(index, hPadding) }
            }
        }

        // Bottom Navigation Bar
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            SquaredBottomNav(navController = navController)
        }
    }
}

// --- CÁC COMPONENT CON (Helper Composables) ---

@Composable
fun HeaderSection(hPadding: androidx.compose.ui.unit.Dp, fontSize: androidx.compose.ui.unit.TextUnit, avatarUri: android.net.Uri?) {
    Row(
        modifier = Modifier
            .padding(horizontal = hPadding, vertical = 24.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Box chứa Ảnh: Tự động đổi giữa Logo và Ảnh người dùng
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .border(2.dp, Color.White, CircleShape)
        ) {
            if (avatarUri != null) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = "User Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text("Xin chào,", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Text(
                "Kiên ✨",
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                style = TextStyle(shadow = Shadow(Color.Black.copy(0.3f), blurRadius = 5f))
            )
        }
    }
}

@Composable
fun MinimalUpcomingCard(widthClass: WindowWidthSizeClass) {
    val cardWidth = if (widthClass == WindowWidthSizeClass.Expanded) 220.dp else 160.dp
    Box(
        modifier = Modifier
            .width(cardWidth)
            .fillMaxHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = 0.42f))
            .border(1.5.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(color = Color(0xFFE0B0FF).copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp)) {
                Text(
                    "SẮP TỚI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFE0B0FF),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    letterSpacing = 1.sp
                )
            }
            Column {
                Text(
                    "Đọc sách",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = if (widthClass == WindowWidthSizeClass.Expanded) 22.sp else 18.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Event, null, tint = Color(0xFFFFCC80), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("26/04/2026", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FinalTaskItem(index: Int, hPadding: androidx.compose.ui.unit.Dp) {
    val status = when(index % 3) {
        0 -> Triple("Hoàn thành", Color(0xFF4CAF50), Icons.Default.CheckCircle)
        1 -> Triple("Đang làm", Color(0xFF2196F3), Icons.Default.Sync)
        else -> Triple("Chưa làm", Color(0xFFFF9800), Icons.Default.RadioButtonUnchecked)
    }
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = hPadding, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(status.third, null, tint = status.second, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text("Học thiết kế UI/UX nâng cao", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1A1A1A))
                Row(Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(status.first, color = status.second, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    Text("  •  ", color = Color.LightGray)
                    Icon(Icons.Default.AccessTime, null, tint = Color(0xFF6200EE), modifier = Modifier.size(14.dp))
                    Text(" 08:30", color = Color(0xFF6200EE), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SectionTitleWithSort(title: String, hPadding: androidx.compose.ui.unit.Dp) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = hPadding, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF6200EE))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF6200EE), Color(0xFFBB86FC))))
                .clickable { }
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sắp xếp", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Default.UnfoldMore, null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun ProfessionalFilterSection(hPadding: androidx.compose.ui.unit.Dp) {
    Row(
        Modifier.padding(start = hPadding, end = hPadding, top = 8.dp, bottom = 8.dp).horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val filters = listOf("Tất cả", "Chưa làm", "Đang làm", "Hoàn thành")
        filters.forEachIndexed { i, text ->
            Surface(
                color = if (i == 0) Color(0xFF6200EE) else Color.White,
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.clickable { }
            ) {
                Text(
                    text = text,
                    color = if (i == 0) Color.White else Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}