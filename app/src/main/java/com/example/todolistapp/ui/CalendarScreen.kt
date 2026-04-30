package com.example.todolistapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.graphics.graphicsLayer
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R

@Composable
fun CalendarScreen(
    navController: NavHostController,
    accountId: Int
) {
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    // --- CẤU HÌNH THÔNG SỐ ADAPTIVE ---
    val horizontalPadding = if (widthClass == WindowWidthSizeClass.Compact) 20.dp else 45.dp
    val headerFontSize = if (widthClass == WindowWidthSizeClass.Compact) 26.sp else 36.sp
    val sectionTitleSize = if (widthClass == WindowWidthSizeClass.Compact) 17.sp else 22.sp

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        // 1. Ảnh nền & Gradient
        Image(
            painter = painterResource(id = R.drawable.bg_h),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(if (widthClass == WindowWidthSizeClass.Compact) 0.4f else 0.5f)
        )
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.42f).background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, PurpleBg.copy(alpha = 0.7f), PurpleBg),
                startY = 200f
            )
        ))

        Column(modifier = Modifier.fillMaxSize()) {
            // Header thích ứng
            Column(
                modifier = Modifier
                    .padding(start = horizontalPadding, end = horizontalPadding, top = 20.dp, bottom = 10.dp)
                    .statusBarsPadding()
            ) {
                Text("Lịch ước mơ ✨", fontSize = headerFontSize, fontWeight = FontWeight.Bold, color = Color.White)
            }

            // --- PHẦN TRÊN CỐ ĐỊNH (LỊCH & STATS) ---
            Column {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .shadow(8.dp, RoundedCornerShape(24.dp)),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        MonthSelectorMini(widthClass)
                        WeekHeaderMini(widthClass)
                        CalendarGridMini(widthClass)
                    }
                }

                // Stats Row thích ứng
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding + 8.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompactStatCard("28", "Tổng công việc", Icons.Default.MenuBook, DeepPurple, widthClass)
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.LightGray.copy(alpha = 0.5f)))
                    CompactProgressCard(0.67f, "Tiến độ", Color(0xFF03DAC6), widthClass)
                }
            }

            // --- VÙNG CUỘN DANH SÁCH TASK ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Kế hoạch ngày 15/05",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = sectionTitleSize,
                    color = DeepPurple,
                    modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 8.dp)
                )

                // Task Items
                DailyTaskItemSync("Đọc 30 trang sách", "07:00", "Hoàn thành", GreenStatus, Icons.Default.MenuBook, horizontalPadding)
                DailyTaskItemSync("Tập luyện 30 phút", "18:00", "Hoàn thành", GreenStatus, Icons.Default.FitnessCenter, horizontalPadding)
                DailyTaskItemSync("Hoàn thành bài luận", "19:30", "Đang làm", BlueStatus, Icons.Default.Edit, horizontalPadding)
                DailyTaskItemSync("Thiền 10 phút", "21:30", "Chưa làm", OrangeStatus, Icons.Default.NightsStay, horizontalPadding)

                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- PHẦN DƯỚI CỐ ĐỊNH (BANNER & NAV) ---
            Column(modifier = Modifier.background(PurpleBg)) {
                CalendarModernBanner(widthClass, horizontalPadding)
                SquaredBottomNav(
                    navController = navController,
                    accountId = accountId
                )
            }
        }
    }
}

// --- CÁC COMPONENT CON ĐÃ ĐƯỢC TỐI ƯU WINDOWSIZE ---

@Composable
fun CalendarModernBanner(widthClass: WindowWidthSizeClass, hPadding: androidx.compose.ui.unit.Dp) {
    val bannerHeight = when (widthClass) {
        WindowWidthSizeClass.Compact -> 85.dp
        WindowWidthSizeClass.Medium -> 105.dp
        else -> 125.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(bannerHeight)
            .padding(horizontal = hPadding, vertical = 6.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(painter = painterResource(id = R.drawable.banner), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent))))
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp), contentAlignment = Alignment.CenterStart) {
            Column {
                Text(
                    "Lập kế hoạch thông minh",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (widthClass == WindowWidthSizeClass.Compact) 15.sp else 20.sp
                )
                Text(
                    "Sắp xếp thời gian, gặt hái thành công ✨",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = if (widthClass == WindowWidthSizeClass.Compact) 11.sp else 14.sp
                )
            }
        }
    }
}

