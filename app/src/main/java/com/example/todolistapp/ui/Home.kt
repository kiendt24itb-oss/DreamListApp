package com.example.todolistapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R

val PurpleBg = Color(0xFFF3F2FF)
val DeepPurple = Color(0xFF6A5AE0)
val SoftPurple = Color(0xFF9D89F3)
val OrangeStatus = Color(0xFFFF9800)
val GreenStatus = Color(0xFF4CAF50)
val BlueStatus = Color(0xFF2196F3)

@Composable
fun Home(navController: NavHostController) { // Sửa 1: Đồng bộ tham số
    // Sửa 2: Lấy windowSize từ Local Provider
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.55f)) {
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

        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(DeepPurple, CircleShape)
                        .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.HourglassEmpty,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "Sắp đến hạn",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 20.sp,
                    style = TextStyle(
                        shadow = Shadow(Color.Black.copy(0.4f), blurRadius = 8f)
                    )
                )
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                // Sửa 3: Thích ứng chiều cao card nếu là màn hình lớn
                modifier = Modifier.height(if (widthClass == WindowWidthSizeClass.Expanded) 180.dp else 145.dp)
            ) {
                items(5) { MinimalUpcomingCard(widthClass) }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(PurpleBg)
                    .padding(top = 12.dp)
            ) {
                ProfessionalFilterSection()
                SectionTitleWithSort("Công việc")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                repeat(10) { index -> FinalTaskItem(index) }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            SquaredBottomNav(navController = navController)
        }
    }
}

@Composable
fun MinimalUpcomingCard(widthClass: WindowWidthSizeClass) {
    Box(
        modifier = Modifier
            // Sửa 4: Thích ứng chiều rộng card
            .width(if (widthClass == WindowWidthSizeClass.Expanded) 220.dp else 160.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = 0.42f))
            .border(1.5.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = SoftPurple.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ) {
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
                    color = Color.White,
                    style = TextStyle(shadow = Shadow(Color.Black, blurRadius = 10f))
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Event, null, tint = Color(0xFFFFCC80), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "26/04/2026",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// CÁC HÀM CÒN LẠI (SectionTitleWithSort, FinalTaskItem, HeaderSection, ProfessionalFilterSection)
// GIỮ NGUYÊN HOÀN TOÀN NHƯ CODE CŨ CỦA BẠN VÌ KHÔNG CẦN CHỈNH SỬA THÊM
@Composable
fun SectionTitleWithSort(title: String) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = DeepPurple)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.horizontalGradient(listOf(DeepPurple, SoftPurple)))
                .clickable { }
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Sắp xếp", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.UnfoldMore, null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun FinalTaskItem(index: Int) {
    val status = when(index % 3) {
        0 -> Triple("Hoàn thành", GreenStatus, Icons.Default.CheckCircle)
        1 -> Triple("Đang làm", BlueStatus, Icons.Default.Sync)
        else -> Triple("Chưa làm", OrangeStatus, Icons.Default.RadioButtonUnchecked)
    }
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
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
                    Icon(Icons.Default.EventNote, null, tint = DeepPurple, modifier = Modifier.size(14.dp))
                    Text(" 26/04/2026", color = DeepPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("  ", color = Color.LightGray)
                    Icon(Icons.Default.AccessTime, null, tint = SoftPurple, modifier = Modifier.size(14.dp))
                    Text(" 08:30", color = SoftPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Row(modifier = Modifier.padding(24.dp).statusBarsPadding(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(56.dp).clip(CircleShape).border(2.dp, Color.White, CircleShape)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text("Xin chào,", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Text("Kiên ✨", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun ProfessionalFilterSection() {
    Row(
        Modifier.padding(horizontal = 20.dp, vertical = 8.dp).horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val filters = listOf(
            Triple("Tất cả", Icons.Default.AllInclusive, DeepPurple),
            Triple("Chưa làm", Icons.Default.RadioButtonUnchecked, OrangeStatus),
            Triple("Đang làm", Icons.Default.Sync, BlueStatus),
            Triple("Hoàn thành", Icons.Default.CheckCircle, GreenStatus)
        )
        filters.forEachIndexed { i, item ->
            Surface(
                color = if (i == 0) item.third else Color.White,
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.clickable { }
            ) {
                Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(item.second, null, tint = if(i==0) Color.White else item.third, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(item.first, color = if(i==0) Color.White else Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}