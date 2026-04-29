package com.example.todolistapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todolistapp.R

@Composable
fun AboutAppScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize().background(PurpleBg)) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Default.ArrowBack, null, tint = DeepPurple)
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo & Tên App
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.White).padding(10.dp)
                )
                Text("Dream List", fontSize = 28.sp, fontWeight = FontWeight.Black, color = DeepPurple)
                Text("Version 1.0.0", fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(30.dp))

                // Nội dung giới thiệu
                Surface(shape = RoundedCornerShape(20.dp), color = Color.White) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Dream List là người bạn đồng hành giúp bạn quản lý, theo dõi và hiện thực hóa những ước mơ của mình. Với giao diện hiện đại và các tính năng thông minh, chúng tôi hy vọng sẽ truyền cảm hứng cho bạn mỗi ngày.",
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Credit
                Text("Phát triển bởi", fontSize = 12.sp, color = Color.Gray)
                Text("Kien Nguyen ✨", fontWeight = FontWeight.Bold, color = DeepPurple)
            }
        }
    }
}