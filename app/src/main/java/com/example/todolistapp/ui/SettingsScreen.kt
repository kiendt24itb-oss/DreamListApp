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
import com.example.todolistapp.R

@Composable
fun SettingsScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {

        // 1. ẢNH NỀN SÁNG & CHỈ MỜ DẦN PHÍA DƯỚI
        Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            Image(
                painter = painterResource(id = R.drawable.bg_h),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Gradient: Chỉ mờ ở phần dưới để hòa vào PurpleBg
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent, // Giữ 50% phía trên trong suốt hoàn toàn
                        PurpleBg.copy(alpha = 0.5f),
                        PurpleBg
                    )
                )
            ))
        }

        // 2. CẤU TRÚC CHÍNH
        Column(modifier = Modifier.fillMaxSize()) {

            // --- HEADER (SÁNG RỰC RỠ) ---
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 28.dp, vertical = 30.dp)
            ) {
                Text(
                    text = "Cài đặt ✨",
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        // Thêm bóng đổ nhẹ để chữ trắng nổi trên nền ảnh sáng
                        shadow = Shadow(color = Color.Black.copy(alpha = 0.2f), blurRadius = 8f)
                    )
                )
                Text(
                    text = "Quản lý tài khoản và trải nghiệm",
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(color = Color.Black.copy(alpha = 0.2f), blurRadius = 4f)
                    )
                )
            }

            // --- VÙNG CUỘN ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                SettingLabel("Tài khoản")
                UserCardWithLogo()

                SettingItemCard("Thông tin cá nhân", Icons.Default.Person)
                SettingItemCard("Đổi mật khẩu", Icons.Default.Lock)
                SettingItemCard("Thông báo", Icons.Default.Notifications, showSwitch = true)

                SettingLabel("Khác")
                SettingItemCard("Điều khoản sử dụng", Icons.Default.Description)
                SettingItemCard("Giới thiệu ứng dụng", Icons.Default.Info)
                SettingItemCard("Đăng xuất", Icons.Default.ExitToApp, isRed = true)

                Spacer(modifier = Modifier.height(20.dp))
            }

            // --- CỐ ĐỊNH PHIÁ DƯỚI (BANNER & FOOTER) ---
            Column(modifier = Modifier.background(PurpleBg)) {
                SettingsBanner()
                SquaredBottomNav(navController = navController)
            }
        }
    }
}

// --- CÁC COMPONENT CON ---

@Composable
fun UserCardWithLogo() {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(58.dp).clip(CircleShape).background(PurpleBg)
            )
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text("Kiên ✨", fontWeight = FontWeight.ExtraBold, fontSize = 19.sp, color = Color(0xFF1A1C2E))
                Text("kien.nguyen@gmail.com", fontSize = 13.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Composable
fun SettingItemCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, showSwitch: Boolean = false, isRed: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isRed) Color.Red else DeepPurple, modifier = Modifier.size(22.dp))
            Text(
                title,
                modifier = Modifier.weight(1f).padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (isRed) Color.Red else Color(0xFF1A1C2E)
            )
            if (showSwitch) {
                Switch(checked = true, onCheckedChange = {}, colors = SwitchDefaults.colors(checkedTrackColor = DeepPurple))
            } else {
                Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun SettingsBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(18.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(start = 24.dp), verticalArrangement = Arrangement.Center) {
            Text("Kỷ luật là sức mạnh", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("Để ước mơ bay xa hơn ✨", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
        }
    }
}

@Composable
fun SettingLabel(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = DeepPurple.copy(alpha = 0.8f),
        modifier = Modifier.padding(start = 28.dp, top = 16.dp, bottom = 8.dp)
    )
}