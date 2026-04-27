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
fun CalendarScreen(navController: NavHostController) {
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        // 1. Ảnh nền & Gradient mờ ảo
        Image(
            painter = painterResource(id = R.drawable.bg_h),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f)
        )
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.42f).background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, PurpleBg.copy(alpha = 0.7f), PurpleBg),
                startY = 200f
            )
        ))

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 10.dp).statusBarsPadding()) {
                Text("Lịch ước mơ ✨", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            // --- CỐ ĐỊNH PHẦN TRÊN ---
            Column {
                // Khối Lịch (Giữ Surface để tách biệt nền ảnh)
                Surface(
                    modifier = Modifier.padding(horizontal = 20.dp).shadow(8.dp, RoundedCornerShape(24.dp)),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        MonthSelectorMini()
                        WeekHeaderMini()
                        CalendarGridMini(widthClass)
                    }
                }

                // KHỐI STATS STYLE MỚI: NHỎ, GỌN, SANG
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 14.dp), // Tăng lề ngang để icon dịch trái hơn
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Thẻ 1: Tổng công việc
                    CompactStatCard(
                        value = "28",
                        label = "Tổng công việc",
                        icon = Icons.Default.MenuBook,
                        color = DeepPurple
                    )

                    // Vạch chia mảnh mai
                    Box(modifier = Modifier.width(1.dp).height(22.dp).background(Color.LightGray.copy(alpha = 0.5f)))

                    // Thẻ 2: Tiến độ
                    CompactProgressCard(
                        percentage = 0.67f,
                        label = "Tiến độ",
                        color = Color(0xFF03DAC6)
                    )
                }
            }

            // --- VÙNG CUỘN (DANH SÁCH TASK) ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Kế hoạch ngày 15/05",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = DeepPurple,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                )

                DailyTaskItemSync("Đọc 30 trang sách", "07:00", "Hoàn thành", GreenStatus, Icons.Default.MenuBook)
                DailyTaskItemSync("Tập luyện 30 phút", "18:00", "Hoàn thành", GreenStatus, Icons.Default.FitnessCenter)
                DailyTaskItemSync("Hoàn thành bài luận", "19:30", "Đang làm", BlueStatus, Icons.Default.Edit)
                DailyTaskItemSync("Thiền 10 phút", "21:30", "Chưa làm", OrangeStatus, Icons.Default.NightsStay)

                Spacer(modifier = Modifier.height(10.dp))
            }

            // --- CỐ ĐỊNH PHẦN DƯỚI (BANNER & FOOTER) ---
            Column(modifier = Modifier.background(PurpleBg)) {
                CalendarModernBanner()
                SquaredBottomNav(navController = navController)
            }
        }
    }
}

// --- CÁC HÀM STATS STYLE MỚI (NHỎ & HIỆN ĐẠI) ---

// --- STATS CẢI TIẾN: LÊN MÀU SẮC RỰC RỠ HƠN ---

@Composable
fun CompactStatCard(value: String, label: String, icon: ImageVector, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Icon dùng màu chủ đạo, có bóng đổ nhẹ
        Icon(
            icon,
            null,
            tint = color,
            modifier = Modifier
                .size(30.dp)
                .graphicsLayer(alpha = 0.9f)
        )
        Spacer(Modifier.width(10.dp))
        Column {
            // Số tổng công việc lên màu DeepPurple cực đậm và dày
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = DeepPurple, // Màu tím đặc trưng của app
                lineHeight = 20.sp
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            )
        }
    }
}

@Composable
fun CompactProgressCard(percentage: Float, label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.End) {
            // Con số % dùng màu xanh Teal/Mint sáng cho nổi bật
            Text(
                text = "${(percentage * 100).toInt()}%",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = color, // Màu xanh ngọc
                lineHeight = 20.sp
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            )
        }
        Spacer(Modifier.width(12.dp))
        // Vòng tròn tiến độ có đổ bóng mờ
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = 1f,
                modifier = Modifier.size(30.dp),
                color = color.copy(alpha = 0.15f), // Màu nền mờ
                strokeWidth = 3.5.dp
            )
            CircularProgressIndicator(
                progress = percentage,
                modifier = Modifier.size(30.dp),
                color = color,
                strokeWidth = 3.5.dp,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

// --- CÁC HÀM CÒN LẠI ---

@Composable
fun CalendarModernBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(18.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text("Lập kế hoạch thông minh", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Sắp xếp thời gian, gặt hái thành công ✨", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun MonthSelectorMini() {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text("Tháng 5, 2026", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DeepPurple)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.ChevronLeft, null, tint = DeepPurple) }
            Text("Hôm nay", color = DeepPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
            IconButton(onClick = {}, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.ChevronRight, null, tint = DeepPurple) }
        }
    }
}

@Composable
fun WeekHeaderMini() {
    val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        days.forEach { Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 11.sp, color = Color.Gray) }
    }
}

@Composable
fun CalendarGridMini(widthClass: WindowWidthSizeClass) {
    val dayCircleSize = if (widthClass == WindowWidthSizeClass.Expanded) 36.dp else 26.dp
    Column {
        for (i in 0 until 5) {
            Row(Modifier.fillMaxWidth().padding(vertical = 1.dp)) {
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
                                    Text("$dayNum", color = if (dayNum == 15) Color.White else Color.Black, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyTaskItemSync(title: String, time: String, status: String, statusColor: Color, icon: ImageVector) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 0.5.dp
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).background(PurpleBg, CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = DeepPurple, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    Text(" • ", color = Color.LightGray)
                    Icon(Icons.Default.AccessTime, null, tint = SoftPurple, modifier = Modifier.size(12.dp))
                    Text(" $time", color = SoftPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}