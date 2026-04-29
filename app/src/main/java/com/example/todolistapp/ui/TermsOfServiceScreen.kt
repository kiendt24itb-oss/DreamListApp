package com.example.todolistapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun TermsOfServiceScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.statusBarsPadding().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, tint = DeepPurple)
                }
                Text("Điều khoản sử dụng", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepPurple)
            }

            // Content
            Surface(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(bottom = 20.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
                    TermSection("1. Quy định chung", "Bằng việc sử dụng ứng dụng Dream List, bạn đồng ý với các điều khoản và điều kiện sử dụng được nêu tại đây.")
                    TermSection("2. Quyền riêng tư", "Chúng tôi cam kết bảo mật thông tin cá nhân và dữ liệu ước mơ của bạn. Dữ liệu chỉ được lưu trữ nhằm phục vụ mục đích quản lý cá nhân.")
                    TermSection("3. Trách nhiệm người dùng", "Người dùng chịu trách nhiệm bảo mật tài khoản và mật khẩu của mình. Không sử dụng ứng dụng cho các mục đích vi phạm pháp luật.")
                    TermSection("4. Thay đổi điều khoản", "Chúng tôi có quyền cập nhật các điều khoản này bất cứ lúc nào để phù hợp với sự phát triển của ứng dụng.")

                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Cập nhật lần cuối: 29/04/2026", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun TermSection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(title, fontWeight = FontWeight.Bold, color = DeepPurple, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(content, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
    }
}