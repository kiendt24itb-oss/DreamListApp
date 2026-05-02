package com.example.todolistapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.todolistapp.LocalWindowSizeClass
import com.example.todolistapp.R
import com.example.todolistapp.utils.SessionManager
import com.example.todolistapp.viewmodel.AuthViewModel

@Composable
fun Register(navController: NavHostController) {
    val viewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var infoMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    val registerResult by viewModel.registerResult.collectAsState()
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(registerResult) {
        registerResult?.let { result ->
            if (result.success) {
                infoMessage = "Đăng ký thành công. Vui lòng đăng nhập."
                isError = false
                navController.navigate("Login") {
                    popUpTo("Register") { inclusive = true }
                }
            } else {
                infoMessage = result.message
                isError = true
            }
        }
    }

    LaunchedEffect(error) {
        if (error != null) {
            infoMessage = error
            isError = true
        }
    }

    val windowSize = LocalWindowSizeClass.current
    val widthClass = windowSize.widthSizeClass

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. GIỮ LẠI NỀN VÀ LÀM MỜ (Alpha) - GIỮ NGUYÊN
        Image(
            painter = painterResource(id = R.drawable.b_g),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // 2. Logo - TỰ ĐỘNG THÍCH ỨNG THEO MÀN HÌNH
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(
                    if (widthClass == WindowWidthSizeClass.Expanded) 170.dp else 130.dp
                )
            )

            // 3. CHỮ DREAM LIST - GIỮ NGUYÊN logic offset của bạn
            Text(
                text = "dream list",
                fontSize = 24.sp,
                color = Color(0xFF4A3DA1),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.offset(y = (-25).dp)
            )

            // 4. TIÊU ĐỀ TẠO TK - GIỮ NGUYÊN
            Text(
                text = "Tạo tài khoản",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3DA1)
            )

            // DÒNG MÔ TẢ + NGÔI SAO - GIỮ NGUYÊN
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Bắt đầu hành trình chinh phục ước mơ của bạn",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Ô nhập liệu
            CustomTextField(value = fullName, onValueChange = { fullName = it }, label = "Họ và tên", icon = Icons.Default.Person)
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(value = email, onValueChange = { email = it }, label = "Email", icon = Icons.Default.Email)
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(value = password, onValueChange = { password = it }, label = "Mật khẩu", icon = Icons.Default.Lock, isPassword = true)
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Xác nhận mật khẩu", icon = Icons.Default.Lock, isPassword = true)

            Spacer(modifier = Modifier.height(35.dp))

            // Nút bấm đăng ký
            GradientButton(text = if (loading) "Đang tạo..." else "Tạo tài khoản") {
                infoMessage = null
                if (fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    infoMessage = "Vui lòng điền đủ thông tin."
                    isError = true
                } else if (password != confirmPassword) {
                    infoMessage = "Mật khẩu và xác nhận mật khẩu không khớp."
                    isError = true
                } else {
                    viewModel.register(fullName.trim(), email.trim(), password)
                }
            }

            infoMessage?.let { messageText ->
                Text(
                    text = messageText,
                    color = if (isError) Color.Red else Color.Green,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Chuyển sang Đăng nhập - GIỮ NGUYÊN
            Row(modifier = Modifier.padding(bottom = 32.dp)) {
                Text("Đã có tài khoản? ", color = Color.Gray)
                Text(
                    text = "Đăng nhập",
                    color = Color(0xFF6B62D9),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}