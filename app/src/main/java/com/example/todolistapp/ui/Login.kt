package com.example.todolistapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R

@Composable
fun Login(navController: NavHostController) { // Đã đồng bộ tham số
    // Lấy size từ kho chứa toàn cục
    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Hình nền - GIỮ NGUYÊN
        Image(
            painter = painterResource(id = R.drawable.b_g),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.10f))

            // 2. Logo -> TỰ ĐỘNG THÍCH ỨNG THEO MÀN HÌNH
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(
                    if (widthClass == WindowWidthSizeClass.Expanded) 200.dp else 160.dp
                )
            )

            Text(
                text = "dream list",
                fontSize = 46.sp,
                color = Color(0xFF4A3DA1),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.offset(y = (-8).dp)
            )
            Text(
                text = "Biến ước mơ của bạn\nthành những kế hoạch mỗi ngày ✨",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color(0xFF5E549E).copy(alpha = 0.7f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 3. Input Fields - GIỮ NGUYÊN
            CustomTextField(value = "", onValueChange = {}, label = "Email", icon = Icons.Default.Email)
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(value = "", onValueChange = {}, label = "Mật khẩu", icon = Icons.Default.Lock, isPassword = true)

            Text(
                text = "Quên mật khẩu?",
                modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                color = Color(0xFF5E549E),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 4. Button - GIỮ NGUYÊN LOGIC ĐIỀU HƯỚNG
            GradientButton(text = "Đăng nhập") {
                navController.navigate("Home")
            }

            Spacer(modifier = Modifier.weight(0.1f))

            // 5. Chuyển sang Đăng ký - GIỮ NGUYÊN
            Row {
                Text("Chưa có tài khoản? ", color = Color.Gray)
                Text(
                    text = "Đăng ký",
                    color = Color(0xFF6B62D9),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("Register") }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- CÁC COMPONENT PHỤ GIỮ NGUYÊN HOÀN TOÀN ---
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        placeholder = { Text(text = label, color = Color.Gray.copy(alpha = 0.6f)) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF9D89F3)) },
        trailingIcon = {
            if (isPassword) {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = Color.LightGray)
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun GradientButton(text: String, onClick: () -> Unit) {
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF9D89F3), Color(0xFF6B62D9)),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text + " ✨", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}