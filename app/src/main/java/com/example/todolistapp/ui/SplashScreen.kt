package com.example.todolistapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlaylistAddCheck
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WbCloudy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R

@Composable
fun SplashScreenContent(
    navController: NavHostController // Sửa 1: Đồng bộ chỉ nhận navController
) {
    // Sửa 2: Lấy windowSize từ LocalWindowSizeClass
    val windowWidthSize = LocalWindowSizeClass.current.widthSizeClass

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Ảnh nền - GIỮ NGUYÊN
        Image(
            painter = painterResource(id = R.drawable.b_g),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.1f))

            // 2. Logo - Sửa nhẹ biến check windowSize cho khớp với Local
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(if (windowWidthSize == WindowWidthSizeClass.Expanded) 300.dp else 260.dp)
            )

            // 3. Tên ứng dụng - GIỮ NGUYÊN
            Text(
                text = "dream list",
                fontSize = 56.sp,
                color = Color(0xFF4A3DA1),
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.offset(y = (-10).dp)
            )

            Text(
                text = "Biến ước mơ của bạn\nthành những kế hoạch mỗi ngày ✨",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFF5E549E).copy(alpha = 0.8f),
                lineHeight = 24.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.weight(0.08f))

            // 4. BA TÍNH NĂNG - GIỮ NGUYÊN
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                FancyFeatureItem(
                    icon = Icons.Rounded.PlaylistAddCheck,
                    label = "Lập kế hoạch\ndễ dàng",
                    mainColor = Color(0xFF7E72D6)
                )

                FancyFeatureItem(
                    icon = Icons.Rounded.Star,
                    label = "Theo dõi tiến độ\nmỗi ngày",
                    mainColor = Color(0xFFFFD54F)
                )

                FancyFeatureItem(
                    icon = Icons.Rounded.WbCloudy,
                    label = "Biến ước mơ\nthành hiện thực",
                    mainColor = Color(0xFF90CAF9)
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))

            // 5. Nút Bắt đầu - Sửa: Dùng navController để navigate
            Button(
                onClick = { navController.navigate("Login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B62D9)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    "Bắt đầu ngay ✨",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

// Hàm này ko liên quan đến Nav hay Size nên GIỮ NGUYÊN HOÀN TOÀN
@Composable
private fun FancyFeatureItem(icon: ImageVector, label: String, mainColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(110.dp)
    ) {
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = mainColor,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF4A3DA1),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp
        )
    }
}