@Composable
fun CompactStatCard(value: String, label: String, icon: ImageVector, color: Color, widthClass: WindowWidthSizeClass) {
    val iconSize = if (widthClass == WindowWidthSizeClass.Compact) 30.dp else 38.dp
    val valSize = if (widthClass == WindowWidthSizeClass.Compact) 20.sp else 26.sp

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(iconSize).graphicsLayer(alpha = 0.9f))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(text = value, fontSize = valSize, fontWeight = FontWeight.Black, color = DeepPurple, lineHeight = valSize)
            Text(text = label, fontSize = 11.sp, color = Color.Gray.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CompactProgressCard(percentage: Float, label: String, color: Color, widthClass: WindowWidthSizeClass) {
    val indicatorSize = if (widthClass == WindowWidthSizeClass.Compact) 30.dp else 38.dp
    val valSize = if (widthClass == WindowWidthSizeClass.Compact) 20.sp else 26.sp

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "${(percentage * 100).toInt()}%", fontSize = valSize, fontWeight = FontWeight.Black, color = color, lineHeight = valSize)
            Text(text = label, fontSize = 11.sp, color = Color.Gray.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(progress = 1f, modifier = Modifier.size(indicatorSize), color = color.copy(alpha = 0.15f), strokeWidth = 3.5.dp)
            CircularProgressIndicator(progress = percentage, modifier = Modifier.size(indicatorSize), color = color, strokeWidth = 3.5.dp, strokeCap = StrokeCap.Round)
        }
    }
}

@Composable
fun DailyTaskItemSync(title: String, time: String, status: String, statusColor: Color, icon: ImageVector, hPadding: androidx.compose.ui.unit.Dp) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = hPadding, vertical = 5.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 0.8.dp
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(42.dp).background(PurpleBg, CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = DeepPurple, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(status, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    Text(" • ", color = Color.LightGray)
                    Icon(Icons.Default.AccessTime, null, tint = SoftPurple, modifier = Modifier.size(13.dp))
                    Text(" $time", color = SoftPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun MonthSelectorMini(widthClass: WindowWidthSizeClass) {
    val fontSize = if (widthClass == WindowWidthSizeClass.Compact) 16.sp else 20.sp
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text("Tháng 5, 2026", fontWeight = FontWeight.ExtraBold, fontSize = fontSize, color = DeepPurple)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.ChevronLeft, null, tint = DeepPurple) }
            Text("Hôm nay", color = DeepPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp))
            IconButton(onClick = {}, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.ChevronRight, null, tint = DeepPurple) }
        }
    }
}

@Composable
fun WeekHeaderMini(widthClass: WindowWidthSizeClass) {
    val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    val fontSize = if (widthClass == WindowWidthSizeClass.Compact) 11.sp else 13.sp
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        days.forEach { Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = fontSize, color = Color.Gray, fontWeight = FontWeight.Medium) }
    }
}

@Composable
fun CalendarGridMini(widthClass: WindowWidthSizeClass) {
    val dayCircleSize = if (widthClass == WindowWidthSizeClass.Expanded) 40.dp else 30.dp
    val fontSize = if (widthClass == WindowWidthSizeClass.Compact) 12.sp else 14.sp

    Column {
        for (i in 0 until 5) {
            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                for (j in 0 until 7) {
                    val dayNum = i * 7 + j - 2
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (dayNum in 1..31) {
                            Surface(
                                modifier = Modifier.size(dayCircleSize),
                                shape = CircleShape,
                                color = if (dayNum == 15) DeepPurple else Color.Transparent
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("$dayNum", color = if (dayNum == 15) Color.White else Color.Black, fontSize = fontSize, fontWeight = if(dayNum == 15) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}